package org.example.sasalele_pos.Dashboard.Produk;

import org.example.sasalele_pos.exceptions.InvalidProductException;
import org.example.sasalele_pos.model.*;
import org.example.sasalele_pos.services.ProductService;

import javax.swing.*;
import javax.swing.text.MaskFormatter;
import java.awt.*;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static org.example.sasalele_pos.Dashboard.Produk.ProdukPanel.refreshProductTable;

public class ShowDetailDialog extends JPanel {
    User currentUser;

    JLabel idLabel, nameLabel, priceLabel;
    JTextField idTextField, nameField, priceField;
    JPanel buttonPanel, productDetailsPanel;
    JButton cancelButton, saveButton;

    public ShowDetailDialog(User currentUser, String selectedType) {
        ShowAddDialog.currentUser = currentUser;

        // Create the dialog for selecting product type
        JDialog productTypeDialog = new JDialog((Frame) null, "Type: " + selectedType, true);
        productTypeDialog.setSize(400, 200);  // Set dialog size
        productTypeDialog.setLocationRelativeTo(null);

        JPanel productTypePanel = new JPanel();
        productTypePanel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        productTypeDialog.add(productTypePanel, BorderLayout.CENTER);

        switch (selectedType) {
            case "PERISHABLE":
                // gbc Y = 0
                gbc.gridy = 0;
                idLabel = new JLabel("Kode Produk");
                gbc.gridx = 0;
                productTypePanel.add(idLabel, gbc);

                idTextField = new JTextField(10);
                gbc.gridx = 1;
                productTypePanel.add(idTextField, gbc);

                // gbc Y = 1
                gbc.gridy = 1;
                nameLabel = new JLabel("Nama Produk");
                gbc.gridx = 0;
                productTypePanel.add(nameLabel, gbc);

                nameField = new JTextField(10);
                gbc.gridx = 1;
                productTypePanel.add(nameField, gbc);

                // gbc Y = 2
                gbc.gridy = 2;
                priceLabel = new JLabel("Harga Produk");
                gbc.gridx = 0;
                productTypePanel.add(priceLabel, gbc);

                priceField = new JTextField(10);
                gbc.gridx = 1;
                productTypePanel.add(priceField, gbc);

                // gbc Y = 3
                gbc.gridy = 3;
                JLabel expiryLabel = new JLabel("Tanggal Kadaluwarsa");
                gbc.gridx = 0;
                productTypePanel.add(expiryLabel, gbc);

                MaskFormatter dateFormatter = null;
                try {
                    dateFormatter = new MaskFormatter("####-##-##"); // Format: yyyy-MM-dd
                    dateFormatter.setPlaceholderCharacter('_'); // Placeholder for missing characters
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                JFormattedTextField dateField = new JFormattedTextField(dateFormatter);
                dateField.setColumns(10);
                gbc.gridx = 1;
                productTypePanel.add(dateField, gbc);

                // gbc Y = 4
                gbc.gridy = 4;
                saveButton = new JButton("Save");
                saveButton.addActionListener(e -> {
                    String id = idTextField.getText();
                    String name = nameField.getText();
                    double price = Double.parseDouble(priceField.getText());
                    String expiry = dateField.getText();
                    if (!expiry.isEmpty()) {
                        // Convert the string to a LocalDate object using DateTimeFormatter
                        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                        LocalDate expiryDate = LocalDate.parse(expiry, formatter);

                        ProductService productService = new ProductService();
                        try {
                            productService.addProduct(new PerishableProduct(id, name, price, expiryDate));
                            JOptionPane.showMessageDialog(productDetailsPanel, "Produk berhasil ditambahkan");
                        } catch (InvalidProductException ex) {
                            throw new RuntimeException(ex);
                        }
                    }
                    productTypeDialog.dispose();
                    refreshProductTable(new ProdukPanel(currentUser).getRightTable());
                });
                gbc.gridx = 0;
                gbc.gridwidth = 2;
                productTypePanel.add(saveButton, gbc);
                break;
            case "NON_PERISHABLE":
                // gbc Y = 0
                gbc.gridy = 0;
                idLabel = new JLabel("Kode Produk");
                gbc.gridx = 0;
                productTypePanel.add(idLabel, gbc);

                idTextField = new JTextField(10);
                gbc.gridx = 1;
                productTypePanel.add(idTextField, gbc);

                // gbc Y = 1
                gbc.gridy = 1;
                nameLabel = new JLabel("Nama Produk");
                gbc.gridx = 0;
                productTypePanel.add(nameLabel, gbc);

                nameField = new JTextField(10);
                gbc.gridx = 1;
                productTypePanel.add(nameField, gbc);

                // gbc Y = 2
                gbc.gridy = 2;
                priceLabel = new JLabel("Harga Produk");
                gbc.gridx = 0;
                productTypePanel.add(priceLabel, gbc);

                priceField = new JTextField(10);
                gbc.gridx = 1;
                productTypePanel.add(priceField, gbc);

                // gbc Y = 3
                gbc.gridy = 3;
                saveButton = new JButton("Save");
                saveButton.addActionListener(e -> {
                    String id = idTextField.getText();
                    String name = nameField.getText();
                    double price = Double.parseDouble(priceField.getText());

                    ProductService productService = new ProductService();
                    try {
                        productService.addProduct(new NonPerishableProduct(id, name, price));
                        JOptionPane.showMessageDialog(productDetailsPanel, "Produk berhasil ditambahkan");
                    } catch (InvalidProductException ex) {
                        throw new RuntimeException(ex);
                    }
                    productTypeDialog.dispose();
                    refreshProductTable(new ProdukPanel(currentUser).getRightTable());
                });
                gbc.gridx = 0;
                gbc.gridwidth = 2;
                productTypePanel.add(saveButton, gbc);
                break;
            case "BUNDLE":
                // gbc Y = 0
                gbc.gridy = 0;
                idLabel = new JLabel("Kode Produk");
                gbc.gridx = 0;
                productTypePanel.add(idLabel, gbc);

                idTextField = new JTextField(10);
                gbc.gridx = 1;
                productTypePanel.add(idTextField, gbc);

                // gbc Y = 1
                gbc.gridy = 1;
                nameLabel = new JLabel("Nama Produk");
                gbc.gridx = 0;
                productTypePanel.add(nameLabel, gbc);

                nameField = new JTextField(10);
                gbc.gridx = 1;
                productTypePanel.add(nameField, gbc);

                // gbc Y = 2
                gbc.gridy = 2;
                priceLabel = new JLabel("Harga Produk");
                gbc.gridx = 0;
                productTypePanel.add(priceLabel, gbc);

                priceField = new JTextField(10);
                gbc.gridx = 1;
                productTypePanel.add(priceField, gbc);

                // gbc Y = 3
                gbc.gridy = 3;
                saveButton = new JButton("Save");
                saveButton.addActionListener(e -> {
                    String id = idTextField.getText();
                    String name = nameField.getText();
                    double price = Double.parseDouble(priceField.getText());

                    ProductService productService = new ProductService();
                    try {
                        productService.addProduct(new BundleProduct(id, name, price));
                        JOptionPane.showMessageDialog(productDetailsPanel, "Produk berhasil ditambahkan");
                    } catch (InvalidProductException ex) {
                        throw new RuntimeException(ex);
                    }
                    productTypeDialog.dispose();
                    refreshProductTable(new ProdukPanel(currentUser).getRightTable());
                });
                gbc.gridx = 0;
                gbc.gridwidth = 2;
                productTypePanel.add(saveButton, gbc);
                break;
            case "DIGITAL":
                // gbc Y = 0
                gbc.gridy = 0;
                idLabel = new JLabel("Kode Produk");
                gbc.gridx = 0;
                productTypePanel.add(idLabel, gbc);

                idTextField = new JTextField(10);
                gbc.gridx = 1;
                productTypePanel.add(idTextField, gbc);

                // gbc Y = 1
                gbc.gridy = 1;
                nameLabel = new JLabel("Nama Produk");
                gbc.gridx = 0;
                productTypePanel.add(nameLabel, gbc);

                nameField = new JTextField(10);
                gbc.gridx = 1;
                productTypePanel.add(nameField, gbc);

                // gbc Y = 2
                gbc.gridy = 2;
                priceLabel = new JLabel("Harga Produk");
                gbc.gridx = 0;
                productTypePanel.add(priceLabel, gbc);

                priceField = new JTextField(10);
                gbc.gridx = 1;
                productTypePanel.add(priceField, gbc);

                // gbc Y = 3
                gbc.gridy = 3;
                JLabel urlLabel = new JLabel("URL");
                gbc.gridx = 0;
                productTypePanel.add(urlLabel, gbc);

                JTextField urlField = new JTextField(10);
                gbc.gridx = 1;
                productTypePanel.add(urlField, gbc);

                // gbc Y = 4
                gbc.gridy = 4;
                JLabel vendorLabel = new JLabel("Nama Vendor");
                gbc.gridx = 0;
                productTypePanel.add(vendorLabel, gbc);

                JTextField vendorField = new JTextField(10);
                gbc.gridx = 1;
                productTypePanel.add(vendorField, gbc);

                // gbc Y = 5
                gbc.gridy = 5;
                saveButton = new JButton("Save");
                saveButton.addActionListener(e -> {
                    String id = idTextField.getText();
                    String name = nameField.getText();
                    double price = Double.parseDouble(priceField.getText());
                    String url = urlField.getText();
                    String vendor = vendorField.getText();

                    ProductService productService = new ProductService();
                    try {
                        productService.addProduct(new DigitalProduct(id, name, price, url, vendor));
                        JOptionPane.showMessageDialog(productDetailsPanel, "Produk berhasil ditambahkan");
                    } catch (InvalidProductException ex) {
                        throw new RuntimeException(ex);
                    }
                    productTypeDialog.dispose();
                });
                gbc.gridx = 0;
                gbc.gridwidth = 2;
                productTypePanel.add(saveButton, gbc);
                break;
        }

        productTypeDialog.setVisible(true);
    }
}
