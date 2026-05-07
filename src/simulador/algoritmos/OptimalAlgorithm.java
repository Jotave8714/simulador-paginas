package simulador.algoritmos;

import simulador.model.SimulationResult;
import simulador.model.StepState;
import java.util.*;

// Optimal (Bélády): evicts page whose next use is farthest in the future
public class OptimalAlgorithm implements PageReplacementAlgorithm {

    @Override
    public String getName() { return "Ótimo"; }

    @Override
    public SimulationResult simulate(int[] pages, int frameCount) {
        int[] frames = new int[frameCount];
        Arrays.fill(frames, -1);
        int loaded = 0;
        List<StepState> steps = new ArrayList<>();
        int faults = 0;

        for (int i = 0; i < pages.length; i++) {
            int page = pages[i];
            boolean inMemory = false;
            for (int f : frames) {
                if (f == page) { inMemory = true; break; }
            }

            if (!inMemory) {
                faults++;
                if (loaded < frameCount) {
                    for (int j = 0; j < frameCount; j++) {
                        if (frames[j] == -1) { frames[j] = page; loaded++; break; }
                    }
                } else {
                    frames[findVictim(frames, pages, i + 1)] = page;
                }
            }
            steps.add(new StepState(page, frames, !inMemory));
        }

        return new SimulationResult(getName(), faults, steps);
    }

    private int findVictim(int[] frames, int[] pages, int from) {
        int evictSlot = 0;
        int farthest = -1;
        for (int j = 0; j < frames.length; j++) {
            int nextUse = Integer.MAX_VALUE;
            for (int k = from; k < pages.length; k++) {
                if (pages[k] == frames[j]) { nextUse = k; break; }
            }
            if (nextUse > farthest) { farthest = nextUse; evictSlot = j; }
        }
        return evictSlot;
    }
}
