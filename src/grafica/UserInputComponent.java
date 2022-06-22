package grafica;

import javax.swing.*;
import javax.swing.text.DefaultCaret;
import java.awt.*;
import java.util.HashMap;

public class UserInputComponent extends JPanel {
    private ClientApp client;
    private JScrollPane scroll;
    private JTextPane dataReceived = new JTextPane();
    private HashMap<String, JTextPane> chats = new HashMap<>();

    public UserInputComponent(ClientApp client){
        this.client = client;
        this.setPreferredSize(new Dimension((client.WIDTH*7)/10, (client.HEIGHT*8)/10));

        this.dataReceived.setFont(client.font);
        this.dataReceived.setEditable(false);
        this.dataReceived.setPreferredSize(new Dimension((client.WIDTH*7)/10, (client.HEIGHT*8)/10));

        this.scroll = new JScrollPane(dataReceived, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);

        ((DefaultCaret)dataReceived.getCaret()).setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);

        this.add(scroll);
    }

    public void addChat(String user){
        this.chats.put(user, new JTextPane());
    }


    public void getChat(String user){
        if(!this.chats.containsKey(user))
            addChat(user);
        this.dataReceived = this.chats.get(user);
    }

    public void setText(String msg) {
        String tmp = this.dataReceived.getText();
        if ((tmp.matches(""))) this.dataReceived.setText(msg);
        else this.dataReceived.setText(tmp + "\n" + msg);
    }
}
