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

	public void makeOnlineUsersRequest() {
		try {
			serverOutput.writeObject("UsersListRequest:to:ServerSender");
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
			String msg, dest, data;
			this.client.user = client.getMessage();
			serverOutput.writeObject("RegisterRequest:to:"+this.client.dest);
			serverOutput.writeObject(this.client.user);

			while (!this.client.isClosed()) {
				msg = this.client.getMessage();
				dest = client.dest;
				data = msg.split(":to:")[0];

				if(dest.equals("ServerSender")) {
					serverOutput.writeObject(msg);
				} else {
					PublicKey destKey = this.client.getDestPublicKey(dest);
					if (dest != null) {
						byte[] messaggioCriptato = AsymmetricEncr.cripta(data, destKey);
						client.showMessage(data, this.client.user);
						serverOutput.writeObject("CriptedMessage:to:"+dest);
						serverOutput.writeObject(messaggioCriptato);
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}