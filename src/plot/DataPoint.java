package plot;

import java.awt.*;

/**
 * Created by simonmarklucas on 18/07/2017.
 */
public class DataPoint {
    public String name;
    public double x;
    public double y;
    public Color color;

    public DataPoint(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public DataPoint(String name, double x, double y) {

        this.name = name;
        this.x = x;
        this.y = y;
    }

    public DataPoint setColor(Color color) {
        this.color = color;
        return this;
    }

}
