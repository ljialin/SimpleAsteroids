package distance.convolution

import distance.pattern.Pattern
import distance.pattern.PatternDistribution
import evodef.DefaultMutator
import evodef.Mutator
import evodef.SearchSpace
import utilities.Picker
import java.util.*

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
class ConvMutator : Mutator {
    var forceBorder = false
    var convNTuple: ConvNTuple? = null
    fun setConvNTuple(convNTuple: ConvNTuple?): ConvMutator {
        this.convNTuple = convNTuple
        return this
    }

    fun setForceBorder(forceBorder: Boolean): ConvMutator {
        this.forceBorder = forceBorder
        return this
    }

    var verbose = false
    override fun randMut(x: IntArray): IntArray {
        // y will be the mutated copy
        val y = IntArray(x.size)
        // first make a true copy
        for (i in x.indices) {
            y[i] = x[i]
        }

        // now decide which indices to replace
        val index = random.nextInt(convNTuple!!.indices.size)
        val replacementIndices = convNTuple!!.indices[index]

        // now pick a set of values to replace them with
        val patterns = convNTuple!!.sampleDis!!.statMap.keys.toList()
        val pattern = patterns[random.nextInt(patterns.size)]

        for (i in replacementIndices.indices) {
            y[replacementIndices[i]] = pattern.v[i]
        }
        if (forceBorder) { // System.out.println("Forcing border");
            writeBorder(y)
        }
        return y
    }

    fun randMutOld(x: IntArray): IntArray {
        val y = IntArray(x.size)
        // writeBorder(x);
// forceBorder(y);
// copy the solution
        for (i in x.indices) {
            y[i] = x[i]
        }
        // now work out which parts are making largest contribution to divergence
// to do this look out for the sample dis
// we alerady have the p distribution
// also need the q distribution to see which ones differ the most
// this is the distribution of keys in the evolved image
        val qDis = PatternDistribution()
        var nExist = 0
        for (index in convNTuple!!.indices) {
            val key = Pattern().setPattern(x, index)
            qDis.add(key)
            if (convNTuple!!.sampleDis.statMap.containsKey(key)) {
                nExist++
            }
        }
        val leftTiles = convNTuple!!.indices[0]
        // System.out.println(Arrays.toString(leftTiles));
//        int[] v1 = sampleValues(x, leftTiles);
//        double leftKey = convNTuple.address(x, leftTiles);
//        int[] v2 = convNTuple.sampleDis.valueArrays.get(leftKey);
//
//        System.out.println("Evolved: " + Arrays.toString(v1));
//        System.out.println("Stored:  " + Arrays.toString(v2));
//        System.out.println("Key:     " + leftKey);
//        System.out.format("%d / % d (available: %d) \n ", nExist, convNTuple.indices.size(), convNTuple.sampleDis.statMap.size() );
//        System.out.println();
// create a picker object to find the best one
        val toReplace = Picker<IntArray>(Picker.MAX_FIRST)
        if (verbose) {
            println("Searching for replacement indices:")
        }
        for (index in convNTuple!!.indices) {
            val key = Pattern().setPattern(x, index)
            // the key thing now is to pick the one that is most replaceable
// for each key find the degree of mismatch
            val p = convNTuple!!.sampleDis.getProb(key)
            val q = qDis.getProb(key)
            // a high score indicates something likely in the image x that
// was not likely in the sample
// add some random noise to make it give varied results even when the
// image is all sky, for example
            var misfitScore = q * Math.log(q / p) + noiseLevel * random.nextDouble()
            // don't know why this should be inverted
// misfitScore = 1 / misfitScore;
            misfitScore = random.nextDouble()
            // System.out.format("\t %.4f\t %.4f\t %.4f\t %s\n", p, q, misfitScore, Arrays.toString(a));
            toReplace.add(misfitScore, index)
            // we are looking for keys that are over-represented in the image x
// this could mean that it never occurs in the sampleDis
// or just that it occurs less frequently
// ok, may also need to store the values in order to recover them?
// or not, could just store the indices to overwrite them
        }
        // System.out.println();
        val replacementIndices = toReplace.best
        if (verbose) {
            System.out.format("To replace, picked: %s : %.4f\n", Arrays.toString(replacementIndices), toReplace.bestScore)
            println()
            println("Searching for replacement values")
        }
        // now find out the one to replace it with
        val filler = Picker<Pattern>(Picker.MAX_FIRST)
        for (key in convNTuple!!.sampleDis.statMap.keys) {
            val p = convNTuple!!.sampleDis.getProb(key)
            val q = qDis.getProb(key)
            var fillScore = p * Math.log(p / q) + noiseLevel * random.nextDouble()
            fillScore = random.nextDouble()
            filler.add(fillScore, key)
            // System.out.format("\t %.4f\t %.4f\t %.4f\t %s\n", p, q, fillScore, Arrays.toString(convNTuple.sampleDis.valueArrays.get(key)));
        }
        val oldValues = IntArray(replacementIndices.size)
        for (i in oldValues.indices) {
            oldValues[i] = x[replacementIndices[i]]
        }
        // we now have the one to modify
        val fillKey = filler.best
        val values = fillKey.v
        if (verbose) {
            println("Old values: " + Arrays.toString(oldValues))
            System.out.format("Picked replacement values: %s : %.4f\n", Arrays.toString(values), filler.bestScore)
            println()
        }
        // now go ahead and copy the values in
        for (i in values.indices) {
            y[replacementIndices[i]] = values[i]
        }
        if (forceBorder) { // System.out.println("Forcing border");
            writeBorder(y)
        }
        // trying to fix bug: see effect of setting everything to sky
//        for (int i=0; i<y.length; i++) {
//            y[i] = 2;
//        }
        if (verbose) {
            // System.out.format("Fitness changed from %.3f to %.3f\n\n", convNTuple!!.getKLDivergence(x), convNTuple!!.getKLDivergence(y))
        }
        return y
    }

    fun sampleValues(x: IntArray, ix: IntArray): IntArray {
        val v = IntArray(ix.size)
        for (i in ix.indices) v[i] = x[ix[i]]
        return v
    }

    fun writeBorder(y: IntArray) {
        val w = convNTuple!!.imageWidth
        val h = convNTuple!!.imageHeight
        for (i in y.indices) {
            if (i % w == 0 || i % w == w - 1 || i / w == 0 || i / w == h - 1) {
                y[i] = borderValue
            }
        }
    }

    override fun setSearchSpace(searchSpace: SearchSpace): DefaultMutator {
        println("setSearchSPace(): Not relevant")
        return DefaultMutator(null)
    }

    override fun setSwap(swapMutation: Boolean): DefaultMutator {
        println("Does nothing")
        return DefaultMutator(null)
    }

    companion object {
        var random = Random()
        var noiseLevel = 1e-1
        var borderValue = 1 // MarioReader.border
    }
}
