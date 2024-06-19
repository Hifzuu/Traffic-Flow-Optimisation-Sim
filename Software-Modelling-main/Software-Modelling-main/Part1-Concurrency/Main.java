import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Main {
    public static void main(String[] args) {
        Clock clock = new Clock();
        // clock.start(); // Start the clock thread

        // Create roads with specified capacities
        Road roadNorthToC = new Road("", 50);
        Road roadCToB = new Road("", 10);
        Road roadBToA = new Road("", 7);
        Road roadEastToB = new Road("", 30);
        Road roadSouthToA = new Road("", 60);
        Road roadAToIndustrialPark = new Road("", 15);
        Road roadAToB = new Road("", 7);
        Road roadBToC = new Road("", 10);
        Road roadCToShoppingCentre = new Road("", 7);
        Road roadCToD = new Road("", 10);
        Road roadDToUniversity = new Road("", 15);
        Road roadDToStation = new Road("", 15);

        // Create entry points with specified rates
        EntryPoint entryNorth = new EntryPoint(roadNorthToC, 1, 550, clock); // 550 cars per hour
        EntryPoint entryEast = new EntryPoint(roadEastToB, 2, 300, clock); // 300 cars per hour
        EntryPoint entrySouth = new EntryPoint(roadSouthToA, 3, 550, clock); // 550 cars per hour

        // System.out.println(entryNorth.getInterval());

        // Junction A
        Road[] entryRoadsA = { roadSouthToA, roadBToA };
        Road[] exitRoadsA = { roadAToIndustrialPark, roadAToB };
        Junction junctionA = new Junction("JunctionA", entryRoadsA, exitRoadsA, new int[] { 4, 5 }, 60, clock);

        // Junction B
        Road[] entryRoadsB = { roadEastToB, roadCToB, roadAToB };
        Road[] exitRoadsB = { roadBToC, roadBToA };
        Junction junctionB = new Junction("JunctionB", entryRoadsB, exitRoadsB, new int[] { 4, 5 }, 60, clock);

        // Junction C
        Road[] entryRoadsC = { roadNorthToC, roadBToC };
        Road[] exitRoadsC = { roadCToB, roadCToShoppingCentre, roadCToD };
        Junction junctionC = new Junction("JunctionC", entryRoadsC, exitRoadsC, new int[] { 4, 5 }, 30, clock);

        // Junction D
        Road[] entryRoadsD = { roadCToD };
        Road[] exitRoadsD = { roadDToUniversity, roadDToStation };
        Junction junctionD = new Junction("JunctionD", entryRoadsD, exitRoadsD, new int[] { 4, 5 }, 30, clock);

        // Create car parks with specified capacities
        CarPark carParkIndustrialPark = new CarPark("IndustrialPark", 1000, roadAToIndustrialPark, clock);
        CarPark carParkShoppingCentre = new CarPark("ShoppingCentre", 400, roadCToShoppingCentre, clock);
        CarPark carParkStation = new CarPark("Station", 250, roadDToStation, clock);
        CarPark carParkUniversity = new CarPark("University", 200, roadDToUniversity, clock);

        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

        // Start the entry points and junctions in separate threads
        // start entry points
        clock.start();
        entryEast.start();
        entrySouth.start();
        entryNorth.start();

        // //start junctions
        junctionA.start();
        junctionB.start();
        junctionC.start();
        junctionD.start();

        // start car parks
        carParkIndustrialPark.start();
        carParkShoppingCentre.start();
        carParkStation.start();
        carParkUniversity.start();







        final int[] a = {0}; // Elapsed time counter in minutes

        // Schedule the task to run every minute
        scheduler.scheduleAtFixedRate(() -> {
            System.out.println("TIME: " + (a[0] * 10) + "m"); // Output elapsed time
            System.out.println(carParkIndustrialPark.getCarParkName() + ": " +
                    carParkIndustrialPark.getCounter() + " Spaces");
            System.out.println(carParkShoppingCentre.getCarParkName() + ": " +
                    carParkShoppingCentre.getCounter() + " Spaces");
            System.out.println(carParkStation.getCarParkName() + ": " +
                    carParkStation.getCounter() + " Spaces");
            System.out.println(carParkUniversity.getCarParkName() + ": " +
                    carParkUniversity.getCounter() + " Spaces");
            System.out.println(); // New line for clarity
            a[0]++;
        }, 0, 1, TimeUnit.MINUTES);

        // Cancel the scheduler after 6 iterations
        scheduler.schedule(() -> {
            scheduler.shutdown();
        }, 6, TimeUnit.MINUTES);










        // if (clock.getTick() % 60 == 0) {
        // System.out.println(
        // carParkIndustrialPark.getCarParkName() + ": " +
        // carParkIndustrialPark.getCounter()
        // + " Spaces");
        // System.out.println(
        // carParkShoppingCentre.getCarParkName() + ": " +
        // carParkShoppingCentre.getCounter()
        // + " Spaces");
        // System.out.println(carParkStation.getCarParkName() + ": " +
        // carParkStation.getCounter() + " Spaces");
        // System.out.println(
        // carParkUniversity.getCarParkName() + ": " + carParkUniversity.getCounter() +
        // " Spaces");
        // }

        // int elapsedTimeInMinutes = clock.getElapsedTimeInMinutes();
        // if (elapsedTimeInMinutes % 10 == 0) { // Check if elapsed time is a multiple
        // of 10 minutes
        // System.out.println("Program Ended!");

        // System.out.println(
        // carParkIndustrialPark.getCarParkName() + ": " +
        // carParkIndustrialPark.getCounter() + " Spaces");
        // System.out.println(
        // carParkShoppingCentre.getCarParkName() + ": " +
        // carParkShoppingCentre.getCounter() + " Spaces");
        // System.out.println(carParkStation.getCarParkName() + ": " +
        // carParkStation.getCounter() + " Spaces");
        // System.out.println(carParkUniversity.getCarParkName() + ": " +
        // carParkUniversity.getCounter() + " Spaces");
        // }

        try {
            clock.join();

            entrySouth.join();
            entryEast.join();
            entryNorth.join();

            junctionA.join();
            junctionB.join();
            junctionC.join();
            junctionD.join();

            carParkIndustrialPark.join();
            carParkShoppingCentre.join();
            carParkStation.join();
            carParkUniversity.join();
        } catch (InterruptedException e) {

        }

        System.out.println("\n");
        System.out.println("Total Cars Generated: " + EntryPoint.getTotalCars());
        System.out.println("Cars Queued: " + Road.getTotalCars());
        System.out.println("Cars Parked: " + CarPark.getTotalCars());

    }
}