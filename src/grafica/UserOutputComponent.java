package grafica;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class UserOutputComponent extends JPanel {
    private ClientApp client;
    private JTextArea userData = new JTextArea();
    private Button sendButton = new Button("send");
    private ActionListener listener = new MyListener();

    public UserOutputComponent(ClientApp client){
        this.client = client;
        this.setLayout(new BorderLayout());
        this.setPreferredSize(new Dimension(client.WIDTH, client.HEIGHT/10));
        this.setBorder(BorderFactory.createLineBorder(Color.black));

        this.sendButton.setPreferredSize(new Dimension(client.WIDTH/10, client.HEIGHT/10));
        this.sendButton.addActionListener(listener);
        this.userData.setPreferredSize(new Dimension(client.WIDTH, client.HEIGHT/10));
        this.userData.setFont(client.font);

        this.add(userData, "Center");
        this.add(sendButton, "East");
    }

    public class MyListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            if(e.getActionCommand().equals("send")){
                String msg = userData.getText();
                userData.setText("");
                client.sendMessage(msg);
            }
        }
    }

}
