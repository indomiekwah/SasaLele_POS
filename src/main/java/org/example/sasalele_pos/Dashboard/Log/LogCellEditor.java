package org.example.sasalele_pos.Dashboard.Log;

import org.example.sasalele_pos.exceptions.InvalidTransactionException;
import org.example.sasalele_pos.model.User;
import org.example.sasalele_pos.services.TransactionService;

import javax.swing.*;
import java.awt.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LogCellEditor extends DefaultCellEditor {
    private String label;
    private final User currentUser;

    public LogCellEditor(JTextField checkBox, User currentUser) {
        super(checkBox);
        this.currentUser = currentUser;
    }

    @Override
    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
        JButton refundButton = new JButton("Refund");
        refundButton.addActionListener(e -> {
            String desc = table.getModel().getValueAt(row, 2).toString();

            String regex = "(TX-\\d+).*(\\d+\\.\\d+)";
            Pattern pattern = Pattern.compile(regex);
            Matcher matcher = pattern.matcher(desc);

            if (matcher.find()) {
                String transactionId = matcher.group(1);

                TransactionService transactionService = new TransactionService();

                try {
                    transactionService.processRefund(transactionId, currentUser.getUsername());
                } catch (InvalidTransactionException ex) {
                    throw new RuntimeException(ex);
                }
            } else {
                System.out.println("No match found");
            }
        });

        JPanel refundPanel = new JPanel();
        refundPanel.add(refundButton);
        return refundPanel;
    }

    @Override
    public Object getCellEditorValue() {
        return label;
    }
}
