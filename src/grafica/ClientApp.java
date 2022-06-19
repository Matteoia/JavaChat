package grafica;

import client.Client;

import javax.swing.*;
import javax.swing.text.DefaultCaret;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ClientApp extends JFrame {
    private Client client;
    private final int HEIGHT = 500;
    private final int WIDTH = 800;
    private JTextPane dataReceived = new JTextPane();
    private JPanel bottom = new JPanel(new BorderLayout());
    private JTextArea userData = new JTextArea();
    private Button button = new Button("INVIA");
    private ActionListener listener = new MyListener();
    private final Font font = new Font("SansSerif", Font.BOLD, 17);

    public ClientApp(String ip, int port){
        JScrollPane scroll = new JScrollPane(dataReceived, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        ((DefaultCaret)dataReceived.getCaret()).setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setSize(this.WIDTH, this.HEIGHT);
        this.setResizable(false);
        this.setLayout(new BorderLayout());
        this.dataReceived.setEditable(false);
        this.add(scroll, "Center");
        this.bottom.setBorder(BorderFactory.createLineBorder(Color.black));
        this.bottom.add(userData, "Center");
        this.button.addActionListener(listener);
        this.bottom.add(button, "East");
        this.button.setPreferredSize(new Dimension(WIDTH/10, HEIGHT/8));
        this.userData.setFont(font);
        this.dataReceived.setFont(font);
        this.add(bottom, "South");
        this.bottom.setPreferredSize(new Dimension(WIDTH, HEIGHT/8));
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
