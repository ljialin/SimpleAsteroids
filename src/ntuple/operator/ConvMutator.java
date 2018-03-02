package ntuple.operator;

import evodef.DefaultMutator;
import evodef.Mutator;
import evodef.SearchSpace;
import levelgen.MarioReader;
import ntuple.ConvNTuple;
import ntuple.SparseDistribution;
import utilities.Picker;

import java.util.Arrays;
import java.util.Random;

/*
 This class is a special mutator for use with a convolutional n-tuple.

 The operator uses a convolutional n-tuple to model the distribution of
 n-tuple indices.

 The mutation works as follows:

 1. Iterate over all filter locations (specified by the indices list of arrays of int), and for each one compute
    the degree of misfit between what is expected and what is observed.

    Note that this has to be done with respect to the entire image i.e. the match is holistic.

 2. Pick the key that has the maximum degree of misfit; actually we need the location it occurs at as well

 3. We now need to choose a set of replacement values to fill in to that filter location.
    The choice aims to find the one from the sample set that has the greatest mismatch i.e.
    between how frequent it should be compared to how frequent it actually is

 4. The set of values associated there are copied across.

 */

public class ConvMutator implements Mutator {

    static Random random = new Random();
    static double noiseLevel = 1e-0;

    boolean forceBorder = true;
    static int borderValue = MarioReader.border;

    ConvNTuple convNTuple;

    public ConvMutator setConvNTuple(ConvNTuple convNTuple) {
        this.convNTuple = convNTuple;
        return this;
    }

    public ConvMutator setForceBorder(boolean forceBorder) {
        this.forceBorder = forceBorder;
        return this;
    }

    boolean verbose = false;

    @Override
    public int[] randMut(int[] x) {
        int[] y = new int[x.length];

        // copy the solution
        for (int i=0; i<x.length; i++) {
            y[i] = x[i];
        }

        // now work out which parts are making largest contribution to divergence
        // to do this look out for the sample dis

        // we alerady have the p distribution

        // also need the q distribution to see which ones differ the most
        // this is the distribution of keys in the evolved image

        SparseDistribution qDis = new SparseDistribution();
        for (int[] index : convNTuple.indices) {
            double key = convNTuple.address(x, index);
            qDis.add(key);
        }

        // create a picker object to find the best one
        Picker<int[]> toReplace = new Picker<>(Picker.MAX_FIRST);

        if (verbose) {
            System.out.println("Searching for replacement indices:");
        }

        for (int[] a : convNTuple.indices) {
            double key = convNTuple.address(x, a);

            // the key thing now is to pick the one that is most replaceable
            // for each key find the degree of mismatch

            double p = convNTuple.sampleDis.getProb(key);
            double q = qDis.getProb(key);

            // a high score indicates something likely in the image x that
            // was not likely in the sample
            // add some random noise to make it give varied results even when the
            // image is all sky, for example

            double misfitScore = q * Math.log(q/p) + noiseLevel * random.nextDouble();

            // don't know why this should be inverted
            // misfitScore = 1 / misfitScore;

            // misfitScore = random.nextDouble();

            // System.out.format("\t %.4f\t %.4f\t %.4f\t %s\n", p, q, misfitScore, Arrays.toString(a));

            toReplace.add(misfitScore, a);
            // we are looking for keys that are over-represented in the image x
            // this could mean that it never occurs in the sampleDis
            // or just that it occurs less frequently


            // ok, may also need to store the values in order to recover them?
            // or not, could just store the indices to overwrite them

        }
        // System.out.println();

        int[] replacementIndices = toReplace.getBest();

        if (verbose) {
            System.out.format("To replace, picked: %s : %.4f\n", Arrays.toString(replacementIndices), toReplace.getBestScore());
            System.out.println();

            System.out.println("Searching for replacement values");
        }



        // now find out the one to replace it with


        Picker<Double> filler = new Picker<>(Picker.MAX_FIRST);
        for (Double key : convNTuple.sampleDis.statMap.keySet()) {
            double p = convNTuple.sampleDis.getProb(key);
            double q = qDis.getProb(key);

            
            double fillScore = p * Math.log(p/q) + noiseLevel * random.nextDouble();


            // fillScore = random.nextDouble();
            filler.add(fillScore , key);

            // System.out.format("\t %.4f\t %.4f\t %.4f\t %s\n", p, q, fillScore, Arrays.toString(convNTuple.sampleDis.valueArrays.get(key)));


        }


        int[] oldValues = new int[replacementIndices.length];
        for (int i=0; i<oldValues.length; i++) {
            oldValues[i] = x[replacementIndices[i]];
        }

        // we now have the one to modify
        Double fillKey = filler.getBest();
        int[] values = convNTuple.sampleDis.valueArrays.get(fillKey);


        if (verbose) {
            System.out.println("Old values: " + Arrays.toString(oldValues));
            System.out.format("Picked replacement values: %s : %.4f\n", Arrays.toString(values), filler.getBestScore());
            System.out.println();
        }

        // now go ahead and copy the values in

        for (int i=0; i<values.length; i++) {
            y[replacementIndices[i]] = values[i];
        }

        if (forceBorder) {
            writeBorder(y);
        }



        // trying to fix bug: see effect of setting everything to sky
//        for (int i=0; i<y.length; i++) {
//            y[i] = 2;
//        }

        if (verbose) {
            System.out.format("Fitness changed from %.3f to %.3f\n\n", convNTuple.getKLDivergence(x, 0), convNTuple.getKLDivergence(y, 0));
        }
        return y;

    }

    public void writeBorder(int[] y) {
        int w = convNTuple.imageWidth, h = convNTuple.imageHeight;
        for (int i=0; i<y.length; i++) {
            if (i % w == 0 || i % w == (w-1) || i / w == 0 || i / w == (h-1)) {
                y[i] = borderValue;
            }
        }
    }

    @Override
    public DefaultMutator setSearchSpace(SearchSpace searchSpace) {
        System.out.println("Not relevant");
        return null;
    }

    @Override
    public DefaultMutator setSwap(boolean swapMutation) {
        System.out.println("Does nothing");
        return null;
    }
}
