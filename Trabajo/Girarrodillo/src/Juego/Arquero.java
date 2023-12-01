package Juego;

public class Arquero extends Campeon
{
	private String nom = "ARQ";

	public Arquero(Jugador jug, Jugador riv)
	{
		super(jug,riv);
		this.danoCorona = 3;
		this.danoMuralla = 2;
		this.tABase = 4;
		this.turnosAtaque = this.tABase;
	}
	
	@Override
	public String getNom()
	{
		return nom;
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
		super.accion();
		if(this.tABase > 2)this.tABase--;
	}

	@Override
	public void mejorar()
	{
		if(nom.equals("ARQ"))
		{
			nom = "ARQ+";
			this.danoCorona = 4;
			this.danoMuralla = 3;
		}
		else if(nom.equals("ARQ+"))
		{
			nom = "ARQ++";
			this.danoCorona = 6;
			this.danoMuralla = 4;
		}
	}
}
