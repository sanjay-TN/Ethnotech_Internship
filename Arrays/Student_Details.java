package Arrays;

import java.util.Scanner;

// Store 5 student name in string array, store their marks in int array and display student name,total marks,grade.

public class Student_Details {
    public static void main(String[] args) {
        String[] names = new String[5];
        int[] marks = new int[5];

        Scanner sc = new Scanner(System.in);
        for (int i = 0; i < names.length; i++) {
            System.out.println("enter Student name: ");
            names[i] = sc.next();
            System.out.println("enter student marks:");
            marks[i] = sc.nextInt();

        }

        char grade = 0;
        for (int i = 0; i < names.length; i++) {
            if (marks[i] > 35 && marks[i] <= 60) {
                grade = 'C';
            } else if (marks[i] >= 80 && marks[i] <= 90) {
                grade = 'B';
            } else if (marks[i] > 90) {
                grade = 'A';
            } else if (marks[i] < 35) {
                grade = 'F';
            }

            System.out.println("name: " + names[i] + "\t" + " marks: " + marks[i] + "\t" + " grade is: " + grade);
        }
        sc.close();

    }

}
