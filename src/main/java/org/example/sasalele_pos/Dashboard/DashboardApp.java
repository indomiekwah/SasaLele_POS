package org.example.sasalele_pos.Dashboard;

import org.example.sasalele_pos.Dashboard.Akun.AkunPanel;
import org.example.sasalele_pos.Dashboard.Log.LogPanel;
import org.example.sasalele_pos.Dashboard.Produk.ProdukPanel;
import org.example.sasalele_pos.Dashboard.Transaksi.TransaksiPanel;
import org.example.sasalele_pos.MainApp;
import org.example.sasalele_pos.model.User;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class DashboardApp extends JPanel {
    static User currentUser;

    private final JPanel sidebarPanel, mainPanel;
    private CardLayout cardLayout;  // To switch between different content in the main panel


    public DashboardApp(User currentUser) {
        DashboardApp.currentUser = currentUser;
        setLayout(new BorderLayout());

        // Create Sidebar and Main content area
        sidebarPanel = createSidebar();
        mainPanel = createMainPanel();

        // Add Sidebar and Main Panel to the frame
        add(sidebarPanel, BorderLayout.WEST);
        add(mainPanel, BorderLayout.CENTER);
    }

    private JPanel createSidebar() {
        JPanel sidebar = new JPanel();
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));  // Stack components vertically
        sidebar.setPreferredSize(new Dimension(200, getHeight()));
        sidebar.setBackground(new Color(34, 40, 49));  // Sidebar background color

        // Top label "Sasa - Lele"
        JLabel topLabel = new JLabel("Sasa - Lele", JLabel.CENTER);
        topLabel.setFont(new Font("Serif", Font.BOLD, 20));
        topLabel.setForeground(Color.WHITE);
        topLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        sidebar.add(topLabel);
        sidebar.add(Box.createVerticalStrut(20)); // Space between top label and buttons

        // Add filler to push buttons to the center vertically
        sidebar.add(Box.createVerticalGlue());

        // Buttons for Sidebar sections
        String[] buttonLabels = {"Transaksi", "Produk", "Akun", "Log"};
        for (String label : buttonLabels) {
            JButton button = new JButton(label);
            button.setFont(new Font("Arial", Font.PLAIN, 18));
            button.setAlignmentX(Component.CENTER_ALIGNMENT);
            button.setPreferredSize(new Dimension(180, 40));

            button.addActionListener(new SidebarButtonListener());

            button.setBackground(Color.WHITE);  // Default background color (white)
            button.setForeground(Color.BLACK);  // Text color
            sidebar.add(button);
            sidebar.add(Box.createVerticalStrut(10)); // Space between buttons
        }

        // Add filler to push buttons to the center vertically (if needed)
        sidebar.add(Box.createVerticalGlue());

        // Logout button (to go back to the login screen)
        JButton logoutButton = new JButton("Logout");
        logoutButton.setFont(new Font("Arial", Font.PLAIN, 18));
        logoutButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        logoutButton.setPreferredSize(new Dimension(180, 40));

        logoutButton.addActionListener(e -> Logout(logoutButton));

        sidebar.add(logoutButton);
        sidebar.add(Box.createVerticalStrut(10));

        return sidebar;
    }

    private JPanel createMainPanel() {
        cardLayout = new CardLayout();
        JPanel panel = new JPanel(cardLayout);

        // Create panels for each section in the main frame
        TransaksiPanel transaksiPanel = new TransaksiPanel(currentUser);
        ProdukPanel produkPanel = new ProdukPanel();
        AkunPanel akunPanel = new AkunPanel();
        LogPanel logPanel = new LogPanel();

        // Add all panels to the main panel (card layout)
        panel.add(transaksiPanel, "Transaksi");
        panel.add(produkPanel, "Produk");
        panel.add(akunPanel, "Akun");
        panel.add(logPanel, "Log");

        // Initially show Transaksi panel
        cardLayout.show(panel, "Transaksi");
        highlightButton("Transaksi");

        return panel;
    }

    // ActionListener for sidebar buttons
    private class SidebarButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String command = e.getActionCommand();
            // Reset all button colors to default
            resetButtonColors();

            // Switch between the panels based on button clicked
            switch (command) {
                case "Produk":
                    cardLayout.show(mainPanel, "Produk");
                    highlightButton("Produk");
                    break;
                case "Akun":
                    cardLayout.show(mainPanel, "Akun");
                    highlightButton("Akun");
                    break;
                case "Log":
                    cardLayout.show(mainPanel, "Log");
                    highlightButton("Log");
                    break;
                default:
                    cardLayout.show(mainPanel, "Transaksi");
                    highlightButton("Transaksi");
                    break;
            }
        }
    }

    // Method to reset button colors (to default)
    private void resetButtonColors() {
        for (Component comp : sidebarPanel.getComponents()) {
            if (comp instanceof JButton button) {
                button.setBackground(Color.WHITE);  // Set to default white color
                button.setForeground(Color.BLACK);  // Optional: Change text color to white for better visibility
            }
        }
    }

    // Highlight the active button in the sidebar (blue for active)
    private void highlightButton(String panelName) {
        for (Component comp : sidebarPanel.getComponents()) {
            if (comp instanceof JButton button) {
                if (button.getText().equals(panelName)) {
                    button.setBackground(new Color(54, 137, 209));  // Blue for active button
                    button.setForeground(Color.WHITE);  // Optional: Change text color to white for better visibility
                }
            }
        }
    }

    // Method to return to the login screen
    private void Logout(JButton button) {
        // Clear session or user-related data (e.g., currentUser)
        currentUser = null; // Assuming you store the logged-in user in currentUser

        // Optionally, display a message that the user has logged out
        JOptionPane.showMessageDialog(null, "You have successfully logged out!");

        // Close the current DashboardApp window
        Window win = SwingUtilities.getWindowAncestor(button);
        if (win != null) {
            win.dispose(); // Close the DashboardApp window
        }

        // Show the login screen (MainApp or login window)
        new MainApp().setVisible(true); // Assuming MainApp is your login screen
    }
}
