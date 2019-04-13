package views;


import controller.*;
import model.Miner;
import mysocket.ExchangeThread;
import mysocket.Server;

import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame {
    private static MainFrame theMainFrame;
    private static Dimension preferredSizeDimension = new Dimension(1200, 900);

    public MainFrame() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setTitle("黄金矿工");
        this.setLocationRelativeTo(null);
        this.setPreferredSize(preferredSizeDimension);
        theMainFrame = this;
        GameController gameController = new GameController();
        JPanel launch = new LaunchPanel();
        this.setContentPane(launch);
        this.setSize(preferredSizeDimension);
    }


    public static MainFrame getMainFrame() {
        return theMainFrame;
    }


}
