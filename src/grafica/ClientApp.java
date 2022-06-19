package grafica;

import client.Client;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ClientApp extends JFrame {
    private Client client;
    private final int HEIGHT = 500;
    private final int WIDTH = 800;
    private JTextPane dataReceived = new JTextPane();
    private JPanel bottom = new JPanel(new BorderLayout());
    private JTextPane userData = new JTextPane();
    private Button button = new Button("INVIA");
    private ActionListener listener = new MyListener();

    public ClientApp(String ip, int port){
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setSize(this.WIDTH, this.HEIGHT);
        this.setResizable(false);
        this.setLayout(new BorderLayout());
        this.dataReceived.setEditable(false);
        this.add(dataReceived, BorderLayout.CENTER);
        this.bottom.setBorder(BorderFactory.createLineBorder(Color.black));
        this.bottom.add(userData, BorderLayout.CENTER);
        this.button.addActionListener(listener);
        this.bottom.add(button, BorderLayout.EAST);
        this.add(bottom, BorderLayout.SOUTH);
        this.client = new Client(this, ip, port);
        this.client.start();
    }

    public void showMessage(String s) {
        String tmp = this.dataReceived.getText();
        if ((tmp.matches(""))) this.dataReceived.setText(s);
        else this.dataReceived.setText(tmp + "\n" + s);
    }

    public class MyListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            if(e.getActionCommand().equals("INVIA")){
                String msg = userData.getText();
                userData.setText("");
                client.sendMessage(msg);
            }
        }
    }

    public static void main(String[] args){
        ClientApp ca = new ClientApp("localhost", 5000);
        ca.setVisible(true);
    }
}
