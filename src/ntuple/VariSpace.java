package ntuple;

import evodef.SearchSpace;

/**
 * Created by sml on 14/11/2016.
 */

public class VariSpace implements SearchSpace {


    int[] nValues;
    int nDims;

    public VariSpace(int[] nValues) {
        this.nValues = nValues;
        nDims = nValues.length;
    }

    @Override
    public int nDims() {
        return nDims;
    }

    @Override
    public int nValues(int i) {
        return nValues[i];
    }
}
