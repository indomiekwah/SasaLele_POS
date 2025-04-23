package org.example.sasalele_pos.Dashboard.Akun;

import javax.swing.*;
import javax.swing.table.TableCellRenderer;
import java.awt.*;

public class AccountButtonRenderer extends JPanel implements TableCellRenderer {
    public AccountButtonRenderer() {
        setLayout(new GridBagLayout());
        setOpaque(true);
    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        removeAll();
        JButton editButton = new JButton("Edit");
        JButton deleteButton = new JButton("Delete");

        editButton.setPreferredSize(new Dimension(80, 30));
        deleteButton.setPreferredSize(new Dimension(80, 30));

        editButton.setFont(new Font("Arial", Font.PLAIN, 12));
        deleteButton.setFont(new Font("Arial", Font.PLAIN, 12));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(0, 5, 0, 5);
        gbc.gridx = 0;
        add(editButton, gbc);
        gbc.gridx = 1;
        add(deleteButton, gbc);

        return this;
    }
}
