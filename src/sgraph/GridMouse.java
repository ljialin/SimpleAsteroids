package sgraph;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class GridMouse extends MouseAdapter {
    GridView view;

    public GridMouse setView(GridView view) {
        this.view = view;
        return this;
    }

    public void mouseClicked(MouseEvent e) {
        // System.out.println(e);
        view.processClick(e.getX(), e.getY());
    }
}
