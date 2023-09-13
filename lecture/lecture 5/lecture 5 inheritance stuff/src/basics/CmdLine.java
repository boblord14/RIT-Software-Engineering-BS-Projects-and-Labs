package basics;

/**
 * A demonstration of looping over the command line arguments and working
 * with a native array of String's.
 */
public class CmdLine {
    /**
     * The main method.
     *
     * @param args the command line arguments (required)
     */
    public static void main(String[] args) {
        // counted for loop
        System.out.println("COUNTED FOR LOOP:");
        for (int i=0; i<args.length; ++i) {
            System.out.println(args[i]);
        }
        System.out.println();

        // while loop
        System.out.println("WHILE LOOP:");
        int i = 0;
        while (i < args.length) {
            System.out.println(args[i]);
            ++i;
        }
        System.out.println();

        // do while loop
        System.out.println("DO WHILE LOOP:");
        i = 0;
        do {
            System.out.println(args[i]);
            ++i;
        } while (i < args.length);
        System.out.println();

        // foreach loop
        System.out.println("FOR-EACH LOOP:");
        for (String s : args) {
            System.out.println(s);
        }
        System.out.println();
    }
}

/*
$ java CmdLine a bc d e fg
COUNTED FOR LOOP:
a
bc
d
e
fg

WHILE LOOP:
a
bc
d
e
fg

DO WHILE LOOP:
a
bc
d
e
fg

FOR-EACH LOOP:
a
bc
d
e
fg
*/
