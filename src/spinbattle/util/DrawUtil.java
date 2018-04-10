package spinbattle.util;

import java.awt.*;
import java.awt.geom.Rectangle2D;

public class DrawUtil {

    int fontSize = 14;
    Color fg = Color.black;

    public DrawUtil setColor(Color fg) {
        this.fg = fg;
        return this;
    }

    public DrawUtil setFontSize(int fontSize) {
        this.fontSize = fontSize;
        return this;
    }

    public DrawUtil centreString(Graphics2D g, String str, double x, double y) {
        g.setFont(new Font("Monospaced", Font.BOLD, fontSize));
        Rectangle2D rect = g.getFontMetrics().getStringBounds(str, g);
        // Rectangle2D rect = g.getFontMetrics().get
        g.setColor(fg);
        // use a drawString method for now
        int sx = (int) x - (int) rect.getWidth() / 2;
        int sy = (int) y + (int)  + rect.getHeight() / 2 - g.getFontMetrics().getDescent();
        g.drawString(str, sx, sy);
        return this;
    }
}
