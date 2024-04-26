package org.example;

import java.util.ArrayList;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

public class V6 {
    public int n;
    public ArrayList<Philosopher6> pList = new ArrayList<>();
    public ArrayList<Fork6> fList = new ArrayList<>();

    public V6(int n) {
        this.n = n;
        for(int i = 0; i < n; i++) {
            fList.add(new Fork6(i));
        }
        for(int i = 0; i < n; i++) {
            Fork6 leftFork = fList.get(i);
            Fork6 rightFork = fList.get((i + 1) % n);
            pList.add(new Philosopher6(i, rightFork, leftFork, n));
        }
    }

    public void simulation() {
        for(Philosopher6 p : pList) {
            p.start();
        }
    }
    public void printSimulationStats() throws InterruptedException {
        for (Philosopher6 p : pList) {
            p.join();
            p.stats.calculateAverageTime();
            System.out.println("Stats for philosopher: " + p.i);
            p.stats.printTimes();
        }
    }
}

class Philosopher6 extends Thread {
    public int i;
    public Fork6 rightFork;
    public Fork6 leftFork;
    public Stats stats;
    public static Semaphore waiter;
    public Philosopher6(int i, Fork6 rF, Fork6 lF, int n) {
        this.i = i;
        this.rightFork = rF;
        this.leftFork = lF;
        this.stats = new Stats();
        waiter = new Semaphore(n - 1);
    }
    public void eat() {
        System.out.println("Philosopher " + i + " ate");
    }
    public void think() {
        System.out.println("Philosopher " + i + " is thinking");
    }

    @Override
    public void run() {
        for (int iter = 0; iter < 200; iter++) {
            think();
            stats.startTiming();
            try {
                if (!waiter.tryAcquire(0, TimeUnit.MILLISECONDS)) {
                    rightFork.take(i);
                    System.out.println("Philosopher " + i + " took the right fork");
                    leftFork.take(i);
                    System.out.println("Philosopher " + i + " took the left fork");
                    waiter.release();
                } else {
                    leftFork.take(i);
                    System.out.println("Philosopher " + i + " took the left fork");
                    rightFork.take(i);
                    System.out.println("Philosopher " + i + " took the right fork");
                    waiter.release();
                }

            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            stats.endTiming();
            eat();
            rightFork.put(i);
            leftFork.put(i);
            System.out.println("Forks put down");
        }
    }
}

class Fork6 {
    public int i;
    public boolean free = true;
    public int philosopherUsing;
    public Fork6(int i) {
        this.i = i;
    }
    public synchronized void take(int p) {
        while(!free) {
            try {
                wait();
            } catch (InterruptedException e)  {
                throw new RuntimeException(e);
            }
        }
        free = false;
        philosopherUsing = p;
    }
    public synchronized void put(int p) {
        if(!free && philosopherUsing == p) {
            free = true;
            notify();
        }
    }
}
