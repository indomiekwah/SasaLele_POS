package org.example.sasalele_pos.Dashboard.Log;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class LogPanel extends JPanel {

    public LogPanel() {
        setLayout(new BorderLayout());

        JLabel titleLabel = new JLabel("Log", JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 30));
        titleLabel.setForeground(Color.BLACK);
        add(titleLabel, BorderLayout.NORTH);

        // Center Panel
        JPanel centerPanel = new JPanel(new BorderLayout());
        JTable centerTable = createLogTable();
        JScrollPane centerScrollPane = new JScrollPane();
        centerScrollPane.setViewportView(centerTable);
        centerPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        centerPanel.add(centerScrollPane, BorderLayout.CENTER);
        add(centerPanel, BorderLayout.CENTER);
    }

    private JTable createLogTable() {
        String[][] data = {
                {"2025-04-22T21:32:43.653521", "TRANSACTION", "Transaksi TX-1745332361913 berhasil diproses. Total: Rp58500.0"},
                {"2025-04-22T22:24:57.985568", "REFUND", "Refund TX-1745335496954 untuk transaksi TX-1745332361913"}
        };
        String[] logTableColumns = {"Timestamp", "Tipe", "Deskripsi Log", "Aksi"};

        // Create the table model and JTable
        DefaultTableModel logTableModel = new DefaultTableModel(data, logTableColumns);
        JTable logTable = new JTable(logTableModel);

//        logTable.getColumn("Aksi").setCellEditor(new centerButtonRenderer(new JTextField()));
//        logTable.getColumn("Aksi").setCellEditor(new centerCellEditor(new JTextField()));

        return logTable;
    }
}
