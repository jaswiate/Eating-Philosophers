package org.example;

public class Stats {
    public int size = 0;
    public Long startTime;
    public Integer totalTime = 0;
    public Integer maxTime = 0;
    public Integer averageTime = 0;

    public void startTiming() {
        startTime = System.currentTimeMillis();
    }
    public void endTiming() {
        Long endTime = System.currentTimeMillis();
        size += 1;
        int curTime = 1000 * Math.toIntExact(endTime - startTime);
        if (curTime > maxTime) { maxTime = curTime; }
        totalTime += curTime;
    }
    public void calculateAverageTime() {
        if (size != 0) {
            averageTime = totalTime / size;
        }
    }
    public void printTimes() {
        System.out.println("totalTime waiting: " + totalTime);
        System.out.println("maxTime waiting: " + maxTime);
        System.out.println("averageTime waiting: " + averageTime);
        System.out.println("###########################");
    }
}
