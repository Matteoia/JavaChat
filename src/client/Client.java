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
	private Semaphore attesaChiave = new Semaphore(0);
	private Sender sender;
	private Receiver receiver;
	private ClientApp grafica;
	private Semaphore messageS = new Semaphore(0);
	private String msg;
	private String[] users;
	private Semaphore usersSemaphore = new Semaphore(0);


	public Client(ClientApp grafica, String ip, int port) {
		try{
			this.grafica = grafica;
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
			attesaChiave.acquire();
			return chiaviF.get(nomeDestinatario);
		}catch (Exception e){
			e.printStackTrace();
		}
		return null;
	}

	public void addPublicKey(String nome, PublicKey key) {
		chiaviF.put(nome, key);
		attesaChiave.release();
	}

	public void free(){
		attesaChiave.release();
	}

	public void sendPublicKey(String nomeMittente) {
		this.sender.sendPublicKey(nomeMittente, chiavi.getPublic());
	}

	public boolean isClosed(){
		return socket.isClosed();
	}

	public void sendMessage(String msg) {
		this.msg = msg;
		messageS.release();
	}

	public void showMessage(String s) {
		grafica.showMessage(s);
	}

	public String getMessage() {
		try{
			if(this.msg == null){
				messageS.acquire();
				String tmp = msg;
				msg = null;
				return tmp;
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
			this.usersSemaphore.acquire();
			return this.users;
		}catch(Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public void usersReceived() {
		this.usersSemaphore.release();
	}

	public void setUsers(String[] users) {
		this.users = users;
	}
}
