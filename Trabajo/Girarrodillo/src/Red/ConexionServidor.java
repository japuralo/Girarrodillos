package Red;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.time.Instant;
import java.util.Map.Entry;
import java.util.concurrent.TimeUnit;

import Jugador.Jugador;

public class ConexionServidor implements Runnable
{
	private Socket socket;
	private ObjectInputStream in;
	private ObjectOutputStream out;
	private int idJugador;	//Id del Jugador Cliente.
	private Paquete cal;	//Recibe información del cliente.
	
	private Partida par;	//Partida que está jugando/espectando el cliente asociado.
	private Servidor serv;	
	
	private boolean jugador;	//Marca si la conexión pertenece a un jugador o a un espectador.
	
	public ConexionServidor(Socket s)
	{
		this.socket = s;
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
	
	public void setPartida(Partida p)
	{
		this.par = p;
	}
	
	public void setIdJugador(int id)
	{
		this.idJugador = id;
	}
	
	public void setServidor(Servidor s)
	{
		this.serv = s;
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
			for(Entry<Integer, Partida> p : serv.partidas.entrySet())
			{
				if(p.getValue().toString() != null && !p.getValue().toString().equals(""))
				{
					out.writeBytes(p.getKey() + ". " + p.getValue().toString() + "\n");
					out.flush();
				}
			}
			out.writeBytes("FIN\n");
			out.flush();
			
			System.out.println("Esperando opción");
			int op = in.readInt();
			if(op == 0)
			{
				SalaEspera.addCliente(this);
				this.jugador = true;
			}
			else
			{
				this.jugador = false;
			}
			
			if(jugador)
			{
				while(par == null)
				{
					TimeUnit.MILLISECONDS.sleep(100);
				}
				
				out.writeInt(idJugador);
				out.flush();
				
				mandarRival();
				
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
			else
			{
				this.idJugador = 3;
				this.par = serv.partidas.get(op);
				String s = par.toString();
				while(par.fin == 0)
				{
					if(!s.equals(par.toString()))
					{
						TimeUnit.SECONDS.sleep(2);
						out.writeBytes(par.toString() + "\n");
						out.flush();
						s = par.toString();
					}
				}
				out.writeBytes(par.toString() + "\n");
				out.flush();
				out.writeBytes("FIN" + "\n");
				out.flush();
				if(par.fin == 1)
				{
					out.writeBytes("Ha ganado el Jugador 1\n");
					out.flush();
				}
				else
				{
					out.writeBytes("Ha ganado el Jugador 2\n");
					out.flush();
				}
			}
			
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
}
