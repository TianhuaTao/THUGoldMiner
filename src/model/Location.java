package model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Random;

public class Location implements Serializable {
    public double x;
    public double y;
    private static ArrayList<Location> historyLoc = new ArrayList<>();

    public Location(double x, double y) {
        this.x = x;
        this.y = y;
    }
    public static void clearHistory(){
        historyLoc.clear();
    }

    public static Location newRandomLocation() {
        Location loc;
        do {
            Random r = new Random();
            double x = r.nextDouble();
            double y = r.nextDouble();
            while (y < 0.25) {
                y = r.nextDouble();
            }
            loc = new Location(x, y);
        } while (!isGood(loc));
        historyLoc.add(loc);
        return loc;
    }

    static boolean isGood(Location loc) {
        for (Location l : historyLoc) {
            if (tooClose(loc, l))
                return false;
        }
        if (loc.x > 0.95 || loc.x < 0.05)
            return false;
        if (loc.y > 0.95)
            return false;
        return true;
    }

    static boolean tooClose(Location l1, Location l2) {
        return (Math.abs(l1.x - l2.x) < 0.1) && (Math.abs(l1.y - l2.y) < 0.1);
    }

}
