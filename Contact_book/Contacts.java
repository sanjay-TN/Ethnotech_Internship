import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

class Duplicate_Key extends Exception {
    Duplicate_Key(String s) {
        super(s);
    }

    Duplicate_Key() {
    }
}

class No_data extends Exception {
    No_data(String s) {
        super(s);
    }

    No_data() {
    }
}

public class Contacts {
    static Scanner s = new Scanner(System.in);
    static HashMap<Integer, String> hm = new HashMap<>();
    public static int choice;

    public static void main(String[] args) throws Duplicate_Key, No_data {

        do {
            System.out.println("1.Add...");
            System.out.println("2.Remove...");
            System.out.println("3.View...");
            System.out.println("4.Exit...");
            System.out.println("enter your choice: ");
            choice = s.nextInt();
            switch (choice) {
                case 1:
                    System.out.println("enter key: ");
                    int key = s.nextInt();
                    System.out.println("enter phone numbers: ");
                    String ph = s.next();
                    add(key, ph);
                    break;

                case 2:
                    System.out.println("enter key: ");
                    int n = s.nextInt();
                    remove(n);
                    break;
                case 3:
                    show();
                    break;

                default:
                    System.out.println("exititing...");
                    return;

            }

        } while (choice <= 4);

    }

    static void add(int a, String b) {
        try {
            if (hm.containsKey(a))
                throw new Duplicate_Key("duplication is not allowed ..plese try with inserting different key ");
            else {
                hm.put(a, b);
                System.out.println("added key: " + a + " phone number: " + b);
            }
        } catch (Duplicate_Key e) {
            e.printStackTrace();
        }

    }

    static void remove(int k) {
        try {
            if (!hm.containsKey(k))
                throw new No_data("no data is available for this key plese enter correct key...");
            else {
                System.out.println("removed key whith values " + hm.remove(k));
            }
        } catch (No_data e) {
            e.printStackTrace();
        }
    }

    static void show() {
        for (Map.Entry<Integer, String> entry : hm.entrySet()) {
            System.out.println("key: " + entry.getKey() + " phone number: " + entry.getValue());
        }
    }
}
