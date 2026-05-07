package simulador.algoritmos;

import simulador.model.SimulationResult;

public interface PageReplacementAlgorithm {
    SimulationResult simulate(int[] pages, int frameCount);
    String getName();
}
