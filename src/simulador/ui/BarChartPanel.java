package simulador.ui;

import simulador.model.SimulationResult;
import javax.swing.*;
import java.awt.*;
import java.util.Collections;
import java.util.List;

public class BarChartPanel extends JPanel {

    static final Color[] ALGO_COLORS = {
        new Color(70, 130, 180),   // FIFO - steel blue
        new Color(255, 140, 0),    // LRU  - orange
        new Color(60, 160, 80),    // Clock - green
        new Color(200, 60, 60)     // Otimo - red
    };

    private List<SimulationResult> results = Collections.emptyList();

    public BarChartPanel() {
        setBackground(Color.WHITE);
        setPreferredSize(new Dimension(0, 220));
    }

    public void setData(List<SimulationResult> results) {
        this.results = results;
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (results.isEmpty()) return;

        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        int w = getWidth();
        int h = getHeight();
        int padL = 50, padR = 20, padT = 20, padB = 40;
        int chartW = w - padL - padR;
        int chartH = h - padT - padB;

        int maxFaults = results.stream().mapToInt(SimulationResult::getPageFaultCount).max().orElse(1);
        if (maxFaults == 0) maxFaults = 1;

        int n = results.size();
        int totalGaps = n + 1;
        int barW = Math.max(20, (chartW - totalGaps * 10) / n);
        int gapW = (chartW - barW * n) / (n + 1);

        // axes
        g2.setColor(new Color(100, 100, 100));
        g2.drawLine(padL, padT, padL, padT + chartH);
        g2.drawLine(padL, padT + chartH, padL + chartW, padT + chartH);

        // y-axis ticks & grid
        g2.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 10));
        int tickCount = Math.min(maxFaults, 6);
        for (int t = 0; t <= tickCount; t++) {
            int val = maxFaults * t / tickCount;
            int y = padT + chartH - (int) ((double) val / maxFaults * chartH);
            g2.setColor(new Color(200, 200, 200));
            g2.drawLine(padL + 1, y, padL + chartW, y);
            g2.setColor(new Color(80, 80, 80));
            g2.drawLine(padL - 3, y, padL, y);
            FontMetrics fm = g2.getFontMetrics();
            g2.drawString(String.valueOf(val), padL - 6 - fm.stringWidth(String.valueOf(val)), y + 4);
        }

        // bars
        for (int i = 0; i < n; i++) {
            SimulationResult r = results.get(i);
            int faults = r.getPageFaultCount();
            int barH = (int) ((double) faults / maxFaults * chartH);
            int x = padL + gapW + i * (barW + gapW);
            int y = padT + chartH - barH;

            Color base = ALGO_COLORS[i % ALGO_COLORS.length];
            g2.setColor(base);
            g2.fillRoundRect(x, y, barW, barH, 6, 6);
            g2.setColor(base.darker());
            g2.drawRoundRect(x, y, barW, barH, 6, 6);

            // value on top
            g2.setColor(Color.DARK_GRAY);
            g2.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 12));
            String val = String.valueOf(faults);
            FontMetrics fm = g2.getFontMetrics();
            g2.drawString(val, x + (barW - fm.stringWidth(val)) / 2, y - 4);

            // algorithm label below axis
            g2.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 11));
            fm = g2.getFontMetrics();
            String name = r.getAlgorithmName();
            g2.setColor(Color.DARK_GRAY);
            g2.drawString(name, x + (barW - fm.stringWidth(name)) / 2, padT + chartH + 16);
        }

        g2.dispose();
    }
}
