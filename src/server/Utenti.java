package server;

import java.io.ObjectOutputStream;
import java.util.HashMap;
import java.util.Map;

public class Utenti {
    private final Map<String, ObjectOutputStream> connessioni;
    public Utenti(){
        connessioni = new HashMap<>();
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

    public String getOnlineUser() {
        StringBuilder s = new StringBuilder();
        for(String key : connessioni.keySet())
            s.append(key).append("\n");
       return s.toString();
    }

}
