package test;

import utilities.StatSummary;

import java.util.Random;

public class ModuleStatsTest {
    static int nModules = 16;
    static int nStudents = 100;
    static Random random = new Random();
    static double mean = 65;
    static double sd = 20;
    static double threshold = 60;
    static StatSummary marks = new StatSummary("Marks");
    static StatSummary baseLine = new StatSummary("Baseline");
    public static void main(String[] args) {
        double nGood = 0;
        double nTot = 0;
        double nBase = 0;
        for (int i=0; i<nStudents; i++) {
            double base = randomMark();
            if (base >= threshold) nBase++;

            StatSummary student = new StatSummary();
            for (int j=0; j<nModules; j++) {
                double mark = randomMark();
                marks.add(mark);
                student.add(mark);
            }
            System.out.println(student.mean());
            if (student.mean() >= threshold) {
                nGood++;
            }
            nTot++;
        }
        System.out.println(marks);
        System.out.println();
        System.out.println("Percentage good at module level =" + 100 * nBase / nTot);
        System.out.println("Percentage good overall= " + 100 * nGood / nTot);
    }

    static double randomMark() {
        return random.nextGaussian() * sd + mean;
    }

}
