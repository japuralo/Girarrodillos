package Campeones;

import java.io.Serializable;

import Jugador.Jugador;

public abstract class Campeon implements Serializable
{
	private static final long serialVersionUID = 1L;
	public int danoCorona;
	public int danoMuralla;
	public int turnosAtaque;
	public int tABase;
	public Jugador jugador;
	public Jugador rival;
	private int subidaNivel = 2;
	
	public Campeon(Jugador jug, Jugador riv)
	{
		this.jugador = jug;
		this.rival = riv;
	}
	
	public abstract String getNom();
	
	public abstract void setNom(String n);
	
	public int getDanoCorona()
	{
		return this.danoCorona;
	}
	
	public void setDanoCorona(int dc)
	{
		this.danoCorona = dc;
	}
	
	public int getDanoMuralla()
	{
		return this.danoMuralla;
	}
	
	public void setDanoMuralla(int dm)
	{
		this.danoMuralla = dm;
	}
	
	public int getTurnosAtaque()
	{
		return this.turnosAtaque;
	}
	
	public void setTurnosAtaque(int ta)
	{
		this.turnosAtaque = ta;
	}
	
	public Jugador getJugador()
	{
		return this.jugador;
	}

	public void setJugador(Jugador jugador)
	{
		this.jugador = jugador;
	}

	public int getSubidaNivel()
	{
		return subidaNivel;
	}

	public void setSubidaNivel(int subidaNivel)
	{
		this.subidaNivel = subidaNivel;
	}

	public String toString()
	{
		String aux;
		aux = this.getNom() + "(" + this.getTurnosAtaque() + ")";
		return aux;
	}
	
	public void resetearTA()
	{
		this.turnosAtaque = this.tABase;
	}
	
	public void accion()
	{
		if(this.rival.getPm() > 0 && !this.getNom().equals("ASE") && !this.getNom().equals("ASE+") && !this.getNom().equals("ASE++"))
		{
			this.rival.setPm(this.rival.getPm() - this.danoMuralla);
			System.out.println("¡"+this.danoMuralla+" daño a Muralla!");
			if(this.rival.getPm()<0) this.rival.setPm(0);
		}
		else
		{
			this.rival.setPc(this.rival.getPc() - this.danoCorona);
			System.out.println("¡"+this.danoCorona+" daño a Corona!");
		}
	}
	
	public abstract void mejorar();
}
