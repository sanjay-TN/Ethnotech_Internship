package Login_validation;

import java.util.Scanner;

//  write a program to check login validation username="admin", password=1234 with number of attempts=3.
public class Login {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        int attempts = 1;
        boolean logged_in = false;

        while (attempts <= 3) {
            System.out.println("enter username");
            String username = sc.nextLine();
            System.out.println("enter password: ");
            int password = sc.nextInt();
            sc.nextLine();

            if (username.equals("admin") && password == 1234) {
                System.out.println("welcome to Login page....");
                logged_in = true;
                break;

            } else {
                attempts++;
                if (attempts <= 3) {
                }
                System.out.println("invalid username password plese try again..");
            }

        }
        if (logged_in == false)

        {

            System.out.println("you have reached your  attempts try later..");
        }
        sc.close();
    }
}
