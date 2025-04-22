package org.example.sasalele_pos.Dashboard.Transaksi;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class TransaksiPanel extends JPanel {

    public TransaksiPanel() {
        setLayout(new BorderLayout());

        JLabel titleLabel = new JLabel("Transaksi", JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 30));
        titleLabel.setForeground(Color.BLACK);
        add(titleLabel, BorderLayout.NORTH);

        JPanel centerLayout = new JPanel();

        GridLayout gridLayout = new GridLayout(1, 2);
        gridLayout.setHgap(10);
        centerLayout.setLayout(gridLayout);
        centerLayout.setBorder(new EmptyBorder(10, 10, 10, 10));
        add(centerLayout, BorderLayout.CENTER);

        // Left Panel
        JScrollPane leftPanel = new JScrollPane();
        JTable leftTable = createLeftTable();
        leftPanel.setViewportView(leftTable);
        centerLayout.add(leftPanel);

        // Right Panel
        JPanel rightPanel = new JPanel();
        rightPanel.setLayout(new BorderLayout());
        rightPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        centerLayout.add(rightPanel);

        // Right Top Panel
        JPanel rightSearchPanel = new JPanel();
        GridLayout rightSearchLayout = new GridLayout(1, 3);
        rightSearchPanel.setLayout(rightSearchLayout);
        rightSearchPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
        rightSearchLayout.setVgap(10);
        rightPanel.add(rightSearchPanel, BorderLayout.NORTH);

        JPanel idPanel = new JPanel();
        JLabel idLabel = new JLabel("Kode Barang: ");
        JTextField idField = new JTextField(10);
        idPanel.add(idLabel);
        idPanel.add(idField);
        rightSearchPanel.add(idPanel);

        JPanel quantityPanel = new JPanel();
        JLabel quantityLabel = new JLabel("Kuantitas: ");
        JTextField quantityField = new JTextField(10);
        quantityPanel.add(quantityLabel);
        quantityPanel.add(quantityField);
        rightSearchPanel.add(quantityPanel);

        JPanel buttonPanel = new JPanel();
        JButton searchButton = new JButton("Tambah Belanja");
        searchButton.addActionListener(e -> {
            JOptionPane.showMessageDialog(null, "Add Clicked!");
        });
        buttonPanel.add(searchButton);
        rightSearchPanel.add(buttonPanel);

        // Right Center Panel
        JTable rightCenterTable = createRightTable();
        JScrollPane rightCenterPanel = new JScrollPane(rightCenterTable);
        rightCenterPanel.setViewportView(rightCenterTable);
        rightPanel.add(rightCenterPanel, BorderLayout.CENTER);

        // Right Bottom Panel
        JPanel rightBottomPanel = new JPanel();
        rightBottomPanel.setLayout(new GridBagLayout());
        rightBottomPanel.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(Color.BLACK, 1),
                        BorderFactory.createEmptyBorder(10, 10, 10, 10)
                )
        );
        rightPanel.add(rightBottomPanel, BorderLayout.SOUTH);

        GridBagConstraints gbc = new GridBagConstraints();

        // Grid Y = 0
        gbc.gridy = 0;
        JLabel labelBelanja = new JLabel("Total Belanja: Rp. ");
        labelBelanja.setFont(new Font("Arial", Font.BOLD, 15));
        labelBelanja.setForeground(Color.BLACK);
        gbc.gridx = 0;
        rightBottomPanel.add(labelBelanja, gbc);

        JLabel totalBelanja = new JLabel("0");
        totalBelanja.setFont(new Font("Arial", Font.BOLD, 15));
        totalBelanja.setForeground(Color.BLACK);
        gbc.gridx = 1;
        rightBottomPanel.add(totalBelanja, gbc);

        // Grid Y = 1
        gbc.gridy = 1;
        JLabel uangLabel = new JLabel("Uang Pelanggan: ");
        uangLabel.setFont(new Font("Arial", Font.PLAIN, 15));
        uangLabel.setForeground(Color.BLACK);
        gbc.gridx = 0;
        rightBottomPanel.add(uangLabel, gbc);

        JTextField uangField = new JTextField(10);
        uangField.setFont(new Font("Arial", Font.PLAIN, 15));
        uangField.setForeground(Color.BLACK);
        gbc.gridx = 1;
        rightBottomPanel.add(uangField, gbc);

        // Grid Y = 2
        gbc.gridy = 2;
        JPanel transButtonPanel = new JPanel();
        JButton transButton = new JButton("Lanjutkan Transaksi");
        transButton.addActionListener(e -> {
            JOptionPane.showMessageDialog(null, "Lanjutkan Transaksi!");
        });
        transButtonPanel.add(transButton);
        gbc.gridwidth = 2;
        gbc.gridx = 0;
        rightBottomPanel.add(transButtonPanel, gbc);
    }

    private JTable createLeftTable() {
        String[][] data = {
                {"1", "Product A", "PERISHABLE", "100.0"},
                {"2", "Product B", "NON_PERISHABLE", "50.0"},
                {"3", "Product C", "DIGITAL", "200.0"},
                {"4", "Product D", "BUNDLE", "120.0"}
        };
        String[] leftTableColumns = {"Kode", "Nama Produk", "Tipe", "Harga"};

        // Create the table model and JTable
        DefaultTableModel leftTableModel = new DefaultTableModel(data, leftTableColumns);
        return new JTable(leftTableModel);
    }

    private JTable createRightTable() {
        String[][] data = {
                {"1", "Product A", "10", "100.0"},
                {"2", "Product B", "5", "50.0"},
                {"3", "Product C", "20", "200.0"},
                {"4", "Product D", "2", "120"}
        };
        String[] rightTableColumns = {"Kode", "Nama Produk", "Kuantitas", "Harga"};
        DefaultTableModel rightTableModel = new DefaultTableModel(data, rightTableColumns);
        return new JTable(rightTableModel);
    }
}
