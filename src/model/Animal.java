package model;

import controller.GameController;
import resource.GMImage;
import views.GamePanel;

import java.awt.*;

public class Animal implements Catchable {
    private Location loc;
    private int direction = 1;
    public boolean caught = false;
    public static final int movingSpeed = 5;

    public Animal(double x, double y, int dir) {
        loc = new Location(x, y);
        direction = dir;
    }

    public Animal() {
        loc = Location.newRandomLocation();
    }

    @Override
    public void transfer() {
        GameController.getTheGameController().sendMsg("animal");
        GameController.getTheGameController().sendMsg(Double.toString(loc.x));
        GameController.getTheGameController().sendMsg(Double.toString(loc.y));
        GameController.getTheGameController().sendMsg(Integer.toString(direction));
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
        return 5;
    }

    @Override
    public double getSpeed() {
        return 0.02 / 5;
    }

    @Override
    public double getRadius() {
        return 22;
    }

    public void update() {
        if (isCaught())
            return;
        loc.x += direction * 0.01 / 5;
        if (loc.x > 0.98 || loc.x < 0.02)
            direction *= -1;
    }

    @Override
    public void paint(Graphics g) {
        Image img;
        if (direction == 1) {
            img = GMImage.getAnimalRight();
        } else {
            img = GMImage.getAnimalLeft();
        }

        GamePanel panel = GamePanel.getTheGamePanel();
        int width = (int) (91 / 2 );
        int height = (int) (67 / 2 );
        int x = (int) (loc.x * 1200 - width / 2);
        int y = (int) (loc.y * 900 - height / 2);
        g.drawImage(img, x, y, width, height, null);
//        g.drawImage(GMImage.getZZ(), x, y, width*2, height*2, null);

    }
}
