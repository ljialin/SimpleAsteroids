package ntuple;

// a convolutional version of the N-Tuple

// it actually uses a standard N-Tuple, but via a convolutional expansion of the input


import evodef.SearchSpace;
import utilities.StatSummary;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class ConvNTuple implements BanditLandscapeModel {

    double epsilon = 0.1;

    public static void main(String[] args) {
        ConvNTuple convNTuple = new ConvNTuple().setImageDimensions(20, 20);
        convNTuple.setFilterDimensions(8, 8);
        convNTuple.setMValues(2).setStride(2);

        convNTuple.makeIndicies();

        System.out.println("Address space size: " + convNTuple.addressSpaceSize());
    }

    // self explanatory variables
    int imageWidth, imageHeight;

    int filterWidth, filterHeight;

    // vecLength should be equal to imageWidth * imageHeight
    // images will often be passed as 1-d vectors, which is faster
    int vecLength;

    //
    int stride;

    int mValues; // number of values possible in each image pixel / tile

    HashMap<Double, StatSummary> ntMap;

    int nSamples;
    SearchSpace searchSpace;

    public ConvNTuple reset() {
        nSamples = 0;
        ntMap = new HashMap<>();
        return this;
    }


    public BanditLandscapeModel setSearchSpace(SearchSpace searchSpace) {
        this.searchSpace = searchSpace;
        return this;
    }

    @Override
    public SearchSpace getSearchSpace() {
        return searchSpace;
    }

    public ConvNTuple setImageDimensions(int imageWidth, int imageHeight) {
        this.imageWidth = imageWidth;
        this.imageHeight = imageHeight;
        return this;
    }

    public ConvNTuple setFilterDimensions(int filterWidth, int filterHeight) {
        this.filterWidth = filterWidth;
        this.filterHeight = filterHeight;
        return this;
    }

    public ConvNTuple setMValues(int mValues) {
        this.mValues = mValues;
        return this;
    }

    public ConvNTuple setStride(int stride) {
        this.stride = stride;
        return this;
    }

    private double address(int[] image, int[] index) {
        // System.out.println(image.length);
        double prod = 1;
        double addr = 0;
        for (int i : index) { //  i<tuple.length; i++) {
            // System.out.println(i);
            addr += prod * image[i];
            prod *= mValues;
        }
        return addr;
    }

    ArrayList<int[]> indices;

    public ConvNTuple makeIndicies() {
        indices = new ArrayList<>();
        for (int i = 0; i < imageWidth - filterWidth; i += stride) {
            for (int j = 0; j < imageHeight - filterHeight; j += stride) {
                // i and j are the start points in the image
                // but we need to iterate over all the filter positions
                // for each start point we create an array of filter sample points in image vector coordinates
                // first calculate the image x,y points, then map them to the one-d vector coords
                int[] a = new int[filterWidth * filterHeight];
                int filterIndex = 0;
                for (int k = 0; k < filterWidth; k++) {
                    for (int l = 0; l < filterHeight; l++) {
                        int x = i + k;
                        int y = j + l;
                        int ix = x + imageWidth * y;
                        a[filterIndex] = ix;
                        filterIndex++;
                    }
                }
                System.out.println(Arrays.toString(a));
                indices.add(a);
            }
        }
        // reset the stats after making new indices
        reset();
        System.out.println("Made index vectors: " + indices.size());
        return this;
    }

    public double addressSpaceSize() {
        // return SearchSpaceUtil.size(searchSpace);
        double size = 1;
        for (int i = 0; i < filterWidth * filterHeight; i++) {
            size *= mValues;
        }
        return size;
    }


    @Override
    public void addPoint(int[] p, double value) {
        // iterate over all the indices
        // calculate an address for each one

        for (int[] index : indices) {
            double address = address(p, index);
            StatSummary ss = getStatsForceCreate(address);
            ss.add(value);
        }
        nSamples++;
    }

    public int nSamples() {
        return nSamples;
    }

    public int nEntries() {
        return ntMap.keySet().size();
    }

    public StatSummary getStatsForceCreate(double address) {
        StatSummary ss = ntMap.get(address);
        if (ss == null) {
            ss = new StatSummary();
            ntMap.put(address, ss);
        }
        return ss;
    }


    @Override
    public int[] getBestSolution() {
        return new int[0];
    }

    @Override
    public int[] getBestOfSampled() {
        return new int[0];
    }

    @Override
    public int[] getBestOfSampledPlusNeighbours(int nNeighbours) {
        return new int[0];
    }

    @Override
    public Double getMeanEstimate(int[] x) {
        StatSummary ssTot = new StatSummary();
        for (int[] index : indices) {
            double address = address(x, index);
            StatSummary ss = ntMap.get(address);
            if (ss != null) {
                ssTot.add(ss.mean());
            }
        }
        return ssTot.mean();
    }

    public StatSummary getNoveltyStats(int[] x) {
        StatSummary ssTot = new StatSummary();
        for (int[] index : indices) {
            double address = address(x, index);
            StatSummary ss = ntMap.get(address);
            if (ss != null) {
                ssTot.add(ss.n());
            } else {
                ssTot.add(0);
            }
        }
        return ssTot;
    }

    @Override
    public double getExplorationEstimate(int[] x) {
        // StatSummary ssTot = new StatSummary();
        StatSummary exploreStats = new StatSummary();
        for (int[] index : indices) {
            double address = address(x, index);
            StatSummary ss = ntMap.get(address);
            if (ss != null) {
                exploreStats.add(explore(ss.n()));
            } else {
                exploreStats.add(explore(0));
            }
        }
        return exploreStats.mean();
    }

    double k = 2.0;

    public double explore(int n_i) {
        return k * Math.sqrt(Math.log(nSamples) / (epsilon + n_i));
    }

}

