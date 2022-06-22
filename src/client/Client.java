package client;

import grafica.ClientApp;
import security.AsymmetricEncr;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.HashMap;
import java.util.concurrent.Semaphore;

public class Client {
	private Socket socket;
	private KeyPair chiavi;
	private HashMap<String, PublicKey> chiaviF = new HashMap<>();
	private Sender sender;
	private Receiver receiver;
	private ClientApp graphicClient;
	private Semaphore userDataSem = new Semaphore(0), autoDataSem = new Semaphore(0);
	private String[] users;
	public String user, dest, msg;


	public Client(ClientApp graphicClient, String ip, int port) {
		try{
			this.graphicClient = graphicClient;
			this.socket = new Socket(ip, port);
			this.chiavi = AsymmetricEncr.generaChiavi();
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	public PrivateKey getPrivateKey(){
		return chiavi.getPrivate();
	}

	public OutputStream getOutputStream(){
		try{
			return socket.getOutputStream();
		}catch(IOException e){
			e.printStackTrace();
		}
		return null;
	}

	public InputStream getInputStream() {
		try{
			return socket.getInputStream();
		}catch(IOException e){
			e.printStackTrace();
		}
		return null;
	}

	public PublicKey getDestPublicKey(String nomeDestinatario){
		try{
			PublicKey ris = chiaviF.get(nomeDestinatario);
			if(ris != null)
				return ris;
			sender.makePublicKeyRequest(nomeDestinatario);
			autoDataSem.acquire();
			return chiaviF.get(nomeDestinatario);
		}catch (Exception e){
			e.printStackTrace();
		}
		return null;
	}

	public void addPublicKey(String nome, PublicKey key) {
		chiaviF.put(nome, key);
		autoDataSem.release();
	}

	public void sendPublicKey(String nomeMittente) {
		this.sender.sendPublicKey(nomeMittente, chiavi.getPublic());
	}

	public boolean isClosed(){
		return socket.isClosed();
	}

	public void setMessage(String msg) {
		this.msg = msg;
		userDataSem.release();
	}

	public void showMessage(String s) {
		graphicClient.showMessage(s);
	}

	public String getMessage() {
		try{
			this.msg = null;
			if(this.msg == null){
				userDataSem.acquire();
				return this.msg;
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return null;
	}

	public void start() {
		try{
			this.sender = new Sender(this);
			this.receiver = new Receiver(this);
			this.sender.start();
			this.receiver.start();
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	public String[] getOnlineUsers() {
		try{
			this.sender.makeOnlineUsersRequest();
			this.autoDataSem.acquire();
			return this.users;
		}catch(Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public void autoDataReceived(){
		this.autoDataSem.release();
	}

	public void setUsers(String[] users) {
		this.users = users;
	}

	public void setDest(String dest) {
		this.dest = dest;
	}
}
