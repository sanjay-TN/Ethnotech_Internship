package Decorators;

import java.util.*;

public class Solution {

    static class Person {
        String firstName, lastName, gender;
        int age, id;

        Person(String fn, String ln, int a, String g, int i) {
            this.firstName = fn;
            this.lastName = ln;
            this.age = a;
            this.gender = g;
            this.id = i;
        }
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        System.out.print("Enter number of people: ");
        int n = Integer.parseInt(sc.nextLine());

        List<Person> list = new ArrayList<>();
        System.out.println("Enter details (FirstName LastName Age Gender):");

        for (int i = 0; i < n; i++) {
            String input = sc.nextLine();
            String[] data = input.split(" ");

            // list.add(new Person(data[0], data[1], Integer.parseInt(data[2]), data[3],
            // i));
            list.add(new Person(input, input, Integer.parseInt(data[2]), input, i));
        }

        list.sort((p1, p2) -> {
            if (p1.age != p2.age) {
                return p1.age - p2.age;
            }
            return p1.id - p2.id;
        });

        System.out.println("\n--- Sorted Directory ---");
        for (Person p : list) {
            String title = p.gender.equalsIgnoreCase("M") ? "Mr." : "Ms.";
            System.out.println(title + " " + p.firstName + " " + p.lastName);
        }

        sc.close();
    }
}
