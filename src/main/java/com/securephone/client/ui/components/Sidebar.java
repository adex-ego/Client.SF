package com.securephone.client.ui.components;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionListener;
import com.securephone.client.ui.UIManager;

/**
 * Sidebar - Barre latérale fixe avec 4 boutons principaux
 * - Home (chats)
 * - Notifications (cloche + badge)
 * - Contacts (recherche + ajout)
 * - Settings (thème + logout)
 */
public class Sidebar extends JPanel {
    
    private JButton homeButton;
    private JButton notificationsButton;
    private JButton contactsButton;
    private JButton settingsButton;
    private JLabel notificationBadge;
    
    public Sidebar() {
        initUI();
    }
    
    private void initUI() {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBackground(UIManager.getPrimary());
        setPreferredSize(new Dimension(70, 0));
        setBorder(BorderFactory.createMatteBorder(0, 0, 0, 1, UIManager.getBorder()));
        
        // Espacement haut
        add(Box.createVerticalStrut(10));
        
        // Bouton Home
        homeButton = createButton("🏠", "Home");
        add(homeButton);
        add(Box.createVerticalStrut(10));
        
        // Bouton Notifications
        JPanel notifPanel = new JPanel(new BorderLayout());
        notifPanel.setBackground(UIManager.getPrimary());
        notifPanel.setMaximumSize(new Dimension(50, 50));
        
        notificationsButton = createButton("🔔", "Notifications");
        notifPanel.add(notificationsButton, BorderLayout.CENTER);
        
        // Badge de notification
        notificationBadge = new JLabel("0");
        notificationBadge.setFont(new Font("Arial", Font.BOLD, 10));
        notificationBadge.setForeground(Color.WHITE);
        notificationBadge.setBackground(Color.RED);
        notificationBadge.setOpaque(true);
        notificationBadge.setHorizontalAlignment(SwingConstants.CENTER);
        notificationBadge.setVerticalAlignment(SwingConstants.CENTER);
        notificationBadge.setPreferredSize(new Dimension(18, 18));
        notificationBadge.setBorder(BorderFactory.createLineBorder(Color.RED, 1));
        notifPanel.add(notificationBadge, BorderLayout.EAST);
        
        add(notifPanel);
        add(Box.createVerticalStrut(10));
        
        // Bouton Contacts
        contactsButton = createButton("👤", "Contacts");
        add(contactsButton);
        add(Box.createVerticalStrut(10));
        
        // Espacement flexible au milieu
        add(Box.createVerticalGlue());
        
        // Bouton Settings (en bas)
        settingsButton = createButton("⚙️", "Settings");
        add(settingsButton);
        add(Box.createVerticalStrut(10));
    }
    
    private JButton createButton(String icon, String tooltip) {
        JButton button = new JButton(icon);
        button.setFont(new Font("Arial", Font.PLAIN, 20));
        button.setBackground(UIManager.getPrimary());
        button.setForeground(UIManager.getOnPrimary());
        button.setBorderPainted(false);
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setPreferredSize(new Dimension(50, 50));
        button.setMaximumSize(new Dimension(50, 50));
        button.setToolTipText(tooltip);
        
        // Hover effect
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent e) {
                button.setBackground(UIManager.getPrimaryVariant());
            }
            
            @Override
            public void mouseExited(java.awt.event.MouseEvent e) {
                button.setBackground(UIManager.getPrimary());
            }
        });
        
        return button;
    }
    
    // Getters pour les boutons
    public JButton getHomeButton() {
        return homeButton;
    }
    
    public JButton getNotificationsButton() {
        return notificationsButton;
    }
    
    public JButton getContactsButton() {
        return contactsButton;
    }
    
    public JButton getSettingsButton() {
        return settingsButton;
    }
    
    public void setNotificationCount(int count) {
        if (count > 0) {
            notificationBadge.setText(String.valueOf(count));
            notificationBadge.setVisible(true);
        } else {
            notificationBadge.setVisible(false);
        }
    }
    
    public int getNotificationCount() {
        String text = notificationBadge.getText();
        return text.isEmpty() ? 0 : Integer.parseInt(text);
    }
    
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        setBackground(UIManager.getPrimary());
    }
}
