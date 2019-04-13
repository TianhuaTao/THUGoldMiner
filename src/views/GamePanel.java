package views;

import controller.GameController;
import model.Catchable;
import model.Miner;
import resource.GMImage;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;


public class GamePanel extends JPanel {
    private static GamePanel theGamePanel;

    public GamePanel() {
        theGamePanel = this;
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int x = e.getX();
                int y = e.getY();
                GameController gc = GameController.getTheGameController();
                if (x > 10 && x < 70 && y > 10 && y < 70) {
                    if (gc.getState() == GameController.PLAYING) {
                        gc.pause();
                        System.out.println("PAUSE");
                    } else if (gc.getState() == GameController.PAUSE) {
                        gc.resume();
                        System.out.println("RESUME");
                    }
                }
            }
        });
    }


    public static GamePanel getTheGamePanel() {
        return theGamePanel;
    }

    public double getWidthRatio() {
        return 1;
    }

    public double getHeightRatio() {
        return 1;
    }


    @Override
    protected void paintComponent(Graphics g) {
        Graphics gg = g;
        BufferedImage image = new BufferedImage(1200, 900, BufferedImage.TYPE_INT_ARGB);
        g = image.getGraphics();

        g.setColor(Color.BLACK);
        super.paintComponent(g);
        Image backGround = GMImage.getGameBackground();
        g.drawImage(backGround, 0, 0, 1200, 900, null);
        Miner.Miner1().paint(g);
        Miner.Miner2().paint(g);
        g.setFont(new Font("宋体", Font.BOLD, 28));
        g.setColor(Color.BLACK);
        g.drawString("Player 1", (int) (1200 * 0.2), (int) (900 * 0.05));
        g.drawString("Player 2", (int) (1200 * 0.7), (int) (900 * 0.05));
        g.drawString(Integer.toString(GameController.countDown), (int) (1200 * 0.48), (int) (900 * 0.05));
        g.drawString(Integer.toString(Miner.Miner1().getScore()), (int) (1200 * 0.2), (int) (900 * 0.1));
        g.drawString(Integer.toString(Miner.Miner2().getScore()), (int) (1200 * 0.7), (int) (900 * 0.1));

        synchronized (GameController.getTheGameController().getThingsToCatch()) {
            for (Catchable c : GameController.getTheGameController().getThingsToCatch()) {
                c.paint(g);
//            g.setColor(Color.RED);
//            g.drawOval((int) (c.getLocation().x * getWidth()), (int) (c.getLocation().y * getHeight()), (int) c.getRadius(), (int) c.getRadius());
//            g.drawRect(5,5,100,100);
            }
        }
        g.drawImage(GMImage.getFrame(), 10, 10, 60, 60, null);
        g.setFont(new Font("宋体", Font.BOLD, 40));
        g.setColor(new Color(130, 77, 37));
        g.drawString("||", 25, 55);

        gg.drawImage(image, 0, 0, getWidth(), getHeight(), null);
    }
}
