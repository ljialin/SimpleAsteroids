package spinbattle.util;

import java.awt.*;
import java.awt.geom.Rectangle2D;

public class DrawUtil {
    public static void centreString(Graphics2D g, String str, double x, double y) {
        int fontSize = 14;
        g.setFont(new Font("Monospaced", Font.BOLD, fontSize));
        Rectangle2D rect = g.getFontMetrics().getStringBounds(str, g);
        g.setColor(Color.black);
        // use a drawString method for now
        int sx = (int) x - (int) rect.getWidth() / 2;
        int sy = (int) y + (int)  + rect.getHeight() / 2;

        g.drawString(str, sx, sy);
    }


}
