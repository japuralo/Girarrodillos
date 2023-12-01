package Juego;

public class Mago extends Campeon
{
	private String nom = "MAG";

	public Mago(Jugador jug, Jugador riv)
	{
		super(jug,riv);
		this.danoCorona = 1;
		this.danoMuralla = 1;
		this.tABase = 5;
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
		super.accion();
		super.accion();
		super.accion();
		if(nom.equals("MAG+") || nom.equals("MAG++"))
		{
			super.accion();
			super.accion();
		}
	}

	@Override
	public void mejorar()
	{
		if(nom.equals("MAG"))
		{
			nom = "MAG+";
		}
		else if(nom.equals("MAG+"))
		{
			nom = "MAG++";
			this.danoMuralla = 2;
		}
	}

}
