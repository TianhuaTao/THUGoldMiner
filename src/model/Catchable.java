package model;

import java.awt.*;
import java.io.Serializable;

public interface Catchable extends Serializable {
    boolean isCaught();

    void setCaught(boolean caught);

    double getSpeed();

    int getValue();

    double getRadius();

    void paint(Graphics g);

    Location getLocation();

    void transfer();
}
