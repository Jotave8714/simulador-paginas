package simulador.algoritmos;

import simulador.model.SimulationResult;
import simulador.model.StepState;
import java.util.*;

public class LruAlgorithm implements PageReplacementAlgorithm {

    @Override
    public String getName() { return "LRU"; }

    @Override
    public SimulationResult simulate(int[] pages, int frameCount) {
        // access-order LinkedHashMap: first entry = LRU (least recently used)
        LinkedHashMap<Integer, Integer> pageToSlot = new LinkedHashMap<>(16, 0.75f, true);
        int[] frames = new int[frameCount];
        Arrays.fill(frames, -1);
        int nextSlot = 0;
        List<StepState> steps = new ArrayList<>();
        int faults = 0;

        for (int page : pages) {
            boolean fault = !pageToSlot.containsKey(page);
            if (fault) {
                faults++;
                if (pageToSlot.size() == frameCount) {
                    int evict = pageToSlot.keySet().iterator().next();
                    int slot = pageToSlot.remove(evict);
                    frames[slot] = page;
                    pageToSlot.put(page, slot);
                } else {
                    frames[nextSlot] = page;
                    pageToSlot.put(page, nextSlot++);
                }
            } else {
                pageToSlot.get(page); // updates access order
            }
            steps.add(new StepState(page, frames, fault));
        }

        return new SimulationResult(getName(), faults, steps);
    }
}
