package Jugador;

import java.util.ArrayList;
import java.util.List;

public class Rodillos
{
	private Rodillo r1;
	private Rodillo r2;
	private Rodillo r3;
	private Rodillo r4;
	private Rodillo r5;
	private boolean candado1 = false;
	private boolean candado2 = false;
	private boolean candado3 = false;
	private boolean candado4 = false;
	private boolean candado5 = false;
	
	public Rodillos()
	{
		this.r1 = Rodillo.X;
		this.r2 = Rodillo.X;
		this.r3 = Rodillo.X;
		this.r4 = Rodillo.X;
		this.r5 = Rodillo.X;
	}
	
	public Rodillo getR1()
	{
		return this.r1;
	}
	
	public void setR1(Rodillo r)
	{
		this.r1=r;
	}
	
	public Rodillo getR2()
	{
		return this.r2;
	}
	
	public void setR2(Rodillo r)
	{
		this.r2=r;
	}
	
	public Rodillo getR3()
	{
		return this.r3;
	}
	
	public void setR3(Rodillo r)
	{
		this.r3=r;
	}
	
	public Rodillo getR4()
	{
		return this.r4;
	}
	
	public void setR4(Rodillo r)
	{
		this.r4=r;
	}
	
	public Rodillo getR5()
	{
		return this.r5;
	}
	
	public void setR5(Rodillo r)
	{
		this.r5=r;
	}
	
	public boolean getCandado1()
	{
		return this.candado1;
	}
	
	public void setCandado1(boolean c)
	{
		this.candado1 = c;
	}
	
	public boolean getCandado2()
	{
		return this.candado2;
	}
	
	public void setCandado2(boolean c)
	{
		this.candado2 = c;
	}
	
	public boolean getCandado3()
	{
		return this.candado3;
	}
	
	public void setCandado3(boolean c)
	{
		this.candado3 = c;
	}
	
	public boolean getCandado4()
	{
		return this.candado4;
	}
	
	public void setCandado4(boolean c)
	{
		this.candado4 = c;
	}
	
	public boolean getCandado5()
	{
		return this.candado5;
	}
	
	public void setCandado5(boolean c)
	{
		this.candado5 = c;
	}
	
	public void cambiarR1()
	{
		this.candado1 = !this.candado1;
	}
	
	public void cambiarR2()
	{
		this.candado2 = !this.candado2;
	}
	
	public void cambiarR3()
	{
		this.candado3 = !this.candado3;
	}
	
	public void cambiarR4()
	{
		this.candado4 = !this.candado4;
	}
	
	public void cambiarR5()
	{
		this.candado5 = !this.candado5;
	}
	
	public void girar()
	{
		if(!candado1) this.r1 = Rodillo.rodilloAleatorio();
		if(!candado2) this.r2 = Rodillo.rodilloAleatorio();
		if(!candado3) this.r3 = Rodillo.rodilloAleatorio();
		if(!candado4) this.r4 = Rodillo.rodilloAleatorio();
		if(!candado5) this.r5 = Rodillo.rodilloAleatorio();
	}
	
	public String toString()
	{
		String aux;
		String c1,c2,c3,c4,c5;
		if(this.candado1) c1 = "b";
		else c1 = "d";
		if(this.candado2) c2 = "b";
		else c2 = "d";
		if(this.candado3) c3 = "b";
		else c3 = "d";
		if(this.candado4) c4 = "b";
		else c4 = "d";
		if(this.candado5) c5 = "b";
		else c5 = "d";
		aux = r1.toString()+"("+c1+")/"+r2.toString()+"("+c2+")/"+r3.toString()+"("+c3+")/"+r4.toString()+"("+c4+")/"+r5.toString()+"("+c5+")";
		return aux;
	}
}
