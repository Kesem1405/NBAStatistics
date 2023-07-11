package org.example;

import javax.swing.*;

public class Window extends JFrame {

    public Window (int width, int height) {
        this.setSize(width, height);
        this.setResizable(false);
        this.setLayout(null);
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.add(new MainScene(0, 0, width, height));
        this.setVisible(true);
    }
}
