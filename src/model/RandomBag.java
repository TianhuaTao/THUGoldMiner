package model;

import controller.GameController;
import resource.GMImage;
import views.GamePanel;

import java.awt.*;
import java.util.Random;

public class RandomBag implements Catchable {
    private int value;
    private double speed;
    private Location loc;
    public boolean caught = false;

    @Override
    public void transfer() {
        GameController.getTheGameController().sendMsg("bag");
        GameController.getTheGameController().sendMsg(Double.toString(loc.x));
        GameController.getTheGameController().sendMsg(Double.toString(loc.y));
        GameController.getTheGameController().sendMsg(Integer.toString(value));
        GameController.getTheGameController().sendMsg(Double.toString(speed));
    }

    public RandomBag(double x,double y,int val,double speed){
        loc = new Location(x,y);
        value =val;
        this.speed=speed;
    }

    @Override
    public void setCaught(boolean caught) {
        this.caught = caught;
    }

    public RandomBag() {
        loc = Location.newRandomLocation();
        Random random = new Random();
        value = random.nextInt(1001);
        speed = random.nextDouble() * 0.01;
    }


    @Override
    public boolean isCaught() {
        return caught;
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
        return 20;
    }

    @Override
    public void paint(Graphics g) {
        int width = 30;
        int height = 35;
        int x = (int) (loc.x * 1200 - width / 2);
        int y = (int) (loc.y * 900 - height / 2);
        g.drawImage(GMImage.getRandomBag(), x, y, width, height, null);
    }
}
