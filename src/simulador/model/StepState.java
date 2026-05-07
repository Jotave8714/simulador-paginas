package simulador.model;

public class StepState {
    public final int page;
    public final int[] frames;
    public final boolean pageFault;

    public StepState(int page, int[] frames, boolean pageFault) {
        this.page = page;
        this.frames = frames.clone();
        this.pageFault = pageFault;
    }
}
