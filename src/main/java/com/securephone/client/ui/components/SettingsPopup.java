package com.securephone.client.ui.components;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import com.securephone.client.ui.UIManager;

/**
 * SettingsPopup - Popup pour les paramètres (thème + logout)
 */
public class SettingsPopup extends JDialog {
    
    private Runnable onThemeToggle;
    private Runnable onLogout;
    
    public SettingsPopup(Frame owner) {
        super(owner, "Settings", false);
        initUI();
    }
    
    private void initUI() {
        setDefaultCloseOperation(JDialog.HIDE_ON_CLOSE);
        setSize(300, 200);
        setLocationRelativeTo(getOwner());
        setResizable(false);
        
        // Panel principal
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBackground(UIManager.getBackground());
        mainPanel.setBorder(new EmptyBorder(15, 15, 15, 15));
        
        // Header
        JLabel headerLabel = new JLabel("Settings");
        headerLabel.setFont(new Font("Arial", Font.BOLD, 16));
        headerLabel.setForeground(UIManager.getOnBackground());
        mainPanel.add(headerLabel);
        mainPanel.add(Box.createVerticalStrut(15));
        
        // Theme toggle
        JPanel themePanel = new JPanel(new BorderLayout());
        themePanel.setBackground(UIManager.getSurface());
        themePanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        
        JLabel themeLabel = new JLabel("🌓 Theme");
        themeLabel.setFont(new Font("Arial", Font.PLAIN, 13));
        themeLabel.setForeground(UIManager.getOnSurface());
        themePanel.add(themeLabel, BorderLayout.WEST);
        
        JButton themeToggleButton = new JButton("Toggle");
        themeToggleButton.setBackground(UIManager.getPrimary());
        themeToggleButton.setForeground(UIManager.getOnPrimary());
        themeToggleButton.setBorderPainted(false);
        themeToggleButton.setFocusPainted(false);
        themeToggleButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        themeToggleButton.setPreferredSize(new Dimension(80, 25));
        themeToggleButton.addActionListener(e -> {
            if (onThemeToggle != null) {
                onThemeToggle.run();
            }
        });
        themePanel.add(themeToggleButton, BorderLayout.EAST);
        
        mainPanel.add(themePanel);
        mainPanel.add(Box.createVerticalStrut(10));
        
        // Logout
        JPanel logoutPanel = new JPanel(new BorderLayout());
        logoutPanel.setBackground(UIManager.getSurface());
        logoutPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        
        JLabel logoutLabel = new JLabel("🚪 Logout");
        logoutLabel.setFont(new Font("Arial", Font.PLAIN, 13));
        logoutLabel.setForeground(UIManager.getOnSurface());
        logoutPanel.add(logoutLabel, BorderLayout.WEST);
        
        JButton logoutButton = new JButton("Exit");
        logoutButton.setBackground(new Color(244, 67, 54)); // Red
        logoutButton.setForeground(Color.WHITE);
        logoutButton.setBorderPainted(false);
        logoutButton.setFocusPainted(false);
        logoutButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        logoutButton.setPreferredSize(new Dimension(80, 25));
        logoutButton.addActionListener(e -> {
            if (onLogout != null) {
                onLogout.run();
            }
        });
        logoutPanel.add(logoutButton, BorderLayout.EAST);
        
        mainPanel.add(logoutPanel);
        mainPanel.add(Box.createVerticalGlue());
        
        setContentPane(mainPanel);
    }
    
    public void setOnThemeToggle(Runnable callback) {
        this.onThemeToggle = callback;
    }
    
    public void setOnLogout(Runnable callback) {
        this.onLogout = callback;
    }
}
