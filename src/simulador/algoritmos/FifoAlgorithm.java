package simulador.algoritmos;

import simulador.model.SimulationResult;
import simulador.model.StepState;
import java.util.*;

public class FifoAlgorithm implements PageReplacementAlgorithm {

    @Override
    public String getName() { return "FIFO"; }

    @Override
    public SimulationResult simulate(int[] pages, int frameCount) {
        int[] frames = new int[frameCount];
        Arrays.fill(frames, -1);
        ArrayDeque<Integer> insertionOrder = new ArrayDeque<>();
        Set<Integer> inMemory = new HashSet<>();
        List<StepState> steps = new ArrayList<>();
        int faults = 0;

        for (int page : pages) {
            boolean fault = !inMemory.contains(page);
            if (fault) {
                faults++;
                if (inMemory.size() == frameCount) {
                    int evict = insertionOrder.poll();
                    inMemory.remove(evict);
                    for (int i = 0; i < frameCount; i++) {
                        if (frames[i] == evict) { frames[i] = page; break; }
                    }
                } else {
                    for (int i = 0; i < frameCount; i++) {
                        if (frames[i] == -1) { frames[i] = page; break; }
                    }
                }
                insertionOrder.add(page);
                inMemory.add(page);
            }
            steps.add(new StepState(page, frames, fault));
        }

        return new SimulationResult(getName(), faults, steps);
    }
}
