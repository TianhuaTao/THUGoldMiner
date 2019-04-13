package views;

import controller.GameController;
import mysocket.Client;
import mysocket.Server;
import resource.GMImage;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

public class LaunchPanel extends JPanel {
    public static LaunchPanel theLaunchPanel;

    public LaunchPanel() {
        theLaunchPanel = this;
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int x = e.getX();
                int y = e.getY();
                GameController gc = GameController.getTheGameController();
                if (x > (int) (0.4 * getWidth()) && x < (int) (0.4 * getWidth()) + 200 && y > 0.35 * getHeight() && y < 0.35 * getHeight() + 60) {
                    new NewServer();
                    System.out.println("init");
                } else if (x > (int) (0.4 * getWidth()) && x < (int) (0.4 * getWidth()) + 200 && y > 0.55 * getHeight() && y < 0.55 * getHeight() + 60) {
                    new NewClient();
                    System.out.println("join");
                }
            }
        });
    }

    public boolean isWaiting = false;

    @Override
    public void paint(Graphics g) {
        Graphics gg = g;
        BufferedImage image = new BufferedImage(1200, 900, BufferedImage.TYPE_INT_ARGB);
        g = image.getGraphics();

        if (isWaiting) {
            g.drawImage(GMImage.getGameBackground(), 0, 0, 1200, 900, null);
            g.setFont(new Font("宋体", Font.BOLD, 40));
            g.drawString("等待连接", (int) (0.42 * 1200), (int) (0.5 * 900));
        } else {
            g.drawImage(GMImage.getGameBackground(), 0, 0, 1200, 900, null);
            g.setFont(new Font("宋体", Font.BOLD, 40));
            g.setColor(Color.WHITE);
            g.drawImage(GMImage.getFrame(), (int) (0.4 * 1200), (int) (0.35 * 900), 200, 60, null);
            g.drawImage(GMImage.getFrame(), (int) (0.4 * 1200), (int) (0.55 * 900), 200, 60, null);
            g.drawString("发起游戏", (int) (0.42 * 1200), (int) (0.4 * 900));
            g.drawString("加入游戏", (int) (0.42 * 1200), (int) (0.6 * 900));
        }

        gg.drawImage(image, 0, 0, getWidth(), getHeight(), null);

    }
}
