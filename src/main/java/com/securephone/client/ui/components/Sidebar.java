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
        setBackground(UIManager.getBackground());
        setPreferredSize(new Dimension(80, 0));
        setBorder(BorderFactory.createMatteBorder(0, 1, 0, 0, UIManager.getBorder()));
        
        // Espacement haut
        add(Box.createVerticalStrut(20));
        
        // Bouton Home
        homeButton = createButton("Home");
        add(homeButton);
        add(Box.createVerticalStrut(5));
        
        // Bouton Notifications avec badge
        JPanel notifPanel = new JPanel();
        notifPanel.setLayout(new BoxLayout(notifPanel, BoxLayout.Y_AXIS));
        notifPanel.setBackground(UIManager.getBackground());
        notifPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        notificationsButton = createButton("Notifs");
        
        // Badge de notification
        notificationBadge = new JLabel("0");
        notificationBadge.setFont(new Font("Arial", Font.BOLD, 10));
        notificationBadge.setForeground(Color.WHITE);
        notificationBadge.setBackground(new Color(244, 67, 54));
        notificationBadge.setOpaque(true);
        notificationBadge.setHorizontalAlignment(SwingConstants.CENTER);
        notificationBadge.setPreferredSize(new Dimension(20, 20));
        notificationBadge.setMaximumSize(new Dimension(20, 20));
        notificationBadge.setVisible(false);
        notificationBadge.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        notifPanel.add(notificationsButton);
        notifPanel.add(Box.createVerticalStrut(2));
        notifPanel.add(notificationBadge);
        
        add(notifPanel);
        add(Box.createVerticalStrut(5));
        
        // Bouton Contacts
        contactsButton = createButton("Contact");
        add(contactsButton);
        add(Box.createVerticalStrut(5));
        
        // Espacement flexible au milieu
        add(Box.createVerticalGlue());
        
        // Bouton Settings (en bas)
        settingsButton = createButton("Settings");
        add(settingsButton);
        add(Box.createVerticalStrut(20));
    }
    
    private JButton createButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.PLAIN, 11));
        button.setBackground(UIManager.getPrimary());
        button.setForeground(UIManager.getOnPrimary());
        button.setBorderPainted(false);
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setPreferredSize(new Dimension(70, 40));
        button.setMaximumSize(new Dimension(70, 40));
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        // Hover effect
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            Color originalBg = button.getBackground();
            
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
    
    public void updateTheme() {
        setBackground(UIManager.getBackground());
        setBorder(BorderFactory.createMatteBorder(0, 1, 0, 0, UIManager.getBorder()));
        
        // Mettre à jour tous les boutons
        for (Component comp : getComponents()) {
            if (comp instanceof JButton) {
                JButton btn = (JButton) comp;
                btn.setBackground(UIManager.getPrimary());
                btn.setForeground(UIManager.getOnPrimary());
            } else if (comp instanceof JPanel) {
                comp.setBackground(UIManager.getBackground());
                for (Component subComp : ((JPanel) comp).getComponents()) {
                    if (subComp instanceof JButton) {
                        JButton btn = (JButton) subComp;
                        btn.setBackground(UIManager.getPrimary());
                        btn.setForeground(UIManager.getOnPrimary());
                    }
                }
            }
        }
        repaint();
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
}
