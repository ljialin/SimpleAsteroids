package ntuple.operator;

import evodef.DefaultMutator;
import evodef.Mutator;
import evodef.SearchSpace;
import jdk.javadoc.internal.doclets.formats.html.SourceToHTMLConverter;
import ntuple.ConvNTuple;

/*
 This class is a special mutator for use with a convolutional n-tuple.

 The operator uses a convolutional n-tuple to model the distribution of
 n-tuple indices.

 Next we have the following:



 */

public class ConvMutator implements Mutator {


    ConvNTuple convNTuple;

    @Override
    public int[] randMut(int[] x) {
        int[] y = new int[x.length];

        


        for (int i=0; i<x.length; i++) {
            y[i] = x[i];
        }

        return y;
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
