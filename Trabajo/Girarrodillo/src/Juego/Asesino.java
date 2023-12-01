package Juego;

import java.util.List;

public class Asesino extends Campeon
{
	private String nom = "ASE";
	private int aturdimiento = 1;
	
	public Asesino(Jugador jug, Jugador riv)
	{
		super(jug,riv);
		this.danoCorona = 1;
		this.danoMuralla = 0;
		this.tABase = 3;
		this.turnosAtaque = this.tABase;
	}

	@Override
	public String getNom()
	{
		return this.nom;
	}

	@Override
	public void setNom(String n)
	{
		this.nom = n;
	}
	
	@Override
	public void accion()
	{
		System.out.println("¡"+this.getNom()+" ataca!");
		List<Campeon> enemigos = this.rival.getCampeones();
		if(enemigos.get(0).getTurnosAtaque() <= enemigos.get(1).getTurnosAtaque())
		{
			enemigos.get(0).setTurnosAtaque(enemigos.get(0).getTurnosAtaque() + this.aturdimiento);
			System.out.println("¡"+this.getNom()+" aturde a "+enemigos.get(0).getNom()+"!");
		}
		else
		{
			enemigos.get(1).setTurnosAtaque(enemigos.get(1).getTurnosAtaque() + this.aturdimiento);
			System.out.println("¡"+this.getNom()+" aturde a "+enemigos.get(1).getNom()+"!");
		}
		super.accion();
	}

	@Override
	public void mejorar()
	{
		if(nom.equals("ASE"))
		{
			nom = "ASE+";
			this.danoCorona = 2;
		}
		else if(nom.equals("ASE+"))
		{
			nom = "ASE++";
			this.aturdimiento = 2;
		}
		
	}
}
