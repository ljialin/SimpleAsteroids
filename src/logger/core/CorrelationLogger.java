package logger.core;

import java.util.ArrayList;
import java.util.Arrays;

public class CorrelationLogger {
    int tot = 0;
    int[][] count;
    int n;

    public void log(ArrayList<Integer> actions) {
        if (count == null) {
            n = actions.size();
            count = new int[n][n];
        }
        // now iterate over all of them
        for (int i=0; i<n; i++) {
            for (int j=0; j<n; j++) {
                if (actions.get(i) == actions.get(j)) {
                    count[i][j]++;
                }
            }
            tot += n;
        }
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int[] a : count) {
            sb.append(Arrays.toString(a) + "\n");
        }
        return sb.toString();
    }

}
