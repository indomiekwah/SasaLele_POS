package org.example.sasalele_pos.Dashboard.Transaksi;

import org.example.sasalele_pos.database.ProductDAO;
import org.example.sasalele_pos.functions.CurrencyParser;
import org.example.sasalele_pos.model.Product;
import org.example.sasalele_pos.model.User;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class ShowTransactionDialog {
    User currentUser;

    public ShowTransactionDialog(User currentUser, double totalHarga, double uangDiberikan, double kembalian, JTable table) {
        this.currentUser = currentUser;

        // Create the dialog for selecting product type
        JDialog productTypeDialog = new JDialog((Frame) null, "Transaction Detail", true);
        productTypeDialog.setSize(400, 200);  // Set dialog size
        productTypeDialog.setLocationRelativeTo(null);

        // Set the layout manager for the dialog (using BorderLayout here)
        productTypeDialog.setLayout(new BorderLayout());

        JPanel centerLayout = new JPanel();
        centerLayout.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();

        // Add the center layout panel to the dialog
        productTypeDialog.add(centerLayout, BorderLayout.CENTER);

        // gbc Y = 0
        gbc.gridy = 0;
        JLabel totalLabel = new JLabel("Total Belanja: Rp. " + String.format("%,.2f", totalHarga));
        totalLabel.setHorizontalAlignment(SwingConstants.LEFT);
        gbc.gridx = 0;
        centerLayout.add(totalLabel, gbc);

        // gbc Y = 1
        gbc.gridy = 1;
        JLabel uangLabel = new JLabel("Uang Pelanggan: Rp. " + String.format("%,.2f", uangDiberikan));
        uangLabel.setHorizontalAlignment(SwingConstants.LEFT);
        gbc.gridx = 0;
        centerLayout.add(uangLabel, gbc);

        // gbc Y = 2
        gbc.gridy = 2;
        JLabel kembalianLabel = new JLabel("Kembalian: Rp. " + String.format("%,.2f", kembalian));
        kembalianLabel.setHorizontalAlignment(SwingConstants.LEFT);
        gbc.gridx = 0;
        centerLayout.add(kembalianLabel, gbc);

        // gbc Y = 3
        gbc.gridy = 3;
        JPanel emptyPanel = new JPanel();
        gbc.gridx = 0;
        centerLayout.add(emptyPanel, gbc);

        // gbc Y = 4
        gbc.gridy = 4;
        JTable transactionTable = createTransactionTable(table);
        gbc.gridx = 0;
        centerLayout.add(transactionTable, gbc);

        // gbc Y = 5
        gbc.gridy = 5;
        JPanel emptyPanel2 = new JPanel();
        gbc.gridx = 0;
        centerLayout.add(emptyPanel2, gbc);

        // gbc Y = 6
        gbc.gridy = 6;
        JButton okButton = new JButton("Done");
        okButton.addActionListener(e -> {
            productTypeDialog.dispose();
        });
        gbc.gridx = 0;
        gbc.gridwidth = 3;
        centerLayout.add(okButton, gbc);

        productTypeDialog.setVisible(true);
    }

    private static JTable createTransactionTable(JTable table) {
        String[][] data = new String[table.getRowCount()][5];  // 5 columns: ID, Name, Type, Price

        for (int i = 0; i < table.getRowCount(); i++) {
            data[i][0] = (String) table.getValueAt(i, 0);  // ID
            data[i][1] = (String) table.getValueAt(i, 1);  // Nama Produk
            data[i][2] = (String) table.getValueAt(i, 2);  // Qty
            data[i][3] = (String) table.getValueAt(i, 3);  // Harga
            int qty = Integer.parseInt((String) table.getValueAt(i, 2));
            double harga = CurrencyParser.convertCurrencyToDouble((String) table.getValueAt(i, 3));
            data[i][4] = String.format("Rp. " + "%,.2f", qty * harga); // Total (Qty * Harga)
        }

        String[] tableColumns = {"ID", "Nama Produk", "Qty", "Harga", "Total"};

        DefaultTableModel tableModel = new DefaultTableModel(data, tableColumns);

        return new JTable(tableModel);
    }
}