package ru.hse.homework;

/**
 * this class is intended to create all instances in this program
 * to set up and interrupt all threads in this project
 */
public class SetUpper {
    /**
     * this is constructor of this class
     * it receives mode of anti-theft system and delay of motion sensor
     *
     * @param antiTheftSystemMode
     * @param sensorDelay
     */
    public SetUpper(Security antiTheftSystemMode, int sensorDelay) {
        wheelbarrow = new Wheelbarrow();
        swan = new Creature(wheelbarrow, 60.0);
        cancer = new Creature(wheelbarrow, 180.0);
        pike = new Creature(wheelbarrow, 300.0);
        enables = antiTheftSystemMode;

        switch (antiTheftSystemMode) {
            case CAMERA -> wheelbarrow.setUpCamera();
            case MOTION_SENSOR -> wheelbarrow.setUpMotionSensor(sensorDelay);
            case ALL -> {
                wheelbarrow.setUpCamera();
                wheelbarrow.setUpMotionSensor(sensorDelay);
            }
        }
    }

    /**
     * this method set up all threads in this program
     */
    public void setUpThreads() {
        switch (enables) {
            case CAMERA -> wheelbarrow.camera.start();
            case MOTION_SENSOR -> wheelbarrow.motionSensor.start();
            case ALL -> {
                wheelbarrow.camera.start();
                wheelbarrow.motionSensor.start();
            }
        }

        swan.start();
        cancer.start();
        pike.start();
    }

    /**
     * this method interrupt all threads in this program
     */
    public void interruptThreads() {
        switch (enables) {
            case CAMERA -> wheelbarrow.camera.interrupt();
            case MOTION_SENSOR -> wheelbarrow.motionSensor.interrupt();
            case ALL -> {
                wheelbarrow.camera.interrupt();
                wheelbarrow.motionSensor.interrupt();
            }
        }

        swan.interrupt();
        cancer.interrupt();
        pike.interrupt();
    }

    /**
     * this method join all threads to do not let program to finish till the end of program
     */
    public void joinThreads() throws InterruptedException {
        switch (enables) {
            case CAMERA -> wheelbarrow.camera.join();
            case MOTION_SENSOR -> wheelbarrow.motionSensor.join();
            case ALL -> {
                wheelbarrow.camera.join();
                wheelbarrow.motionSensor.join();
            }
        }
        swan.join();
        cancer.join();
        pike.join();
    }
    /**
     * this method return end coordinate x of wheelbarrow
     *
     * @return
     */
    public double getWheelbarrowCoordinateX() {
        return wheelbarrow.getX();
    }

    /**
     * this method return end coordinate y of wheelbarrow
     *
     * @return
     */
    public double getWheelbarrowCoordinateY() {
        return wheelbarrow.getY();
    }

    private final Wheelbarrow wheelbarrow;
    private final Creature swan;
    private final Creature cancer;
    private final Creature pike;
    private final Security enables;
}
