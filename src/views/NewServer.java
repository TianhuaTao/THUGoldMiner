package views;

import controller.GameController;
import mysocket.Server;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.net.InetAddress;

public class NewServer extends JDialog {
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JTextField IPTextField;
    private JTextField portTextField;
    private JLabel warningText;

    public NewServer() {

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

        String ipstr = null;
        try {
            ipstr = InetAddress.getLocalHost().getHostAddress();
        } catch (Exception e) {

        }
        IPTextField.setText(ipstr);
        warningText.setPreferredSize(new Dimension(-1, 30));

        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void error() {
        warningText.setText("Please enter a correct IP and port");
    }

    private void onOK() {
        int port = 0;

        boolean success = true;

        String portString = portTextField.getText();
        try {
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
            Server.Init(port);
            Server.thePort = port;
            dispose();
            GameController.getTheGameController().initGame(true);
        }
    }

    private void onCancel() {
        // add your code here if necessary
        dispose();
    }
}
