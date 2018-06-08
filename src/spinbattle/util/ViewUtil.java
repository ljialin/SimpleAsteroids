package spinbattle.util;

import caveswing.view.CaveView;
import spinbattle.view.SpinBattleView;

public class ViewUtil {
    public static void waitUntilReady(SpinBattleView view) throws Exception {
        int i = 0;
        while (view.nPaints == 0) {
            // System.out.println(i++ + " : " + CaveView.nPaints);
            Thread.sleep(50);
        }
    }
}
