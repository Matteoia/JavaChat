package grafica;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;

public class OnlineUsersBar extends JPanel {
    private ClientApp graphicClient;
    private JList<String> users = new JList<>();
    private Button button = new Button("Show Users");
    private MyListener listener = new MyListener();
    private SelectionListener sListener = new SelectionListener();


    public OnlineUsersBar(ClientApp graphicClient){
        this.graphicClient = graphicClient;
        this.setLayout(new BorderLayout());
        this.users.addListSelectionListener(sListener);
        this.button.addActionListener(listener);
        this.button.setPreferredSize(new Dimension((graphicClient.WIDTH*20)/100, graphicClient.HEIGHT/10));
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
                String[] tmp = graphicClient.getOnlineUsers();
                if(tmp != null){
                    DefaultListModel dlm = new DefaultListModel();
                    for(String user : tmp)
                        dlm.addElement(user);
                    users.setModel(dlm);
                }
            }
        }
    }

    private class SelectionListener implements ListSelectionListener {
        @Override
        public void valueChanged(ListSelectionEvent e) {
            graphicClient.loadUserChat(users.getSelectedValue());
        }
    }
}
