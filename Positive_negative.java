import java.util.Scanner;

public class Positive_negative {
    public static void main(String[] args) {
        Scanner s = new Scanner(System.in);
        System.out.println("enter a numer");
        int a = s.nextInt();
        if (a > 0) {
            System.out.println("positive");
        } else {
            System.out.println("negative");
        }
        s.close();
    }
}
