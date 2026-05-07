package simulador.ui;

import javax.swing.*;
import java.awt.*;
import java.util.function.BiConsumer;

public class InputPanel extends JPanel {
    private final JTextField pageField;
    private final JSpinner frameSpinner;
    private final BiConsumer<int[], Integer> onSimulate;

    public InputPanel(BiConsumer<int[], Integer> onSimulate) {
        this.onSimulate = onSimulate;
        setLayout(new FlowLayout(FlowLayout.LEFT, 10, 8));
        setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(0, 0, 1, 0, Color.LIGHT_GRAY),
            BorderFactory.createEmptyBorder(2, 8, 2, 8)
        ));

        add(new JLabel("Sequência de páginas:"));

        pageField = new JTextField("7 0 1 2 0 3 0 4 2 3 0 3 2 1 2 0 1 7 0 1", 38);
        pageField.setToolTipText("Números separados por espaço ou vírgula");
        add(pageField);

        add(new JLabel("Quadros:"));
        frameSpinner = new JSpinner(new SpinnerNumberModel(3, 1, 20, 1));
        frameSpinner.setPreferredSize(new Dimension(55, 26));
        add(frameSpinner);

        JButton btn = new JButton("Simular");
        btn.addActionListener(e -> handleSimulate());
        add(btn);

        pageField.addActionListener(e -> handleSimulate());
    }

    private void handleSimulate() {
        String text = pageField.getText().trim();
        if (text.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                "Informe a sequência de páginas.", "Entrada inválida", JOptionPane.WARNING_MESSAGE);
            return;
        }
        String[] tokens = text.split("[,\\s]+");
        int[] pages;
        try {
            pages = new int[tokens.length];
            for (int i = 0; i < tokens.length; i++) {
                pages[i] = Integer.parseInt(tokens[i].trim());
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this,
                "A sequência deve conter apenas números inteiros.", "Entrada inválida", JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (pages.length == 0) {
            JOptionPane.showMessageDialog(this,
                "Informe ao menos uma página.", "Entrada inválida", JOptionPane.WARNING_MESSAGE);
            return;
        }
        onSimulate.accept(pages, (Integer) frameSpinner.getValue());
    }
}
