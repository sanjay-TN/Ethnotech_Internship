package RegEx;

import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class reg {
    public static void checkregex(String re,String str){
        Pattern pattern=Pattern.compile(re);
        Matcher macher=pattern.matcher(str);
        boolean maches=macher.matches();
        System.out.println(maches);
    }

    public static void main(String[] args) {
        while (true) {
            System.out.println("enter regular expression: ");
            Scanner sc=new Scanner(System.in);
            String re=sc.next();
            System.out.println("enter your string: ");
            String str=sc.next();
            reg.checkregex(re, str);
            System.out.println("want to exit? [Y/N]");
            String choice=sc.nextLine();
            if (choice.equalsIgnoreCase("Y")) {
                break;
            }
        }
    }
    
}
