package org.example.sasalele_pos.Dashboard.Akun;

import org.example.sasalele_pos.database.UserDAO;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

import static org.example.sasalele_pos.Dashboard.Akun.AkunPanel.refreshUserTable;

public class AccountCellEditor extends DefaultCellEditor {
    private JPanel panel;
    private JButton editButton, deleteButton;
    private JTable table;

    public AccountCellEditor(JCheckBox checkBox) {
        super(checkBox);
        panel = new JPanel(new GridBagLayout());

        editButton = new JButton("Edit");
        deleteButton = new JButton("Delete");

        editButton.setPreferredSize(new Dimension(80, 30));
        deleteButton.setPreferredSize(new Dimension(80, 30));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(0, 5, 0, 5);
        gbc.gridx = 0;
        panel.add(editButton, gbc);
        gbc.gridx = 1;
        panel.add(deleteButton, gbc);

        // Menangani klik tombol Edit
        editButton.addActionListener(e -> {
            int row = table.getSelectedRow();
            String username = table.getValueAt(row, 0).toString();
            String role = table.getValueAt(row, 1).toString();

            // Menampilkan form edit username dan role
            JTextField usernameField = new JTextField(username);

            // Dropdown (JComboBox) untuk role
            String[] roles = {"User", "Admin"};
            JComboBox<String> roleComboBox = new JComboBox<>(roles);
            roleComboBox.setSelectedItem(role);  // Menetapkan role yang ada sebagai pilihan yang terpilih

            // Menyusun form input
            Object[] message = {
                    "Username:", usernameField,
                    "Role:", roleComboBox
            };

            int option = JOptionPane.showConfirmDialog(panel, message, "Edit Akun", JOptionPane.OK_CANCEL_OPTION);

            if (option == JOptionPane.OK_OPTION) {
                // Ambil data baru dari input form
                String newUsername = usernameField.getText();
                String newRole = (String) roleComboBox.getSelectedItem(); // Ambil nilai yang dipilih dari combo box

                // Update database
                boolean success = new UserDAO().updateUser(username, newUsername, newRole);

                if (success) {
                    refreshUserTable(table);
                    JOptionPane.showMessageDialog(panel, "Akun berhasil diperbarui.");
                } else {
                    JOptionPane.showMessageDialog(panel, "Gagal memperbarui akun.");
                }
            }

            fireEditingStopped();
        });

        // Menangani klik tombol Delete
        deleteButton.addActionListener(e -> {
            int result = JOptionPane.showConfirmDialog(panel, "Yakin hapus akun?", "Konfirmasi", JOptionPane.YES_NO_OPTION);
            if (result == JOptionPane.YES_OPTION) {
                int row = table.getSelectedRow();
                String username = table.getValueAt(row, 0).toString();

                // Hapus dari database
                boolean success = new UserDAO().deleteUser(username);

                if (success) {
                    refreshUserTable(table);
                    JOptionPane.showMessageDialog(panel, "Akun berhasil dihapus.");
                } else {
                    JOptionPane.showMessageDialog(panel, "Gagal menghapus akun.");
                }
            }
            fireEditingStopped();
        });
    }

    @Override
    public Component getTableCellEditorComponent(JTable table, Object value,
                                                 boolean isSelected, int row, int column) {
        this.table = table; // Set reference to table
        return panel;
    }

    @Override
    public Object getCellEditorValue() {
        return "";
    }
}
