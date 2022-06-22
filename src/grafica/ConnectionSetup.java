package grafica;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.concurrent.Semaphore;

public class ConnectionSetup extends JFrame {
    private String ip;
    private int port;
    private MyListener listener = new MyListener();
    private JTextField ipField = new JTextField(), portField = new JTextField();
    private JButton button = new JButton("send");
    private JLabel ipLabel = new JLabel("Server Ip"), portLabel = new JLabel("Server Port");
    private Semaphore sem = new Semaphore(0);
    private final Font font = new Font("SansSerif", Font.PLAIN, 13);

    public ConnectionSetup() {
        this.setSize(new Dimension(400, 200));
        this.setResizable(false);
        this.setLayout(new GridLayout(3, 1));
        ipField.setPreferredSize(new Dimension(200, 40));
        portField.setPreferredSize(new Dimension(200, 40));
        ipField.setFont(font);
        portField.setFont(font);

        JPanel first = new JPanel();
        first.add(ipLabel);
        first.add(ipField);
        this.add(first);

        JPanel second = new JPanel();
        second.add(portLabel);
        second.add(portField);
        this.add(second);

        JPanel third = new JPanel();
        this.button.addActionListener(listener);
        this.button.setPreferredSize(new Dimension(100, 30));
        third.add(button);
        this.add(third);
        this.setVisible(true);
    }


    public void setIp(){
        this.ip = ipField.getText();
        sem.release();
    }
    public void setPort(){
        this.port = Integer.parseInt(portField.getText());
        sem.release();
    }
    public String getIp(){
        try{
            sem.acquire();
        }catch(Exception e){
            e.printStackTrace();
        }
        return this.ip;
    }
    public int getPort(){
        try{
            sem.acquire();
        }catch(Exception e){
            e.printStackTrace();
        }
        return this.port;
    }

    private class MyListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            if(e.getActionCommand().equals("send")){
                setPort();
                setIp();
            }
        }
    }
}
