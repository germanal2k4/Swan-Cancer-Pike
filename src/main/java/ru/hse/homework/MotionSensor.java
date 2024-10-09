package ru.hse.homework;

/**
 * this class is for Motion sensor for wheelbarrow
 */
public class MotionSensor extends Thread {
    /**
     * this is constructor of motion sensor
     * it receives instance of wheelbarrow and delay of sensor
     *
     * @param wheelbarrow
     * @param sensorDelay
     */
    public MotionSensor(Wheelbarrow wheelbarrow, int sensorDelay) {
        this.wheelbarrow = wheelbarrow;
        this.sensorDelay = sensorDelay;
    }

    /**
     * this method increments count when coordinates of wheelbarrow is changed
     * when it's reach delay of sensor is notifies all objects
     */
    public void coordinatesChange() {
        count++;
        if (count % sensorDelay == 0 && count != 0) {
            flag = true;
            wheelbarrow.notifyAll();
        }

    }

    /**
     * this method intended for tests to see how much times creatures pushed the wheelbarrow
     *
     * @return
     */
    public int getCount() {
        return count;
    }

    /**
     * motion sensor waits while it will be notified afterward it prints coordinates of wheelbarrow
     * in current moment
     * it also has a flag to notify creatures that it is ready to print
     */
    @Override
    public void run() {
        synchronized (wheelbarrow) {
            while (!isInterrupted()) {
                try {
                    wheelbarrow.wait();
                } catch (InterruptedException e) {
                    break;
                }
                if (flag) {
                    System.out.printf("Motion sensor message x coordinate is %.2f , y coordinate is %.2f\n", wheelbarrow.getX(), wheelbarrow.getY());
                    wheelbarrow.notifyAll();
                    flag = false;
                }
            }
            synchronized (wheelbarrow) {
                if (flag) {
                    System.out.printf("Motion sensor message x coordinate is %.2f , y coordinate is %.2f\n", wheelbarrow.getX(), wheelbarrow.getY());
                    wheelbarrow.notifyAll();
                    flag = false;
                }
            }
        }
    }

    private final Wheelbarrow wheelbarrow;
    public final int sensorDelay;
    private int count = 0;
    public boolean flag = false;
}
