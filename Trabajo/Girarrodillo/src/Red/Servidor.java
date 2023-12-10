package Red;

import java.io.DataInputStream;
import java.io.DataOutputStream;
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
import Jugador.Rodillos;

public class Servidor
{
	private ServerSocket servidor;
	private int numJugadores;
	private ConexionServidor cj1;
	private ConexionServidor cj2;
	private int turnos;
	private Jugador j1 = null;
	private Jugador j2 = null;
	private int jugListos = 0;
	private int turnoAcabado = 0;
	private int acc = 0;
	
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
	
	private class ConexionServidor implements Runnable
	{
		private Socket socket;
		private ObjectInputStream in;
		private ObjectOutputStream out;
		private int idJugador;
		private Paquete cal;
		
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
		
		public void leerJugador() throws Exception
		{
			Jugador aux = (Jugador) in.readObject();
			aux.toString();
			if(idJugador == 1)
			{
				j1 = aux;
			}
			else
			{
				j2 = aux;
			}
		}
		
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
		
		public void mandarPartida() throws Exception
		{
			if(idJugador == 1)
			{
				out.writeObject(j1);
			}
			else
			{
				out.writeObject(j2);
			}
			
			in.readInt();
			
			if(idJugador == 1)
			{
				out.writeObject(j2);
			}
			else
			{
				out.writeObject(j1);
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

				while(!juegoAcabado())
				{
					turnoAcabado = 0;
					cal = (Paquete) in.readObject();
					turnoAcabado++;
					while(turnoAcabado != 2)
					{
						TimeUnit.SECONDS.sleep(1);
					}
					TimeUnit.SECONDS.sleep(1);
					
					calcularTurno(obtenerJugadorPorId(), cal);
					acc++;
					
					while(acc != 0)
					{
						TimeUnit.SECONDS.sleep(1);
					}
					TimeUnit.SECONDS.sleep(1);
				}
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
		}
	}
	
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
		cj1.mandarPartida();
		cj2.mandarPartida();
	}
	
	public boolean juegoAcabado() throws Exception
	{
		List<Jugador> jugadores = new ArrayList<>();
		jugadores.add(j1);
		jugadores.add(j2);
        for(Jugador j : jugadores)
        {
            if(j.getPc() <= 0)
            {
            	System.out.println("FINAL");
            	cj1.out.writeBytes("FINAL");
            	cj2.out.writeBytes("FINAL");
            	try
            	{
					cj1.socket.close();
					cj2.socket.close();
				}
            	catch(Exception e)
            	{
					e.printStackTrace();
				}
                return true;
            }
        }
        return false;
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
			
			while(!serv.juegoAcabado())
			{
				while(serv.acc != 2)
				{
					TimeUnit.SECONDS.sleep(1);
				}
				TimeUnit.SECONDS.sleep(1);

				serv.acciones();
				serv.acc = 0;
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
}