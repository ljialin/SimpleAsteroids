package features;

import math.Vector2d;

public class AgentCentric {

    // currenty this is just handling the transform
    // part of the feature extraction

    public static void main(String[] args) {

        AgentCentric ac = new AgentCentric(new Vector2d(100, 50), new Vector2d(0, 1));

        Vector2d z = new Vector2d(100, 100);
        // System.out.println(ac.transform(z));

    }

    // position and heading
    Vector2d s, h, tmp;

    public AgentCentric(Vector2d s, Vector2d h) {
        this.s = s;
        this.h = h;
        tmp = new Vector2d();
    }

    public Vector2d transform(Vector2d position) {
        // first of all subtract the position of the agent
        tmp = tmp.set(position).subtract(s);
        // System.out.println(tmp);
        // now rotate it
        tmp.contraRotate(h);
        return tmp.copy();
    }

}
