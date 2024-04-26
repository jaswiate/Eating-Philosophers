package org.example;

import java.util.ArrayList;
import java.util.concurrent.Semaphore;

public class V5 {
    public int n;
    public ArrayList<Philosopher5> pList = new ArrayList<>();
    public ArrayList<Fork5> fList = new ArrayList<>();

    public V5(int n) {
        this.n = n;
        for(int i = 0; i < n; i++) {
            fList.add(new Fork5(i));
        }
        for(int i = 0; i < n; i++) {
            Fork5 leftFork = fList.get(i);
            Fork5 rightFork = fList.get((i + 1) % n);
            pList.add(new Philosopher5(i, rightFork, leftFork, n));
        }
    }

    public void simulation() {
        for(Philosopher5 p : pList) {
            p.start();
        }
    }
    public void printSimulationStats() throws InterruptedException {
        for (Philosopher5 p : pList) {
            p.join();
            p.stats.calculateAverageTime();
            System.out.println("Stats for philosopher: " + p.i);
            p.stats.printTimes();
        }
    }
}

class Philosopher5 extends Thread {
    public int i;
    public Fork5 rightFork;
    public Fork5 leftFork;
    public Stats stats;
    public static Semaphore waiter;
    public Philosopher5(int i, Fork5 rF, Fork5 lF, int n) {
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
            waiter.acquireUninterruptibly();
            leftFork.take(i);
            System.out.println("Philosopher " + i + " took the left fork");
            rightFork.take(i);
            System.out.println("Philosopher " + i + " took the right fork");
            waiter.release();
            stats.endTiming();
            eat();
            rightFork.put(i);
            leftFork.put(i);
            System.out.println("Forks put down");
        }
    }
}

class Fork5 {
    public int i;
    public boolean free = true;
    public int philosopherUsing;
    public Fork5(int i) {
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

