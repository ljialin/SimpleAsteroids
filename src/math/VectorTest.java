package math;

public class VectorTest {
    public static void main(String[] args) {
        Vector2d a = new Vector2d(0, 0);
        Vector2d b = new Vector2d(3, 4);
        System.out.println(a.dist(b));
    }
}
