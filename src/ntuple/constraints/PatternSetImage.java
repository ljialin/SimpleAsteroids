package ntuple.constraints;

import java.awt.*;
import java.util.Arrays;

public class PatternSetImage {

    public static void main(String[] args) {
        Color color = new Color(100, 200, 255);

        float[] comp = new float[3];
        System.out.println(color.getColorComponents(comp));
        System.out.println(Arrays.toString(comp));

    }

    int width, height;

    public PatternSetImage setDimensions(int width, int height) {
        this.width = width;
        this.height = height;
        return this;
    }

    public PatternSetImage initialise(PatternSet patternSet) {
        // set up the image array to be these pattern sets
        return this;
    }

    public PatternSetImage iterate() {

        // find the lowest non-zero entropy pattern
        // this means the one ...


        return this;
    }



}
