package Red;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.*;

import Campeones.Campeon;
import Jugador.Jugador;

public class Servidor
{
	private ServerSocket servidor;
	public ConcurrentHashMap<Integer, Partida> partidas;	//Guarda las partidas que se están jugando / han jugado.
	
	public Servidor()
	{
		System.out.println("Servidor iniciado.");
		//this.numJugadores = 0;
		try
		{
			this.servidor = new ServerSocket(9876);
			this.partidas = new ConcurrentHashMap<>();
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}
	}
	
	//Acepta conexiones de Clientes hasta que haya 2 conectados y no más.
	//Comienza los hilos que gestionan a cada Cliente.
	public void aceptarConexion(ExecutorService pool)
	{
		System.out.println("Esperando conexiones.");
		try
		{
			while(true)
			{
				Socket cli = servidor.accept();
				ConexionServidor cs = new ConexionServidor(cli);
				cs.setServidor(this);
				pool.submit(cs);
			}
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}
	}
	
	//Registra una partida nueva con un número de identificación.
	public int registrarPartida(Partida p)
	{
		int i = 1;
		boolean asignado = false;
		while(partidas.containsKey(i))
		{
			i++;
		}
		this.partidas.put(i, p);
		
		return i;
	}
	
	//Muestra las partidas almacenadas.
	public void mostrarPartidas()
	{
		for(Entry<Integer, Partida> p : partidas.entrySet())
		{
			p.getValue().mostrarPartida();
		}
	}
	
	public static void main(String[] args)
	{
		ExecutorService pool = Executors.newCachedThreadPool();
		try
		{
			Servidor serv = new Servidor();
			SalaEspera se = new SalaEspera(pool, serv);
			pool.submit(se);
			serv.aceptarConexion(pool);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
}