package org.example;

import java.util.ArrayList;

public class V1 {
    public int n;
    public ArrayList<Philosopher1> pList = new ArrayList<>();
    public ArrayList<Fork1> fList = new ArrayList<>();

    public V1(int n) {
        this.n = n;
        for(int i = 0; i < n; i++) {
            fList.add(new Fork1(i));
        }
        for(int i = 0; i < n; i++) {
            Fork1 leftFork = fList.get(i);
            Fork1 rightFork = fList.get((i + 1) % n);
            pList.add(new Philosopher1(i, rightFork, leftFork));
        }
    }
    public void simulation() {
        for(Philosopher1 p : pList) {
            p.start();
        }
    }
    public void printSimulationStats() throws InterruptedException {
        for (Philosopher1 p : pList) {
            p.join();
            p.stats.calculateAverageTime();
            System.out.println("Stats for philosopher: " + p.i);
            p.stats.printTimes();
        }
    }
}

class Philosopher1 extends Thread {
    public int i;
    public Fork1 rightFork;
    public Fork1 leftFork;
    public Stats stats;
    public Philosopher1(int i, Fork1 rF, Fork1 lF) {
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
        think();
        leftFork.take(i);
        System.out.println("Philosopher " + i + " took the left fork");
        rightFork.take(i);
        System.out.println("Philosopher " + i + " took the right fork");
        eat();
        rightFork.put(i);
        leftFork.put(i);
        System.out.println("Forks put down");
    }
}

class Fork1 {
    public int i;
    public boolean free = true;
    public int philosopherUsing;
    public Fork1(int i) {
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