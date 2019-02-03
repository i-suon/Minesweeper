package com.minesweeper;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;

import static javax.swing.SwingConstants.CENTER;


public class Minesweeper extends JFrame {
    private JLabel statusbar;

    public Minesweeper() {
        initUI();
    }

    private void initUI() {
        Border raised = BorderFactory.createRaisedBevelBorder();
        Border lowered = BorderFactory.createLoweredBevelBorder();
        statusbar = new JLabel("0");
        add(statusbar, BorderLayout.NORTH);
        statusbar.setHorizontalAlignment(CENTER);
        statusbar.setBorder(BorderFactory.createCompoundBorder(raised, lowered));
        add(new Board(this));

        setResizable(false);
        pack();
        setTitle("Minesweeper");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
    }

    public JLabel getStatusBar() {
        return statusbar;
    }

    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            JFrame game = new Minesweeper();
            game.setVisible(true);
        });
    }
}
