package ru.hse.homework;

import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;


import java.awt.geom.Point2D;
import java.io.*;
import java.util.ArrayList;
import java.util.Random;
import java.util.stream.Stream;

/**
 * this class tests all the method and classes in project
 */
public class programTest {
    /**
     * arguments we cannot use to execute program
     *
     * @return
     */
    public static Stream<Arguments> testData() {
        return Stream.of(
                Arguments.of((Object) new String[]{"1", "3"}),
                Arguments.of((Object) new String[]{"2"}),
                Arguments.of((Object) new String[]{"3"}),
                Arguments.of((Object) new String[]{"3", "26"}),
                Arguments.of((Object) new String[]{"2", "0"}),
                Arguments.of((Object) new String[]{"0", "-1"}),
                Arguments.of((Object) new String[]{"fsaf"}),
                Arguments.of((Object) new String[]{"-1", "2"}),
                Arguments.of((Object) new String[]{"1", "2", "3"})
        );
    }

    /**
     * this test check that we cannot execute program with incorrect argument
     *
     * @param input
     * @throws InterruptedException
     */
    @ParameterizedTest
    @MethodSource("testData")
    void testMainWrongParams(String[] input) throws InterruptedException {
        final ByteArrayOutputStream outputStreamCaptor = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStreamCaptor));
        Main.main(input);
        Assertions.assertEquals(outputStreamCaptor.toString()
                .trim(), """
                Launch program with at least 1 argument
                if you don't want to enable any elements of anti-theft system launch program with 1 argument which is 0
                if you want to set up motion sensor launch program with motion sensor first argument should be 2 and second should be delay between 0 and 25
                if you want to set up camera first argument should be 1 and it should be the only argument
                if you want to set up both first argument should be 3 and second should be delay between 0 and 25""");
    }

    /**
     * this test checks that SetUpper set-ups wheelbarrow in appropriate place
     */
    @Test
    void testSetUpper() {
        SetUpper setUpper = new SetUpper(Security.ALL, 3);
        Assertions.assertEquals(new Point2D.Double(0.0, 0.0), new Point2D.Double(setUpper.getWheelbarrowCoordinateX(), setUpper.getWheelbarrowCoordinateY()));
    }

    /*
    Общее замечание, тестирование через файл добавлено, чтобы вы мой дорогой проверяющий могли убедиться в правильности вывода своими глазами
     */

    /**
     * this test checks is motion sensor works properly with coordinates
     *
     * @throws IOException
     * @throws InterruptedException
     */
    @Test
    void testMotionSensorSimple() throws IOException, InterruptedException {
        File file = new File("panirovka.txt");

        try (PrintStream outputStream = new PrintStream(file);
             BufferedReader inputFile = new BufferedReader(new FileReader("panirovka.txt"))) {
            System.setOut(outputStream);
            Wheelbarrow wheelbarrow = new Wheelbarrow();
            wheelbarrow.setUpMotionSensor(1);
            wheelbarrow.motionSensor.start();
            Thread.sleep(1000);
            wheelbarrow.setUpCoordinates(1, (90.0 / 180.0) * Math.PI);
            Thread.sleep(4000);
            wheelbarrow.motionSensor.interrupt();
            wheelbarrow.motionSensor.join();
            String line = inputFile.readLine();
            outputStream.flush();

            Assertions.assertEquals("Motion sensor message x coordinate is 0,00 , y coordinate is 1,00", line);
        }
    }

    /**
     * this test check that camera prints right coordinates in right time
     *
     * @throws IOException
     * @throws InterruptedException
     */
    @Test
    void testCameraSimple() throws IOException, InterruptedException {
        File file = new File("panirovka.txt");
        try (PrintStream outputStream = new PrintStream(file);
             BufferedReader inputFile = new BufferedReader(new FileReader("panirovka.txt"))) {

            System.setOut(outputStream);

            Wheelbarrow wheelbarrow = new Wheelbarrow();
            wheelbarrow.setUpCamera();
            wheelbarrow.camera.start();
            wheelbarrow.setUpCoordinates(1, (90.0 / 180.0) * Math.PI);
            Thread.sleep(2050);
            wheelbarrow.camera.interrupt();
            wheelbarrow.camera.join();
            String line = inputFile.readLine();
            outputStream.flush();

            Assertions.assertEquals("Camera message x coordinate is 0,00 , y coordinate is 1,00", line);
        }
    }

    /**
     * this test checks that camera works correctly on out task
     *
     * @throws IOException
     * @throws InterruptedException
     */
    @RepeatedTest(5)
    void testDefaultCamera() throws IOException, InterruptedException {
        File outputFile = new File("panirovka.txt");
        try (PrintStream out = new PrintStream(outputFile);
             BufferedReader inputFile = new BufferedReader(new FileReader("panirovka.txt"))) {
            System.setOut(out);
            SetUpper setUpper = new SetUpper(Security.values()[1], 0);
            Random random = new Random();
            int seconds = random.nextInt(5, 10); // граница 10, чтобы вы мой дорогой проверящий долго не ждали
            setUpper.setUpThreads();
            Thread.sleep((seconds * 2 + 1) * 1000); // 1 секунда это погрешность пока проснется камера и перейдет из состояния готовности в исполнение, можно технически поставить и меньше
            setUpper.interruptThreads();
            setUpper.joinThreads();
            int count = 0;
            while (inputFile.readLine() != null) {
                count++;
            }
            Assertions.assertEquals(seconds, count);
        }
    }

    /**
     * this test checks that camera works correctly on 100 creatures
     * pushing wheelbarrow
     *
     * @throws IOException
     * @throws InterruptedException
     */
    @RepeatedTest(5)
    void testMultipleCreaturesCamera() throws IOException, InterruptedException {
        File outputFile = new File("panirovka.txt");
        try (PrintStream out = new PrintStream(outputFile);
             BufferedReader inputFile = new BufferedReader(new FileReader("panirovka.txt"))) {
            System.setOut(out);
            Random random = new Random();
            int seconds = random.nextInt(5, 10);
            Wheelbarrow wheelbarrow = new Wheelbarrow();
            wheelbarrow.setUpCamera();
            ArrayList<Creature> creatures = new ArrayList<>();
            for (int i = 0; i < 100; i++) {
                creatures.add(new Creature(wheelbarrow, 60));
            }
            wheelbarrow.camera.start();
            for (var it : creatures) {
                it.start();
            }
            Thread.sleep(1000 * (2 * seconds + 1));
            for (var it : creatures) {
                it.interrupt();
            }
            wheelbarrow.camera.interrupt();
            for (var it : creatures) {
                it.join();
            }
            wheelbarrow.camera.join();
            int count = 0;
            while (inputFile.readLine() != null) {
                count++;
            }
            Assertions.assertEquals(seconds, count);
        }
    }

    /**
     * this test checks correct work of both elements of anti-theft system with 100 creatures
     *
     * @throws IOException
     * @throws InterruptedException
     */
    @RepeatedTest(2)
    void testMultipleCreaturesAntiTheftSystem() throws IOException, InterruptedException {
        File outputFile = new File("panirovka.txt");
        try (PrintStream out = new PrintStream(outputFile);
             BufferedReader inputFile = new BufferedReader(new FileReader("panirovka.txt"))) {
            System.setOut(out);
            Random random = new Random();
            int seconds = random.nextInt(5, 10);
            Wheelbarrow wheelbarrow = new Wheelbarrow();
            wheelbarrow.setUpCamera();
            wheelbarrow.setUpMotionSensor(1);
            ArrayList<Creature> creatures = new ArrayList<>();
            for (int i = 0; i < 100; i++) {
                creatures.add(new Creature(wheelbarrow, 60));
            }
            wheelbarrow.motionSensor.start();
            wheelbarrow.camera.start();
            for (var it : creatures) {
                it.start();
            }
            Thread.sleep(1000 * (2 * seconds + 1));
            for (var it : creatures) {
                it.interrupt();
            }
            wheelbarrow.camera.interrupt();
            wheelbarrow.motionSensor.interrupt();
            for (var it : creatures) {
                it.join();
            }
            wheelbarrow.camera.join();
            wheelbarrow.motionSensor.join();
            int count = 0;
            while (inputFile.readLine() != null) {
                count++;
            }
            Assertions.assertEquals(seconds + wheelbarrow.motionSensor.getCount(), count);
        }
    }

    /**
     * this test checks that Motion sensor works correctly on 100 creatures
     * pushing wheelbarrow
     *
     * @throws IOException
     * @throws InterruptedException
     */
    @RepeatedTest(5)
    void testMultipleCreaturesMotionSensor() throws IOException, InterruptedException {
        File outputFile = new File("panirovka.txt");
        try (PrintStream out = new PrintStream(outputFile);
             BufferedReader inputFile = new BufferedReader(new FileReader("panirovka.txt"))) {
            System.setOut(out);
            Random random = new Random();
            Wheelbarrow wheelbarrow = new Wheelbarrow();
            wheelbarrow.setUpMotionSensor(1);
            ArrayList<Creature> creatures = new ArrayList<>();
            for (int i = 0; i < 100; i++) {
                creatures.add(new Creature(wheelbarrow, 60));
            }
            wheelbarrow.motionSensor.start();
            for (var it : creatures) {
                it.start();
            }
            Thread.sleep(1000 * (random.nextInt(5, 10)));
            for (var it : creatures) {
                it.interrupt();
            }
            wheelbarrow.motionSensor.interrupt();
            for (var it : creatures) {
                it.join();
            }
            wheelbarrow.motionSensor.join();
            int count = 0;
            while (inputFile.readLine() != null) {
                count++;
            }
            Assertions.assertEquals(wheelbarrow.motionSensor.getCount(), count);
        }
    }

    /**
     * this test checks right work with doubles
     */
    @Test
    public void testDoubleEquals() {
        Assertions.assertTrue(Util.doubleEquals(10.0, 10.0));
        Assertions.assertTrue(Util.doubleEquals(-5.0, -5.0));
        Assertions.assertTrue(Util.doubleEquals(0.0, 0.0));
        Assertions.assertTrue(Util.doubleEquals(1.01, 1.01));
        Assertions.assertTrue(Util.doubleEquals(-3.0, -3.001));
        Assertions.assertTrue(Util.doubleEquals(0.49, 0.49));

        Assertions.assertFalse(Util.doubleEquals(1.0, 1.1));
        Assertions.assertFalse(Util.doubleEquals(-3.0, -2.9));
        Assertions.assertFalse(Util.doubleEquals(0.5, 0.51));

        Assertions.assertTrue(Util.doubleEquals(0.0, 0.00001));
        Assertions.assertTrue(Util.doubleEquals(0.0, -0.00001));
        Assertions.assertFalse(Util.doubleEquals(0.0, 0.01));
        Assertions.assertFalse(Util.doubleEquals(0.0, -0.01));
    }
}
