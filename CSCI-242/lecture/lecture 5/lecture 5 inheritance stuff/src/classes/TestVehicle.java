package classes;

/**
 * A test class for the Vehicle class
 */
public class TestVehicle {
    /**
     * The main method
     * @param args command line arguments (unused)
     */
    public static void main(String[] args) {
        Vehicle vehicle1 = new Vehicle("Honda", 30, 15, 100);
        vehicle1.accelerate(20);
                
        Vehicle vehicle1ref = vehicle1;      // reference to same vehicle
        vehicle1ref.decelerate(10);

        System.out.println(vehicle1);         // implicit: vehicle.toString()

        Vehicle vehicle2 = new Vehicle();
        System.out.println("Location: " + vehicle2.getLocation());
        System.out.println(vehicle2);         // implicit: vehicle.toString()

        Vehicle vehicle3 = new Vehicle();
        System.out.println("Same vehicle? " + (vehicle2 == vehicle3));
        System.out.println("Equivalent vehicle? " + (vehicle2.equals(vehicle3)));
    }
}



