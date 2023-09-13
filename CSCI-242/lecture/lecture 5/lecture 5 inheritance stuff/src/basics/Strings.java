package basics;

import java.util.Scanner;

public class Strings {
    public static void main(String[] args) {
        String s1 = new String("hello");
        String s2 = "hello";   // canonical string
        String s3 = s1;        // another reference to same string that s1 refers to

        // string length
        System.out.println("s1.length(): " + s1.length());

        // string comparison, == vs equals
        System.out.println("s1 == s3? " +  (s1 == s3));
        System.out.println("s1 == s2? " +  (s1 == s2));
        System.out.println("s1.equals(s2)? " + s1.equals(s2));

        // string's behaving badly - null reference
        String s4 = null;
        try {
            System.out.println("s4.length(): " + s4.length());
        } catch (NullPointerException npe) {
            System.err.println("s4 is: " + npe.getMessage());
        }

        // accessing characters in a string
        for (int i=0; i<s1.length(); ++i) {
            System.out.println("s1[" + i + "]: " + s1.charAt(i));
        }

        // "slicing" a string, e.g. in python s1[1:3]
        System.out.println("s1.substring(1,3): " + s1.substring(1,3));

        // splitting a string into a native array of strings
        String things = new String ("apple banana carrot donut");
        String[] fields = things.split(" ");
        for (int i=0; i<fields.length; ++i) {
            System.out.println("fields[" + i + "]: " + fields[i]);
        }

        // it is now legal to switch on a string
        switch (fields[0]) {
            case "apple":
            case "banana":
                System.out.println("fruit!");
                break;
            case "carrot":
                System.out.println("vegetable!");
                break;
            case "donut":
                System.out.println("dessert!");
                break;
            default:
                System.out.println("???");
        }

        // converting a string to an integer
        String sNum = "135";
        int num = Integer.parseInt(sNum);
        System.out.println("num: " + num);

        // converting a number to a string
        sNum = Integer.toString(num);
        System.out.println("sNum:" + sNum);

        // reading a string from standard input
        Scanner in = new Scanner(System.in);
        System.out.print("Enter a string: ");
        String input = in.next();
        System.out.println("You entered: " + input);

        // reading an integer from standard input
        System.out.print("Enter a number: ");
        num = in.nextInt();
        System.out.println("You entered: " + num);
    }
}

/*
$ java Strings
s1.length(): 5
s1 == s3? true
s1 == s2? false
s1.equals(s2)? true
s4 is: null
s1[0]: h
s1[1]: e
s1[2]: l
s1[3]: l
s1[4]: o
s1.substring(1,3): el
fields[0]: apple
fields[1]: banana
fields[2]: carrot
fields[3]: donut
fruit!
num: 135
sNum:135
Enter a string: hello
You entered: hello
Enter a number: 10
You entered: 10
*/