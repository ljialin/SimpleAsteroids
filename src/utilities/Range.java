package utilities;

/**
 * User: Simon
 * Date: 21-Feb-2008
 * Time: 13:10:11
 */
public class Range {

    public static void main(String[] args) {
        Range r = new Range(-5, 10);
        for (double x = r.min; x<= r.max; x += 0.1) {
            System.out.println(x + "\t " + r.map(x));
        }
    }

    public double min, max;

    public Range(double min, double max) {
        this.min = min;
        this.max = max;
    }

    public double map(double x) {
        return (x - min) / (max - min);
    }

    public double inverse(double y) {
        return min + y * (max - min);
    }

    public void zoom(double x, double fac) {
        // zooms by this factor centred on x
        // if fact > 1 then zoom in
        // else zoom out
        // easy - the new size will be the current size / fac
        double size = max - min;
        double newSize = size / fac;
        // x specifies the new centre
        // x is always specified as between zero (min) and one (max)
        double centre = min + x * (max - min);
        min = centre - newSize/2;
        max = centre + newSize/2;
        System.out.format("%f to %f\n", min, max);
    }

    public String toString() {
        return "Range: " + min + " ... " + max;
    }
}
