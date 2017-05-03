package evodef;

import java.util.ArrayList;

/**
 * Created by simonmarklucas on 24/10/2016.
 */
public class EvoVectorSet implements SearchSpace{

    public ArrayList<EvoDoubleSet> params;

    public EvoVectorSet(ArrayList<EvoDoubleSet> params) {
        this.params = params;
    }

    public EvoVectorSet() {
        params = new ArrayList<>();
    }

    @Override
    public int nDims() {
        return params.size();
    }

    @Override
    public int nValues(int i) {
        return params.get(i).values.length;
    }

    public void printValues(int[] p) {
        for (int i=0; i<nDims(); i++) {
            System.out.println(params.get(i).name + "\t " + params.get(i).values[p[i]]);
        }
    }

}
