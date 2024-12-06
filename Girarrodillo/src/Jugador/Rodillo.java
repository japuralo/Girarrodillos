package Jugador;

import java.util.Random;

public enum Rodillo
{
	I,II,III,D,DD,DDD,X,M,MM,MMM;	//Posibles valores de un Rodillo.
	
	private static final Random azar = new Random();
	
	//El Rodillo toma un valor al azar de entre los disponibles.
	public static Rodillo rodilloAleatorio()
	{
		Rodillo[] rodillos = values();
		Rodillo r = rodillos[azar.nextInt(rodillos.length)];
		if(r == Rodillo.DD || r == Rodillo.DDD || r == Rodillo.II || r == Rodillo.III || r == Rodillo.MM || r == Rodillo.MMM)
		{
			r = rodillos[azar.nextInt(rodillos.length)];
		}
		if(r == Rodillo.DDD || r == Rodillo.III || r == Rodillo.MMM)
		{
			r = rodillos[azar.nextInt(rodillos.length)];
		}
		return r;
	}
}
