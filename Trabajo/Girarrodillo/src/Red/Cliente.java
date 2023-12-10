package Red;

import java.io.DataInputStream;
import java.io.DataOutputStream;
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
	private ConexionCliente cc;
	private int idJugador;
	private int idRival;
	private int turnos;
	private List<Campeon> lc;
	private Jugador jug;
	private Jugador riv;
	
	public Cliente()
	{
		this.lc = new ArrayList<Campeon>();
		this.jug = new Jugador(lc);
	}

	public void conectarServidor()
	{
		this.cc = new ConexionCliente();
	}
	
	private class ConexionCliente
	{
		private Socket socket;
		private ObjectInputStream in;
		private ObjectOutputStream out;
		
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
	
	public void leerJugador()
	{
		try
		{
			this.jug =(Jugador) cc.in.readObject();
		}
		catch (ClassNotFoundException e)
		{
			e.printStackTrace();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}
	
	public void leerRival()
	{
		try
		{
			this.riv =(Jugador) cc.in.readObject();
		}
		catch (ClassNotFoundException e)
		{
			e.printStackTrace();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}
	
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
	
	public boolean juegoAcabado()
	{
		List<Jugador> jugadores = new ArrayList<>();
		jugadores.add(jug);
		jugadores.add(riv);
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
		try
		{
			Cliente c = new Cliente();
			c.conectarServidor();
			c.elegirCampeon();
			c.leerRival();
			
			while(!c.juegoAcabado())
			{
				c.mostrarPartida();
				c.turno();
				c.leerJugador();
				c.cc.out.writeInt(0);
				c.cc.out.flush();
				c.leerRival();
			}
		}
		 catch (Exception e)
		{
			e.printStackTrace();
		}
	}
}
