package grafica;

import javax.swing.*;
import javax.swing.text.DefaultCaret;
import java.awt.*;

public class UserInputComponent extends JPanel {
    private ClientApp client;
    private JScrollPane scroll;
    private JTextPane dataReceived = new JTextPane();

    public UserInputComponent(ClientApp client){
        this.client = client;
        this.setPreferredSize(new Dimension((client.WIDTH*70)/100, client.HEIGHT));

        this.dataReceived.setFont(client.font);
        this.dataReceived.setEditable(false);
        this.dataReceived.setPreferredSize(new Dimension((client.WIDTH*70)/100, client.HEIGHT));

        this.scroll = new JScrollPane(dataReceived, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        this.scroll.setPreferredSize(new Dimension((client.WIDTH*70)/100, client.HEIGHT));
        ((DefaultCaret)dataReceived.getCaret()).setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);

        this.add(scroll);
    }


    public void setText(String msg) {
        String tmp = this.dataReceived.getText();
        if ((tmp.matches(""))) this.dataReceived.setText(msg);
        else this.dataReceived.setText(tmp + "\n" + msg);
    }
}
