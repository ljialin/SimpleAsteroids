package evodef;

import java.util.Random;

/**
 * Created by simonmarklucas on 14/08/2016.
 */
public class RegularSearchSpace implements SearchSpace {

    int nDims;
    int m;

    public RegularSearchSpace(int nDims, int m) {
        this.nDims = nDims;
        this.m = m;
    }

    @Override
    public int nDims() {
        return nDims;
    }

    @Override
    public int nValues(int i) {
        return m;
    }

}
