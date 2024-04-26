package org.example;

import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;

public class V4 {
    public int n;
    public ArrayList<Philosopher4> pList = new ArrayList<>();
    public ArrayList<Fork4> fList = new ArrayList<>();

    public V4(int n) {
        this.n = n;
        for(int i = 0; i < n; i++) {
            fList.add(new Fork4(i));
        }
        for(int i = 0; i < n; i++) {
            Fork4 leftFork = fList.get(i);
            Fork4 rightFork = fList.get((i + 1) % n);
            pList.add(new Philosopher4(i, rightFork, leftFork));
        }
    }

    public void simulation() {
        for(Philosopher4 p : pList) {
            p.start();
        }
    }
    public void printSimulationStats() throws InterruptedException {
        for (Philosopher4 p : pList) {
            p.join();
            p.stats.calculateAverageTime();
            System.out.println("Stats for philosopher: " + p.i);
            p.stats.printTimes();
        }
    }
}

class Philosopher4 extends Thread {
    public int i;
    public Fork4 rightFork;
    public Fork4 leftFork;
    public Stats stats;
    public Philosopher4(int i, Fork4 rF, Fork4 lF) {
        this.i = i;
        this.rightFork = rF;
        this.leftFork = lF;
        this.stats = new Stats();
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
            if (ThreadLocalRandom.current().nextBoolean()) {
                rightFork.take(i);
                System.out.println("Philosopher " + i + " took the right fork");
                leftFork.take(i);
                System.out.println("Philosopher " + i + " took the left fork");
            } else {
                leftFork.take(i);
                System.out.println("Philosopher " + i + " took the left fork");
                rightFork.take(i);
                System.out.println("Philosopher " + i + " took the right fork");
            }
            stats.endTiming();
            eat();
            rightFork.put(i);
            leftFork.put(i);
            System.out.println("Forks put down");
        }
    }
}

class Fork4 {
    public int i;
    public boolean free = true;
    public int philosopherUsing;
    public Fork4(int i) {
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
