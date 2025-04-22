package org.example.sasalele_pos.Dashboard.Akun;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class AkunPanel extends JPanel {

    public AkunPanel() {
        setLayout(new BorderLayout());

        JLabel titleLabel = new JLabel("Akun", JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 30));
        titleLabel.setForeground(Color.BLACK);
        add(titleLabel, BorderLayout.NORTH);

        // Center Panel
        JPanel centerPanel = new JPanel(new BorderLayout());
        JTable centerTable = createAccountTable();
        JScrollPane centerScrollPane = new JScrollPane();
        centerScrollPane.setViewportView(centerTable);
        centerPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        centerPanel.add(centerScrollPane, BorderLayout.CENTER);
        add(centerPanel, BorderLayout.CENTER);

        // Bottom Panel
        JPanel bottomPanel = new JPanel();
        JButton tambahButton = new JButton("Tambah Produk");
        tambahButton.setFont(new Font("Arial", Font.BOLD, 20));
        tambahButton.setForeground(Color.BLACK);
        bottomPanel.add(tambahButton);
        add(bottomPanel, BorderLayout.SOUTH);
    }

    private JTable createAccountTable() {
        String[][] data = {
                {"user1", "User"},
                {"user2", "User"},
                {"admin1", "Admin"},
                {"admin2", "Admin"},
        };
        String[] accountTableColumns = {"Nama Pengguna", "Role", "Aksi"};

        // Create the table model and JTable
        DefaultTableModel accountTableModel = new DefaultTableModel(data, accountTableColumns);
        JTable accountTable = new JTable(accountTableModel);

//        accountTable.getColumn("Aksi").setCellEditor(new centerButtonRenderer(new JTextField()));
//        accountTable.getColumn("Aksi").setCellEditor(new centerCellEditor(new JTextField()));

        return accountTable;
    }
}
