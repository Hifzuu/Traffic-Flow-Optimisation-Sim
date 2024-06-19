import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalTime;

public class Junction extends Thread {
    private int MAX_ROUTES = 4;

    private Road[] entryRoads;
    private Road[] exitRoads;
    private int numEntryRoutes;
    private int numExitRoutes;
    private int[] sequence; // Sequence of entry routes to cycle through
    private int greenDuration; // Duration of green light for each route in milliseconds
    private Clock clock;
    private String name;
    private int counter;
    private boolean currentRoadFull;

    // Constructor
    public Junction(String name, Road[] entryRoads, Road[] exitRoads, int[] sequence, int greenDuration, Clock clock) {
        this.name = name;
        this.entryRoads = entryRoads;
        this.exitRoads = exitRoads;
        this.sequence = sequence;
        this.greenDuration = greenDuration;
        this.clock = clock;
        this.counter = 0;
        this.currentRoadFull = false;
    }

    @Override
    public void run() {
        try (FileWriter fw = new FileWriter(name + "_log.txt")) { 
            while (clock.isRunning()) {
                for (int i = 0; i < entryRoads.length; i++) {

                    int start = clock.getTick();
                    int end = start + (greenDuration / 10);
                    Vehicle vehicle = null;

                    // Green Light
                    while (clock.getTick() < end) {
                        if (!entryRoads[i].isEmpty()) {
                            String destination = entryRoads[i].getFrontCarDestination();

                            if (destination != null) {
                                moveCar(destination, entryRoads[i]);
                            }
                        }

                        try {
                            sleep(sleepingTime()); // Simulate the duration of car passing
                        } catch (InterruptedException e) {
                            Thread.currentThread().interrupt();
                            e.printStackTrace();
                        }
                    }
                    
                    writeLog(fw, vehicle, entryRoads[i]); // Write to log file
                    counter = 0;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void writeLog(FileWriter fw, Vehicle vehicle, Road road) throws IOException {
        int waitingCars = road.getRoadSize();
        String direction = road.getRoadName();
        String currentTime = LocalTime.now().toString();
        String logMessage;

        if (waitingCars > 0 && currentRoadFull) {
            logMessage = "Time: " + clock.getElapsedTimeInMinutes() + "m" + clock.getElapsedTimeInSeconds() + "s -"
                    + name + ": " + counter +
                    " cars through from " + direction + ", " + waitingCars +
                    " cars waiting. " + ": GRIDLOCK\n";
        } else {
            logMessage = "Time: " + clock.getElapsedTimeInMinutes() + "m" + clock.getElapsedTimeInSeconds() + "s -"
                    + name + ": " + counter +
                    " cars through from " + direction + ", " + waitingCars +
                    " cars waiting.\n";
        }

        fw.write(logMessage);
        fw.flush(); // Ensure the data is written immediately
        // counter = 0;
    }

    private synchronized long sleepingTime() // 12 cars per minute
    {
        long intervalDuration = 6;

        // Calculate sleeping time in milliseconds
        long sleepingTime = (intervalDuration * 1000) / 12;

        return sleepingTime;
    }

    private void moveCar(String destination, Road road) {
        if (name == "JunctionA" && destination == "IndustrialPark") {
            Road exitingRoad = exitRoads[0];
            if (!exitingRoad.isFull()) {
                Vehicle vehicle = road.removeVehicle();
                exitingRoad.addVehicle(vehicle);
                counter++;
            } else {
                currentRoadFull = true;
            }
        } else if (name == "JunctionA" && destination != "IndustrialPark") {
            Road exitingRoad = exitRoads[1];
            if (!exitingRoad.isFull()) {
                Vehicle vehicle = road.removeVehicle();
                exitingRoad.addVehicle(vehicle);
                counter++;
            } else {
                currentRoadFull = true;
            }
        } else if (name == "JunctionB" && destination == "IndustrialPark") {
            Road exitingRoad = exitRoads[1];
            if (!exitingRoad.isFull()) {
                Vehicle vehicle = road.removeVehicle();
                exitingRoad.addVehicle(vehicle);
                counter++;
            } else {
                currentRoadFull = true;
            }

        } else if (name == "JunctionB" && destination != "IndustrialPark") {
            Road exitingRoad = exitRoads[0];
            if (!exitingRoad.isFull()) {
                Vehicle vehicle = road.removeVehicle();
                exitingRoad.addVehicle(vehicle);
                counter++;
            } else {
                currentRoadFull = true;
            }
        } else if (name == "JunctionC" && destination == "IndustrialPark") {
            Road exitingRoad = exitRoads[0];
            if (!exitingRoad.isFull()) {
                Vehicle vehicle = road.removeVehicle();
                exitingRoad.addVehicle(vehicle);
                counter++;
            } else {
                currentRoadFull = true;
            }
        } else if (name == "JunctionC" && destination == "ShoppingCentre") {
            Road exitingRoad = exitRoads[1];
            if (!exitingRoad.isFull()) {
                Vehicle vehicle = road.removeVehicle();
                exitingRoad.addVehicle(vehicle);
                counter++;
            } else {
                currentRoadFull = true;
            }
        } else if (name == "JunctionC"
                && (destination != "IndustrialPark" && destination != "ShoppingCentre")) {
            Road exitingRoad = exitRoads[2];
            if (!exitingRoad.isFull()) {
                Vehicle vehicle = road.removeVehicle();
                exitingRoad.addVehicle(vehicle);
                counter++;
            } else {
                currentRoadFull = true;
            }
        } else if (name == "JunctionD" && destination == "University") {
            Road exitingRoad = exitRoads[0];
            if (!exitingRoad.isFull()) {
                Vehicle vehicle = road.removeVehicle();
                exitingRoad.addVehicle(vehicle);
                counter++;
            } else {
                currentRoadFull = true;
            }
        } else if (name == "JunctionD" && destination == "Station") {
            Road exitingRoad = exitRoads[1];
            if (!exitingRoad.isFull()) {
                Vehicle vehicle = road.removeVehicle();
                exitingRoad.addVehicle(vehicle);
                counter++;
            } else {
                currentRoadFull = true;
            }
        }
    }
}