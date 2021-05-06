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
		Collections.sort(l.getNumberList());
		Set set = new HashSet(l.getNumberList());
		List list = new ArrayList(set);
		l.getNumberList().clear();
		l.getNumberList().addAll(list);
		acabat = true;
		return l;
	}

}
