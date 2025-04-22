package org.example.sasalele_pos.Dashboard.Transaksi;

import javax.swing.*;
import javax.swing.table.TableCellRenderer;
import java.awt.*;

public class RightButtonRenderer extends JPanel implements TableCellRenderer {
    public RightButtonRenderer() {
        setLayout(new FlowLayout(FlowLayout.LEFT));  // Align buttons horizontally
    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        // Create buttons for the actions using text
        JButton decreaseButton = new JButton("<-");
        JButton deleteButton = new JButton("D");
        JButton increaseButton = new JButton("->");

        // Add buttons to the panel
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(decreaseButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(increaseButton);

        return buttonPanel;
    }
}

