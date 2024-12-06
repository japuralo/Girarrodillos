package Campeones;

import java.io.Serializable;

import Jugador.Jugador;

public class Caballero extends Campeon implements Serializable
{
	private String nom = "CAB";
	
	public Caballero(Jugador jug, Jugador riv)
	{
		super(jug,riv);
		this.danoCorona = 3;
		this.danoMuralla = 3;
		this.tABase = 3;
		this.turnosAtaque = this.tABase;
	}
	
	public String getNom()
	{
		return nom;
	}

	public void setNom(String n)
	{
		this.nom = n;
	}

	@Override
	public void accion()
	{
		System.out.println("¡"+this.getNom()+" ataca!");
		super.accion();
	}

	@Override
	public void mejorar()
	{
		if(nom.equals("CAB"))
		{
			nom = "CAB+";
			this.danoCorona = 5;
			this.danoMuralla = 5;
		}
		else if(nom.equals("CAB+"))
		{
			nom = "CAB++";
			this.danoCorona = 7;
			this.danoMuralla = 5;
		}
	}
}
