package simulador.ui;

import simulador.algoritmos.*;
import simulador.model.SimulationResult;
import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class MainWindow extends JFrame {
    private final ResultsPanel resultsPanel;
    private final StepTablePanel stepTablePanel;
    private final JTabbedPane tabbedPane;

    public MainWindow() {
        super("Simulador de Substituição de Páginas");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(920, 620);
        setMinimumSize(new Dimension(720, 500));
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(0, 0));

        JLabel title = new JLabel("Simulador de Substituição de Páginas", SwingConstants.CENTER);
        title.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 16));
        title.setBorder(BorderFactory.createEmptyBorder(10, 0, 4, 0));
        title.setForeground(new Color(40, 40, 80));

        InputPanel inputPanel = new InputPanel(this::runSimulation);

        resultsPanel   = new ResultsPanel();
        stepTablePanel = new StepTablePanel();

        tabbedPane = new JTabbedPane();
        tabbedPane.addTab("Resumo & Gráfico", resultsPanel);
        tabbedPane.addTab("Passo a Passo", stepTablePanel);

        JPanel top = new JPanel(new BorderLayout());
        top.add(title, BorderLayout.NORTH);
        top.add(inputPanel, BorderLayout.SOUTH);

        add(top, BorderLayout.NORTH);
        add(tabbedPane, BorderLayout.CENTER);
    }

    private void runSimulation(int[] pages, int frameCount) {
        List<PageReplacementAlgorithm> algorithms = new ArrayList<>();
        algorithms.add(new FifoAlgorithm());
        algorithms.add(new LruAlgorithm());
        algorithms.add(new ClockAlgorithm());
        algorithms.add(new OptimalAlgorithm());

        List<SimulationResult> results = new ArrayList<>();
        for (PageReplacementAlgorithm alg : algorithms) {
            results.add(alg.simulate(pages, frameCount));
        }

        // console output per requirements
        System.out.println("\n--- Resultados (quadros=" + frameCount + ", paginas=" + pages.length + ") ---");
        for (SimulationResult r : results) {
            System.out.printf("%-12s - %d faltas de página%n", r.getAlgorithmName(), r.getPageFaultCount());
        }

        resultsPanel.update(results);
        stepTablePanel.update(results);
        tabbedPane.setSelectedIndex(0);
    }
}
