package simulador.ui;

import simulador.model.SimulationResult;
import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class ResultsPanel extends JPanel {
    private final DefaultTableModel tableModel;
    private final BarChartPanel chartPanel;

    public ResultsPanel() {
        setLayout(new BorderLayout(6, 6));
        setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));

        String[] cols = {"Algoritmo", "Faltas de Página", "Acertos", "Taxa de Acerto"};
        tableModel = new DefaultTableModel(cols, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };

        JTable table = new JTable(tableModel);
        table.setRowHeight(26);
        table.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 13));
        table.getTableHeader().setFont(new Font(Font.SANS_SERIF, Font.BOLD, 13));
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.setShowGrid(true);
        table.setGridColor(new Color(220, 220, 220));

        // center-align numeric columns
        DefaultTableCellRenderer center = new DefaultTableCellRenderer();
        center.setHorizontalAlignment(SwingConstants.CENTER);
        for (int i = 1; i < cols.length; i++) table.getColumnModel().getColumn(i).setCellRenderer(center);

        // color-code algorithm name column
        table.getColumnModel().getColumn(0).setCellRenderer(new AlgoNameRenderer());

        JScrollPane tableScroll = new JScrollPane(table);
        tableScroll.setPreferredSize(new Dimension(0, 140));

        chartPanel = new BarChartPanel();

        JLabel chartTitle = new JLabel("Comparativo de Faltas de Página", SwingConstants.CENTER);
        chartTitle.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 13));
        chartTitle.setBorder(BorderFactory.createEmptyBorder(4, 0, 2, 0));

        add(tableScroll, BorderLayout.NORTH);
        add(chartTitle, BorderLayout.CENTER);
        add(chartPanel, BorderLayout.SOUTH);
    }

    public void update(List<SimulationResult> results) {
        tableModel.setRowCount(0);
        int total = results.isEmpty() ? 1 : Math.max(1, results.get(0).getSteps().size());

        for (SimulationResult r : results) {
            int faults = r.getPageFaultCount();
            int hits = total - faults;
            double rate = (double) hits / total * 100.0;
            tableModel.addRow(new Object[]{
                r.getAlgorithmName(),
                faults,
                hits,
                String.format("%.1f%%", rate)
            });
        }
        chartPanel.setData(results);
    }

    // tint each algorithm row's name cell with its chart color
    private static class AlgoNameRenderer extends DefaultTableCellRenderer {
        @Override
        public Component getTableCellRendererComponent(
                JTable tbl, Object val, boolean sel, boolean focus, int row, int col) {
            Component c = super.getTableCellRendererComponent(tbl, val, sel, focus, row, col);
            if (!sel && row < BarChartPanel.ALGO_COLORS.length) {
                Color b = BarChartPanel.ALGO_COLORS[row];
                c.setBackground(new Color(
                    (b.getRed()   + 255 * 2) / 3,
                    (b.getGreen() + 255 * 2) / 3,
                    (b.getBlue()  + 255 * 2) / 3
                ));
            }
            return c;
        }
    }
}
