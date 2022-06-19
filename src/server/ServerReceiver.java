package server;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.security.PublicKey;


public class ServerReceiver extends Thread {
	private static Server server;
	private String utente;
	private Socket sockUtente;
	private ObjectInputStream utenteInput;
	private ObjectOutputStream utenteOutput;

	public ServerReceiver(Socket sockUtente) throws Exception {
		this.sockUtente = sockUtente ;
		utenteInput = new ObjectInputStream(this.sockUtente.getInputStream());
		utenteOutput = new ObjectOutputStream(this.sockUtente.getOutputStream());
	}

	public static void setServer(Server server) {
		ServerReceiver.server = server;
	}

	
	public void listen() throws Exception{
		Object ob = utenteInput.readObject();
		if(ob instanceof String ) {
			String data = (String)ob;
			String[] dataV = data.split(":to:");
			if(dataV.length == 2){
				String msg = dataV[0];
				String destinatario = dataV[1].replaceAll("\\s+", "");
				ObjectOutputStream streamDestinatario = server.getUserStream(destinatario);
				if(streamDestinatario == null){
					utenteOutput.writeObject("UserNotFound:from:ServerSender");
				}else{
					if(msg.equals("PublicKeyRequest")){
						streamDestinatario.writeObject(msg+":from:"+this.utente);
					}if(msg.equals("PublicKeyResponse")){
						PublicKey key = (PublicKey) utenteInput.readObject();
						streamDestinatario.writeObject(msg+":from:"+this.utente);
						streamDestinatario.writeObject(key);
					}if(msg.equals("CriptedMessage")){
						byte[] criptato = (byte[])utenteInput.readObject();
						streamDestinatario.writeObject(msg+":from:"+this.utente);
						streamDestinatario.writeObject(criptato);
					}
				}
			}else{
				utenteOutput.writeObject("CommandResponse:from:ServerSender");
				utenteOutput.writeObject(GestoreComandi.gestisci(data));
			}
		}
	}

	private void registerUser() throws Exception{
		Object ob = utenteInput.readObject();
		if(ob instanceof String ) {
			String data = (String)ob;
			if(this.utente == null && data.contains("UserName:from:")){
				this.utente = data.split("UserName:from:")[1];
				server.addUtente(this.utente, utenteOutput);
				utenteOutput.writeObject("WelcomeResponse:from:ServerSender");
				utenteOutput.writeObject("Benvenuto/a "+utente+"\n" +
						"Per la lista dei comandi scrivere /h");
			}
		}
	}


	@Override
	public void run() {
		try {
			registerUser();
			while(!sockUtente.isClosed() || !server.serverSocket.isClosed())
				listen();
		}catch(Exception e) {
			server.remove(utente);
			System.out.println("utente disconnesso");
		}
	}
}