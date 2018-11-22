package distance.object;

import caveswing.core.CaveGameState;
import caveswing.core.CaveSwingParams;

public class TestObjectWalk {
    public static void main(String[] args) {
        CaveGameState caveGameState = new CaveGameState().setParams(new CaveSwingParams());
        // ObjectWalker
        System.out.println("Before setup");
        new ObjectWalker().walk(caveGameState).printSummary();
        caveGameState.setup();
        System.out.println("\nAfter setup");
        new ObjectWalker().walk(caveGameState).printSummary();
    }
}
