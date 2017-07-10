package utilities;

import javax.swing.*;

/**
 * Created by simonmarklucas on 10/07/2017.
 */


import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class ImageWriteExample extends JComponent {

    static String root = "data/res/drawable-";
    static String name = "ic_launcher.png";
//    static String[] files = {"ldpi/", "mdpi/", "hdpi/", "xdpi/", "play/"};
//    static int[] sizes = {36, 48, 72, 96, 512};

    static int size = 512;

    public static void main(String[] args) throws Exception {
        // this code is derived from an auto-icon builder for
        // Android projects, but has now been trimmed down to a simple example
        String dir = root + size;
        File fDir = new File(dir);
        fDir.mkdirs();
        String fileName = dir + name;
        ImageWriteExample iwe = new ImageWriteExample(size);
        // new JEasyFrame(iwe, "Bandit Icon: " + size);
        iwe.saveImage(fileName);
    }

    void saveImage(String fileName) throws Exception {
        try {
            // TYPE_INT_ARGB specifies the image format: 8-bit RGBA packed
            // into integer pixels
            // if (getWidth() < )
            // BufferedImage bi = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_ARGB);
            BufferedImage bi = new BufferedImage(size, size, BufferedImage.TYPE_INT_ARGB);
            Graphics2D graphics = bi.createGraphics();
            drawWithShadow(graphics);
            File file = new File(fileName);
            ImageIO.write(bi, "PNG", file);
        } catch (IOException ie) {
            ie.printStackTrace();
        }

    }

    public void message(Graphics2D g) {
        Font font = new Font("TimesRoman", Font.BOLD, size / 3);
        g.setFont(font);
        String message = "?";
        FontMetrics fontMetrics = g.getFontMetrics();
        int stringWidth = fontMetrics.stringWidth(message);
        int stringHeight = fontMetrics.getAscent();
        // g.setPaint(new Color(0xFFCC00CC, true));
        g.setPaint(new Color(0xFF000000, true));
        g.drawString(message, (size - stringWidth) / 2, size / 2 + stringHeight / 4);
    }

    public void paintComponent(Graphics go) {
        Graphics2D g = (Graphics2D) go;
        drawWithShadow(g);
    }

    public void drawWithShadow(Graphics2D g) {
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        // now paint the dudes ...
        Color bg = new Color(255, 255, 255, 255);
        g.setColor(bg);
        g.fillRect(0, 0, getWidth(), getHeight());
        draw(g, true);
        Composite composite = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.4f);
        g.setComposite(composite);
        g.translate(size / 36, size / 36);
        // g.rotate(0.1);
        draw(g, false);
    }

    public void draw(Graphics2D g, boolean withText) {
        int xPad = size / 16;
        int w = size / 4;
        int h = 2 * size / 4;
        int yPad = size / 4;

        Color tmp = new Color(0xDDAAFF, false);
        Color left = new Color(0xAA66CC, false);
        g.setColor(left);
        g.fillRect(xPad, yPad, w, h);

        Color mid = new Color(0xFFBB33, false);
        g.setColor(mid);
        g.fillRect(2 * xPad + w, yPad, w, h);

        Color right = new Color(0xFF4444, false);
        g.setColor(right);
        g.fillRect(3 * xPad + 2 * w, yPad, w, h);

        if (withText) message(g);
    }

    public ImageWriteExample(int size) {
        this.size = size;
    }

    public Dimension getPreferredSize() {
        return new Dimension(size, size);
    }
}

