package client.GUI;

import client.Connection.AuthConnect;
import client.Connection.AuthenticationConnection;
import client.Connection.Connect;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.net.Socket;
import java.security.PrivateKey;

public class ConnectView extends JFrame implements ActionListener{
    private RolitView rolitView;

    private JButton connectButton;
    private JTextField usernameText;
    private JTextField passwordText;
    private JTextField ipText;
    private JTextField portText;
    private JCheckBox aiBox;
    private int port;
    public final static String AUTH_SERVER = "130.89.163.155";
    public final static int AUTH_PORT = 2013;

    public ConnectView(RolitView rolitView) {
        this.rolitView = rolitView;
        setupFrame();
    }

    private void setupFrame() {
        connectButton = new JButton("Connect");
        connectButton.addActionListener(this);
        add(connectButton, BorderLayout.SOUTH);

        JPanel springPanel = new JPanel(new SpringLayout());

        JLabel usernameLabel = new JLabel("Username:", JLabel.TRAILING);
        springPanel.add(usernameLabel);
        usernameText = new JTextField(20);
        usernameLabel.setLabelFor(usernameText);
        springPanel.add(usernameText);

        JLabel passwordLabel = new JLabel("Password:", JLabel.TRAILING);
        springPanel.add(passwordLabel);
        passwordText = new JTextField(20);
        passwordLabel.setLabelFor(passwordText);
        springPanel.add(passwordText);

        JLabel ipLabel = new JLabel("IP:", JLabel.TRAILING);
        springPanel.add(ipLabel);
        ipText = new JTextField("localhost", 20);
        ipLabel.setLabelFor(ipText);
        springPanel.add(ipText);

        JLabel portLabel = new JLabel("Port:", JLabel.TRAILING);
        springPanel.add(portLabel);
        portText = new JTextField("2727", 20);
        portLabel.setLabelFor(portText);
        springPanel.add(portText);

        JLabel aiLabel = new JLabel("AI:", JLabel.TRAILING);
        springPanel.add(aiLabel);
        aiBox = new JCheckBox();
        aiLabel.setLabelFor(aiBox);
        springPanel.add(aiBox);

        //Adjust constraints for the label so it's at (5,5).
        SpringUtilities.makeGrid(springPanel, 5, 2, 5, 5, 5, 5);

        add(springPanel, BorderLayout.CENTER);

        pack();
        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent event) {
        if(event.getSource() == connectButton) {
            if(!usernameText.getText().isEmpty() && !passwordText.getText().isEmpty() && !ipText.getText().isEmpty() && !portText.getText().isEmpty()) {
                try {
                    port = Integer.parseInt(portText.getText());
                } catch (NumberFormatException e) {
                    JOptionPane.showMessageDialog(this, "Port must be a number!", "Invalid port!", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                AuthConnect authConnect = new AuthConnect(this, AUTH_SERVER, AUTH_PORT);
                authConnect.start();
                rolitView.getStatusLabel().setText("Authenticating user: " + usernameText.getText());
                this.dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));
            }
        }
    }
    public void setupAuthentication(Socket clientSocket) {
        new AuthenticationConnection(clientSocket, this, usernameText.getText(), passwordText.getText());
        System.out.println("AuthenticationConnection");
    }

    public void createClientConnection(PrivateKey privateKey){
        Connect connect = new Connect(rolitView, ipText.getText(), port, usernameText.getText(), aiBox.isSelected(), privateKey);
        connect.start();
        rolitView.getStatusLabel().setText("Connecting to " + ipText.getText() + ":" + port + "...");

    }
}
