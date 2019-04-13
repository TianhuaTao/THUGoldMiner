package resource;

import javax.swing.*;
import java.awt.*;

public class GMImage {
    public static GMImage image = new GMImage();

    private static ImageIcon gameBackgroundIcon = new ImageIcon(image.getClass().getResource(  "/res/image/game_background.png"));
    private static ImageIcon miner1Icon = new ImageIcon(image.getClass().getResource( "/res/image/miner1.png"));
    private static ImageIcon miner2Icon = new ImageIcon(image.getClass().getResource( "/res/image/miner2.png"));
    private static ImageIcon goldIcon = new ImageIcon(image.getClass().getResource( "/res/image/gold.png"));
    private static ImageIcon stoneIcon = new ImageIcon(image.getClass().getResource( "/res/image/stone_1.png"));
    private static ImageIcon zz = new ImageIcon(image.getClass().getResource( "/res/image/zz.png"));
    private static ImageIcon hookIcon = new ImageIcon(image.getClass().getResource( "/res/image/hook.png"));
    private static ImageIcon animalLeft = new ImageIcon(image.getClass().getResource( "/res/image/animal_left.png"));
    private static ImageIcon animalRight = new ImageIcon(image.getClass().getResource( "/res/image/animal_right.png"));
    private static ImageIcon RandomBag = new ImageIcon(image.getClass().getResource( "/res/image/random_bag.png"));
    private static ImageIcon frame = new ImageIcon(image.getClass().getResource( "/res/image/dialog-frame-yellow.png"));



    public static Image getGameBackground() {
        return gameBackgroundIcon.getImage();
    }

    public static Image getGold() {
        return goldIcon.getImage();
    }

    public static Image getMiner1() {
        return miner1Icon.getImage();
    }

    public static Image getMiner2() {
        return miner2Icon.getImage();
    }

    public static Image getStone() {
        return stoneIcon.getImage();
    }

    public static Image getZZ() {
        return zz.getImage();
    }

    public static Image getHook() {
        return hookIcon.getImage();
    }

    public static Image getAnimalLeft() {
        return animalLeft.getImage();
    }

    public static Image getAnimalRight() {
        return animalRight.getImage();
    }

    public static Image getRandomBag() {
        return RandomBag.getImage();
    }

    public static Image getFrame() {
        return frame.getImage();
    }
}
