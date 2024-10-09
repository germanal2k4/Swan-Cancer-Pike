package ru.hse.homework;

import java.io.PrintStream;

/**
 * this class is intended to capture output of main
 */
public class Application {
    /**
     * this method captures output
     *
     * @param output
     */
    public Application(PrintStream output) {
        this.output = output;
    }

    /**
     * empty run method
     *
     * @param args
     */
    public void run(String[] args) {

    }

    private final PrintStream output;
}

