package asteroids;

import java.awt.*;
import java.util.Random;

/**
 * Created by Simon M. Lucas
 * sml@essex.ac.uk
 * Date: 26/12/11
 * Time: 12:00
 */
public interface Constants {
    double saucerProb = 0.005;
    int width = 640;
    int height = 480;
    Dimension size = new Dimension(width, height);
    int safeRadius = height/20;
    Color bg = Color.black;
    Font font = new Font("Courier", Font.PLAIN, 20);

    int delay = 40;
    double ac = 0.01;
    double t = 1.0;
    Random rand = new Random();
    int[] asteroidScore = {50, 100, 200};
    int largeShipScore = 500;
    int smallSaucerScore = 1000;
    int firstLevel = 50;
    int increasePerLevel = firstLevel / 4;
    int lifeThreshold = 10000;

    // how many smaller asteroids an asteroid splits into
    int nSplits = 3;

    int clockwise = 1;
    int antiClockwise = -1;
    int noRotate = 0;

    int nStars = 500;


    // scaling factor applied to random asteroid speed
    double speedScale = 0.5;

}
