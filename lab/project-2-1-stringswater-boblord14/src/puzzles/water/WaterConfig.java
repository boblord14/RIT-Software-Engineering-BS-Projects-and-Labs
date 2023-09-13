package puzzles.water;

import puzzles.common.solver.Configuration;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

public class WaterConfig implements Configuration {

    private final int amount;

    private final int[][] bucketVals;

    /**
     * Main config for the water variant, constructor here
     *
     * @param amount what the final amount of water in any bucket should be
     * @param bucketVals 2d array composed of the bucket sizes in the first slot, and the corresponding current fill levels
     *                   in the second slot
     *
     * Rest is all stuff managed by the solver and the hashmap, just like the strings version
     */
    public WaterConfig(int amount, int[][] bucketVals){
        this.amount=amount;
        this.bucketVals=bucketVals;
    }

    /**
     * isSolution is a bit more complex here. Just tests every bucket to see if it has the amount of water in it that the
     * solution wants, and returns true if any of them do
     *
     * @return if any buckets have the solution amount of water
     */
    @Override
    public boolean isSolution() {
        for (int[] bucketVal : bucketVals) {
            if (bucketVal[1] == amount) {
                return true;
            }
        }
        return false;
    }

    /**
     *  Again this is the beefy one. Sort of similar to strings but with some key differences due to the nature of it.
     *  neighborList is back in exactly the same form/function it was in the strings version. Read the documentation for that
     *  one if you're interested.
     *
     *  First off, for each of the 3 main configuration variants(dump, fill, pour), each one uses a copy of bucketVals
     *  created by a for loop. This copy is termed bucketOptions and is the 2d array passed to the new WaterConfigs that
     *  get added to neighborList. bucketOptions' state gets reset to the state of bucketVals every time a different bucket
     *  is used as the original states stay the same. Sadly copying 2d arrays is a bit annoying if I don't want them to
     *  reference each other. Hence the for loop tacked on each main one with the Arrays.copyOf in it.
     *
     *  First new thing is adding the configurations where x bucket gets dumped out fully. A for loops allows it to dump
     *  every bucket as a seperate config, and the end states of the buckets are recorded in bucketOptions. If a bucket
     *  isn't empty, it can be dumped, and does so by setting the value for it in bucketOptions to 0 and then adding
     *  the current state of bucketOptions to a new WaterConfig that gets tossed into neighborList.
     *
     *  The second big one is the fill configurations. Very similar to the dump ones, but just tests if a bucket isn't full
     *  first, and then fills it assuming it isn't full. Same process before and after as the dump configurations.
     *
     *  The third and most complicated one is the pour configurations. This uses a double for loop, as every bucket should
     *  be able to pour into every other bucket(exception of itself of course). In the for loops, i is the bucket doing the
     *  pouring, j is the target bucket getting poured into. An int amountToPour is the empty space in the j bucket.
     * Then, a big if statement is used to prune off some branches, making sure that i and j can't be the same bucket(can't
     * pour into self), that the j bucket isn't full(end result would be the same as dumping i), that the i bucket isn't
     * empty(result would be no change in bucket states), and that the i bucket's fill and amount to pour arent the same
     * (result would be the same as filling bucket j and emptying i, saves total configs this way).
     * After this if statement is passed, the pouring commences. An if statement testing if there will be room left to
     * fill in the j bucket, if so the entire contents of i is dumped into j, i becomes 0 and j becomes j + i. If no room
     * would be left after the pour, the amountToFill is filled up from i, with whatever left in i remaining in i and j
     * being set to full(by setting the fill value equal to the capacity). After that the bucketOptions that this all took
     * place in was added to fresh WaterConfigs and given to neighborList.
     *
     * @return all valid possible configs that aren't the same as the initial state
     */
    @Override
    public Collection<Configuration> getNeighbors() {
        ArrayList<Configuration> neighborList = new ArrayList<>();
        //add dump configurations
        for(int i=0;i< bucketVals.length;i++){
            int[][] bucketOptions= new int[bucketVals.length][2];
            for(int j=0;j< bucketVals.length;j++){
                bucketOptions[j]=Arrays.copyOf(bucketVals[j], 2);
            }
            if(bucketVals[i][1] !=0){
                bucketOptions[i][1]=0;
                neighborList.add(new WaterConfig(amount, bucketOptions));
            }
        }
        //add fill configurations
        for(int i=0;i< bucketVals.length;i++){
            int[][] bucketOptions= new int[bucketVals.length][2];
            for(int j=0;j< bucketVals.length;j++){
                bucketOptions[j]=Arrays.copyOf(bucketVals[j], 2);
            }
            if(bucketVals[i][1] != bucketVals[i][0]){
                bucketOptions[i][1] = bucketVals[i][0];
                neighborList.add(new WaterConfig(amount, bucketOptions));
            }
        }
        //add pour configurations
        for(int i=0;i< bucketVals.length;i++){
          for (int j=0;j< bucketVals.length;j++){
              int amountToPour = bucketVals[j][0]-bucketVals[j][1];
              if(i!=j && (bucketVals[j][1]!=bucketVals[j][0] && (bucketVals[i][1]!=0)) && (bucketVals[i][1]!=amountToPour)){
                  int[][] bucketOptions= new int[bucketVals.length][2];
                  for(int k=0;k< bucketVals.length;k++){
                      bucketOptions[k]=Arrays.copyOf(bucketVals[k], 2);
                  }
                if (amountToPour>bucketVals[i][1]){
                    bucketOptions[i][1] = 0;
                    bucketOptions[j][1] = (bucketVals[j][1] + bucketVals[i][1]);
                } else{
                    bucketOptions[i][1] = (bucketVals[i][1] - amountToPour);
                    bucketOptions[j][1] = bucketVals[j][0];
                }
                  neighborList.add(new WaterConfig(amount, bucketOptions));
              }
          }
        }
        return neighborList;
    }

    /**
     * identical to the strings variant
     * @param other whatever else gets checked here. most likely another configuration
     * @return if the two hashcodes are equal or not
     */
    @Override
    public boolean equals(Object other){
        return (this.hashCode()==other.hashCode());
    }

    /**
     * uses the toString method below this and then hashcodes that
     * @return hashcode of an array containing the fill of every bucket
     */
    @Override
    public int hashCode(){
        return this.toString().hashCode();
    }

    /**
     * takes all the bucket's fills in a single 1d array, and toStrings that
     * @return all the fills of the buckets
     */
    @Override
    public String toString(){
        int[] temp = new int[bucketVals.length];
        for(int i=0;i< bucketVals.length;i++){
            temp[i]=bucketVals[i][1];
        }
        return Arrays.toString(temp);
    }
}
