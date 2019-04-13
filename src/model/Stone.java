package model;

import controller.GameController;
import resource.GMImage;
import views.GamePanel;

import javax.swing.*;
import java.awt.*;

public class Stone implements Catchable {
    private Location loc;
    public boolean caught = false;


    @Override
    public boolean isCaught() {
        return caught;
    }

    @Override
    public void setCaught(boolean caught) {
        this.caught = caught;
    }

    @Override
    public double getSpeed() {
        return 0.001;
    }

    @Override
    public int getValue() {
        return 20;
    }

    @Override
    public double getRadius() {
        return 40;
    }

    @Override
    public Location getLocation() {
        return loc;
    }

    public Stone() {
        loc = Location.newRandomLocation();
    }

    public Stone(double x, double y) {
        loc = new Location(x, y);
    }

    public static Stone newRandomStone() {
        return new Stone();
    }

    @Override
    public void transfer() {
        GameController.getTheGameController().sendMsg("stone");
        GameController.getTheGameController().sendMsg(Double.toString(loc.x));
        GameController.getTheGameController().sendMsg(Double.toString(loc.y));
    }


    @Override
    public void paint(Graphics g) {
        GamePanel panel = GamePanel.getTheGamePanel();
        int width = (int) (65 * panel.getWidthRatio());
        int height = (int) (55 * panel.getHeightRatio());
        int x = (int) (loc.x * 1200 - width / 2);
        int y = (int) (loc.y * 900 - height / 2);
        g.drawImage(GMImage.getStone(), x, y, width, height, null);

    }
}
