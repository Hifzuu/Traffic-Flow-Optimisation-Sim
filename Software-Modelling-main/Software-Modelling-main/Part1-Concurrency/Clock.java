public class Clock extends Thread {
    private int currentTime;
    private static final int SIMULATED_TIME_PER_TICK = 10;
    private static final int REAL_TIME_PER_TICK = 1000;
    private static final int TOTAL_SIMULATED_TIME = 360;

    public Clock() {
        this.currentTime = 0;
    }

    @Override
    public void run() {
        // int totalTicks = TOTAL_SIMULATED_TIME * 1000 / REAL_TIME_PER_TICK;
        int totalTicks = TOTAL_SIMULATED_TIME;

        for (int tick = 0; tick < totalTicks && isRunning(); tick++) {
            try {
                sleep(REAL_TIME_PER_TICK);
                tick();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                e.printStackTrace();  
            }
        }
        
    }

    public synchronized int getElapsedTimeInMinutes() {
        return currentTime / 6;
    }

    public synchronized int getElapsedTimeInSeconds()
    {
        return (currentTime % 6)*10;
    }

    public synchronized boolean isRunning() {
        return currentTime < TOTAL_SIMULATED_TIME;
    }

    private synchronized void tick() {
        currentTime++;
    }

    public int getTick() {
        return currentTime;
    }

    public synchronized int getCurrentTime() {
        return currentTime;
    }

    public static int convertToRealTime(int simulatedTime) {
        return simulatedTime * REAL_TIME_PER_TICK / SIMULATED_TIME_PER_TICK;
    }
}
