package org.example.sasalele_pos.Dashboard.Produk;

import javax.swing.*;
import javax.swing.table.TableCellRenderer;
import java.awt.*;

public class ButtonRendererProduct extends JPanel implements TableCellRenderer {
    public ButtonRendererProduct() {
        setLayout(new FlowLayout(FlowLayout.LEFT));  // Align buttons horizontally
    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        // Create buttons for the actions using text
        JButton editButton = new JButton("Edit");
        JButton deleteButton = new JButton("Delete");

        // Add buttons to the panel
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(editButton);
        buttonPanel.add(deleteButton);

        return buttonPanel;
    }
}
