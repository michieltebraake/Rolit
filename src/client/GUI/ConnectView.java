package client.GUI;

import client.Connection.Connect;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;

public class ConnectView extends JFrame implements ActionListener{
    private RolitView rolitView;

    private JButton connectButton;
    private JTextField usernameText;
    private JTextField passwordText;
    private JTextField ipText;
    private JTextField portText;
    private JCheckBox aiBox;

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
                int port;
                try {
                    port = Integer.parseInt(portText.getText());
                } catch (NumberFormatException e) {
                    JOptionPane.showMessageDialog(this, "Port must be a number!", "Invalid port!", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                Connect connect = new Connect(rolitView, ipText.getText(), port, usernameText.getText(), passwordText.getText(), aiBox.isSelected());
                connect.start();
                this.dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));
            }
        }
    }
}
