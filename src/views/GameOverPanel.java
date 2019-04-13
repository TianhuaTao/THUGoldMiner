package views;

import controller.GameController;
import model.Miner;
import model.MinerRecord;
import resource.GMImage;
import sun.misc.GC;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.util.ArrayList;

public class GameOverPanel extends JPanel {
    public static GameOverPanel theGameOverPanel;
    int score1;
    int score2;
    ArrayList<MinerRecord> arrayList = null;
    ArrayList<String> recordStringArray = null;

    public void setRecordStringArray(ArrayList<String> recordStringArray) {
        this.recordStringArray = recordStringArray;
    }

    public GameOverPanel(ArrayList<MinerRecord> list, int myScore, int hisScore) {
        arrayList = list;
        theGameOverPanel = this;
        score1 = myScore;
        score2 = hisScore;
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (GameController.getTheGameController().isClient) return;
                int x = e.getX();
                int y = e.getY();
                GameController gc = GameController.getTheGameController();
                if (x > (int) (0.4 * getWidth()) && x < (int) (0.4 * getWidth()) + 200 && y > 0.35 * getHeight() && y < 0.35 * getHeight() + 60) {
                    gc.restart();
                    System.out.println("restart");
                }
            }
        });
    }


    void showRecord() {

    }


    @Override
    public void paint(Graphics g) {
        Graphics gg = g;
        BufferedImage image = new BufferedImage(1200, 900, BufferedImage.TYPE_INT_ARGB);
        g = image.getGraphics();

        if (GameController.getTheGameController().isServer) {
            g.drawImage(GMImage.getGameBackground(), 0, 0, 1200, 900, null);
            g.setFont(new Font("宋体", Font.BOLD, 40));
            g.setColor(Color.WHITE);
            g.drawImage(GMImage.getFrame(), (int) (0.4 * 1200), (int) (0.35 * 900), 200, 60, null);
            g.drawString("重新开始", (int) (0.42 * 1200), (int) (0.4 * 900));
        } else if (GameController.getTheGameController().isClient) {
            g.drawImage(GMImage.getGameBackground(), 0, 0, 1200, 900, null);
            g.setFont(new Font("宋体", Font.BOLD, 40));
            g.setColor(Color.WHITE);
            g.drawImage(GMImage.getFrame(), (int) (0.38 * 1200), (int) (0.35 * 900), 330, 60, null);
            g.drawString("等待重新开始", (int) (0.42 * 1200), (int) (0.4 * 900));
        }
        if (GameController.getTheGameController().isServer) {
            if (arrayList != null) {
                g.setFont(new Font("宋体", Font.BOLD, 20));
                g.setColor(Color.BLACK);
                g.drawString("High Scores:", (int) (0.1 * (1200)), (int) ((0.5) * (900)));
                for (int i = 0; i < 5; i++) {
                    if (i >= arrayList.size()) break;
                    g.setFont(new Font("宋体", Font.BOLD, 20));
                    g.setColor(Color.BLACK);
                    g.drawString(arrayList.get(i).toString(), (int) (0.1 * 1200), (int) ((0.6 + (0.08 * i)) * 900));
                }
            }
        } else if (GameController.getTheGameController().isClient) {
            if (recordStringArray != null) {
                g.setFont(new Font("宋体", Font.BOLD, 20));
                g.setColor(Color.BLACK);
                g.drawString("High Scores:", (int) (0.1 * 1200), (int) ((0.5) * (900)));
                for (int i = 0; i < 5; i++) {
                    if (i >= recordStringArray.size()) break;
                    g.setFont(new Font("宋体", Font.BOLD, 20));
                    g.setColor(Color.BLACK);
                    g.drawString(recordStringArray.get(i), (int) (120), (int) ((0.6 + (0.08 * i)) * 900));
                }
            }
        }
        g.setFont(new Font("宋体", Font.BOLD, 20));
        g.setColor(Color.BLACK);
        g.drawString("Your Score: " + score1, (int) (0.1 * 1200), (int) ((0.3) * 900));
        g.drawString("Rival's Score: " + score2, (int) (0.1 * 1200), (int) ((0.4) * 900));

        gg.drawImage(image, 0, 0, getWidth(), getHeight(), null);

    }
}

