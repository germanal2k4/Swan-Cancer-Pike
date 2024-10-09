package ru.hse.homework;

import java.util.Random;

/**
 * this class intended for creature which pushes wheelbarrow
 */
public class Creature extends Thread {
    /**
     * this is constructor of this class
     * it receives instance of wheelbarrow creature push and angle
     *
     * @param wheelbarrow
     * @param angle
     */
    public Creature(Wheelbarrow wheelbarrow, double angle) {
        this.wheelbarrow = wheelbarrow;
        this.angle = angle;
        this.startPosition = new Random().nextDouble(1, 10);
    }

    /**
     * this is run of creature
     * it looks if anti-theft system elements are ready to print if it's true creature wait
     * else it pushes the wheelbarrow and sleep afterward
     */
    @Override
    public void run() {
        while (!isInterrupted()) {
            synchronized (wheelbarrow) {
                if (isInterrupted()) return;
                while ((wheelbarrow.motionSensor != null && wheelbarrow.motionSensor.flag) ||
                        (wheelbarrow.camera != null && wheelbarrow.camera.flag)) {
                    try {
                        wheelbarrow.wait();
                    } catch (InterruptedException e) {
                        return;
                    }
                }
                if (isInterrupted()) return;
                wheelbarrow.setUpCoordinates(startPosition,
                        (angle / 180.0) * Math.PI);
            }
            try {
                Random random = new Random();
                Thread.sleep(random.nextInt(1000, 5000));
            } catch (InterruptedException i) {
                return;
            }
        }
    }

    private final Wheelbarrow wheelbarrow;
    private final double angle;
    private final double startPosition;
}
