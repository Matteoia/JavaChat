package client;

import security.AsymmetricEncr;

import java.io.ObjectInputStream;
import java.security.PublicKey;

public class Receiver extends Thread {
	private Client client;
	private Object obj;
	private ObjectInputStream serverInput;
	
	public Receiver(Client client) throws Exception {
		this.client = client;
		this.serverInput = new ObjectInputStream(client.getInputStream());
	}
	
	@Override
	public void run() {
		try {
			while(!client.isClosed()) {
				obj = serverInput.readObject();
				if(obj instanceof String) {
					String msg = (String)obj;
					String[] data = msg.split(":from:");
					if(data.length == 2){
						String messaggio = data[0];
						String nomeMittente = data[1];
						if(nomeMittente.equals("ServerSender")){
							if(messaggio.equals("UsersListResponse")){
								client.setUsers((String[])serverInput.readObject());
								client.autoDataReceived();
							}if(messaggio.equals("StringResponse"))
								client.showMessage((String)serverInput.readObject(), nomeMittente);
						}else{
							if(messaggio.equals("PublicKeyRequest")){
								client.sendPublicKey(nomeMittente);
							}if(messaggio.equals("PublicKeyResponse")){
								PublicKey key = (PublicKey) serverInput.readObject();
								client.addPublicKey(nomeMittente, key);
							}if(messaggio.equals("CriptedMessage")){
								byte[] messaggioCifrato = (byte[])serverInput.readObject();
								String messaggioInChiaro = AsymmetricEncr.decripta(messaggioCifrato, client.getPrivateKey());
								client.showMessage(messaggioInChiaro, nomeMittente);
							}
						}
					}
				}
			}

		}catch(Exception e) {
			System.err.println(e);
		}
	}
	
}