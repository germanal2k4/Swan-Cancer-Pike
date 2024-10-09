package ru.hse.homework;

/**
 * this class intended for wheelbarrow, which will be pushed by creatures
 */
public class Wheelbarrow {
    /**
     * this is constructor of wheelbarrow
     * x,y equals - by default
     */
    public Wheelbarrow() {
        this.x = 0.0;
        this.y = 0.0;
    }

    /**
     * this method set-ups camera
     */
    public void setUpCamera() {
        this.camera = new Camera(this);
    }

    /**
     * this method set-ups  motion sensor
     *
     * @param delay
     */
    public void setUpMotionSensor(int delay) {
        this.motionSensor = new MotionSensor(this, delay);
    }

    /**
     * this is getter for X coordinate
     *
     * @return
     */
    public synchronized double getX() {
        return x;
    }

    /**
     * this is getter for Y coordinate
     *
     * @return
     */
    public synchronized double getY() {
        return y;
    }

    /**
     * this method intended for creatures which push the wheelbarrow
     * also it calls method of motion sensor for notifying that coordinates are changed
     *
     * @param speed
     * @param angle
     */
    public synchronized void setUpCoordinates(double speed, double angle) {
        x += speed * Math.cos(angle);
        y += speed * Math.sin(angle);
        if (motionSensor != null) {
            motionSensor.coordinatesChange();
        }
    }

    private double x, y;
    public Camera camera;
    public MotionSensor motionSensor;
}
