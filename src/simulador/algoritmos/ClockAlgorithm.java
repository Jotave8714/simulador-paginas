package simulador.algoritmos;

import simulador.model.SimulationResult;
import simulador.model.StepState;
import java.util.*;

// Second-chance (Clock) algorithm: circular hand sweeps refBits before evicting
public class ClockAlgorithm implements PageReplacementAlgorithm {

    @Override
    public String getName() { return "Relógio"; }

    @Override
    public SimulationResult simulate(int[] pages, int frameCount) {
        int[] frames = new int[frameCount];
        boolean[] refBit = new boolean[frameCount];
        Arrays.fill(frames, -1);
        int hand = 0;
        int loaded = 0;
        List<StepState> steps = new ArrayList<>();
        int faults = 0;

        for (int page : pages) {
            int foundSlot = -1;
            for (int i = 0; i < frameCount; i++) {
                if (frames[i] == page) { foundSlot = i; break; }
            }

            if (foundSlot != -1) {
                refBit[foundSlot] = true;
                steps.add(new StepState(page, frames, false));
            } else {
                faults++;
                if (loaded < frameCount) {
                    for (int i = 0; i < frameCount; i++) {
                        if (frames[i] == -1) {
                            frames[i] = page;
                            refBit[i] = true;
                            loaded++;
                            break;
                        }
                    }
                } else {
                    while (refBit[hand]) {
                        refBit[hand] = false;
                        hand = (hand + 1) % frameCount;
                    }
                    frames[hand] = page;
                    refBit[hand] = true;
                    hand = (hand + 1) % frameCount;
                }
                steps.add(new StepState(page, frames, true));
            }
        }

        return new SimulationResult(getName(), faults, steps);
    }
}
