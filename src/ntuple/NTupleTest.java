package ntuple;

import evodef.SearchSpace;

/**
 * Created by simonmarklucas on 09/11/2016.
 *
 *  The idea is to model a search space using n-tuple sampling.
 *
 *  For each point in the space, we model the distribution of
 *  the number of each n-tuples that have occurred so far.
 *
 *  Then when considering which point to evaluate next, we
 *  look at what we know about it so far (can be in comparison
 *  to the current point)
 *
 *  Now, we do this in a number of different ways...
 *
 *  What we need is an estimate of how novel the new
 *  point is, compared to the other proposed points.
 *
 *  This can be broken down in to the novelty fingerprint
 *  of the point.
 *
 *  Think of it as an iteration over all the n-tuples that
 *  we use to model it.
 *
 *  Then we may summarise it in some way, and then make
 *  a decision.
 *
 *  A couple of things worth mentioning here:
 *
 *   1. We could do it in a differential way,
 *      observing only which tuples differ from the current point.
 *
 *   2. Or an absolute way, where we model the total value
 *      of the current point, both in terms of how heavily it has been sampled
 *      and the estimate of its value
 *
 *   Now, need to consider what the interface looks like...
 *
 *   Well, need to iterate over both the sample points and
 *
 */
public class NTupleTest {

    public static void main(String[] args) {

        System.out.println();

    }



    void estimate(int[] p) {

        // so we look up the value estimates, seeing p as
        // a unigram

        // and do this for every point in the search space...

        for (int i=0; i<p.length; i++) {

        }

    }

    void printUnary(SearchSpace searchSpace) {
        for (int i=0; i<searchSpace.nDims(); i++) {



        }
    }






}
