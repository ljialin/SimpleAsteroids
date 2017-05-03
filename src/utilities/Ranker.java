package utilities;

/**
 * Created by simonmarklucas on 03/12/2016.
 */

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.TreeMap;

public class Ranker<T> {
    // ranks items according to a sort criteria

    public static void main(String[] args) {
        Ranker<Integer> ranker = new Ranker<Integer>();
        ranker.add(1.0, 1);
        ranker.add(3.0, 2);
        ranker.add(1.1, 3);
        ranker.add(5.0, 0);
        System.out.println(ranker.getRank(2));
        System.out.println(ranker.getRank(1));
        System.out.println(ranker.nTied());
    }

    public TreeMap<Double,List<T>> map;
    HashSet<T> values;
    int nItems;
    int order;
    public static int MAX_FIRST = -1;
    public static int MIN_FIRST = 1;
    // boolean strict = true;

    public Ranker() {
        this(MAX_FIRST);
    }

    public Ranker(int order) {
        this.order = order;
        map = new TreeMap<Double, List<T>>();
        values = new HashSet<T>();
        nItems = 0;
    }

    public void add(Double key, T value) {
        // each value must be unique: keep it in the set of values
        // and throw an exception if violated
        if (values.contains(value))
            throw new RuntimeException("Ranker already contains: " + value);
        values.add(value);
        // reverse the natural key order if necessary
        // System.out.println("Key added: " + key);
        key *= order;
        List<T> list = map.get(key);
        if (list == null) {
            list = new ArrayList<T>();
            map.put(key,list);
        } else {
            // we have a collision
        }
        list.add(value);
        nItems++;
    }

    public List<T> getBest() {
        return map.values().iterator().next();
    }

    public int getRank(T value) {
        // items are ranked from 0 to nItems-1
        int pos = 0;
        for (List<T> list : map.values()) {
            if (list.contains(value)) return pos + list.size()-1;
            // if (list.contains(value)) return pos;
            pos += list.size();
        }
        // rank is simply pos
        return pos;
    }

    public int nTied() {
        int tot = 0;
        for (List<T> list : map.values()) {
            if (list.size() > 1) tot += list.size();
        }
        // rank is simply pos
        return tot;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (List<T> list : map.values()) {
            sb.append(list);
        }
        return sb.toString();
    }
}
