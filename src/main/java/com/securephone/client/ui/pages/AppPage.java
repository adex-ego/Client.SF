package com.securephone.client.ui.pages;

import javax.swing.*;
import java.awt.*;
import com.securephone.client.ui.UIManager;
import com.securephone.client.ui.frames.MainFrame;

/**
 * AppPage - Page principale de l'application après connexion
 * Page blanche pour le moment
 */
public class AppPage extends JPanel {
    
    private MainFrame mainFrame;
    
    public AppPage(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
        initUI();
    }
    
    private void initUI() {
        setLayout(new BorderLayout());
        setBackground(UIManager.getBackground());
    }
    
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        setBackground(UIManager.getBackground());
    }
}
