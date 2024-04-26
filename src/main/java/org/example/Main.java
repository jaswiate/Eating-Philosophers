package org.example;

import com.github.sh0nk.matplotlib4j.Plot;
import com.github.sh0nk.matplotlib4j.PythonExecutionException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class Main {
    public static void main(String[] args) throws InterruptedException, PythonExecutionException, IOException {

        int[] nOfPhilosophers = {2, 5, 8, 15};

        for (int n : nOfPhilosophers) {
            List<Integer> averageTimes = new ArrayList<>();

            for (int x = 0; x < n; x++) {
                averageTimes.add(0);
            }

            for (int x = 0; x < 30; x++) {
//                V1 naiveVariant = new V1(n);
//                naiveVariant.simulation();
//                 naiveVariant.printSimulationStats();

//                V2 bothForksVariant = new V2(n);
//                bothForksVariant.simulation();
////                bothForksVariant.printSimulationStats();
//                for (Philosopher2 p : bothForksVariant.pList) {
//                    p.join();
//                    p.stats.calculateAverageTime();
//                    averageTimes.set(p.i, averageTimes.get(p.i) + p.stats.averageTime);
//                }

//                V3 asymmetricVariant = new V3(n);
//                asymmetricVariant.simulation();
////                asymmetricVariant.printSimulationStats();
//                for (Philosopher3 p : asymmetricVariant.pList) {
//                    p.join();
//                    p.stats.calculateAverageTime();
//                    averageTimes.set(p.i, averageTimes.get(p.i) + p.stats.averageTime);
//                }

//                V4 coinThrowVariant = new V4(n);
//                coinThrowVariant.simulation();
//                coinThrowVariant.printSimulationStats();

//                V5 waiterVariant = new V5(n);
//                waiterVariant.simulation();
////                waiterVariant.printSimulationStats();
//                for (Philosopher5 p : waiterVariant.pList) {
//                    p.join();
//                    p.stats.calculateAverageTime();
//                    averageTimes.set(p.i, averageTimes.get(p.i) + p.stats.averageTime);
//                }

//                JADALNIA SIE ZAKLESZCZA NIE WIEM CZEMU
//                V6 diningRoomVariant = new V6(5);
//                diningRoomVariant.simulation();
////                diningRoomVariant.printSimulationStats();
//                for (Philosopher6 p : diningRoomVariant.pList) {
//                    p.join();
//                    p.stats.calculateAverageTime();
//                    averageTimes.set(p.i, averageTimes.get(p.i) + p.stats.averageTime);
//                }
            }

            for (int x = 0; x < n; x++) {
                averageTimes.set(x, averageTimes.get(x) / 30);
            }

            Plot plot = Plot.create();
            plot.hist().add(averageTimes);
            plot.show();
        }

    }
}