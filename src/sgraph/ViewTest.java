package sgraph;

import utilities.JEasyFrame;

public class ViewTest {
    public static void main(String[] args) {
        GraphParams params = new GraphParams();
        Graph graph = new Graph().setParams(params).setRandomLabels();
        GridView view = new GridView().setGraph(graph);
        GridMouse gridMouse = new GridMouse();
        gridMouse.view = view;

        view.addMouseListener(gridMouse);
        new JEasyFrame(view, "Grid Graph GVGAISimpleTest");
    }
}
