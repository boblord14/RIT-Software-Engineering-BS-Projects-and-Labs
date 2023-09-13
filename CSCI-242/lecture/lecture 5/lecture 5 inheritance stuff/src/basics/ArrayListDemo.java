package basics;

import java.util.ArrayList;

/**
 * Demonstrates some basic use of an ArrayList.
 */
public class ArrayListDemo {
    /**
     * The main method.
     *
     * @param args command line arguments (unused)
     */
    public static void main(String[] args) {
        // create an empty array of integers
        ArrayList<Integer> values = new ArrayList<>();

        // append some values to the list
        values.add(4);
        values.add(7);
        values.add(2);
        values.add(9);
        values.add(3);

        // counted for loop
        for (int i=0; i<values.size(); ++i) {
            System.out.println("values[" + i + "]: " + values.get(i));
        }

        // ArrayList's toString returns a string similar to Python's list syntax
        System.out.println("values: " + values);
        System.out.println("# elements in values: " + values.size());
        System.out.println("values index of 9: " + values.indexOf(9));

        System.out.println("values contains 9? " + values.contains(9));
        System.out.println("values contains 0? " + values.contains(0));

        // copy the list and change elements in the copy
        ArrayList<Integer> copy = new ArrayList<>(values);
        copy.remove(Integer.valueOf(7));
        copy.add(0, 1);
        System.out.println("copy: " + copy);
        System.out.println("values == copy? " + (values == copy));
    }
}
