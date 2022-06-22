package server;

import java.io.ObjectOutputStream;
import java.util.HashMap;
import java.util.Map;

public class Utenti {
    private final Map<String, ObjectOutputStream> connessioni;
    public Utenti(){
        connessioni = new HashMap<>();
        connessioni.put("ServerSender", null);
    }
    public ObjectOutputStream getUserStream(String utente) {
        return connessioni.get(utente);
    }
    public void addUtente(String u, ObjectOutputStream oos) {
        connessioni.put(u, oos);
    }

    public void removeUtente(String u){
        connessioni.remove(u);
    }

    public String[] getOnlineUser() {
        return connessioni.keySet().toArray(new String[0]);
    }

}
