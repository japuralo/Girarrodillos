package Juego;

public class Ingeniero extends Campeon
{
	private String nom = "ING";
	private int construccion = 2;
	
	public Ingeniero(Jugador jug, Jugador riv)
	{
		super(jug,riv);
		this.danoCorona = 1;
		this.danoMuralla = 3;
		this.tABase = 4;
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
		super.accion();
		if(this.jugador.getPm()<10)
		{
			this.jugador.setPm(this.jugador.getPm() + this.construccion);
			System.out.println(this.nom+" construye "+this.construccion+" de Muralla.");
			if(this.jugador.getPm()>10) this.jugador.setPm(10);
		}
	}

	@Override
	public void mejorar()
	{
		if(nom.equals("ING"))
		{
			nom = "ING+";
			this.danoCorona = 2;
			this.danoMuralla = 5;
		}
		else if(nom.equals("ING+"))
		{
			nom = "ING++";
			this.danoCorona = 4;
			this.danoMuralla = 5;
			this.tABase = 3;
			this.turnosAtaque = 3;
		}
	}
}
