import java.util.Scanner;

public class Bank {
    private String username;
    private int Account_number;
    private double amount;
    private double balance;

    public void get_username() {
        System.out.println("username is: " + username);

    }

    public void set_username(String username) {
        this.username = username;
    }

    public void get_Account_number() {
        System.out.println("Account number Is: " + Account_number);

    }

    public void set_Account_number(int Account_number) {
        this.Account_number = Account_number;
    }

    public void get_amount() {
        System.out.println("Initial Deposited amount is: " + amount);

    }

    public void set_amount(double amount) {

        this.amount = amount;

    }

    public double get_balance() {
        balance += amount;
        return balance;
    }

    public void deposit(double amount) {
        if (amount > 0) {
            balance += amount;
            System.out.println("deposited " + amount + " rupees successfully...");
        }
        System.out.println("your current balance is:" + balance + "rupees");
    }

    public void withdraw(double amount) {
        if (amount > 0 && amount <= balance) {
            balance -= amount;
            System.out.println("you have withdrawed " + amount + " rupees");
        }
        System.out.println("your current balance after withdrawing " + amount + " is..." + balance);
    }

    public void check_balance() {
        balance += amount;
        System.out.println("current balance is: " + balance);
    }

}
