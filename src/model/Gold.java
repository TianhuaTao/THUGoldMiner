package model;

import controller.GameController;
import resource.GMImage;
import views.GamePanel;

import java.awt.*;
import java.util.Random;

public class Gold implements Catchable {
    public static final int BIG = 2;
    public static final int MIDDLE = 1;
    public static final int SMALL = 0;
    public boolean caught = false;
    private int size;
    private int value;
    private double speed;
    private double Radius;
    private Location loc;


    @Override
    public void transfer() {
        GameController.getTheGameController().sendMsg("gold");
        GameController.getTheGameController().sendMsg(Double.toString(loc.x));
        GameController.getTheGameController().sendMsg(Double.toString(loc.y));
        GameController.getTheGameController().sendMsg(Integer.toString(size));
    }

    public Gold(double x, double y, int size) {
        this.size = size;
        loc = new Location(x, y);
        if (size == BIG) {
            value = 500;
            speed = 0.008 / 5;
            Radius = 50 * 1.4;
        } else if (size == MIDDLE) {
            value = 200;
            speed = 0.012 / 5;
            Radius = 45;
        } else if (size == SMALL) {
            value = 100;
            speed = 0.015 / 5;
            Radius = 50 * 0.5;
        }
    }

    public Gold(int size) {
        this.size = size;
        loc = Location.newRandomLocation();
        if (size == BIG) {
            value = 500;
            speed = 0.008 / 5;
            Radius = 50 * 1.4;
        } else if (size == MIDDLE) {
            value = 200;
            speed = 0.012 / 5;
            Radius = 45;
        } else if (size == SMALL) {
            value = 100;
            speed = 0.015 / 5;
            Radius = 50 * 0.5;
        }
    }

    @Override
    public boolean isCaught() {
        return caught;
    }

    @Override
    public void setCaught(boolean caught) {
        this.caught = caught;
    }

    @Override
    public Location getLocation() {
        return loc;
    }

    @Override
    public int getValue() {
        return value;
    }

    @Override
    public double getSpeed() {
        return speed;
    }

    @Override
    public double getRadius() {
        return Radius;
    }

    public static Gold newRandomGold() {
        Random random = new Random();
        return new Gold(random.nextInt(3));
    }

    @Override
    public void paint(Graphics g) {
        double scale = 1;
        if (size == BIG) scale = 1.5;
        else if (size == MIDDLE) scale = 1;
        else if (size == SMALL) scale = 0.5;
        int width = (int) (63  * scale);
        int height = (int) (57  * scale);
        int x = (int) (loc.x * 1200 - width / 2);
        int y = (int) (loc.y * 900- height / 2);
        g.drawImage(GMImage.getGold(), x, y, width, height, null);
    }
}
