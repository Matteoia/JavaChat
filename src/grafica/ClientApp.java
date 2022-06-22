package grafica;

import client.Client;

import javax.swing.*;
import java.awt.*;

public class ClientApp extends JFrame {
    private Client clientController;
    public  final int HEIGHT = 500;
    public  final int WIDTH = 800;
    private UserOutputComponent userOutput;
    private UserInputComponent userInput;
    private OnlineUsersBar usersBar;
    public final Font font = new Font("SansSerif", Font.BOLD, 17);

    public ClientApp(String ip, int port){
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setSize(this.WIDTH, this.HEIGHT);
        this.setResizable(false);
        this.setLayout(new BorderLayout());

        this.userInput = new UserInputComponent(this);
        this.userOutput = new UserOutputComponent(this);
        this.usersBar = new OnlineUsersBar(this);
        this.add(userInput, "Center");
        this.add(userOutput, "South");
        this.add(usersBar, "East");

        this.clientController = new Client(this, ip, port);
        this.clientController.start();
    }

    public void showMessage(String msg) {
        this.userInput.setText(msg);
    }

    public void setMessage(String msg) {
        String dest = this.usersBar.getSelectedUser();
        if(dest == null)
            dest = "ServerSender";
        this.clientController.setMessage(msg);
        this.clientController.setDest(dest);
    }

    public String[] getOnlineUsers() {
        if(this.clientController.user != null)
            return this.clientController.getOnlineUsers();
        return null;
    }

    public static void main(String[] args) {
        ClientApp ca = new ClientApp("localhost", 5000);
        ca.setVisible(true);
    }
}
