package views;

import controller.GameController;
import mysocket.Client;
import mysocket.ExchangeThread;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.net.InetAddress;
import java.net.UnknownHostException;

public class NewClient extends JDialog {
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JTextField IPTextField;
    private JTextField portTextField;
    private JLabel warningText;


    public NewClient() {
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);

        buttonOK.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onOK();
            }
        });

        buttonCancel.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        });

        // call onCancel() when cross is clicked
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                onCancel();
            }
        });

        // call onCancel() on ESCAPE
        contentPane.registerKeyboardAction(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        }, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);

        warningText.setPreferredSize(new Dimension(-1, 30));
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void error() {
        warningText.setText("Please enter a correct IP and port");
    }


    private void onOK() {
        boolean success = true;
        InetAddress address = null;
        int port = 0;


        success = true;
        String IPString = IPTextField.getText();
        String portString = portTextField.getText();
        try {
            address = InetAddress.getByName(IPString);
            port = Integer.parseInt(portString);
            if (port < 1 || port > 65535) {
                error();
                success = false;
            }
        } catch (Exception e) {
            error();
            success = false;
        }

        if (success) {
            dispose();
            Client.inetAddress = address;
            Client.thePort = port;
            Client.Init(address, port);
            GameController.getTheGameController().joinGame();
            GameController.getTheGameController().exchangeThread = Client.getExchangeThread();
            GameController.getTheGameController().connectDidSucceed();

        }


    }

    private void onCancel() {
        // add your code here if necessary
        dispose();
    }

    private void createUIComponents() {
        // TODO: place custom component creation code here
    }
}
