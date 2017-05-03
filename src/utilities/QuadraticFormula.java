package utilities;

/**
 * Created by simonmarklucas on 12/03/2017.
 */
public class QuadraticFormula {

    public static void main(String[] args) {

        QuadraticFormula qf = new QuadraticFormula(1, 1, -6);

        System.out.println(qf.root1());
        System.out.println(qf.root2());

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
