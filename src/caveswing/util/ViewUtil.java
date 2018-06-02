package caveswing.util;

import caveswing.view.CaveView;

public class ViewUtil {
    public static void waitUntilReady(CaveView view) throws Exception {
        int i = 0;
        while (view.nPaints == 0) {
            // System.out.println(i++ + " : " + CaveView.nPaints);
            Thread.sleep(50);
        }
    }
}
