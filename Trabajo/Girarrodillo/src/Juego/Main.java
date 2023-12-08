package Juego;

import java.util.ArrayList;
import java.util.List;

import Campeones.Campeon;
import Jugador.Jugador;

public class Main
{
	public static void main(String[] args)
	{
		List<Campeon> lc1 = new ArrayList<>();
		List<Campeon> lc2 = new ArrayList<>();
		Jugador j1 = new Jugador(lc1);
		Jugador j2 = new Jugador(lc2);
		
		Partida p = new Partida(j1,j2);
		
		p.empezarPartida();
		int i = 0;
		while(!p.juegoAcabado())
		{
			p.turno();
			i++;
			if(i%2 == 0) p.acciones();
		}
	}
}
