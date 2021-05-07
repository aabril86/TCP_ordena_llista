package a6;

import java.io.*;
import java.net.Socket;
import java.util.*;

public class ThreadSevidorAdivina implements Runnable {
/* Thread que gestiona la comunicació de SrvTcPAdivina.java i un cllient ClientTcpAdivina.java */
	
	Socket clientSocket = null;
	Llista msgEntrant;
	Llista msgSortint;

	boolean acabat;
	
	public ThreadSevidorAdivina(Socket clientSocket) throws IOException {
		this.clientSocket = clientSocket;
		acabat = false;
	}

	@Override
	public void run() {
		try {
			ObjectOutputStream outToClient = new ObjectOutputStream(clientSocket.getOutputStream());
			ObjectInputStream inFromClient = new ObjectInputStream(clientSocket.getInputStream());
			while(!acabat) {
				msgEntrant = (Llista)inFromClient.readObject();
				msgSortint = generaResposta(msgEntrant);
				outToClient.writeObject(msgSortint);
				
			}
		}catch(IOException | ClassNotFoundException e){
			System.out.println(e.getLocalizedMessage());
		}

		try {
			clientSocket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public Llista generaResposta(Llista l) {
		//al convertir-la a HashSet s'ordena i s'eliminan els elements repetits
		Set set = new HashSet(l.getNumberList());
		//Creo una altra amb el contingut del HashSet i substitueixo la llista original amb el contingut modificat
		List list = new ArrayList(set);
		l.getNumberList().clear();
		l.getNumberList().addAll(list);
		acabat = true;
		return l;
	}

}
