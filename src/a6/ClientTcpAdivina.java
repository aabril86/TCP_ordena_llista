package a6;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ClientTcpAdivina extends Thread {
	
	String hostname;
	int port;
	boolean continueConnected;
	int intents;

	
	public ClientTcpAdivina(String hostname, int port) {
		this.hostname = hostname;
		this.port = port;
		continueConnected = true;
		intents=0;
	}

	public void run() {

		Socket socket;
		List<Integer> numberList = new ArrayList<>();

		//OMPLO LA LLISTA AMB NUMEROS ALEATORIS
		for (int i = 0; i < 5; i++) {
			numberList.add((int)(Math.random() * 10));
		}

		//crear llista
		Llista llista = new Llista("llista", numberList);
		//veure llista creada
		System.out.println("Llista a enviar:\n" + llista.getNom());
		for (Integer i:llista.getNumberList()) {
			System.out.println(i);
		}
		try {
			socket = new Socket(InetAddress.getByName(hostname), port);

			ObjectOutputStream outToServer = new ObjectOutputStream(socket.getOutputStream());
			ObjectInputStream inFromServer = new ObjectInputStream(socket.getInputStream());

			//enviar llista
			outToServer.writeObject(llista);
			//rebre resposta
			Llista llistaFromServer = (Llista) inFromServer.readObject();
			//mostrar resposta
			System.out.println("RESPOSTA SERVIDOR:\n" + llistaFromServer.getNom());
			for (Integer i:llistaFromServer.getNumberList()) {
				System.out.println(i);
			}
		 	close(socket);

		} catch (UnknownHostException ex) {
			System.out.println("Error de connexió. No existeix el host: " + ex.getMessage());
		} catch (IOException ex) {
			System.out.println("Error de connexió indefinit: " + ex.getMessage());
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}

	}

	private void close(Socket socket){
		//si falla el tancament no podem fer gaire cosa, només enregistrar
		//el problema
		try {
			//tancament de tots els recursos
			if(socket!=null && !socket.isClosed()){
				if(!socket.isInputShutdown()){
					socket.shutdownInput();
				}
				if(!socket.isOutputShutdown()){
					socket.shutdownOutput();
				}
				socket.close();
			}
		} catch (IOException ex) {
			//enregistrem l'error amb un objecte Logger
			Logger.getLogger(ClientTcpAdivina.class.getName()).log(Level.SEVERE, null, ex);
		}
	}
	
	public static void main(String[] args) {
        ClientTcpAdivina clientTcp = new ClientTcpAdivina("localhost",5558);
        clientTcp.start();
	}
}
