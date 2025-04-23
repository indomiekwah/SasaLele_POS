package org.example.sasalele_pos.Dashboard.Log;

import org.example.sasalele_pos.Dashboard.Produk.ProdukPanel;
import org.example.sasalele_pos.database.LogDAO;
import org.example.sasalele_pos.model.Log;
import org.example.sasalele_pos.model.User;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class LogPanel extends JPanel {
    static JTable table;
    static User currentUser;

    public LogPanel(User currentUser) {
        LogPanel.currentUser = currentUser;

        setLayout(new BorderLayout());

        JLabel titleLabel = new JLabel("Log", JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 30));
        titleLabel.setForeground(Color.BLACK);
        add(titleLabel, BorderLayout.NORTH);

        // Center Panel
        JPanel centerPanel = new JPanel(new BorderLayout());
        JTable centerTable = createLogTable();
        JScrollPane centerScrollPane = new JScrollPane();
        centerScrollPane.setViewportView(centerTable);
        centerPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        centerPanel.add(centerScrollPane, BorderLayout.CENTER);
        add(centerPanel, BorderLayout.CENTER);
        table = centerTable;
    }

    private JTable createLogTable() {
        List<Log> logs = new LogDAO().getAllLogs();

        String[][] data = new String[logs.size()][4];
        String[] logTableColumns = {"Timestamp", "Tipe", "Deskripsi Log", "Aksi"};

        for (int i = 0; i < logs.size(); i++) {
            data[i][0] = logs.get(i).getTimestamp().toString();
            data[i][1] = logs.get(i).getType();
            data[i][2] = logs.get(i).getDescription();
            data[i][3] = "Aksi";
        }

        // Create the table model and JTable
        DefaultTableModel logTableModel = new DefaultTableModel(data, logTableColumns);
        JTable logTable = new JTable(logTableModel);

        logTable.getColumn("Aksi").setCellRenderer(new LogButtonRenderer());
        logTable.getColumn("Aksi").setCellEditor(new LogCellEditor(new JTextField(), currentUser));

        return logTable;
    }

    public static void refreshLogTable(JTable table) {
        List<Log> logs = new LogDAO().getAllLogs();

        String[][] data = new String[logs.size()][4];
        String[] logTableColumns = {"Timestamp", "Tipe", "Deskripsi Log", "Aksi"};

        for (int i = 0; i < logs.size(); i++) {
            data[i][0] = logs.get(i).getTimestamp().toString();
            data[i][1] = logs.get(i).getType();
            data[i][2] = logs.get(i).getDescription();
            data[i][3] = "Aksi";
        }

        DefaultTableModel logTableModel = new DefaultTableModel(data, logTableColumns);
        table.setModel(logTableModel);

        table.getColumn("Aksi").setCellRenderer(new LogButtonRenderer());
        table.getColumn("Aksi").setCellEditor(new LogCellEditor(new JTextField(), currentUser));
    }

    public static JTable getLogTable() {
        return table;
    }
}
