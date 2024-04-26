package org.example;

import java.util.ArrayList;
import java.util.concurrent.locks.ReentrantLock;

public class V2 {
    public int n;
    public ArrayList<Philosopher2> pList = new ArrayList<>();
    public ArrayList<Fork2> fList = new ArrayList<>();

    public V2(int n) {
        this.n = n;
        for (int i = 0; i < n; i++) {
            fList.add(new Fork2(i));
        }
        for (int i = 0; i < n; i++) {
            Fork2 leftFork = fList.get(i);
            Fork2 rightFork = fList.get((i + 1) % n);
            pList.add(new Philosopher2(i, rightFork, leftFork));
        }
    }

    public void simulation() {
        for (Philosopher2 p : pList) {
            p.start();
        }
    }
    public void printSimulationStats() throws InterruptedException {
        for (Philosopher2 p : pList) {
            p.join();
            p.stats.calculateAverageTime();
            System.out.println("Stats for philosopher: " + p.i);
            p.stats.printTimes();
        }
    }
}

class Philosopher2 extends Thread {
    public int i;
    public Fork2 rightFork;
    public Fork2 leftFork;
    public Stats stats;
    public Philosopher2(int i, Fork2 rF, Fork2 lF) {
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
            stats.startTiming();
            think();
            while (true) {
                if (!leftFork.tryLock()) {
                    continue;
                }
                if (!rightFork.tryLock()) {
                    leftFork.unlock();
                    continue;
                }
                break;
            }
            System.out.println("Philosopher " + i + " took both forks");
            stats.endTiming();
            eat();
            leftFork.unlock();
            rightFork.unlock();
            System.out.println("Forks put down");
        }
    }
}

class Fork2 extends ReentrantLock {
    public int i;
    public Fork2(int i) {
        this.i = i;
    }
}
