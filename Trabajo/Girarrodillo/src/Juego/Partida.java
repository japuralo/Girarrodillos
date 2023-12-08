package Juego;

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

public class Partida
{
	private List<Jugador> jugadores = new ArrayList<>();
	private int turno = 0;
	
	public Partida(Jugador j1, Jugador j2)
	{
		this.jugadores.add(j1);
		this.jugadores.add(j2);
	}
	
	public void empezarPartida()
	{
		Scanner in = new Scanner(System.in);
		String inp;
		
		for(int i = 0;i<2;i++)
		{
			System.out.println("Jugador"+(i+1)+":");
			for(int j = 0;j<2;j++)
			{
				boolean repetir = true;
				Campeon cam = null;
				while(repetir)
				{
					repetir = false;
					System.out.println("Elige un Campeón: Caballero(CAB), Mago(MAG), Arquero(ARQ), Ingeniero(ING), Asesino(ASE), Clérigo(CLE)");
					inp = in.nextLine();
					if(inp.equals("CAB")) cam = new Caballero(jugadores.get(i),jugadores.get((i+1)%2));
					else if(inp.equals("MAG")) cam = new Mago(jugadores.get(i),jugadores.get((i+1)%2));
					else if(inp.equals("ARQ")) cam = new Arquero(jugadores.get(i),jugadores.get((i+1)%2));
					else if(inp.equals("ING")) cam = new Ingeniero(jugadores.get(i),jugadores.get((i+1)%2));
					else if(inp.equals("ASE")) cam = new Asesino(jugadores.get(i),jugadores.get((i+1)%2));
					else if(inp.equals("CLE")) cam = new Clerigo(jugadores.get(i),jugadores.get((i+1)%2));
					else repetir = true;
				}
				jugadores.get(i).aniadirCampeon(cam);
			}
		}
		
		jugadores.get(0).mostrar();
		System.out.println("");
		jugadores.get(1).mostrar();
	}
	
	public void turno()
	{
		Jugador jaux = jugadores.get(turno % jugadores.size());
		System.out.println("");
		System.out.println("Turno del jugador "+((turno % jugadores.size())+1));
		for(int i=0;i<3;i++)
		{
			if(i!=0)jaux.bloquear();
			jaux.girar();
			jaux.mostrar();
		}
		calcularTurno(jaux);
		System.out.println("");
		jaux.mostrar();
		jaux.resetarCandados();
		if(!juegoAcabado()) turno++;
	}
	
	public boolean juegoAcabado()
	{
        for(Jugador j : this.jugadores)
        {
            if(j.getPc() <= 0)
            {
            	System.out.println("FINAL");
                return true;
            }
        }
        return false;
    }
	
	public void calcularTurno(Jugador j)
	{
		int[] calculo = new int[3];
		calculo[0] = 0;
		calculo[1] = 0;
		calculo[2] = 0;
		String aux = "";
		Rodillos r = j.getRodillos();
		List<Campeon> c = j.getCampeones();
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
		if(calculo[0] > 0)
		{
			c.get(0).turnosAtaque = c.get(0).turnosAtaque - calculo[0];
			if(c.get(0).getTurnosAtaque() < 0) c.get(0).setTurnosAtaque(0);
		}
		if(calculo[1] > 0)
		{
			c.get(1).turnosAtaque = c.get(1).turnosAtaque - calculo[1];
			if(c.get(1).getTurnosAtaque() < 0) c.get(1).setTurnosAtaque(0);
		}
		if(calculo[2] > 0 && j.getPm()<10)
		{
			j.setPm(j.getPm() + calculo[2]);
			System.out.println("");
			System.out.println(calculo[2]+" de Muralla construida.");
			if(j.getPm()>10) j.setPm(10);
		}
	}
	
	public void acciones()
	{
		System.out.println("----------------------------");
		System.out.println("");
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
	}
}