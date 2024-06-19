public class Vehicle {
    private String destination;
    private long entryTime;
    private long parkingTime;

    // Constructor
    public Vehicle(String destination, long entryTime) {
        this.destination = destination;
        this.entryTime = entryTime;
    }

    // Method to get the destination
    public String getDestination() {
        return destination;
    }

    // Method to set parking time
    public void setParkingTime(long parkingTime) {
        this.parkingTime = parkingTime;
    }

    // Method to simulate the time taken for the journey
    public long calculateJourneyTime() {
        return parkingTime - entryTime;
    }
}
