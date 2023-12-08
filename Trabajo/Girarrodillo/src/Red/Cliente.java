package Red;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class Cliente
{
	private ConexionCliente cc;
	private int idJugador;
	private int idRival;
	private int turnos;
	
	public Cliente()
	{
		
	}

	public void conectarServidor()
	{
		this.cc = new ConexionCliente();
	}
	
	private class ConexionCliente
	{
		private Socket socket;
		private DataInputStream in;
		private DataOutputStream out;
		
		public ConexionCliente()
		{
			try
			{
				this.socket = new Socket("localhost",9876);
				this.in = new DataInputStream(socket.getInputStream());
				this.out = new DataOutputStream(socket.getOutputStream());
				
				idJugador = in.readInt();
				System.out.println("Conectado: Jugador "+idJugador);
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}
		}
	}
	
	public static void main(String[] args)
	{
		Cliente c = new Cliente();
		c.conectarServidor();
	}
}
