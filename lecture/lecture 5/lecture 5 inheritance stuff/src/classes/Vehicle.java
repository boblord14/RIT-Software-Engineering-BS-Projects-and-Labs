package classes;

import java.util.Random;

/**
 * A vehicle in the Cannonball Run race.
 */
public class Vehicle {
    // these are the default constant values for all vehicles
    private static final String DEFAULT_NAME = "Default";
    private static final int DEFAULT_MPG = 30;
    private static final double DEFAULT_CAPACITY = 15;
    private static final int DEFAULT_MAX_SPEED = 100;
    private static final double REFILL_THRESHOLD = 1;
        
    /** vehicle's name */
    private String name;            
    
    /** miles per gallon */
    private int mpg;                    
    
    /** tank capacity in gallons */
    private double tankCapacity;        
    
    /** amount of fuel left in gallons */
    private double fuelLeft;            
    
    /** current speed in miles per hour */
    private int speed;
    
    /** maximum speed in miles per hour */
    private int maxSpeed;
    
    /** location on the road in miles */
    private int location;            
    
    /** Used for random number generation */
    private Random random;
    
    /**
     * The default constructor for a vehicle.
     */
    public Vehicle() {
        // demonstration of "constructor linking" where the other constructor
        // is invoked with the default values provided here
        this(DEFAULT_NAME, DEFAULT_MPG, DEFAULT_CAPACITY, DEFAULT_MAX_SPEED);
    }
    
    /**
     * Construct a vehicle object.
     * 
     * @param name The name of the vehicle.
     * @param mpg The miles per gallon for the vehicle.
     * @param tankCapacity The amount of fuel the vehicle can hold in gallons.
     * @param maxSpeed The maximum speed of the vehicle in miles per hour.
     */
    public Vehicle(String name, int mpg, double tankCapacity, int maxSpeed) {
        this.name = name;
        this.mpg = mpg;
        this.tankCapacity = tankCapacity;
        this.maxSpeed = maxSpeed;
        
        fuelLeft = tankCapacity;
        location = 0;                // start at the beginning of the road
        random = new Random();
    }
    
    /**
     * The vehicle will travel on the road at a random speed for the
     * number of minutes specified.  If the vehicle is low on gas
     * it will refuel instead of moving for this interval.
     * 
     * @param minutes The number of minutes to travel.
     * @param road The road the vehicle is traveling on.
     */
    public void travel(int minutes, Road road) {    
        speed = random.nextInt(maxSpeed);           // random speed
        int distanceToTravel = (int)(minutes / 60.0 * speed); // how far to go
        double fuelUsed = (double)distanceToTravel / mpg;     // fuel consumed
        
        // if the amount of fuel drops below the threshold, refuel
        if (fuelLeft - fuelUsed < REFILL_THRESHOLD) {
            fuelLeft = tankCapacity;
            System.out.println("\t" + name + " is refueling.");
        // otherwise travel on the road
        } else {
            location += distanceToTravel;
            if (location > road.getLength()) {    // stay on the road
                location = road.getLength();
            }
            fuelLeft -= fuelUsed;
            System.out.println("\t" + name + " travels " + distanceToTravel + 
                    " miles at " + speed  + " miles per hour to the " +
                    location + " mile marker.");
        }
    }
    
    /**
     * Get the name of the vehicle.
     * 
     * @return The vehicles name
     */
    public String getName() {
        return name;
    }
    
    /**
     * Get the location of the vehicle on the road.
     * 
     * @return The vehicles location.
     */
    public int getLocation() {
        return location;
    }

    /**
     * Increase speed by the given amount, up to maxSpeed
     * @param factor Amount to increase speed
     */
    public void accelerate(int factor) {
        speed += factor;
        if (speed > maxSpeed) {
            speed = maxSpeed;
        }
    }
    
    /**
     * Decrease speed by the given amount, down to zero
     * @param factor Amount to decrease speed
     */
    public void decelerate(int factor) {
        speed -= factor;
        if (speed < 0) {
            speed = 0;
        }
    }
    
    /**
     * Return a string representation of the vehicle.
     *
     * @return A printable string for the vehicle object.
     */
    @Override
    public String toString() {
        return "Vehicle(" + name +
                ", mpg=" + mpg +
                ", tankCapacity=" + tankCapacity +
                ", fuelLeft=" + fuelLeft +
                ", maxSpeed=" + maxSpeed +
                ", location=" + location + ")";
    }
    
    /**
     * Compares name, tankCapacity and maxSpeed of two Vehicles.
     *
     * @return Whether this Vehicle is equal to the one passed in.
     */
    @Override
    public boolean equals(Object other) {
        boolean result = false;
        if (other instanceof Vehicle) {
            Vehicle ov = (Vehicle) (other);
            result = name.equals(ov.name) &&
                    tankCapacity == ov.tankCapacity &&
                    maxSpeed == ov.maxSpeed;
        }
        return result;
    }
}