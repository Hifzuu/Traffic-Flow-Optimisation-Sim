import java.util.concurrent.Semaphore;

public class Road {
    private String name;
    private final int capacity;
    private Vehicle[] cars;
    private int front;
    private int rear;
    private int size;
    public static int totalCars = 0;
    private Semaphore semaphore = new Semaphore(1);

    public Road(String name, int capacity) {
        this.name = name;
        this.capacity = capacity;
        this.cars = new Vehicle[capacity];
        this.front = 0;
        this.rear = 0;
        this.size = 0;
    }

    private synchronized static void incrementTotalCars()
    {
        totalCars++;
    }

    private synchronized static void decrementTotalCars()
    {
        totalCars--;
    }

    public synchronized static int getTotalCars()
    {
        return totalCars;
    }

    public synchronized String getFrontCarDestination() {
        if (!isEmpty()) {
            return cars[front].getDestination();
        } else {
            return null; // or throw an exception, depending on your requirements
        }
    }

    // Method to add a vehicle to the road
    public void addVehicle(Vehicle vehicle) {
        try {
            semaphore.acquire();
            if (hasSpace()) {
                cars[rear] = vehicle;
                rear = (rear + 1) % capacity;
                size++;
                incrementTotalCars();
                // totalCars++;
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } finally {
            semaphore.release();
        }
    }

    public String getRoadName()
    {
        return name;
    }

    // Method to remove a vehicle from the road
    public Vehicle removeVehicle() {
        try {
            semaphore.acquire();
            if (!isEmpty()) {
                Vehicle vehicle = cars[front];
                front = (front + 1) % capacity;
                size--;
                // totalCars--;
                decrementTotalCars();
                return vehicle;
            }
            return null;
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return null;
        } finally {
            semaphore.release();
        }
    }

    public synchronized int getRoadSize()
    {
        return size;
    }

    // Method to check if the road has space for a new vehicle
    public synchronized boolean hasSpace() 
    {
        return size < capacity;
    }

    // Method to check if the road is empty
    public synchronized boolean isEmpty() 
    {
        return size == 0;
    }

    public synchronized boolean isFull()
    {
        return size == capacity;
    }
}
