public class CarPark extends Thread {
    private String name;
    private Road entryRoad;
    private final Vehicle[] cars;
    public static int totalCars = 0;
    private final int capacity;
    private int front; // Index of the front of the circular buffer
    private int rear; // Index of the rear of the circular buffer
    private int size; // Current number of elements in the buffer

    private Clock clock;
    private int counter = 0;

    // Constructor
    public CarPark(String name, int capacity, Road entryRoad, Clock clock) {
        this.name = name;
        this.capacity = capacity;
        this.entryRoad = entryRoad;
        this.cars = new Vehicle[capacity];
        this.front = 0;
        this.rear = 0;
        this.size = 0;

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
        while (clock.isRunning()) {

            // Sleep for the time it takes to admit each car (e.g., 12 seconds) which is 10
            try {
                sleep(getSleepTime());
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                e.printStackTrace();
            }

            if (!entryRoad.isEmpty()) {
                // Admit a car from the entry road to the car park

                if (counter < capacity)
                {
                    Vehicle car = entryRoad.removeVehicle();
                    if (car != null) {
                        if (addVehicle(car)) {
                            counter++;
                            incrementTotalCars();
                            long parkingTime = System.currentTimeMillis();
                            // System.out.println("welcome to CarPark, Vehicle with destination " +
                            // car.getDestination()
                            // + " parked at " + parkingTime);
                        }
                    }
                }
            }
        }
    }

    private long getSleepTime() {
        return 12000 / 10;
    }

    // Method to check if there is a vehicle available for admission
    private synchronized boolean hasVehicle() {
        return size > 0;
    }

    // Method to add a vehicle to the car park
    public synchronized boolean addVehicle(Vehicle vehicle) {
        if (hasSpace()) {
            cars[rear] = vehicle;
            rear = (rear + 1) % capacity;
            size++;
            // totalCars++;
            notify(); // Notify waiting threads that a vehicle is added
            return true;
        }
        return false;
    }

    public String getCarParkName() {
        return name;
    }

    public int getCounter() {
        return this.capacity - this.size;
    }

    // Method to remove a vehicle from the car park
    public synchronized Vehicle removeVehicle() {
        if (!isEmpty()) {
            Vehicle vehicle = cars[front];
            front = (front + 1) % capacity;
            size--;
            return vehicle;
        }
        return null;
    }

    // Method to check if the car park has space for a new vehicle
    public synchronized boolean hasSpace() {
        return size < capacity;
    }

    // Method to check if the car park is empty
    public synchronized boolean isEmpty() {
        return size == 0;
    }
}
