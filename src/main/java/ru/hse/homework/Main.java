package ru.hse.homework;


public class Main {
    /**
     * This method parses input arguments and setup threads
     * after this sleep for 25 seconds and them interrupt them and prints the answer
     *
     * @param args
     * @throws InterruptedException
     */
    public static void main(String[] args) throws InterruptedException {
        SetUpper setUpper;
        new Application(System.out).run(args);
        int enabledElements;
        int motionSensorDelay = 0;
        try {
            if (args.length > 2) {
                throw new NumberFormatException();
            }
            enabledElements = Integer.parseInt(args[0]);
            if (enabledElements < 0 || enabledElements > 3) {
                throw new NumberFormatException();
            }
            if (((enabledElements == 2 || enabledElements == 3) && args.length == 1) || ((enabledElements == 1 || enabledElements == 0) && args.length == 2)) {
                throw new NumberFormatException();
            }
            if ((enabledElements == 2 || enabledElements == 3) && (Integer.parseInt(args[1]) <= 0 || Integer.parseInt(args[1]) > 25)) {
                throw new NumberFormatException();
            }
            if (enabledElements == 2 || enabledElements == 3) {
                motionSensorDelay = Integer.parseInt(args[1]);
            }
            setUpper = new SetUpper(Security.values()[enabledElements], motionSensorDelay);
        } catch (ArrayIndexOutOfBoundsException | NumberFormatException e) {
            System.out.println("""
                    Launch program with at least 1 argument
                    if you don't want to enable any elements of anti-theft system launch program with 1 argument which is 0
                    if you want to set up motion sensor launch program with motion sensor first argument should be 2 and second should be delay between 0 and 25
                    if you want to set up camera first argument should be 1 and it should be the only argument
                    if you want to set up both first argument should be 3 and second should be delay between 0 and 25""");
            return;
        }

        setUpper.setUpThreads();
        Thread.sleep(25000);

        setUpper.interruptThreads();

        setUpper.joinThreads();
        System.out.println("X coordinate is " + setUpper.getWheelbarrowCoordinateX() + ", Y coordinate is " + setUpper.getWheelbarrowCoordinateY());
    }
}