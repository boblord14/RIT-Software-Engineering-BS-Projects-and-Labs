package puzzles.strings;

import puzzles.common.solver.Configuration;

import java.util.ArrayList;
import java.util.Collection;


public class StringsConfig implements Configuration {

    private final String input;

    private final String output;

    /**
     * Main config class for the strings puzzle.
     *
     * Starts by taking the input and output as arguments in the constructor, and storing them locally as private final
     * strings. After that the rest is called by the solver method or various hashmap things.
     *
     * @param input the initial string being given to the config
     * @param output what the final output should be
     */
    public StringsConfig(String input, String output){
        this.input=input;
        this.output=output;
    }

    /**
     * This isSolution is nothing special. Just a quick string equals check.
     *
     * @return the validity of the input being equal to the output.
     */
    @Override
    public boolean isSolution() {
        return (input.equals(output));
    }
/**
 * This is the bulk of the challenge for the strings variant. Does a lot of things here.
 *
*I asked prof Strout, this variant of getNeighbors getting the neighbors of one character at a time along the string
 * instead of every possible neighbor for every character in the string at once, is totally fine. If you want to see
 * getNeighbors done in the more traditional method that was probably intended see the water variant of this method.
 *
 * Ok diving into it. First uses an arrayList of configurations called neighborList which stores the valid configs
 * to return to the solver. After that a for loop goes through the input and output string, and compares the characters
 * at each specific index in the string(thankfully theyre the same length).
 *
 * If they aren't equal, it grabs the neighbors of that character(1 char up and 1 char down), does a quick check to make
 * sure they're still in the ascii bounds(capitals between 65 and 90) in ascii), does a quick check for chars with ascii
 * values that shouldnt be there(returns immediately if so to cause a no solutions response sooner or later), and rebuilds
 * two full strings based around the two changed characters.
 *
 * After that, I have a little bit of pruning/optimization. As its an easy test with characters and strings of how objectively
 * "more correct" a possible neighbor is, a custom function called charDist determines the "correctivity" of each neighbor
 * and adds the more correct one into neighborList first(which then gets processed faster in the queue). After this, the
 * function just breaks the for loop and returns whatever configs are in neighborList
 *
 * If I really really wanted to optimize even harder I could only return the more correct one, but I feel that would defeat
 * the purpose of a "graph" in the breath first search as it would essentially just be a linear straight shot to the solution.
 * This way instead makes it a bit more how it's supposed to be.
 */
    @Override
    public Collection<Configuration> getNeighbors() {
        ArrayList<Configuration> neighborList = new ArrayList<>();
        for(int i=0; i<this.output.length();i++){
            int asciiChar = (int)(this.input.charAt(i));
            int asciiResultChar = (int)(this.output.charAt(i));
            if (asciiChar!=asciiResultChar){
                int forwardChar = asciiChar+1;
                int backwardChar = asciiChar-1;
                if (forwardChar>90){
                    forwardChar=65;
                }
                if (backwardChar<65){
                    backwardChar=90;
                }
                if (65>forwardChar || 90<backwardChar){
                    return neighborList;
                }
                String forwardCharString = (this.input.substring(0, i) + Character.toString((char) forwardChar) + this.input.substring(i+1));
                String backwardCharString = (this.input.substring(0, i) + Character.toString((char) backwardChar) + this.input.substring(i+1));
                if (charDist(forwardChar, asciiResultChar)>=charDist(backwardChar, asciiResultChar)){
                    //pop the modified strings plus the output into here
                    neighborList.add(new StringsConfig(backwardCharString, this.output));
                    neighborList.add(new StringsConfig(forwardCharString, this.output));
                } else {
                    neighborList.add(new StringsConfig(forwardCharString, this.output));
                    neighborList.add(new StringsConfig(backwardCharString, this.output));
                }
                break;
            }
        }
        return neighborList;
    }

    /**
     *
     * @param inputChar the neighbor char from the input's char that's being built into the substring
     * @param destChar the destination char in the output string that the input char will eventually become
     * @return returns the shortest distance between the input and destination char when you traverse the alphabet
     *
     * This takes the two chars as input(see params above), finds the number of chars between the dest char and the input
     * char via absolute value of subtraction and stores it in firstdiff. This allows it to work regardless if the ascii
     * value of the dest char is greater or lesser than the value of the input char.
     *
     * Temp1 gets the distance between the dest char and the end of the alphabet, then temp2 gets the dist between the
     * input char and start of the alphabet, and then they get added, the sum getting stored in seconDiff. This is the
     * distance between the chars but from the other direction(looping back around to A from Z style)
     *
     * the smaller value is then returned here, which gets used in the getNeighbors to determine which new string is more
     * "correct" as the closer it is to the destination character, the less steps it will take to reach it.
     */
    private int charDist(int inputChar, int destChar){
        int firstDiff = Math.abs(destChar-inputChar);
        int temp1 = (90-destChar);
        int temp2 = (inputChar-65);
        int secondDiff = temp1+temp2;
        return Math.min(firstDiff, secondDiff);
    }

    /**
     * @param other whatever else gets checked here. most likely another configuration
     * @return if the two hashcodes are equal or not
     */
    @Override
    public boolean equals(Object other){
        return (this.hashCode()==other.hashCode());
    }

    /**
     * just returns the hashcode of the only truly unique value in the StringsConfig, the input
     * @return hashcode of the input(string)
     */
    @Override
    public int hashCode(){
        return input.hashCode();
    }

    /**
     * string representation of each StringsConfig is the only unique thing each one has to be identified by, the
     * input
     * @return the input value(already a string)
     */
    @Override
    public String toString(){
        return this.input;
    }

}
