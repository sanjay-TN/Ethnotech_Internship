
public class Account extends Bank {

    public static void main(String[] args) {
        Account a = new Account();
        // setting account info
        a.set_Account_number(101);
        a.set_amount(500);
        a.set_username("abx");

        // retreiving account info
        a.get_Account_number();
        a.get_amount();
        a.get_username();

        // // performing differrent operations
        a.check_balance();
        a.deposit(10000);
        a.withdraw(5005);

    }

}
