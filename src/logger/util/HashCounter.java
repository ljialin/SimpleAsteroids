package logger.util;

import java.util.HashMap;

public class HashCounter {
    HashMap<Double,Count> map = new HashMap<>();

    public HashCounter add(Double key) {
        Count count = map.get(key);
        if (count == null) {
            count = new Count();
            map.put(key,count);
        }
        count.inc();
        return this;
    }

    static class Count {
        int x = 0;
        void inc() { x++; }
    }
}
