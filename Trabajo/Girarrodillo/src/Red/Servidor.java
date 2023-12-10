package Red;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

import Campeones.Campeon;
import Jugador.Jugador;

public class Servidor
{
	private ServerSocket servidor;
	private int numJugadores;		//Número de jugadores conectados al servidor.
	private Jugador j1 = null;		//Primer jugador en conectarse.
	private Jugador j2 = null;		//Segundo jugador en conectarse.
	private int jugListos = 0;		//Número de jugadores que han elegido Campeones.
	private int turnoAcabado = 0;	//Número de jugadores que han acabado su turno.
	private int acc = 0;			//Número de jugadores preparados para que se realicen acciones.
	private int sigTurno = 0;		//Si es 1, puede empezar el siguiente turno.
	private int fin = 0;			//Mientras sea 0, sigue la partida, si cambia a 1/2 es que ese jugador ha ganado.
	private ConexionServidor cj1;	//Conexión con el primer jugador.
	private ConexionServidor cj2;	//Conexión con el segundo jugador.
	
	public Servidor()
	{
		System.out.println("Servidor iniciado.");
		this.numJugadores = 0;
		try
		{
			this.servidor = new ServerSocket(9876);
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}
	}
	
	//Acepta conexiones de Clientes hasta que haya 2 conectados y no más.
	//Comienza los hilos que gestionan a cada Cliente.
	public void aceptarConexion()
	{
		System.out.println("Esperando conexiones.");
		try
		{
			while(this.numJugadores < 2)
			{
				Socket cli = servidor.accept();
				this.numJugadores++;
				System.out.println("Jugadores conectados: "+this.numJugadores);
				ConexionServidor cs = new ConexionServidor(cli, numJugadores);
				if(numJugadores == 1) this.cj1 = cs;
				else this.cj2 = cs;
				
				Thread t = new Thread(cs);
				t.start();
			}
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}
	}
	
	//Clase que gestiona la conexión del servidor con cada cliente mediante hilos.
	private class ConexionServidor implements Runnable
	{
		private Socket socket;
		private ObjectInputStream in;
		private ObjectOutputStream out;
		private int idJugador;	//Id del Jugador Cliente.
		private Paquete cal;	//Recibe información del cliente.
		
		public ConexionServidor(Socket s, int id)
		{
			this.socket = s;
			this.idJugador = id;
			try
			{
				this.in = new ObjectInputStream(socket.getInputStream());
				this.out = new ObjectOutputStream(socket.getOutputStream());
			}
			catch(IOException e)
			{
				e.printStackTrace();
			}
		}
		
		//Manda el Jugador Rival al Cliente.
		public void mandarRival() throws Exception
		{
			if(idJugador == 1)
			{
				out.writeObject(j2);
			}
			else
			{
				out.writeObject(j1);
			}
		}
		
		//Obtiene el Jugador al que corresponde el Id proporcionado.
		public Jugador obtenerJugadorPorId()
		{
			if(idJugador == 1)
			{
				return j1;
			}
			else
			{
				return j2;
			}
		}
		
		//Manda ambos Jugadores al Cliente.
		public void mandarPartida() throws Exception
		{
			if(idJugador == 1)
			{
				out.writeObject(new Paquete(j1.getPc(), j1.getPm(), j1.getCampeones().get(0).getTurnosAtaque(), j1.getCampeones().get(1).getTurnosAtaque()));
				//out.writeObject(j1);
			}
			else
			{
				out.writeObject(new Paquete(j2.getPc(), j2.getPm(), j2.getCampeones().get(0).getTurnosAtaque(), j2.getCampeones().get(1).getTurnosAtaque()));
				//out.writeObject(j2);
			}
			
			in.readInt();
			
			if(idJugador == 1)
			{
				out.writeObject(new Paquete(j2.getPc(), j2.getPm(), j2.getCampeones().get(0).getTurnosAtaque(), j2.getCampeones().get(1).getTurnosAtaque()));
				//out.writeObject(j2);
			}
			else
			{
				out.writeObject(new Paquete(j1.getPc(), j1.getPm(), j1.getCampeones().get(0).getTurnosAtaque(), j1.getCampeones().get(1).getTurnosAtaque()));
				//out.writeObject(j1);
			}
		}
		
		@Override
		public void run()
		{
			try
			{
				out.writeInt(idJugador);
				out.flush();
				while(numJugadores != 2)
				{
					TimeUnit.SECONDS.sleep(1);
				}
				
				if(idJugador == 1)
				{
					out.writeObject(j2);
					j1 = (Jugador) in.readObject();
					jugListos++;
				}
				else
				{
					out.writeObject(j1);
					j2 = (Jugador) in.readObject();
					jugListos++;
				}
				
				while(jugListos != 2)
				{
					TimeUnit.SECONDS.sleep(1);
				}
				
				mandarRival();

				while(fin == 0)
				{
					turnoAcabado = 0;
					cal = (Paquete) in.readObject();
					turnoAcabado++;
					while(turnoAcabado != 2)
					{
						TimeUnit.SECONDS.sleep(1);
					}
					TimeUnit.SECONDS.sleep(1);
					
					out.writeInt(0);
					out.flush();
					
					calcularTurno(obtenerJugadorPorId(), cal);
					acc++;
					
					while(acc != 0)
					{
						TimeUnit.SECONDS.sleep(1);
					}
					TimeUnit.SECONDS.sleep(1);
					
					mandarPartida();
					
					while(sigTurno == 0)
					{
						TimeUnit.SECONDS.sleep(1);
					}
					
					in.readInt();
					out.writeInt(fin);
					out.flush();
				}
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
		}
	}
	
	//Calcula los resultados del turno del Jugador j mediante los valores del Paquete cal.
	public void calcularTurno(Jugador j, Paquete cal)
	{
		List<Campeon> cam = j.getCampeones();
		if(cal.getC1() > 0)
		{
			cam.get(0).turnosAtaque = cam.get(0).turnosAtaque - cal.getC1();
			if(cam.get(0).getTurnosAtaque() < 0) cam.get(0).setTurnosAtaque(0);
		}
		if(cal.getC2() > 0)
		{
			cam.get(1).turnosAtaque = cam.get(1).turnosAtaque - cal.getC2();
			if(cam.get(1).getTurnosAtaque() < 0) cam.get(1).setTurnosAtaque(0);
		}
		if(cal.getC3() > 0 && j.getPm()<10)
		{
			j.setPm(j.getPm() + cal.getC3());
			System.out.println("");
			System.out.println(cal.getC3()+" de Muralla construida.");
			if(j.getPm()>10) j.setPm(10);
		}
	}
	
	//Gestiona las acciones a realizar por los Campeones.
	public void acciones() throws Exception
	{
		System.out.println("----------------------------");
		List<Jugador> jugadores = new ArrayList<>();
		jugadores.add(j1);
		jugadores.add(j2);
		List<Campeon> campeones = new ArrayList<>();
		for(Jugador j : jugadores)
		{
			for(Campeon c : j.getCampeones())
			{
				campeones.add(c);
			}
		}
		for(Campeon c : campeones)
		{
			if(c.getTurnosAtaque() <= 0)
			{
				if(c.getJugador().equals(j1))
				{
					c.setRival(j2);
				}
				else
				{
					c.setRival(j1);
				}
				c.accion();
				c.setSubidaNivel(c.getSubidaNivel() - 1);
				if(c.getSubidaNivel() == 0)
				{
					System.out.println("¡"+c.getNom()+" ha mejorado!");
					c.mejorar();
				}
				c.resetearTA();
			}
		}
		System.out.println("");
		jugadores.get(0).mostrar();
		System.out.println("");
		jugadores.get(1).mostrar();
		System.out.println("");
		System.out.println("----------------------------");
		System.out.println("");
		j1 = jugadores.get(0);
		j2 = jugadores.get(1);
	}
	
	//Controla si ha acabado el juego y quién ha ganado de haberlo hecho.
	public void juegoAcabado() throws Exception
	{
		List<Jugador> jugadores = new ArrayList<>();
		jugadores.add(j1);
		jugadores.add(j2);
        for(Jugador j : jugadores)
        {
            if(j.getPc() <= 0)
            {
            	System.out.println("FINAL");
            	if(j.equals(j1)) this.fin = 2;
            	else this.fin = 1;
            }
        }
        this.sigTurno = 1;
    }
	
	public static void main(String[] args)
	{
		try
		{
			Servidor serv = new Servidor();
			serv.aceptarConexion();
			
			while(serv.jugListos != 2)
			{
				TimeUnit.SECONDS.sleep(1);
			}
			
			while(serv.fin == 0)
			{
				serv.sigTurno = 0;
				while(serv.acc != 2)
				{
					TimeUnit.SECONDS.sleep(1);
				}
				TimeUnit.SECONDS.sleep(1);

				serv.acciones();
				serv.acc = 0;
				serv.juegoAcabado();
				TimeUnit.SECONDS.sleep(10);
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
}