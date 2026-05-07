package simulador.ui;

import simulador.model.SimulationResult;
import simulador.model.StepState;
import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class StepTablePanel extends JPanel {
    private final JComboBox<String> algoSelector;
    private final JTable table;
    private final DefaultTableModel tableModel;
    private List<SimulationResult> currentResults;

    private static final Color FAULT_ROW = new Color(255, 200, 200);
    private static final Color HIT_ROW   = new Color(220, 245, 220);

    public StepTablePanel() {
        setLayout(new BorderLayout(4, 4));
        setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));

        JPanel toolbar = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 4));
        toolbar.add(new JLabel("Algoritmo:"));
        algoSelector = new JComboBox<>();
        algoSelector.addActionListener(e -> refreshTable());
        toolbar.add(algoSelector);

        JLabel legend = new JLabel(
            "  ■ Falta de página    ■ Acerto",
            SwingConstants.LEFT
        );
        legend.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 11));
        toolbar.add(legend);

        tableModel = new DefaultTableModel() {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        table = new JTable(tableModel);
        table.setRowHeight(22);
        table.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12));
        table.getTableHeader().setFont(new Font(Font.SANS_SERIF, Font.BOLD, 12));
        table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        table.setDefaultRenderer(Object.class, new StepCellRenderer());

        add(toolbar, BorderLayout.NORTH);
        add(new JScrollPane(table), BorderLayout.CENTER);
    }

    public void update(List<SimulationResult> results) {
        this.currentResults = results;
        algoSelector.removeAllItems();
        for (SimulationResult r : results) algoSelector.addItem(r.getAlgorithmName());
        if (!results.isEmpty()) algoSelector.setSelectedIndex(0);
    }

    private void refreshTable() {
        if (currentResults == null || currentResults.isEmpty()) return;
        int idx = algoSelector.getSelectedIndex();
        if (idx < 0) return;

        SimulationResult result = currentResults.get(idx);
        List<StepState> steps = result.getSteps();
        if (steps.isEmpty()) return;

        int frameCount = steps.get(0).frames.length;
        String[] cols = new String[frameCount + 3];
        cols[0] = "Passo";
        cols[1] = "Página";
        for (int i = 0; i < frameCount; i++) cols[i + 2] = "Q" + (i + 1);
        cols[frameCount + 2] = "Falta?";

        tableModel.setColumnIdentifiers(cols);
        tableModel.setRowCount(0);

        for (int i = 0; i < steps.size(); i++) {
            StepState s = steps.get(i);
            Object[] row = new Object[cols.length];
            row[0] = i + 1;
            row[1] = s.page;
            for (int j = 0; j < frameCount; j++) {
                row[j + 2] = s.frames[j] == -1 ? "—" : String.valueOf(s.frames[j]);
            }
            row[frameCount + 2] = s.pageFault ? "★ SIM" : "não";
            tableModel.addRow(row);
        }
    }

    private class StepCellRenderer extends DefaultTableCellRenderer {
        @Override
        public Component getTableCellRendererComponent(
                JTable tbl, Object val, boolean sel, boolean focus, int row, int col) {
            Component c = super.getTableCellRendererComponent(tbl, val, sel, focus, row, col);
            if (!sel) {
                int lastCol = tbl.getColumnCount() - 1;
                Object faultVal = lastCol >= 0 ? tbl.getValueAt(row, lastCol) : null;
                c.setBackground("★ SIM".equals(faultVal) ? FAULT_ROW : HIT_ROW);
            }
            setHorizontalAlignment(col == 0 || col == 1 ? SwingConstants.CENTER : SwingConstants.CENTER);
            return c;
        }
    }
}
