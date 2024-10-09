package ru.hse.homework;

/**
 * this class is intended for camera
 */
public class Camera extends Thread {
    /**
     * this is constructor of this class
     * it receives instance of wheelbarrow
     *
     * @param wheelbarrow
     */
    public Camera(Wheelbarrow wheelbarrow) {
        this.wheelbarrow = wheelbarrow;
    }

    /**
     * this is run of this class
     * it sleeps for 2 seconds and prints coordinates of wheelbarrow
     * it also has a flag to notify creatures that it is ready to print
     */
    @Override
    public void run() {
        while (!isInterrupted()) {
            try {
                Thread.sleep(2000);
                flag = true;
            } catch (InterruptedException i) {
                break;
            }
            synchronized (wheelbarrow) {
                if (flag) {
                    System.out.printf("Camera message x coordinate is %.2f , y coordinate is %.2f\n", wheelbarrow.getX(), wheelbarrow.getY());
                    flag = false;
                    wheelbarrow.notifyAll();
                }
            }
        }
        synchronized (wheelbarrow) {
            if (flag) {
                System.out.printf("Camera message x coordinate is %.2f , y coordinate is %.2f\n", wheelbarrow.getX(), wheelbarrow.getY());
                flag = false;
                wheelbarrow.notifyAll();
            }
        }
    }

    private final Wheelbarrow wheelbarrow;
    public boolean flag = false;
}
