public class Prime {
    public static void main(String[] args) {
        // int num1 = 3;
        int num1 = 23;
        if (num1 == 2) {
            System.out.println("prime number");
        } else {
            int count = 0;
            for (int i = 2; i <= num1; i++) {
                if (num1 % i == 0) {
                    count++;
                }
            }
            if (count == 2) {
                System.out.println("prime");
            } else {
                System.out.println("not prime");
            }
        }

    }

}