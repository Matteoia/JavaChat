package client;

import security.AsymmetricEncr;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.security.PublicKey;
import java.util.Scanner;

public class Sender extends Thread {
	private Client client;
	private Scanner sc;
	private ObjectOutputStream serverOutput;
	
	public Sender(Client client) throws IOException {
		this.client = client;
		this.sc = new Scanner(System.in);
		this.serverOutput = new ObjectOutputStream(client.getOutputStream());
	}


	public void makePublicKeyRequest(String nomeDestinatario) {
		try {
			serverOutput.writeObject("PublicKeyRequest:to:"+nomeDestinatario);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void sendPublicKey(String destinatario, PublicKey aPublic) {
		try {
			serverOutput.writeObject("PublicKeyResponse:to:"+destinatario);
			serverOutput.writeObject(aPublic);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void run() {
		try {
			String msg;
			this.client.showMessage("Inserisci il tuo nome");
			msg = this.client.getMessage();
			serverOutput.writeObject("UserName:from:"+msg);

			while (!this.client.isClosed()) {
				msg = this.client.getMessage();
				String[] info = msg.split(":to:");
				if (info.length == 2) {
					String testo = info[0];
					String nomeDestinatario = info[1].replaceAll("\\s+", "");
					PublicKey chiaveDestinatario = this.client.getDestPublicKey(nomeDestinatario);
					if (chiaveDestinatario != null) {
						byte[] messaggioCriptato = AsymmetricEncr.cripta(testo, chiaveDestinatario);
						serverOutput.writeObject("CriptedMessage:to:" + nomeDestinatario);
						serverOutput.writeObject(messaggioCriptato);
					}
				} else
					this.serverOutput.writeObject(msg);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}