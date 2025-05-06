package org.example.sasalele_pos.Dashboard.Transaksi;

import org.example.sasalele_pos.database.ProductDAO;
import org.example.sasalele_pos.exceptions.InvalidTransactionException;
import org.example.sasalele_pos.functions.CurrencyParser;
import org.example.sasalele_pos.model.CartItem;
import org.example.sasalele_pos.model.Product;
import org.example.sasalele_pos.model.User;
import org.example.sasalele_pos.services.TransactionService;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

import static org.example.sasalele_pos.Dashboard.Produk.ProdukPanel.refreshProductTable;

public class TransaksiPanel extends JPanel {

    public static List<CartItem> cartItems = new ArrayList<>();
    public static User currentUser;

    private static JTable productTable;

    public TransaksiPanel(User currentUser) {
        TransaksiPanel.currentUser = currentUser;

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
        productTable = leftTable;

        // Right Panel
        JPanel rightPanel = new JPanel();
        rightPanel.setLayout(new BorderLayout());
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
        JLabel labelBelanja = new JLabel("Total Belanja: ");
        labelBelanja.setFont(new Font("Arial", Font.BOLD, 15));
        labelBelanja.setForeground(Color.BLACK);
        gbc.gridx = 0;
        gbc.gridwidth = 1;
        rightBottomPanel.add(labelBelanja, gbc);

        JLabel totalBelanja = new JLabel("Rp. 0,00");
        rightCenterTable.getModel().addTableModelListener(e -> {
            setTotalPrice(rightCenterTable, totalBelanja);
        });
        totalBelanja.setFont(new Font("Arial", Font.BOLD, 15));
        totalBelanja.setForeground(Color.BLACK);
        gbc.gridx = 1;
        gbc.gridwidth = 2;
        rightBottomPanel.add(totalBelanja, gbc);

        // Grid Y = 1
        gbc.gridy = 1;
        JLabel uangLabel = new JLabel("Uang Pelanggan: ");
        uangLabel.setFont(new Font("Arial", Font.PLAIN, 15));
        uangLabel.setForeground(Color.BLACK);
        gbc.gridx = 0;
        gbc.gridwidth = 1;
        rightBottomPanel.add(uangLabel, gbc);

        JTextField uangField = new JTextField(10);
        uangField.setFont(new Font("Arial", Font.PLAIN, 15));
        uangField.setForeground(Color.BLACK);
        gbc.gridx = 1;
        gbc.gridwidth = 2;
        rightBottomPanel.add(uangField, gbc);

        // Grid Y = 2
        gbc.gridy = 2;
        JPanel transButtonPanel = new JPanel();
        JButton transButton = new JButton("Lanjutkan Transaksi");
        transButton.addActionListener(e -> {
            double hargaBelanja = CurrencyParser.convertCurrencyToDouble(totalBelanja.getText());
            double totalUang = Double.parseDouble(uangField.getText());
            double kembalian = totalUang - hargaBelanja;

            if (hargaBelanja > totalUang) {
                JOptionPane.showMessageDialog(null, "Uang pelanggan kurang: Rp. " + String.format("%,.0f", (hargaBelanja - totalUang)));
            } else {
                TransactionService transactionService = new TransactionService();
                try {
                    double process = transactionService.processSale(cartItems, totalUang, currentUser.getUsername());
                    if (process == totalUang - hargaBelanja) {
                        new ShowTransactionDialog(currentUser, hargaBelanja, totalUang, kembalian, rightCenterTable);

                        idField.setText("");
                        quantityField.setText("");

                        cartItems.clear();
                        DefaultTableModel rightCenterModel = (DefaultTableModel) rightCenterTable.getModel();
                        rightCenterModel.setRowCount(0);
                        rightCenterTable.setModel(rightCenterModel);
                    }
                } catch (InvalidTransactionException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });
        transButtonPanel.add(transButton);
        gbc.gridwidth = 2;
        gbc.gridx = 0;
        rightBottomPanel.add(transButtonPanel, gbc);

        searchButton.addActionListener(e -> {
            String id = idField.getText();
            int quantity = Integer.parseInt(quantityField.getText());

            addProductToCart(rightCenterTable, id, quantity, cartItems);

            idField.setText("");
            quantityField.setText("");
        });
    }

    private static void addProductToCart(JTable table, String id, int quantity, List<CartItem> cartItems) {
        Product product = ProductDAO.getProductById(id);

        if (product != null) {
            CartItem newItem = new CartItem(product, quantity);
            cartItems.add(newItem);

            String[] newRow = new String[5];
            newRow[0] = id;
            newRow[1] = product.getName();
            newRow[2] = String.valueOf(quantity);
            newRow[3] = String.format("Rp. %,.0f", product.getPrice());
            newRow[4] = "Action";

            DefaultTableModel tableModel = (DefaultTableModel) table.getModel();
            tableModel.addRow(newRow);
        } else {
            JOptionPane.showMessageDialog(null, "Produk tidak ditemukan!");
        }
    }

    private static JTable createLeftTable() {
        List<Product> products = ProductDAO.getAllProducts();

        String[][] data = new String[products.size()][4];  // 4 columns: ID, Name, Type, Price

        for (int i = 0; i < products.size(); i++) {
            Product product = products.get(i);

            data[i][0] = product.getId();                // Product ID
            data[i][1] = product.getName();              // Product Name
            data[i][2] = product.getProductType();       // Product Type (e.g., Perishable, Digital)
            data[i][3] = String.format("Rp. %,.0f", product.getPrice()); // Product Price with formatting
        }

        String[] leftTableColumns = {"Kode", "Nama Produk", "Tipe", "Harga"};

        DefaultTableModel leftTableModel = new DefaultTableModel(data, leftTableColumns);

        return new JTable(leftTableModel);
    }

    private static JTable createRightTable() {
        String[][] data = new String[0][5];
        String[] rightTableColumns = {"Kode", "Nama Produk", "Kuantitas", "Harga", "Aksi"};
        DefaultTableModel rightTableModel = new DefaultTableModel(data, rightTableColumns);
        JTable rightTable = new JTable(rightTableModel);
        rightTable.setRowHeight(40);

        rightTable.getColumn("Aksi").setCellRenderer(new RightButtonRenderer());
        rightTable.getColumn("Aksi").setCellEditor(new RightCellEditor(new JCheckBox(), cartItems));

        return rightTable;
    }

    private static void setTotalPrice(JTable table, JLabel totalBelanja) {
        // Initialize the total price to 0
        double totalHarga = 0;

        // Iterate over all the rows of the table (starting from row 0)
        for (int i = 0; i < table.getModel().getRowCount(); i++) {
            try {
                // Get the price and quantity for each row
                double price = CurrencyParser.convertCurrencyToDouble((String) table.getModel().getValueAt(i, 3));  // Assuming the "Price" column is at index 3
                int quantity = (int) CurrencyParser.convertCurrencyToDouble((String) table.getModel().getValueAt(i, 2));     // Assuming the "Quantity" column is at index 2

                // Add the subtotal (price * quantity) to the total price
                totalHarga += price * quantity;
            } catch (Exception e) {
                System.err.println("Error processing row " + i + ": " + e.getMessage());
            }
        }

        totalBelanja.setText(String.format("Rp. %,.0f", totalHarga));
    }

    public static void refreshTable(JTable table) {
        List<Product> products = ProductDAO.getAllProducts();

        // Create a 2D array to store the data for the table
        String[][] data = new String[products.size()][4];  // 4 columns: ID, Name, Type, Price

        // Populate the data array with values from the product list
        for (int i = 0; i < products.size(); i++) {
            Product product = products.get(i);

            data[i][0] = product.getId();                // Product ID
            data[i][1] = product.getName();              // Product Name
            data[i][2] = product.getProductType();       // Product Type (e.g., Perishable, Digital)
            data[i][3] = String.format("Rp. %,.0f", product.getPrice()); // Product Price with formatting
        }

        String[] leftTableColumns = {"Kode", "Nama Produk", "Tipe", "Harga"};

        DefaultTableModel leftTableModel = new DefaultTableModel(data, leftTableColumns);

        table.setModel(leftTableModel);
    }

    public JTable getProductTable() {
        return productTable;
    }
}