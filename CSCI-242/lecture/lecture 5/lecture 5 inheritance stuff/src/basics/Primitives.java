package basics;

import java.util.ArrayList;

/**
 * Demonstrates primitives and the autoboxing/unboxing with their wrapper classes.
 */
public class Primitives {
    private static boolean bool;  // true or false usually implemented as a byte
    private static byte by;       // 8 bit signed: -128 to 127 inclusive
    private static char ch;       // 16 bit unicode: '\u0000' to '\uffff'
    private static double d;      // double precision 64 bit floating point
    private static float f;       // single precision 32 bit floating point
    private static int i;         // 32 bit signed: -2^31 to 2^31-1
    private static long l;        // 64 bit signed: -2^63 to 2^63-1
    private static short s;       // 16 bit signed: -32768 to 32767

    private static void print_primitives() {
        System.out.println("boolean: " + bool);
        System.out.println("byte: " + by);
        System.out.println("char: " + ch);
        System.out.println("double: " + d);
        System.out.println("float: " + f);
        System.out.println("int: " + i);
        System.out.println("long: " + l);
        System.out.println("short: " + s);
    }

    /**
     * The main method.
     *
     * @param args command line arguments (unused)
     */
    public static void main(String[] args) {
        // print the primitives with their default values
        System.out.println("DEFAULT:");
        print_primitives();
        System.out.println();

        // sample assignments
        bool = true;
        by = -1;
        ch = '\u2615';
        d = Math.sqrt(2);
        f = 10.5f;
        i = 10;
        l = 200L;
        s = -5;
        System.out.println("ASSIGNED:");
        print_primitives();
        System.out.println();

        // Autoboxing
        Boolean boolObj = bool;
        Byte byteObj = by;
        Character charObj = ch;
        Double doubleObj = d;
        Float floatObj = f;
        Integer intObj = i;
        Long longObj = l;
        Short shortObj = s;

        // Autoboxing and unboxing
        ArrayList<Integer> vals = new ArrayList<>();
        for (int val=0; val<100; val+=10) {
            vals.add(val);              // Autboxing int -> Integer
        }
        System.out.println("Vals: " + vals);

        for (int index=0; index<vals.size(); ++index) {
            int val = vals.get(index);  // Unboxing Integer -> int
        }
    }
}

/*
$ java Primitives
DEFAULT:
boolean: false
byte: 0
char:  
double: 0.0
float: 0.0
int: 0
long: 0
short: 0

ASSIGNED:
boolean: true
byte: -1
char: ☕
double: 1.4142135623730951
float: 10.5
int: 10
long: 200
short: -5

Vals: [0, 10, 20, 30, 40, 50, 60, 70, 80, 90]
*/