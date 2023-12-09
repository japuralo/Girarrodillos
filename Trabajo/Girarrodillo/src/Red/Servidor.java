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

import Jugador.Jugador;

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
		//private DataInputStream in;
		//private DataOutputStream out;
		private ObjectInputStream in;
		private ObjectOutputStream out;
		private int idJugador;
		
		public ConexionServidor(Socket s, int id)
		{
			this.socket = s;
			this.idJugador = id;
			try
			{
				//this.in = new DataInputStream(socket.getInputStream());
				this.in = new ObjectInputStream(socket.getInputStream());
				//this.out = new DataOutputStream(socket.getOutputStream());
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
			if(idJugador == 1)
			{
				j1 = (Jugador) in.readObject();
			}
			else
			{
				j2 = (Jugador) in.readObject();
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
					leerJugador();
					leerJugador();
				}
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
		}
		
	}
	
	public boolean juegoAcabado()
	{
		List<Jugador> jugadores = new ArrayList<>();
		jugadores.add(j1);
		jugadores.add(j2);
        for(Jugador j : jugadores)
        {
            if(j.getPc() <= 0)
            {
            	System.out.println("FINAL");
                return true;
            }
        }
        return false;
    }
	
	public static void main(String[] args)
	{
		Servidor serv = new Servidor();
		serv.aceptarConexion();
	}
}