package Red;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Servidor
{
	private ServerSocket servidor;
	private int numJugadores;
	private ConexionServidor j1;
	private ConexionServidor j2;
	private int turnos;
	
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
				if(numJugadores == 1) this.j1 = cs;
				else this.j2 = cs;
				
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
		private DataInputStream in;
		private DataOutputStream out;
		private int idJugador;
		
		public ConexionServidor(Socket s, int id)
		{
			this.socket = s;
			this.idJugador = id;
			try
			{
				this.in = new DataInputStream(socket.getInputStream());
				this.out = new DataOutputStream(socket.getOutputStream());
			}
			catch(IOException e)
			{
				e.printStackTrace();
			}
		}
		
		@Override
		public void run()
		{
			try
			{
				out.writeInt(idJugador);
				out.flush();
				
				while(true)
				{
					
				}
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
		}
		
	}
	
	public static void main(String[] args)
	{
		Servidor serv = new Servidor();
		serv.aceptarConexion();
	}
}