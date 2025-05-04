package org.example.sasalele_pos.Dashboard.Log;

import javax.swing.*;
import javax.swing.table.TableCellRenderer;
import java.awt.*;

public class LogButtonRenderer extends JButton implements TableCellRenderer {

    public LogButtonRenderer() {
        setLayout(new FlowLayout(FlowLayout.LEFT));  // Align buttons horizontally
    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        JButton refundButton = new JButton("Refund");

        JPanel refundPanel = new JPanel();
        refundPanel.add(refundButton);

        String type = (String) table.getModel().getValueAt(row, 1);
        refundButton.setVisible("TRANSACTION".equals(type));

        return refundPanel;
    }
}