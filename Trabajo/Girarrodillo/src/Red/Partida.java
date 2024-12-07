package Red;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import Campeones.Campeon;
import Jugador.Jugador;

public class Partida extends Thread
{
	public int numJugadores;		//Número de jugadores conectados al servidor.
	public Jugador j1 = null;		//Primer jugador en conectarse.
	public Jugador j2 = null;		//Segundo jugador en conectarse.
	public int jugListos = 0;		//Número de jugadores que han elegido Campeones.
	public int turnoAcabado = 0;	//Número de jugadores que han acabado su turno.
	public int acc = 0;			//Número de jugadores preparados para que se realicen acciones.
	public int sigTurno = 0;		//Si es 1, puede empezar el siguiente turno.
	public int fin = 0;			//Mientras sea 0, sigue la partida, si cambia a 1/2 es que ese jugador ha ganado.
	private ConexionServidor cj1;	//Conexión con el primer jugador.
	private ConexionServidor cj2;	//Conexión con el segundo jugador.
	
	private List<Socket> sockets;	//Lista de sockets de los jugadores.
	private int id;					//Número de identificación de la partida.
	
	public Partida(List<Socket> ls)
	{
		this.sockets = ls;
	}
	
	public void setId(int i)
	{
		this.id = i;
	}
	
	//Acepta conexiones de Clientes hasta que haya 2 conectados y no más.
	//Comienza los hilos que gestionan a cada Cliente.
	public void empezarPartida()
	{
		this.numJugadores = 0;
		while(this.numJugadores < 2)
		{
			Socket cli = this.sockets.get(numJugadores);
			this.numJugadores++;
			//System.out.println("Jugadores conectados: "+this.numJugadores);
			ConexionServidor cs = new ConexionServidor(this, cli, numJugadores);
			if(numJugadores == 1) this.cj1 = cs;
			else this.cj2 = cs;
			
			Thread t = new Thread(cs);
			t.start();
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
	
	public void mostrarPartida()
	{
		if(j1 != null && j2 != null)
		{
			System.out.println(this.id + ".");
			System.out.println("----------------------------");
			System.out.println("Jugador 1");
			j1.mostrar();
			System.out.println("");
			j2.mostrar();
			System.out.println("Jugador 2");
			System.out.println("----------------------------");
			System.out.println("");
		}
	}
	
	@Override
	public void run()
	{
		try
		{
			empezarPartida();
			while(this.jugListos != 2)
			{
				TimeUnit.SECONDS.sleep(1);
			}
			
			while(this.fin == 0)
			{
				this.sigTurno = 0;
				while(this.acc != 2)
				{
					TimeUnit.SECONDS.sleep(1);
				}
				TimeUnit.SECONDS.sleep(1);

				this.acciones();
				this.acc = 0;
				this.juegoAcabado();
				TimeUnit.SECONDS.sleep(10);
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
}
