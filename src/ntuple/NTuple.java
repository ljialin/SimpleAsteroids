package ntuple;

import evodef.SearchSpace;
import evodef.SearchSpaceUtil;
import gvglink.BattleGameSearchSpace;
import ntuple.params.Param;
import utilities.StatSummary;

import java.util.Arrays;
import java.util.HashMap;

/**
 * Created by simonmarklucas on 13/11/2016.
 *
 *  Purpose is to define N-Tuples on a search space.
 *
 *  Each N-Tuple records the particular dimensions it samples
 *
 *  Each address is used to index to a StatSummary object.
 *
 */
public class NTuple {

    public static void main(String[] args) {
        SearchSpace searchSpace = BattleGameSearchSpace.getSearchSpace();
        System.out.println("Battle Game Size: " + SearchSpaceUtil.size(searchSpace));
        // SearchSpaceUtil.

        // TenSpace is a nice easy one for testing
        searchSpace = new TenSpace(6);

        int[] tuple = new int[]{0, 1, 2};

        NTuple nt = new NTuple(searchSpace, tuple);

        System.out.println("NTuple Size: " + nt.spaceSize());

        int[] p = {1, 2, 3, 2, 1, 5};

        System.out.println("Address: " + nt.address( p) );

        nt.add(p, 10);
        nt.add(p, 1);

        p[0] = 0;

        nt.add(p, 5);

        nt.printNonEmpty();

        System.out.println();
        System.out.println("Maximum values (in bits): ");
        System.out.println("Integer: " + Math.log(Integer.MAX_VALUE) / Math.log(2));
        System.out.println("Long: " + Math.log(Long.MAX_VALUE) / Math.log(2));
        System.out.println("Double: " + Math.log(Double.MAX_VALUE) / Math.log(2));

    }

    public NTuple(SearchSpace searchSpace, int[] tuple) {
        this.searchSpace = searchSpace;
        this.tuple = tuple;
        reset();
    }

    public void reset() {
        nSamples = 0;
        nEntries = 0;

        // clear out all the memory - just make a new structure

        double searchSpaceSize = spaceSize();
        if (searchSpaceSize > sizeLimit) {
            ntMap = new HashMap<>();
        } else {
            ntArray = new StatSummary[(int) spaceSize()];
        }
    }

    // for more than this we'll use a HashMap
    static int sizeLimit = 1200;


    SearchSpace searchSpace;
    int[] tuple;
    HashMap<Double, StatSummary> ntMap;
    StatSummary[] ntArray;

    int nSamples;
    int nEntries;


    public void add(int[] x, double v) {

        // for each address that occurs, we're going to store something
        StatSummary ss = getStatsForceCreate(x);
        ss.add(v);
        nSamples++;
    }

    public void add(int[] x, StatSummary ssIncoming) {

        // for each address that occurs, we're going to store something
        StatSummary ss = getStatsForceCreate(x);
        ss.add(ssIncoming);
        nSamples++;
    }

    public void printNonEmpty() {
        if (ntArray != null) {
            for (int i=0; i<ntArray.length; i++) {
                if (ntArray[i] != null) {
                    StatSummary ss = ntArray[i];
                    // System.out.println(i + "\t " + ss.n() + "\t " + ss.mean());
                    System.out.format("%d\t %.2f\t %d\n", i, ss.mean(), ss.n());
                }
            }
        } else {
            for (Double key : ntMap.keySet()) {
                StatSummary ss = ntMap.get(key);
                System.out.println(key + "\t " + ss.n() + "\t " + ss.mean());
            }
        }
    }

    public void printNonEmpty(Param[] params) {
        for (int i : tuple) {
            System.out.println(params[i].getName() + "\t ");
        }
        System.out.println();
        if (ntArray != null) {
            for (int i=0; i<ntArray.length; i++) {
                if (ntArray[i] != null) {
                    StatSummary ss = ntArray[i];
                    // System.out.println(i + "\t " + ss.n() + "\t " + ss.mean());
                    int[] ind = getIndices(i);
                    System.out.println(Arrays.toString(ind));
                    System.out.println(paramString(params, ind));
                    System.out.format("%d\t %.2f\t %d\n", i, ss.mean(), ss.n());
                    System.out.println();
                }
            }
        } else {
            for (Double key : ntMap.keySet()) {
                StatSummary ss = ntMap.get(key);
                System.out.println(key + "\t " + ss.n() + "\t " + ss.mean());
            }
        }
    }

    public String paramString(Param[] params, int[] ind) {
        StringBuffer sb = new StringBuffer();
        for (int i =0; i<ind.length; i++) {
            sb.append(params[tuple[i]].getValue(ind[ind.length-1-i]) + "\t ");
        }
        return sb.toString();
    }

    /**
     * Get stats but force creation if it does not already exists
     * @param x
     * @return
     */
    public StatSummary getStatsForceCreate(int[] x) {
        double address = address(x);
        if (ntArray != null) {
            StatSummary ss = ntArray[(int) address];
            if (ss == null) {
                ss = new StatSummary();
                nEntries++;
                ntArray[(int) address] = ss;
            }
            return ss;
        } else {
            StatSummary ss = ntMap.get(address);
            if (ss == null) {
                ss = new StatSummary();
                nEntries++;
                ntMap.put(address,ss);
            }
            return ss;
        }
    }

    public StatSummary getStats(int[] x) {
        double address = address(x);
        if (ntArray != null) {
            return ntArray[(int) address];
        } else {
            return ntMap.get(address);
        }
    }

    public int nSamples() {
        return nSamples;
    }

    public double address(int[] x) {

        // iterate over each of the tuple's dimensions
        // and calculate the address by adding in the value
        // after multiplying

        if (x.length != searchSpace.nDims()) {
            throw new RuntimeException("Search space dimensions should equal point dimensions: " +
                    searchSpace.nDims() + " : " + x.length);
        }

        double prod = 1;
        double addr = 0;
        for (int i : tuple) { //  i<tuple.length; i++) {
            if (x[i] >= searchSpace.nValues(i)) {
                throw new RuntimeException("Dimension exceeded: " + i + " : " + x[i] + " : " + searchSpace.nValues(i));
            }
            addr += prod * x[i];
            prod *= searchSpace.nValues(i);
        }
        return addr;
    }

    // note can only handle small sizes
    public int[] getIndices(int x) {

        int[] ind = new int[tuple.length];
        int ix = ind.length-1;
        try {
            for (int i : tuple) {
                // number of dimensions in this position
                int d = searchSpace.nValues(i);
                // take this off
                ind[ix--] = x % d;
                x /= d;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return ind;
    }




    /**
     *
     * @return the size of the search space
     */
    public double spaceSize() {
        // return SearchSpaceUtil.size(searchSpace);

        double size = 1;
        for (int i : tuple) {
            size *= searchSpace.nValues(i);
        }
        return size;
    }

    public String toString() {
        return tuple.length + "\t " + Arrays.toString(tuple) + "\t " + nSamples + "\t " + spaceSize() + "\t " + nEntries;
    }



}
