import java.util.Arrays;

public class SquareCheck {

    public static void main(String[] args) {
        int[] p1 = { 20, 10 };
        int[] p2 = { 10, 20 };
        int[] p3 = { 20, 20 };
        int[] p4 = { 10, 10 };

        System.out.println(isSquare(p1, p2, p3, p4) ? "Yes" : "No");
    }

    static boolean isSquare(int[] p1, int[] p2, int[] p3, int[] p4) {

        int[] d = new int[6];
        d[0] = distance(p1, p2);
        d[1] = distance(p1, p3);
        d[2] = distance(p1, p4);
        d[3] = distance(p2, p3);
        d[4] = distance(p2, p4);
        d[5] = distance(p3, p4);

        Arrays.sort(d);

        return d[0] > 0 &&
                d[0] == d[1] &&
                d[1] == d[2] &&
                d[2] == d[3] &&
                d[4] == d[5] &&
                d[4] == 2 * d[0];
    }

    static int distance(int[] p1, int[] p2) {
        return (p1[0] - p2[0]) * (p1[0] - p2[0])
                + (p1[1] - p2[1]) * (p1[1] - p2[1]);
    }
}
