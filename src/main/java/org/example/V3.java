package org.example;

import java.util.ArrayList;

public class V3 {
    public int n;
    public ArrayList<Philosopher3> pList = new ArrayList<>();
    public ArrayList<Fork3> fList = new ArrayList<>();

    public V3(int n) {
        this.n = n;
        for(int i = 0; i < n; i++) {
            fList.add(new Fork3(i));
        }
        for(int i = 0; i < n; i++) {
            Fork3 leftFork = fList.get(i);
            Fork3 rightFork = fList.get((i + 1) % n);
            pList.add(new Philosopher3(i, rightFork, leftFork));
        }
    }

    public void simulation() {
        for(Philosopher3 p : pList) {
            p.start();
        }
    }
    public void printSimulationStats() throws InterruptedException {
        for (Philosopher3 p : pList) {
            p.join();
            p.stats.calculateAverageTime();
            System.out.println("Stats for philosopher: " + p.i);
            p.stats.printTimes();
        }
    }
}

class Philosopher3 extends Thread {
    public int i;
    public Fork3 rightFork;
    public Fork3 leftFork;
    public Stats stats;
    public Philosopher3(int i, Fork3 rF, Fork3 lF) {
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
            if (i % 2 == 1) {
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

class Fork3 {
    public int i;
    public boolean free = true;
    public int philosopherUsing;
    public Fork3(int i) {
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