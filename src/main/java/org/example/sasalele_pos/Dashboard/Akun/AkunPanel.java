package org.example.sasalele_pos.Dashboard.Akun;

import org.example.sasalele_pos.database.UserDAO;
import org.example.sasalele_pos.model.User;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class AkunPanel extends JPanel {
    static JTable table;

    User currentUser;

    public AkunPanel(User currentUser) {
        this.currentUser = currentUser;
        setLayout(new BorderLayout());

        JLabel titleLabel = new JLabel("Akun", JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 30));
        titleLabel.setForeground(Color.BLACK);
        add(titleLabel, BorderLayout.NORTH);

        // Center Panel
        JPanel centerPanel = new JPanel(new BorderLayout());
        JTable centerTable = createAccountTable();
        JScrollPane centerScrollPane = new JScrollPane();
        centerScrollPane.setViewportView(centerTable);
        centerPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        centerPanel.add(centerScrollPane, BorderLayout.CENTER);
        add(centerPanel, BorderLayout.CENTER);

        table = centerTable;
    }

    private JTable createAccountTable() {
        List<User> users = new UserDAO().getAllUsers();
        String[][] data = new String[users.size()][3];
        String[] accountTableColumns = {"Nama Pengguna", "Role", "Aksi"};

        for (int i = 0; i < users.size(); i++) {
            User user = users.get(i);
            data[i][0] = user.getUsername();
            data[i][1] = user.getRole();
            data[i][2] = "Aksi";
        }

        // Create the table model and JTable
        DefaultTableModel accountTableModel = new DefaultTableModel(data, accountTableColumns);
        JTable accountTable = new JTable(accountTableModel);
        accountTable.setRowHeight(40);

        accountTable.getColumn("Aksi").setCellRenderer(new AccountButtonRenderer());
        accountTable.getColumn("Aksi").setCellEditor(new AccountCellEditor(new JCheckBox()));

        return accountTable;
    }

    public static void refreshUserTable(JTable table) {
        List<User> users = new UserDAO().getAllUsers();

        String[][] data = new String[users.size()][3];
        String[] accountTableColumns = {"Nama Pengguna", "Role", "Aksi"};
        for (int i = 0; i < users.size(); i++) {
            User user = users.get(i);
            data[i][0] = user.getUsername();
            data[i][1] = user.getRole();
            data[i][2] = "Aksi";
        }

        DefaultTableModel accountTableModel = new DefaultTableModel(data, accountTableColumns);
        table.setModel(accountTableModel);
        table.setRowHeight(40);

        table.getColumn("Aksi").setCellRenderer(new AccountButtonRenderer());
        table.getColumn("Aksi").setCellEditor(new AccountCellEditor(new JCheckBox()));
    }

    public static JTable getAccountTable() {
        return table;
    }
}
