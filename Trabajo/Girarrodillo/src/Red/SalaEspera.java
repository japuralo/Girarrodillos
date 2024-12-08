package Red;

import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutorService;

public class SalaEspera implements Runnable
{
	public static List<ConexionServidor> clientes = Collections.synchronizedList(new ArrayList<>());	//Lista de clientes esperando a entrar en partida.
	public ExecutorService pool;																		//Pool de hilos.
	public Servidor serv;																				//Servidor al que pertenece la SalaEspera.
	
	public SalaEspera(ExecutorService es, Servidor s)
	{
		this.pool = es;
		this.serv = s;
	}
	
	//Añade un cliente a la lista de clientes en espera.
	public static void addCliente(ConexionServidor s)
	{
		clientes.add(s);
	}
	
	@Override
	public void run() 
	{
		while(true)
		{
			if(clientes.size() == 2)
			{
				Partida p = new Partida(new ArrayList<ConexionServidor>(clientes));
				serv.registrarPartida(p);
				pool.submit(p);
				clientes.clear();
			}
		}
	}
}
