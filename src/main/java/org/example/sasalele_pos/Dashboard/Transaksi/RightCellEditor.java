package org.example.sasalele_pos.Dashboard.Transaksi;

import org.example.sasalele_pos.model.CartItem;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class RightCellEditor extends DefaultCellEditor {
    private String label;
    private JTable table;
    private List<CartItem> cartItems;

    public RightCellEditor(JCheckBox checkBox, List<CartItem> cartItems) {
        super(checkBox);
        this.cartItems = cartItems;
    }

    @Override
    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
        this.table = table;

        // Create action buttons with text labels
        JButton decreaseButton = new JButton("<-");
        JButton deleteButton = new JButton("D");
        JButton increaseButton = new JButton("->");

        // Set action listeners for buttons
        decreaseButton.addActionListener(e -> handleActionButtonClick(row, "decrease", cartItems));
        deleteButton.addActionListener(e -> handleActionButtonClick(row, "delete", cartItems));
        increaseButton.addActionListener(e -> handleActionButtonClick(row, "increase", cartItems));

        // Add buttons to a panel
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(decreaseButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(increaseButton);

        return buttonPanel;
    }

    private void handleActionButtonClick(int row, String action, List<CartItem> cartItems) {
        DefaultTableModel model = (DefaultTableModel) table.getModel();
        int qty = 0;

        // Ensure the row exists before proceeding
        if (row < 0 || row >= model.getRowCount()) {
            System.out.println("Invalid row index: " + row);
            return;  // Exit if the row is invalid
        }

        // Get current qty as Integer, not String, to avoid casting issues
        try {
            qty = Integer.parseInt((String) model.getValueAt(row, 2));  // Get current qty as Integer
        } catch (NumberFormatException e) {
            System.out.println("Error: " + e.getMessage());
            return;  // Exit if there's an issue with converting qty to Integer
        }

        switch (action) {
            case "decrease" -> {
                if (qty > 0) {
                    model.setValueAt(String.valueOf(qty - 1), row, 2);  // Decrease qty in the table

                    CartItem cartItem = cartItems.get(row);  // Get the CartItem from cartItems list at the same row
                    int newQuantity = cartItem.getQuantity() - 1;  // Decrease the quantity by 1

                    // Set the new quantity, ensuring it's at least 1
                    if (newQuantity >= 1) {
                        cartItem.setQuantity(newQuantity);  // Update the quantity of the CartItem
                    } else {
                        // Optionally, handle if the quantity is 0 or less (e.g., remove from cart)
                        cartItems.remove(row);  // Remove the item from the cart if quantity is zero or less
                        model.removeRow(row);  // Remove the row from the table
                    }
                }
            }
            case "increase" -> {
                model.setValueAt(String.valueOf(qty + 1), row, 2); // Increase qty

                CartItem cartItem = cartItems.get(row);
                int newQuantity = cartItem.getQuantity() + 1;

                cartItem.setQuantity(newQuantity);
            }
            case "delete" -> {
                cartItems.remove(row);  // Remove the item from the cart if quantity is zero or less
                model.removeRow(row);  // Remove the row from the table
            }
        }

        // Refresh the table after deletion to avoid invalid index reference
        table.revalidate();
        table.repaint();
    }

    @Override
    public Object getCellEditorValue() {
        return label;
    }
}
