/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package javaapplication5;
import java.util.Scanner;


/**
 *
 * @author admin
 */
public class JavaApplication5 {

    /**
     * @param args the command line arguments
     */
    public static volatile boolean running = true;
    
    public static void main(String[] args) throws InterruptedException {
        
    
   int threadCount = 4;
    long timeoutMs = 5_000;
    final AtomicLong counter = new AtomicLong(0);
    PiThread[] threads = new PiThread[threadCount];
    for (int i = 0; i < threadCount; i++) {
        threads[i] = new PiThread(counter);
        threads[i].start();
    }

    Thread.sleep(timeoutMs);
    running = false;

    for (int i = 0; i < threadCount; i++) {
        threads[i].join();
    }

    double sum = 0;
    for (int i = 0; i < threadCount; i++) {
        sum += threads[i].getSum();
    }
    System.out.print("counter = " + counter.get());
    System.out.print("PI = " + 4*sum);

}

static class PiThread extends Thread {

    private AtomicLong counter;
    private double sum  = 0;

    public PiThread(AtomicLong counter) {
        this.counter = counter;
    }


    @Override
    public void run() {
        long i;
        while (running && isValidCounter(i = counter.getAndAdd(1))) {
            sum += Math.pow(-1, i) / (2 * i + 1);
        }
    }

    private boolean isValidCounter(long value) {
        return value >= 0 && value < Long.MAX_VALUE;
    }

    public double getSum() {
        return sum;
    }
    
}
}