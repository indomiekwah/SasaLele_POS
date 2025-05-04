package org.example.sasalele_pos.Dashboard.Produk;

import org.example.sasalele_pos.Dashboard.Transaksi.TransaksiPanel;
import org.example.sasalele_pos.database.ProductDAO;
import org.example.sasalele_pos.model.*;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class ProdukPanel extends JPanel {
    public static User currentUser;
    private static JTable rightTable;

    public ProdukPanel(User currentUser) {
        ProdukPanel.currentUser = currentUser;

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
        rightTable = centerTable;

        // Bottom Panel
        JPanel bottomPanel = new JPanel();
        JButton tambahButton = new JButton("Tambah Produk");
        tambahButton.setFont(new Font("Arial", Font.BOLD, 20));
        tambahButton.setForeground(Color.BLACK);
        bottomPanel.add(tambahButton);
        add(bottomPanel, BorderLayout.SOUTH);

        tambahButton.addActionListener(e -> {
            new ShowAddDialog(currentUser);
            refreshProductTable(centerTable);
        });
    }

    private JTable createProductTable() {
        List<Product> products = ProductDAO.getAllProducts();

        String[][] data = new String[products.size()][8];

        for (int i = 0; i < products.size(); i++) {
            Product product = products.get(i);
            data[i][0] = String.valueOf(product.getId());  // ID
            data[i][1] = product.getName();               // Name
            data[i][2] = product.getProductType();        // Type
            data[i][3] = String.valueOf(product.getPrice()); // Price

            // Handle expiry date, URL, and vendor based on product type
            if (product instanceof PerishableProduct perishable) {
                data[i][4] = perishable.getExpiryDate().toString();  // Expiry Date
            } else {
                data[i][4] = "N/A";  // Expiry Date not applicable
            }

            if (product instanceof DigitalProduct digital) {
                data[i][5] = digital.getUrl();  // URL
                data[i][6] = digital.getVendorName();  // Vendor
            } else {
                data[i][5] = "N/A";  // URL not applicable
                data[i][6] = "N/A";  // Vendor not applicable
            }

            if (product instanceof BundleProduct bundle) {
                data[i][4] = "N/A";
                data[i][5] = "N/A";
                data[i][6] = "N/A";
            }

            data[i][7] = "Action";
        }

        String[] productTableColumns = {"Kode", "Nama Produk", "Tipe", "Harga", "Tanggal Kadaluwarsa", "URL Produk", "Nama Vendor", "Aksi"};

        // Create the table model and JTable
        DefaultTableModel centerTableModel = new DefaultTableModel(data, productTableColumns);
        JTable productTable = new JTable(centerTableModel);
        productTable.setRowHeight(40);

        productTable.getColumn("Aksi").setCellRenderer(new ButtonRendererProduct());
        productTable.getColumn("Aksi").setCellEditor(new ButtonEditorProduct(new JCheckBox()));

        return productTable;
    }

    public static void refreshProductTable(JTable table) {
        List<Product> products = ProductDAO.getAllProducts();

        String[][] data = new String[products.size()][8];

        for (int i = 0; i < products.size(); i++) {
            Product product = products.get(i);
            data[i][0] = String.valueOf(product.getId());  // ID
            data[i][1] = product.getName();               // Name
            data[i][2] = product.getProductType();        // Type
            data[i][3] = String.valueOf(product.getPrice()); // Price

            // Handle expiry date, URL, and vendor based on product type
            if (product instanceof PerishableProduct perishable) {
                data[i][4] = perishable.getExpiryDate().toString();  // Expiry Date
            } else {
                data[i][4] = "N/A";  // Expiry Date not applicable
            }

            if (product instanceof DigitalProduct digital) {
                data[i][5] = digital.getUrl();  // URL
                data[i][6] = digital.getVendorName();  // Vendor
            } else {
                data[i][5] = "N/A";  // URL not applicable
                data[i][6] = "N/A";  // Vendor not applicable
            }

            if (product instanceof BundleProduct bundle) {
                data[i][4] = "N/A";
                data[i][5] = "N/A";
                data[i][6] = "N/A";
            }

            data[i][7] = "Action";
        }

        String[] productTableColumns = {"Kode", "Nama Produk", "Tipe", "Harga", "Tanggal Kadaluwarsa", "URL Produk", "Nama Vendor", "Aksi"};

        // Create the table model and JTable
        DefaultTableModel centerTableModel = new DefaultTableModel(data, productTableColumns);
        table.setModel(centerTableModel);
        table.setRowHeight(40);

        table.getColumn("Aksi").setCellRenderer(new ButtonRendererProduct());
        table.getColumn("Aksi").setCellEditor(new ButtonEditorProduct(new JCheckBox()));
    }

    public JTable getRightTable() {
        return rightTable;
    }
}
