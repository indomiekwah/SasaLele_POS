package org.example.sasalele_pos.Dashboard.Produk;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class ProdukPanel extends JPanel {

    public ProdukPanel() {
        setLayout(new BorderLayout());

        JLabel titleLabel = new JLabel("Produk", JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 30));
        titleLabel.setForeground(Color.BLACK);
        add(titleLabel, BorderLayout.NORTH);

        // Center Panel
        JPanel centerPanel = new JPanel(new BorderLayout());
        JTable centerTable = createProductTable();
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

    private JTable createProductTable() {
        String[][] data = {
                {"1", "Product A", "PERISHABLE", "100.0", "2025-05-05", "N/A", "N/A"},
                {"2", "Product B", "NON_PERISHABLE", "50.0", "N/A", "N/A", "N/A"},
                {"3", "Product C", "DIGITAL", "200.0", "N/A", "https://tokopedia.com", "Tokopedia"},
                {"4", "Product D", "BUNDLE", "120.0", "N/A", "N/A", "N/A"},
        };
        String[] productTableColumns = {"Kode", "Nama Produk", "Tipe", "Harga", "Tanggal Kadaluwarsa", "URL Produk", "Nama Vendor", "Aksi"};

        // Create the table model and JTable
        DefaultTableModel centerTableModel = new DefaultTableModel(data, productTableColumns);
        JTable productTable = new JTable(centerTableModel);

//        productTable.getColumn("Aksi").setCellEditor(new centerButtonRenderer(new JTextField()));
//        productTable.getColumn("Aksi").setCellEditor(new centerCellEditor(new JTextField()));

        return productTable;
    }
}
