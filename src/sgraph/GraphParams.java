package sgraph;

import java.util.Random;

public class GraphParams {
    int width = 10;
    int height = 10;

    int nColors = 4;

    public int nNodes() {
        return width * height;
    }

    Random random = new Random();
}
