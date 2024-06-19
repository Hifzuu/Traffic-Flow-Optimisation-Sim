import java.util.Random;

public class EntryPoint extends Thread {
    private static final String[] DESTINATIONS = { "University", "Station", "ShoppingCentre", "IndustrialPark" };
    private static final double[] DESTINATION_WEIGHTS = { 0.1, 0.2, 0.3, 0.4 };

    public static int totalCars = 0;
    private Road road;
    private int entryPointID;
    private int carsPerHour;
    private Random random;
    private Clock clock;

    private int tracker = 0;

    // Constructor
    public EntryPoint(Road road, int entryPointID, int carsPerHour, Clock clock) {
        this.road = road;
        this.entryPointID = entryPointID;
        this.carsPerHour = carsPerHour;
        this.random = new Random();
        this.clock = clock;
    }

    private synchronized static void incrementTotalCars()
    {
        totalCars++;
    }

    public synchronized static int getTotalCars()
    {
        return totalCars;
    }

    @Override
    public void run() {
        // while (tracker < 5) {
        while (clock.isRunning()) {
            try {
                sleep(getInterval());
                // sleep(655);
                // generateVehicle();
                // tracker++;
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            // This is working
            if (!road.isFull()) {
                generateVehicle();
                tracker++;
            }

        }
    }

    private synchronized void generateVehicle() {
        String destination = generateRandomDestination();
        long entryTime = System.currentTimeMillis();
        Vehicle vehicle = new Vehicle(destination, entryTime);
        road.addVehicle(vehicle);
        incrementTotalCars();
    }

    private String generateRandomDestination() {
        // Generate a random number to determine the destination based on weightings
        double random = Math.random() * 100;

        if (random < 10) {
            return "University";
        } else if (random < 30) {
            return "Station";
        } else if (random < 60) {
            return "ShoppingCentre";
        } else {
            return "IndustrialPark";
        }
    }

    private int getInterval() {
        // return (360000 / carsPerHour) ;
        double ratePerMinute = carsPerHour / 6.0;
        int interval = (int) Math.round(60 * 1000 / ratePerMinute); // Convert from minutes to milliseconds
        return interval;
    }

}

// private String chooseDestination() {
// double rand = random.nextDouble();
// double cumulativeWeight = 0.0;

// for (int i = 0; i < DESTINATIONS.length; i++) {
// cumulativeWeight += DESTINATION_WEIGHTS[i];
// if (rand <= cumulativeWeight) {
// return DESTINATIONS[i];
// }
// }
// return DESTINATIONS[DESTINATIONS.length - 1];
// }
