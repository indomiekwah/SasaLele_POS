package org.example.sasalele_pos.Dashboard.Produk;

import org.example.sasalele_pos.model.User;

import javax.swing.*;
import java.awt.*;

public class ShowAddDialog extends JDialog {
    public static User currentUser;

    public ShowAddDialog(User currentUser) {
        ShowAddDialog.currentUser = currentUser;

        // Create the dialog for selecting product type
        JDialog productTypeDialog = new JDialog((Frame) null, "Select Product Type", true);
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
        JLabel tipeProduk = new JLabel("Pilih Tipe Produk ");
        tipeProduk.setFont(new Font("Arial", Font.BOLD, 14));
        tipeProduk.setForeground(Color.BLACK);
        gbc.gridx = 0;
        centerLayout.add(tipeProduk, gbc);

        // Dropdown for selecting product type
        String[] productTypes = {"PERISHABLE", "NON_PERISHABLE", "BUNDLE", "DIGITAL"};
        JComboBox<String> tipeProdukBox = new JComboBox<>(productTypes);
        gbc.gridx = 1;
        centerLayout.add(tipeProdukBox, gbc);

        // gbc Y = 1
        gbc.gridy = 1;
        JPanel buttonLayout = new JPanel();
        JButton okButton = new JButton("OK");
        okButton.setFont(new Font("Arial", Font.BOLD, 14));
        okButton.setForeground(Color.BLACK);
        buttonLayout.add(okButton);
        gbc.gridx = 0;
        gbc.gridwidth = 2;
        centerLayout.add(buttonLayout, gbc);

        okButton.addActionListener(e -> {
            String selectedType = (String) tipeProdukBox.getSelectedItem();
            assert selectedType != null;
            productTypeDialog.dispose();  // Close the product type dialog
            ShowDetailDialog showDetailDialog = new ShowDetailDialog(currentUser, selectedType);
            showDetailDialog.setVisible(true);
        });

        // Show the productTypeDialog
        productTypeDialog.setVisible(true);
    }
}
