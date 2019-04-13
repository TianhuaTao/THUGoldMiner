package model;

import controller.GameController;
import resource.GMImage;
import resource.GMSound;
import views.GamePanel;

import static java.lang.Math.*;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.Serializable;
import java.util.Timer;
import java.util.TimerTask;

public class Hook implements Serializable {
    private Location loc;
    private Miner owner;
    int direction = 1;
    double X;
    double Y;
    double theta = 0;
    int state = FREE;
    static final int OUT = 1;
    static final int IN = 2;
    static final int FREE = 3;
    Catchable onHook = null;
    Timer soundTimer;

    public double getTheta() {
        return theta;
    }

    public Hook(Location loc, Miner m) {
        X = loc.x;
        Y = loc.y;
        this.loc = loc;
        owner = m;
    }


    boolean canCatch(Catchable c) {
        if (c.isCaught()) return false;
        Location l1 = loc;
        Location l2 = c.getLocation();


        double dx = (l1.x - l2.x) * 1200;
        double dy = (l1.y - l2.y) * 900;
        double d = sqrt(dx * dx + dy * dy);

        return d < c.getRadius();
    }

    private static double distance(Location l1, Location l2) {
        return Math.sqrt((l1.x - l2.x) * (l1.x - l2.x) + (l1.y - l2.y) * (l1.y - l2.y));
    }

    private static double absDistance(Location l1, Location l2) {
        GamePanel panel = GamePanel.getTheGamePanel();
        double dx = (l1.x - l2.x) ;
        double dy = (l1.y - l2.y) ;
        return sqrt(dx * dx + dy * dy);
    }

    void paint(Graphics g) {
        if (onHook != null) onHook.paint(g);
        GamePanel panel = GamePanel.getTheGamePanel();
        int width = 50;
        int height = 50;
        int x = (int) (loc.x * 1200 - width / 2);
        int y = (int) (loc.y * 900 - height / 2);
        Image img = rotate(GMImage.getHook(), theta);
        g.drawImage(img, x, y, width, height, null);
        g.drawLine((int) (X * 1200), (int) (Y * 900), (int) (loc.x * 1200), (int) (loc.y * 900));

    }

    public void didCatch(Catchable c) {
        GMSound.playSound(GMSound.CATCH);
        c.setCaught(true);
        onHook = c;
        owner.setChangePic();
        GameController.getTheGameController().getThingsToCatch().remove(c);
    }

    public void update() {

        switch (state) {
            case FREE:
                theta += direction * Math.PI / 20.0 / 3.0;
                if (theta < 1 * Math.PI / 180.0 || theta > 179 * Math.PI / 180.0)
                    direction *= -1;
                break;
            case OUT:
                double v = 0.02 / 5;
                loc.x += cos(theta) * v;
                loc.y += sin(theta) * v;

                if (loc.x > 1 || loc.x < 0 || loc.y > 1 || loc.y < 0) {
                    state = IN;
                }

                for (Catchable c : GameController.getTheGameController().getThingsToCatch()) {
                    if (canCatch(c)) {
                        didCatch(c);
                        state = IN;
                        owner.setState(Miner.PUSHING);
                        break;
                    }
                }

                break;
            case IN:
                double velocity;
                if (onHook == null) velocity = 0.05 / 5;
                else {
                    velocity = onHook.getSpeed();
                }
                loc.x -= cos(theta) * velocity;
                loc.y -= sin(theta) * velocity;


                if (onHook != null) {
                    onHook.getLocation().x = loc.x + 0.012 * cos(theta);
                    onHook.getLocation().y = loc.y + 0.012 * sin(theta);
                }

                if (distance(loc, new Location(X, Y)) < 0.016 || loc.y < 0.15) {
                    owner.didGet(onHook);
                    owner.setState(Miner.FREE);
                    state = FREE;
                    loc.x = X;
                    loc.y = Y;
                    onHook = null;
                }
                break;
            default:
        }


    }

    public void shoot() {
        if (state != FREE) return;
        state = OUT;
    }

    public void setOnHook(boolean b) {
        if (b)
            onHook = null;
    }


    public Location getLocation() {
        return loc;
    }

    public static BufferedImage rotate(Image i, double theta) {
        BufferedImage img = new BufferedImage(i.getWidth(null), i.getHeight(null), BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = img.createGraphics();
        double w = img.getWidth();
        double h = img.getHeight();
        g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g2d.rotate(theta, w / 2, h / 2);
        g2d.drawImage(i, 0, 0, null);
        g2d.dispose();
        return img;
    }
}
