package Red;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.concurrent.TimeUnit;

import Jugador.Jugador;

public class ConexionServidor implements Runnable
{
	private Socket socket;
	private ObjectInputStream in;
	private ObjectOutputStream out;
	private int idJugador;	//Id del Jugador Cliente.
	private Paquete cal;	//Recibe información del cliente.
	
	private Partida par;
	
	public ConexionServidor(Partida p, Socket s, int id)
	{
		this.par = p;
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
			out.writeObject(par.j2);
		}
		else
		{
			out.writeObject(par.j1);
		}
	}
	
	//Obtiene el Jugador al que corresponde el Id proporcionado.
	public Jugador obtenerJugadorPorId()
	{
		if(idJugador == 1)
		{
			return par.j1;
		}
		else
		{
			return par.j2;
		}
	}
	
	//Manda ambos Jugadores al Cliente.
	public void mandarPartida() throws Exception
	{
		if(idJugador == 1)
		{
			out.writeObject(new Paquete(par.j1.getPc(), par.j1.getPm(), par.j1.getCampeones().get(0).getTurnosAtaque(), par.j1.getCampeones().get(1).getTurnosAtaque()));
			//out.writeObject(j1);
		}
		else
		{
			out.writeObject(new Paquete(par.j2.getPc(), par.j2.getPm(), par.j2.getCampeones().get(0).getTurnosAtaque(), par.j2.getCampeones().get(1).getTurnosAtaque()));
			//out.writeObject(j2);
		}
			
		in.readInt();
		
		if(idJugador == 1)
		{
			out.writeObject(new Paquete(par.j2.getPc(), par.j2.getPm(), par.j2.getCampeones().get(0).getTurnosAtaque(), par.j2.getCampeones().get(1).getTurnosAtaque()));
			//out.writeObject(j2);
		}
		else
		{
			out.writeObject(new Paquete(par.j1.getPc(), par.j1.getPm(), par.j1.getCampeones().get(0).getTurnosAtaque(), par.j1.getCampeones().get(1).getTurnosAtaque()));
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
			while(par.numJugadores != 2)
			{
				TimeUnit.SECONDS.sleep(1);
			}
			
			if(idJugador == 1)
			{
				out.writeObject(par.j2);
				par.j1 = (Jugador) in.readObject();
				par.jugListos++;
			}
			else
			{
				out.writeObject(par.j1);
				par.j2 = (Jugador) in.readObject();
				par.jugListos++;
			}
			
			while(par.jugListos != 2)
			{
				TimeUnit.SECONDS.sleep(1);
			}
			
			mandarRival();
			while(par.fin == 0)
			{
				par.turnoAcabado = 0;
				cal = (Paquete) in.readObject();
				par.turnoAcabado++;
				while(par.turnoAcabado != 2)
				{
					TimeUnit.SECONDS.sleep(1);
				}
				TimeUnit.SECONDS.sleep(1);
					
				out.writeInt(0);
				out.flush();
				
				par.calcularTurno(obtenerJugadorPorId(), cal);
				par.acc++;
					
				while(par.acc != 0)
				{
					TimeUnit.SECONDS.sleep(1);
				}
				TimeUnit.SECONDS.sleep(1);
				
				mandarPartida();
					
				while(par.sigTurno == 0)
				{
					TimeUnit.SECONDS.sleep(1);
				}
					
				in.readInt();
				out.writeInt(par.fin);
				out.flush();
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
}
