package Juego;

import java.util.List;

public class Clerigo extends Campeon
{
	private String nom = "CLE";
	private int curacion = 1;
	private int inspiracion = 2;
	
	public Clerigo(Jugador jug, Jugador riv)
	{
		super(jug,riv);
		this.danoCorona = 0;
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
		System.out.println("¡"+this.getNom()+" actúa!");
		List<Campeon> campeones = this.jugador.getCampeones();
		for(Campeon c : campeones)
		{
			if(!c.equals(this))
			{
				c.setTurnosAtaque(c.getTurnosAtaque() - this.inspiracion);
				System.out.println("¡"+this.getNom()+" inspira a "+c.getNom()+"!");
				if(c.turnosAtaque <= 0)
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
		}
		if(this.jugador.getPc()<12)
		{
			this.jugador.setPc(this.jugador.getPc() + this.curacion);
			System.out.println("¡"+this.getNom()+" sana la Corona por "+this.curacion+" puntos!");
			if(this.jugador.getPc()>12) this.jugador.setPc(12);
		}
	}

	@Override
	public void mejorar()
	{
		if(nom.equals("CLE"))
		{
			nom = "CLE+";
			this.tABase = 3;
			this.turnosAtaque = 3;
			this.curacion = 2;
		}
		else if(nom.equals("CLE+"))
		{
			nom = "CLE++";
			this.inspiracion = 3;
		}
	}
}
