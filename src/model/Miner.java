package model;

import controller.GameController;
import resource.GMImage;
import resource.GMSound;
import views.GamePanel;

import java.awt.*;
import java.io.Serializable;
import java.util.Timer;
import java.util.TimerTask;

public class Miner implements Serializable {
    public static final int FREE = 5;
    public static final int THROWING = 6;
    public static final int PUSHING = 7;
    private static Miner m1 = new Miner(new Location(0.4, 0.1));
    private static Miner m2 = new Miner(new Location(0.6, 0.1));
    private Hook hook;
    private int state;
    private Location loc;
    private int score;
    private String recentScore = null;
    private Timer timerToChangePic = new Timer();
    private TimerTask changePicTask;
    Image img = GMImage.getMiner1();

    Miner(Location loc) {
        this.loc = loc;
        state = FREE;
        hook = new Hook(new Location(loc.x, 0.17), this);
    }

    boolean isFree() {
        return state == FREE;
    }

    public static Miner Miner1() {
        return m1;
    }

    public static void reset() {
        m1 = new Miner(new Location(0.4, 0.1));
        m2 = new Miner(new Location(0.6, 0.1));
    }

    public static Miner Miner2() {
        return m2;
    }

    public Hook getHook() {
        return hook;
    }

    public void update() {
        hook.update();
    }

    public void setChangePic() {
        timerToChangePic = new Timer();
        timerToChangePic.schedule(new TimerTask() {
            @Override
            public void run() {
                if (img == GMImage.getMiner1())
                    img = GMImage.getMiner2();
                else
                    img = GMImage.getMiner1();
            }
        }, 0, 1000);
    }

    public void shoot() {
        if (state != FREE) return;
        GMSound.playSound(GMSound.DIG);
        hook.shoot();
    }

    public void shoot(double theta) {
        hook.theta = theta;
        shoot();
    }

    public void setScore(int score) {
        this.score = score;
    }

    public void didGet(Catchable c) {
        if (c == null) return;
        GMSound.playSound(GMSound.GET);
        score += c.getValue();
        timerToChangePic.cancel();
        recentScore = "$" + c.getValue();
        Timer t = new Timer();
        t.schedule(new TimerTask() {
            @Override
            public void run() {
                recentScore = null;
                t.cancel();
            }
        }, 1000, 10);
    }

    public int getScore() {
        return score;
    }

    public void setState(int state) {
        this.state = state;
    }

    public void transfer() {
        GameController.getTheGameController().sendMsg(Integer.toString(score));
        GameController.getTheGameController().sendMsg(Boolean.toString(hook.onHook == null));
    }

    public void paint(Graphics g) {

        if (state == PUSHING) {

        } else
            img = GMImage.getMiner1();

        GamePanel panel = GamePanel.getTheGamePanel();
        int width = 110;
        int height = 110;
        int x = (int) (loc.x * 1200 - width / 2);
        int y = (int) (loc.y * 900 - height / 2);
        g.drawImage(img, x, y, width, height, null);
        if (recentScore != null) {
            g.setColor(new Color(76, 158, 101));
            g.setFont(new Font("宋体", Font.BOLD, 36));
            g.drawString(recentScore, (int) (loc.x * 1200), 30);
            g.setColor(Color.BLACK);
        }
        hook.paint(g);
    }
}
