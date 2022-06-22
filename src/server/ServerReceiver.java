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
			System.out.println(data);
			String[] dataV = data.split(":to:");
			if(dataV.length == 2){
				String msg = dataV[0];
				String destinatario = dataV[1].replaceAll("\\s+", "");
				if(destinatario.equals("ServerSender")){
					if(msg.equals("UsersListRequest")){
						utenteOutput.writeObject("UsersListResponse:from:ServerSender");
						utenteOutput.writeObject(server.getOnlineUsers());
					}
					if(msg.equals("RegisterRequest")){
						this.utente = (String)utenteInput.readObject();
						this.server.addUtente(utente, utenteOutput);
						this.utenteOutput.writeObject("StringResponse:from:ServerSender");
						this.utenteOutput.writeObject("Benvenuto/a "+this.utente);
					}
				}else{
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
				}
			}
		}
	}



	@Override
	public void run() {
		try {
			utenteOutput.writeObject("StringResponse:from:ServerSender");
			utenteOutput.writeObject("Inserisci il tuo nickname per iniziare");
			while(!sockUtente.isClosed() || !server.serverSocket.isClosed())
				listen();
		}catch(Exception e) {
			server.removeUtente(utente);
			System.out.println("utente disconnesso");
		}
	}
}