package Red;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import Campeones.Arquero;
import Campeones.Asesino;
import Campeones.Caballero;
import Campeones.Campeon;
import Campeones.Clerigo;
import Campeones.Ingeniero;
import Campeones.Mago;
import Jugador.Jugador;
import Jugador.Rodillos;

public class Cliente
{
	private ConexionCliente cc;	//Gestiona la conexión con el servidor.
	private int idJugador;		//Id del Jugador Cliente.
	private List<Campeon> lc;	//Lista de campeones.
	private Jugador jug;		//Objeto Jugador del Cliente.
	private Jugador riv;		//Objeto Jugador contrincante.
	private int fin = 0;		//Mientras sea 0, sigue la partida, si cambia a 1/2 es que ese jugador ha ganado.
	
	public Cliente()
	{
		this.lc = new ArrayList<Campeon>();
		this.jug = new Jugador(lc);
	}

	//Crea un objeto ConexionCliente y lo asigna a cc.
	public void conectarServidor()
	{
		this.cc = new ConexionCliente();
	}
	
	//Clase que gestiona la conexion con el servidor.
	private class ConexionCliente
	{
		private Socket socket;
		private ObjectInputStream in;
		private ObjectOutputStream out;
		private Paquete paq;
		
		//Crea la conexión con el servidor y obtiene los canales de comunicación.
		public ConexionCliente()
		{
			try
			{
				this.socket = new Socket("localhost",9876);
				this.out = new ObjectOutputStream(socket.getOutputStream());
				this.in = new ObjectInputStream(socket.getInputStream());
				
				idJugador = in.readInt();
				System.out.println("Conectado: Jugador "+idJugador);
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}
		}
	}
	
	//Muestra el estado de la partida.
	public void mostrarPartida()
	{
		System.out.println("");
		System.out.println("----------------------------");
		System.out.println("Rival");
		riv.mostrar();
		System.out.println("");
		jug.mostrar();
		System.out.println("Jugador");
		System.out.println("----------------------------");
		System.out.println("");
	}
	
	//Lee un Jugador que envíe el Servidor.
	public Jugador leerJugador()
	{
		try
		{
			return (Jugador) cc.in.readObject();
		}
		catch (ClassNotFoundException e)
		{
			e.printStackTrace();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		return null;
	}
	
	//Proceso de selección de Campeones, al acabar envía el objeto Jugador al servidor.
	public void elegirCampeon() throws Exception
	{
		this.riv =(Jugador) cc.in.readObject();
		
		Scanner sin = new Scanner(System.in);
		String inp;
		
		System.out.println("Jugador"+idJugador+":");
		for(int j = 0;j<2;j++)
		{
			boolean repetir = true;
			Campeon cam = null;
			while(repetir)
			{
				repetir = false;
				System.out.println("Elige un Campeón: Caballero(CAB), Mago(MAG), Arquero(ARQ), Ingeniero(ING), Asesino(ASE), Clérigo(CLE)");
				inp = sin.nextLine();
				if(inp.equals("CAB")) cam = new Caballero(jug,riv);
				else if(inp.equals("MAG")) cam = new Mago(jug,riv);
				else if(inp.equals("ARQ")) cam = new Arquero(jug,riv);
				else if(inp.equals("ING")) cam = new Ingeniero(jug,riv);
				else if(inp.equals("ASE")) cam = new Asesino(jug,riv);
				else if(inp.equals("CLE")) cam = new Clerigo(jug,riv);
				else repetir = true;
			}
			jug.aniadirCampeon(cam);		
		}
		
		cc.out.writeObject(jug);
	}
	
	//Gestiona las acciones del jugador durante el turno.
	public void turno() throws Exception
	{
		jug.resetarCandados();
		System.out.println("");
		System.out.println("Tú turno");
		for(int i=0;i<3;i++)
		{
			if(i!=0)jug.bloquear();
			jug.girar();
			jug.mostrar();
		}
		
		calcularTurno(jug);
	}
	
	//Calcula los puntos obtenidos por el jugador en el turno y manda el resultado al servidor.
	public void calcularTurno(Jugador j) throws Exception
	{
		int[] calculo = new int[3];
		calculo[0] = 0;
		calculo[1] = 0;
		calculo[2] = 0;
		String aux = "";
		Rodillos r = j.getRodillos();
		aux = aux + r.getR1()+ r.getR2()+ r.getR3()+ r.getR4()+ r.getR5();
		for(int i=0;i<aux.length();i++)
		{
			if(aux.charAt(i) == 'I') calculo[0]++;
			if(aux.charAt(i) == 'D') calculo[1]++;
			if(aux.charAt(i) == 'M') calculo[2]++;
		}
		calculo[0] = calculo[0] - 2;
		calculo[1] = calculo[1] - 2;
		calculo[2] = calculo[2] - 2;
		Paquete cal = new Paquete(calculo[0], calculo[1], calculo[2]);
		cc.out.writeObject(cal);
	}
	
	//Controla si el juego ha finalizado y quién ha ganado.
	public void juegoAcabado()
	{
		try
		{
			this.fin = cc.in.readInt();
		}
		catch(IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
	
	public void actualizarJugador(Jugador j, Paquete p)
	{
		j.setPc(p.getPc());
		j.setPm(p.getPm());
		j.getCampeones().get(0).setTurnosAtaque(p.getTi());
		j.getCampeones().get(1).setTurnosAtaque(p.getTd());
	}
	
	public static void main(String[] args)
	{
		try
		{
			Cliente c = new Cliente();
			c.conectarServidor();
			c.elegirCampeon();
			c.riv = c.leerJugador();
			
			while(c.fin == 0)
			{
				c.mostrarPartida();
				c.turno();
				c.cc.in.readInt();
				//c.jug = c.leerJugador();
				c.cc.paq = (Paquete) c.cc.in.readObject();
				c.actualizarJugador(c.jug, c.cc.paq);
				c.cc.out.writeInt(0);
				c.cc.out.flush();
				//c.riv = c.leerJugador();
				c.cc.paq = (Paquete) c.cc.in.readObject();
				c.actualizarJugador(c.riv, c.cc.paq);
				c.cc.out.writeInt(0);
				c.cc.out.flush();
				c.juegoAcabado();
			}
			System.out.println("Ha ganado el Jugador "+c.fin);
		}
		 catch (Exception e)
		{
			e.printStackTrace();
		}
	}
}
