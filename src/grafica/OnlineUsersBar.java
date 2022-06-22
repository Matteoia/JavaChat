package grafica;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;

public class OnlineUsersBar extends JPanel {
    private ClientApp client;
    private JList<String> users = new JList<>();
    private Button button = new Button("Show Users");
    private MyListener listener = new MyListener();


    public OnlineUsersBar(ClientApp client){
        this.client = client;
        this.setLayout(new BorderLayout());

        this.button.addActionListener(listener);
        this.button.setPreferredSize(new Dimension((client.WIDTH*20)/100, client.HEIGHT/10));
        this.add(button, "North");
        this.add(users, "Center");

        this.setVisible(true);
    }

    public String getSelectedUser() {
        return this.users.getSelectedValue();
    }

    private class MyListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            if(e.getActionCommand().equals("Show Users")){
                String[] tmp = client.getOnlineUsers();
                if(tmp != null){
                    DefaultListModel dlm = new DefaultListModel();
                    for(String user : tmp)
                        dlm.addElement(user);
                    users.setModel(dlm);
                }
            }
        }
    }
}
