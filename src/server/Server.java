package server;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.Semaphore;

public class Server {
	public ServerSocket serverSocket;
	private final Utenti utenti;
	private Semaphore lettori = new Semaphore(1);;
	private Semaphore scrittori = new Semaphore(1);
	private int nLettori = 0;
	private static Server server;
	private Server(int porta) throws IOException {
		serverSocket = new ServerSocket(porta);
		utenti = new Utenti();
		GestoreComandi.setServer(this);
	}

	synchronized
	public static Server getServer(int porta) throws IOException {
		if(server == null){
			server = new Server(porta);
		}
		return server;
	}

	public void remove(String utente) {
		try {
			inizioScrittura();
			utenti.removeUtente(utente);
			fineScrittura();
		} catch (InterruptedException e) {
			throw new RuntimeException(e);
		}
	}

	private void start() {
		try {
			System.out.println("Avvio il server");
			ServerReceiver.setServer(this);

			while(!serverSocket.isClosed()) {
				Socket utente = serverSocket.accept();
				System.out.println(utente.getInetAddress()+" si e' connesso");
				new ServerReceiver(utente).start();
			}
		}catch(Exception e) {
			e.printStackTrace();
		}
	}

	public void inizioScrittura() throws InterruptedException {
		scrittori.acquire();
	}//inizioScrittura

	public void fineScrittura() {
		scrittori.release();
	}//fineScrittura

	public void inizioLettura() throws InterruptedException {
		lettori.acquire();
		nLettori++;
		if(nLettori == 1)
			scrittori.acquire();
		lettori.release();
	}
	public void fineLettura() throws InterruptedException {
		lettori.acquire();
		nLettori--;
		if(nLettori == 0)
			scrittori.release();
		lettori.release();
	}

	public ObjectOutputStream getUserStream(String utente) {
		ObjectOutputStream result = null;
		try {
			inizioLettura();
			result = utenti.getUserStream(utente);
			fineLettura();
		}catch (Exception e){
			e.printStackTrace();
		}
		return result;
	}

	public void addUtente(String u, ObjectOutputStream oos) {
		try{
			inizioScrittura();
			utenti.addUtente(u, oos);
			fineScrittura();
		}catch (Exception e){
			e.printStackTrace();
		}
	}

	public void removeUtente(String u){
		try{
			inizioScrittura();
			utenti.removeUtente(u);
			fineScrittura();
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	public String getOnlineUser() {
		String utentiOnline = "";
		try{
			inizioLettura();
			utentiOnline = utenti.getOnlineUser();
			fineLettura();
		}catch (Exception e){
			e.printStackTrace();
		}
		return utentiOnline;
	}

	public static void main(String[] args) throws Exception {
		Server s = new Server(5000);
		s.start();
	}
}