package grafica;

import javax.swing.*;
import javax.swing.text.DefaultCaret;
import java.awt.*;
import java.util.HashMap;

public class UserInputComponent extends JPanel {
    private JScrollPane scroll;
    private JTextPane currentChat = new JTextPane();
    private HashMap<String, String> chats = new HashMap<>();
    private final Font font = new Font("SansSerif", Font.BOLD, 17);
    private final int HEIGHT = 400, WIDTH = 560;

    public UserInputComponent(){
        this.setPreferredSize(new Dimension(WIDTH,HEIGHT));
        this.currentChat.setFont(font);
        this.currentChat.setEditable(false);
        this.currentChat.setPreferredSize(new Dimension(WIDTH, HEIGHT));

        this.scroll = new JScrollPane(currentChat, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        ((DefaultCaret)currentChat.getCaret()).setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);

        this.add(scroll);
    }

    public void setText(String msg, String dest) {
        if(!chats.containsKey(dest))
            addChat(dest);
        String tmp = chats.get(dest);
        chats.put(dest, tmp + "\n" +dest+":"+"\n"+msg);
        currentChat.setText(chats.get(dest));
    }

    public void setText(String msg, String dest, String user){
        if(!chats.containsKey(dest))
            addChat(dest);
        String tmp = chats.get(dest);
        chats.put(dest, tmp + "\n" +user+":"+"\n"+msg);
        currentChat.setText(chats.get(dest));

    }

    public void addChat(String user){
        JTextPane textPane = new JTextPane();
        textPane.setText(user+" chat:");
        textPane.setEditable(false);
        textPane.setFont(font);
        textPane.setEditable(false);
        textPane.setPreferredSize(new Dimension(WIDTH, HEIGHT));
        chats.put(user, "");
    }

    public void loadUserChat(String user) {
        System.out.println("Carico la chat con "+user);
        if(!chats.containsKey(user)) addChat(user);
        this.currentChat.setText(chats.get(user));
        this.currentChat.repaint();
        this.repaint();
    }
}
