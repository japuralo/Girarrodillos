package Red;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

import Campeones.Campeon;
import Jugador.Jugador;

public class Servidor
{
	private ServerSocket servidor;
	
	public Servidor()
	{
		System.out.println("Servidor iniciado.");
		//this.numJugadores = 0;
		try
		{
			this.servidor = new ServerSocket(9876);
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
			List<Socket> clientes = new ArrayList<>();
			while(true)
			{
				Socket cli = servidor.accept();
				clientes.add(cli);
				
				if(clientes.size() == 2)
				{
					Partida p = new Partida(new ArrayList<>(clientes));
					pool.submit(p);
					clientes.clear();
				}
			}
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args)
	{
		ExecutorService pool = Executors.newCachedThreadPool();
		try
		{
			Servidor serv = new Servidor();
			serv.aceptarConexion(pool);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
}