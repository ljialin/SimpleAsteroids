package utilities;

/**
 * Created by simonmarklucas on 12/03/2017.
 */
public class QuadraticFormula {

    public static void main(String[] args) {

        // QuadraticFormula qf = new QuadraticFormula(1, -5, 1);
        // QuadraticFormula qf = new QuadraticFormula(-3, 12, -11);
        // QuadraticFormula qf = new QuadraticFormula(3, -52, 160);
        QuadraticFormula qf = new QuadraticFormula(4.9, -0.7, -1.175);



        System.out.println(qf.root1());
        System.out.println(qf.root2());
        System.out.println();

        // this example is a solution to a problem posted on Brilliant.com
        for (double x : new double[]{0.2, 4.8}) {
            System.out.println(x * x + 1 / (x * x));
        }
        System.out.println();

    }

    double a, b, c;

    public QuadraticFormula(double a, double b, double c) {
        this.a = a;
        this.b = b;
        this.c = c;
    }

    double det() {
        return Math.sqrt(b * b - 4 * a * c);
    }

    double root1() {
        return (-b + det()) / (2 * a);
    }
    double root2() {
        return (-b - det()) / (2 * a);
    }
}
