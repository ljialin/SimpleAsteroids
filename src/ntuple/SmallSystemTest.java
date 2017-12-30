package ntuple;

import evodef.EvalNoisyWinRate;
import evodef.FitnessSpace;
import evodef.SearchSpaceUtil;
import utilities.ElapsedTimer;

public class SmallSystemTest {


    public static void main(String[] args) {

        // NTupleSystem nts = new NTupleSystem(new TenSpace(nDims, 2));
        int m = 5;
        int nDims = 5;


        FitnessSpace evalTrue = new EvalNoisyWinRate(nDims, m);
        FitnessSpace evalNoisy = new EvalNoisyWinRate(nDims, m, 1.0);

        NTupleSystem nts = new NTupleSystem();
        nts.setSearchSpace(evalNoisy);
        nts.addTuples();

        nts.addPoint(new int[]{1, 2, 3, 4, 0}, 1);
        nts.addPoint(new int[]{1, 1, 1, 1, 1}, 1);
        nts.addPoint(new int[]{0, 0, 1, 1, 0}, 1);
        nts.addPoint(new int[]{1, 2, 3, 4, 0}, 0);

        nts.printSummaryReport();

        nts.printDetailedReport();


    }

}
