package server;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class GestoreComandi {
    private static Server server;
    private static String commandList;
    private static void getCommandList() throws IOException {
        StringBuilder sb = new StringBuilder();
        File f = new File("./src/javachat/documenti/Comandi.txt");
        System.out.println(f.getAbsolutePath());
        BufferedReader br = new BufferedReader(new FileReader(f));
        String tmp;
        while((tmp = br.readLine()) != null)
            sb.append(tmp).append("\n");
        commandList = sb.toString();
    }
    public static void setServer(Server server)  {
        GestoreComandi.server = server;
    }
    public static String gestisci(String comando) throws IOException{
        switch(comando){
            case "/h":  if(commandList == null)
                            getCommandList();
                        return commandList;
            case "/l":  return server.getOnlineUser();
            default :   return "Comando non riconosciuto";
        }
    }

}
