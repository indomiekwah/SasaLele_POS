package org.example.sasalele_pos.Dashboard.Produk;

import org.example.sasalele_pos.database.ProductDAO;
import org.example.sasalele_pos.exceptions.InvalidProductException;
import org.example.sasalele_pos.model.DigitalProduct;
import org.example.sasalele_pos.model.NonPerishableProduct;
import org.example.sasalele_pos.model.PerishableProduct;
import org.example.sasalele_pos.services.ProductService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.text.MaskFormatter;
import java.awt.*;
import java.sql.SQLException;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

import static org.example.sasalele_pos.Dashboard.Produk.ProdukPanel.refreshProductTable;

public class ButtonEditorProduct extends DefaultCellEditor {

    public ButtonEditorProduct(JCheckBox checkBox) {
        super(checkBox);
    }

    @Override
    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {

        // Create action buttons with text labels
        JButton editButton = new JButton("Edit");
        JButton deleteButton = new JButton("Delete");

        // Add action listener for the Edit button
        editButton.addActionListener(e -> {
            // Get the type of the product from the current row
            String productType = (String) table.getValueAt(row, 2); // Assuming "Type" is in the 3rd column (index 2)

            // Open a dialog based on the product type
            switch (productType) {
                case "PERISHABLE" -> openPerishableProductDialog(table, row);
                case "DIGITAL" -> openDigitalProductDialog(table, row);
                case "BUNDLE" -> openBundleProductDialog(table, row);
                case null, default -> openNonPerishableProductDialog(table, row);
            }
        });

        // Add action listener for the Delete button
        deleteButton.addActionListener(e -> {
            // Only allow deletion of the first row
            if (row == 0) {
                // Get the product ID from the selected row in the table
                String productId = (String) table.getModel().getValueAt(row, 0);

                // Remove the product from the table first
                ((DefaultTableModel) table.getModel()).removeRow(row);

                // Now, attempt to delete the product from the database
                ProductDAO productDAO = new ProductDAO();

                // Check if the product exists and is referenced in the database
                if (Objects.equals(Objects.requireNonNull(ProductDAO.getProductById(productId)).getId(), productId)) {
                    try {
                        // First, remove references in transaction_items table
                        productDAO.deleteProductFromTransactionItems(productId);  // This method will delete related transaction items

                        // Then, delete the product from the products table
                        productDAO.deleteProduct(productId);  // Now delete the product
                        JOptionPane.showMessageDialog(null, "Product deleted successfully!");
                    } catch (SQLException ex) {
                        JOptionPane.showMessageDialog(null, "Failed to delete product: " + ex.getMessage());
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "Product does not exist");
                }
            } else {
                JOptionPane.showMessageDialog(table, "You can only delete the first row.", "Delete Not Allowed", JOptionPane.WARNING_MESSAGE);
            }
        });


        // Add buttons to a panel
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(editButton);
        buttonPanel.add(deleteButton);

        return buttonPanel;
    }

    @Override
    public Object getCellEditorValue() {
        return null;
    }

    // Open dialog for editing a Perishable product
    private void openPerishableProductDialog(JTable table, int row) {
        // Create the dialog panel for perishable product details
        JPanel productDetailsPanel = new JPanel();
        productDetailsPanel.setLayout(new GridLayout(4, 2));

        JLabel idLabel = new JLabel("ID");
        JTextField idTextField = new JTextField(10);
        idTextField.setText((String) table.getModel().getValueAt(row, 0));  // Pre-fill the ID field (disable editing)
        idTextField.setEditable(false);  // Set to non-editable because ID shouldn't be changed
        productDetailsPanel.add(idLabel);
        productDetailsPanel.add(idTextField);

        JLabel nameLabel = new JLabel("Product Name:");
        JTextField nameField = new JTextField(15);
        nameField.setText((String) table.getModel().getValueAt(row, 1));  // Pre-fill with existing product name
        productDetailsPanel.add(nameLabel);
        productDetailsPanel.add(nameField);

        JLabel priceLabel = new JLabel("Price:");
        JTextField priceField = new JTextField(10);
        priceField.setText(String.valueOf((String) table.getModel().getValueAt(row, 3)));  // Pre-fill with product price
        productDetailsPanel.add(priceLabel);
        productDetailsPanel.add(priceField);

        // Create a MaskFormatter for date input (yyyy-MM-dd)
        MaskFormatter dateFormatter = null;
        try {
            dateFormatter = new MaskFormatter("####-##-##"); // Format: yyyy-MM-dd
            dateFormatter.setPlaceholderCharacter('_'); // Placeholder for missing characters
        } catch (ParseException e) {
            e.printStackTrace();
        }

        JLabel expiryDateLabel = new JLabel("Expiry Date:");
        JFormattedTextField dateField = new JFormattedTextField(dateFormatter);
        dateField.setText((String) table.getModel().getValueAt(row, 4));  // Pre-fill expiry date if it's a perishable product
        dateField.setColumns(10); // Set the size of the text field
        productDetailsPanel.add(expiryDateLabel);
        productDetailsPanel.add(dateField);

        // Panel for "Cancel" and "Save" buttons
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER));

        // Cancel Button: closes the dialog
        JButton cancelButton = new JButton("Cancel");
        cancelButton.addActionListener(e -> {
            // Close the dialog when cancel is clicked
            ((JDialog) SwingUtilities.getWindowAncestor(productDetailsPanel)).dispose();
        });

        // Save Button: saves the product details
        JButton saveButton = new JButton("Save");
        saveButton.addActionListener(e -> {
            String productId = idTextField.getText();
            String productName = nameField.getText();
            double productPrice = 0;

            // Check if the price field is valid
            try {
                productPrice = Double.parseDouble(priceField.getText());
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(null, "Invalid price entered. Please enter a valid number.");
                return;
            }

            String dateString = dateField.getText();  // Get the date as a string
            if (!dateString.isEmpty()) {
                try {
                    // Convert the string to a LocalDate object using DateTimeFormatter
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                    LocalDate expiryDate = LocalDate.parse(dateString, formatter);

                    // Now create and update the product
                    ProductService productService = new ProductService();
                    try {
                        // Update the existing product
                        PerishableProduct updatedProduct = new PerishableProduct(productId, productName, productPrice, expiryDate);
                        productService.updateProduct(updatedProduct);  // Update the product in the product service or database
                        JOptionPane.showMessageDialog(null, "Product updated successfully!");
                        // Optionally, dispose of the dialog if product is saved successfully
                        ((JDialog) SwingUtilities.getWindowAncestor(productDetailsPanel)).dispose();
                        refreshProductTable(table);
                    } catch (InvalidProductException ex) {
                        JOptionPane.showMessageDialog(null, "Failed to update product: " + ex.getMessage());
                    }
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(null, "Invalid expiry date format. Please use yyyy-MM-dd.");
                }
            }
        });

        buttonPanel.add(cancelButton);
        buttonPanel.add(saveButton);

        // Adding the productDetailsPanel and buttonPanel to the dialog
        JDialog productDialog = new JDialog();
        productDialog.setTitle("Edit Perishable Product");
        productDialog.setSize(400, 300);
        productDialog.setLocationRelativeTo(null);  // Center the dialog on the screen
        productDialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        productDialog.setLayout(new BorderLayout());
        productDialog.add(productDetailsPanel, BorderLayout.CENTER);
        productDialog.add(buttonPanel, BorderLayout.SOUTH);
        productDialog.setVisible(true);
    }

    // Open dialog for editing a Digital product
    private void openDigitalProductDialog(JTable table, int row) {
        // Create the dialog panel for digital product details
        JPanel productDetailsPanel = new JPanel();
        productDetailsPanel.setLayout(new GridLayout(5, 2)); // Adjusted for 5 fields (ID, Name, Price, URL, Vendor Name)

        JLabel idLabel = new JLabel("ID");
        JTextField idTextField = new JTextField(10);
        idTextField.setText((String) table.getModel().getValueAt(row, 0));  // Pre-fill the ID field (non-editable)
        idTextField.setEditable(false);  // Set to non-editable because ID shouldn't be changed
        productDetailsPanel.add(idLabel);
        productDetailsPanel.add(idTextField);

        JLabel nameLabel = new JLabel("Product Name:");
        JTextField nameField = new JTextField(15);
        nameField.setText((String) table.getModel().getValueAt(row, 1));  // Pre-fill with existing product name
        productDetailsPanel.add(nameLabel);
        productDetailsPanel.add(nameField);

        JLabel priceLabel = new JLabel("Price:");
        JTextField priceField = new JTextField(10);
        priceField.setText(String.valueOf((String) table.getModel().getValueAt(row, 3)));  // Pre-fill with existing product price
        productDetailsPanel.add(priceLabel);
        productDetailsPanel.add(priceField);

        JLabel urlLabel = new JLabel("URL:");
        JTextField urlField = new JTextField(15);
        urlField.setText(((String) table.getModel().getValueAt(row, 5))); // Pre-fill with existing product URL
        productDetailsPanel.add(urlLabel);
        productDetailsPanel.add(urlField);

        JLabel vendorNameLabel = new JLabel("Vendor:");
        JTextField vendorNameField = new JTextField(10);
        vendorNameField.setText(((String) table.getModel().getValueAt(row, 6))); // Pre-fill with existing vendor name
        productDetailsPanel.add(vendorNameLabel);
        productDetailsPanel.add(vendorNameField);

        // Panel for "Cancel" and "Save" buttons
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER));

        // Cancel Button: closes the dialog
        JButton cancelButton = new JButton("Cancel");
        cancelButton.addActionListener(e -> {
            // Close the dialog when cancel is clicked
            ((JDialog) SwingUtilities.getWindowAncestor(productDetailsPanel)).dispose();
        });

        // Save Button: saves the product details
        JButton saveButton = new JButton("Save");
        saveButton.addActionListener(e -> {
            String productId = idTextField.getText();
            String productName = nameField.getText();
            double productPrice = 0;

            // Check if the price field is valid
            try {
                productPrice = Double.parseDouble(priceField.getText());
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(null, "Invalid price entered. Please enter a valid number.");
                return;
            }

            String urlString = urlField.getText();
            String vendorName = vendorNameField.getText();

            // Now create and update the product
            ProductService productService = new ProductService();
            try {
                // Update the existing product
                DigitalProduct updatedProduct = new DigitalProduct(productId, productName, productPrice, urlString, vendorName);
                productService.updateProduct(updatedProduct);  // Update the product in the product service or database
                JOptionPane.showMessageDialog(null, "Product updated successfully!");
                // Optionally, dispose of the dialog if product is saved successfully
                ((JDialog) SwingUtilities.getWindowAncestor(productDetailsPanel)).dispose();
                refreshProductTable(table);
            } catch (InvalidProductException ex) {
                JOptionPane.showMessageDialog(null, "Failed to update product: " + ex.getMessage());
            }
        });

        buttonPanel.add(cancelButton);
        buttonPanel.add(saveButton);

        // Adding the productDetailsPanel and buttonPanel to the dialog
        JDialog productDialog = new JDialog();
        productDialog.setTitle("Edit Digital Product");
        productDialog.setSize(400, 300);
        productDialog.setLocationRelativeTo(null);  // Center the dialog on the screen
        productDialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        productDialog.setLayout(new BorderLayout());
        productDialog.add(productDetailsPanel, BorderLayout.CENTER);
        productDialog.add(buttonPanel, BorderLayout.SOUTH);
        productDialog.setVisible(true);
    }

    // Open dialog for editing a Bundle product
    private void openBundleProductDialog(JTable table, int row) {
        // Create the dialog panel for digital product details
        JPanel productDetailsPanel = new JPanel();
        productDetailsPanel.setLayout(new GridLayout(6, 2));  // Adjusted layout to fit all fields

        // Pre-fill the ID field (disable editing)
        JLabel idLabel = new JLabel("ID");
        JTextField idTextField = new JTextField(10);
        idTextField.setText((String) table.getModel().getValueAt(row, 0));  // Pre-fill with existing product ID
        idTextField.setEditable(false);  // Set to non-editable because ID shouldn't be changed
        productDetailsPanel.add(idLabel);
        productDetailsPanel.add(idTextField);

        // Pre-fill with existing product name
        JLabel nameLabel = new JLabel("Product Name:");
        JTextField nameField = new JTextField(15);
        nameField.setText((String) table.getModel().getValueAt(row, 1));  // Pre-fill with existing product name
        productDetailsPanel.add(nameLabel);
        productDetailsPanel.add(nameField);

        // Pre-fill with product price
        JLabel priceLabel = new JLabel("Price:");
        JTextField priceField = new JTextField(10);
        priceField.setText(String.valueOf(table.getModel().getValueAt(row, 3)));  // Pre-fill with product price
        productDetailsPanel.add(priceLabel);
        productDetailsPanel.add(priceField);

        // URL field for Digital Product
        JLabel urlLabel = new JLabel("URL:");
        JTextField urlField = new JTextField(15);
        urlField.setText((String) table.getModel().getValueAt(row, 5));  // Pre-fill with URL
        productDetailsPanel.add(urlLabel);
        productDetailsPanel.add(urlField);

        // Vendor Name field for Digital Product
        JLabel vendorNameLabel = new JLabel("Vendor:");
        JTextField vendorNameField = new JTextField(10);
        vendorNameField.setText((String) table.getModel().getValueAt(row, 6));  // Pre-fill with vendor name
        productDetailsPanel.add(vendorNameLabel);
        productDetailsPanel.add(vendorNameField);

        // Panel for "Cancel" and "Save" buttons
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER));

        // Cancel Button: closes the dialog
        JButton cancelButton = new JButton("Cancel");
        cancelButton.addActionListener(e -> {
            // Close the dialog when cancel is clicked
            ((JDialog) SwingUtilities.getWindowAncestor(productDetailsPanel)).dispose();
        });

        // Save Button: saves the product details
        JButton saveButton = new JButton("Save");
        saveButton.addActionListener(e -> {
            String productId = idTextField.getText();
            String productName = nameField.getText();
            double productPrice = 0;

            // Check if the price field is valid
            try {
                productPrice = Double.parseDouble(priceField.getText());
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(null, "Invalid price entered. Please enter a valid number.");
                return;
            }

            String urlString = urlField.getText();
            String vendorName = vendorNameField.getText();

            ProductService productService = new ProductService();
            try {
                // Create updated Digital Product and update it
                DigitalProduct updatedProduct = new DigitalProduct(productId, productName, productPrice, urlString, vendorName);
                productService.updateProduct(updatedProduct);  // Update the product in the product service or database
                JOptionPane.showMessageDialog(null, "Product updated successfully!");

                // Optionally, dispose of the dialog if product is saved successfully
                ((JDialog) SwingUtilities.getWindowAncestor(productDetailsPanel)).dispose();
                refreshProductTable(table);
            } catch (InvalidProductException ex) {
                JOptionPane.showMessageDialog(null, "Failed to update product: " + ex.getMessage());
            }
        });

        buttonPanel.add(cancelButton);
        buttonPanel.add(saveButton);

        // Adding the productDetailsPanel and buttonPanel to the dialog
        JDialog productDialog = new JDialog();
        productDialog.setTitle("Edit Digital Product");
        productDialog.setSize(400, 300);
        productDialog.setLocationRelativeTo(null);  // Center the dialog on the screen
        productDialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        productDialog.setLayout(new BorderLayout());
        productDialog.add(productDetailsPanel, BorderLayout.CENTER);
        productDialog.add(buttonPanel, BorderLayout.SOUTH);
        productDialog.setVisible(true);
    }

    // Open dialog for editing a NonPerishable product
    private void openNonPerishableProductDialog(JTable table, int row) {
        // Create the dialog panel for non-perishable product details
        JPanel productDetailsPanel = new JPanel();
        productDetailsPanel.setLayout(new GridLayout(3, 2)); // Adjusted for 3 fields (ID, Name, Price)

        JLabel idLabel = new JLabel("ID");
        JTextField idTextField = new JTextField(10);
        idTextField.setText((String) table.getModel().getValueAt(row, 0));  // Pre-fill the ID field (non-editable)
        idTextField.setEditable(false);  // Set to non-editable because ID shouldn't be changed
        productDetailsPanel.add(idLabel);
        productDetailsPanel.add(idTextField);

        JLabel nameLabel = new JLabel("Product Name:");
        JTextField nameField = new JTextField(15);
        nameField.setText((String) table.getModel().getValueAt(row, 1));  // Pre-fill with existing product name
        productDetailsPanel.add(nameLabel);
        productDetailsPanel.add(nameField);

        JLabel priceLabel = new JLabel("Price:");
        JTextField priceField = new JTextField(10);
        priceField.setText((String) table.getModel().getValueAt(row, 3));  // Pre-fill with existing product price
        productDetailsPanel.add(priceLabel);
        productDetailsPanel.add(priceField);

        // Panel for "Cancel" and "Save" buttons
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER));

        // Cancel Button: closes the dialog
        JButton cancelButton = new JButton("Cancel");
        cancelButton.addActionListener(e -> {
            // Close the dialog when cancel is clicked
            ((JDialog) SwingUtilities.getWindowAncestor(productDetailsPanel)).dispose();
        });

        // Save Button: saves the product details
        JButton saveButton = new JButton("Save");
        saveButton.addActionListener(e -> {
            String productId = idTextField.getText();
            String productName = nameField.getText();
            double productPrice = 0;

            // Check if the price field is valid
            try {
                productPrice = Double.parseDouble(priceField.getText());
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(null, "Invalid price entered. Please enter a valid number.");
                return;
            }

            // Now create and update the product
            ProductService productService = new ProductService();
            try {
                // Update the existing product
                NonPerishableProduct updatedProduct = new NonPerishableProduct(productId, productName, productPrice);
                productService.updateProduct(updatedProduct);  // Update the product in the product service or database
                JOptionPane.showMessageDialog(null, "Product updated successfully!");
                // Optionally, dispose of the dialog if product is saved successfully
                ((JDialog) SwingUtilities.getWindowAncestor(productDetailsPanel)).dispose();
                refreshProductTable(table);
            } catch (InvalidProductException ex) {
                JOptionPane.showMessageDialog(null, "Failed to update product: " + ex.getMessage());
            }
        });

        buttonPanel.add(cancelButton);
        buttonPanel.add(saveButton);

        // Adding the productDetailsPanel and buttonPanel to the dialog
        JDialog productDialog = new JDialog();
        productDialog.setTitle("Edit Non-Perishable Product");
        productDialog.setSize(400, 300);
        productDialog.setLocationRelativeTo(null);  // Center the dialog on the screen
        productDialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        productDialog.setLayout(new BorderLayout());
        productDialog.add(productDetailsPanel, BorderLayout.CENTER);
        productDialog.add(buttonPanel, BorderLayout.SOUTH);
        productDialog.setVisible(true);
    }
}
