package Electricity_bill_calculator.com;

import java.util.Scanner;

// write java program to calculate elecricity bill for first 100 uints 5 rupee per units, next 100units  7rupess per unit, above 200 units, 10 rupees per unit.

public class Program {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        System.out.println("enter number of units...");
        int unit = sc.nextInt();
        int amount;
        // if (unit <= 100) {
        // amount = 5 * unit;
        // } else if (unit > 100 && unit <= 200) {
        // amount = (5 * 100) + 7 * (unit - 100);

        // } else {
        // amount = (5 * 100) + (100 * 7) + 10 * (unit - 200);
        // }

        // System.out.println("amount for " + unit + " unit is: " + amount);

        // using ternary operator

        amount = (unit <= 100) ? amount = unit * 5
                : (unit > 100 && unit < 200) ? 5 * 100 + 7 * (unit - 100)
                        : 5 * 100 + 7 * 100
                                + 10 * (unit - 200);
        System.out.println(amount);
        sc.close();

    }

}
