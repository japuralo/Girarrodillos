package Jugador;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import Campeones.Campeon;

public class Jugador implements Serializable
{
	private static final long serialVersionUID = 1L;
	public List<Campeon> campeones; 	//Campeones del jugador.
	private Rodillos rodillos;			//Rodillos del jugador.
	public int pc = 10;				//Puntos de Corona (vida)
	public int pm = 0;					//Puntos de Muralla (armadura)
	
	public Jugador(List<Campeon> cm)
	{
		this.campeones = cm;
		this.rodillos = new Rodillos();
	}
	
	public List<Campeon> getCampeones()
	{
		return this.campeones;
	}
	
	public void setCampeones(List<Campeon> cm)
	{
		this.campeones = cm;
	}
	
	public Rodillos getRodillos()
	{
		return this.rodillos;
	}
	
	public void setRodillos(Rodillos rs)
	{
		this.rodillos = rs;
	}
	
	public int getPc()
	{
		return this.pc;
	}

	public void setPc(int pc)
	{
		this.pc = pc;
	}

	public int getPm()
	{
		return this.pm;
	}

	public void setPm(int pm)
	{
		this.pm = pm;
	}
	
	//Añade un campeón a la lista del Jugador.
	public void aniadirCampeon(Campeon cm)
	{
		this.campeones.add(cm);
	}

	//Permite des/bloquear uno o varios rodillos.
	public void bloquear()
	{
		Scanner in = new Scanner(System.in);
		String inp;
		String[] rod;
		System.out.println("¿Bloquear/Desbloquear? (1,2,3,4,5)");
		inp = in.nextLine();
		rod = inp.split(",");
		for(String r : rod)
		{
			if(r.equals("1")) rodillos.cambiarR1();
			if(r.equals("2")) rodillos.cambiarR2();
			if(r.equals("3")) rodillos.cambiarR3();
			if(r.equals("4")) rodillos.cambiarR4();
			if(r.equals("5")) rodillos.cambiarR5();
		}
	}
	
	//Gira los rodillos.
	public void girar()
	{
		Scanner in = new Scanner(System.in);
		String inp;
		System.out.println("Presiona Intro para girar los rodillos.");
		inp = in.nextLine();
		rodillos.girar();
	}
	
	//Desbloquea todos los Rodillos.
	public void resetarCandados()
	{
		rodillos.setCandado1(false);
		rodillos.setCandado2(false);
		rodillos.setCandado3(false);
		rodillos.setCandado4(false);
		rodillos.setCandado5(false);
	}
	
	//Muestra toda la información del Jugador.
	public void mostrar()
	{
		System.out.println("PC: "+this.pc+"   PM: "+this.pm);
		System.out.println(this.campeones.get(0).toString() + "   " + this.rodillos.toString() + "   " + this.campeones.get(1).toString());
	}
}
