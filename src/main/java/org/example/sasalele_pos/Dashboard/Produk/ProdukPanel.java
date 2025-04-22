package org.example.sasalele_pos.Dashboard.Produk;

import javax.swing.*;
import java.awt.*;

public class ProdukPanel extends JPanel {

    public ProdukPanel() {
        setLayout(new BorderLayout());

        JLabel titleLabel = new JLabel("Produk", JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 30));
        titleLabel.setForeground(Color.BLACK);
        add(titleLabel, BorderLayout.NORTH);
    }
}
