package ru.hse.homework;

/**
 * this class intended for correct comparison of double numbers
 */
public class Util {
    /**
     * this method checks if numbers are equal with eps 0.01
     *
     * @param a
     * @param b
     * @return a == b
     */
    public static boolean doubleEquals(double a, double b) {
        return (b - eps < a) && (a < b + eps);
    }

    static final double eps = 0.01;
}
