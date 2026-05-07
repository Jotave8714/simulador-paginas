package simulador.model;

import java.util.List;

public class SimulationResult {
    private final String algorithmName;
    private final int pageFaultCount;
    private final List<StepState> steps;

    public SimulationResult(String algorithmName, int pageFaultCount, List<StepState> steps) {
        this.algorithmName = algorithmName;
        this.pageFaultCount = pageFaultCount;
        this.steps = steps;
    }

    public String getAlgorithmName() { return algorithmName; }
    public int getPageFaultCount()   { return pageFaultCount; }
    public List<StepState> getSteps() { return steps; }
}
