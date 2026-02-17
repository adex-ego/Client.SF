package com.securephone.client.ui.components;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import com.securephone.client.ui.UIManager;

/**
 * NotificationsPopup - Popup avec liste de notifications
 * Affiche les notifications avec possibilité de scroller et cliquer
 */
public class NotificationsPopup extends JDialog {
    
    private DefaultListModel<NotificationItem> notificationsModel;
    private JList<NotificationItem> notificationsList;
    private Runnable onNotificationClick;
    
    public NotificationsPopup(Frame owner) {
        super(owner, "Notifications", false);
        initUI();
    }
    
    private void initUI() {
        setDefaultCloseOperation(JDialog.HIDE_ON_CLOSE);
        setSize(350, 400);
        setLocationRelativeTo(getOwner());
        setResizable(true);
        
        // Panel principal
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(UIManager.getBackground());
        mainPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        
        // Header
        JLabel headerLabel = new JLabel("Notifications");
        headerLabel.setFont(new Font("Arial", Font.BOLD, 16));
        headerLabel.setForeground(UIManager.getOnBackground());
        mainPanel.add(headerLabel, BorderLayout.NORTH);
        
        // Liste de notifications
        notificationsModel = new DefaultListModel<>();
        notificationsList = new JList<>(notificationsModel);
        notificationsList.setBackground(UIManager.getBackground());
        notificationsList.setForeground(UIManager.getOnBackground());
        notificationsList.setSelectionBackground(UIManager.getPrimary());
        notificationsList.setSelectionForeground(UIManager.getOnPrimary());
        notificationsList.setCellRenderer(new NotificationCellRenderer());
        notificationsList.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && onNotificationClick != null) {
                onNotificationClick.run();
            }
        });
        
        JScrollPane scrollPane = new JScrollPane(notificationsList);
        scrollPane.getViewport().setBackground(UIManager.getBackground());
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        mainPanel.add(scrollPane, BorderLayout.CENTER);
        
        // Bouton Effacer tout
        JButton clearButton = new JButton("Clear All");
        clearButton.setBackground(UIManager.getPrimary());
        clearButton.setForeground(UIManager.getOnPrimary());
        clearButton.setBorderPainted(false);
        clearButton.setFocusPainted(false);
        clearButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        clearButton.addActionListener(e -> notificationsModel.clear());
        mainPanel.add(clearButton, BorderLayout.SOUTH);
        
        setContentPane(mainPanel);
    }
    
    public void addNotification(String title, String message, String type) {
        NotificationItem item = new NotificationItem(title, message, type);
        notificationsModel.insertElementAt(item, 0); // Ajouter en haut
    }
    
    public int getNotificationCount() {
        return notificationsModel.size();
    }
    
    public void setOnNotificationClick(Runnable callback) {
        this.onNotificationClick = callback;
    }
    
    public NotificationItem getSelectedNotification() {
        return notificationsList.getSelectedValue();
    }
    
    public static class NotificationItem {
        public String title;
        public String message;
        public String type; // "contact_request", "message", "call", etc.
        
        public NotificationItem(String title, String message, String type) {
            this.title = title;
            this.message = message;
            this.type = type;
        }
        
        @Override
        public String toString() {
            return title + ": " + message;
        }
    }
    
    private class NotificationCellRenderer extends DefaultListCellRenderer {
        @Override
        public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
            NotificationItem item = (NotificationItem) value;
            
            JPanel panel = new JPanel(new BorderLayout(5, 5));
            panel.setBorder(new EmptyBorder(5, 5, 5, 5));
            
            if (isSelected) {
                panel.setBackground(UIManager.getPrimary());
            } else {
                panel.setBackground(UIManager.getSurface());
            }
            
            // Icône par type
            String icon;
            if ("contact_request".equals(item.type)) {
                icon = "👤";
            } else if ("message".equals(item.type)) {
                icon = "💬";
            } else if ("call".equals(item.type)) {
                icon = "☎️";
            } else if ("accepted".equals(item.type)) {
                icon = "✅";
            } else {
                icon = "ℹ️";
            }
            
            JLabel iconLabel = new JLabel(icon);
            iconLabel.setFont(new Font("Arial", Font.PLAIN, 16));
            panel.add(iconLabel, BorderLayout.WEST);
            
            // Titre et message
            JPanel textPanel = new JPanel();
            textPanel.setLayout(new BoxLayout(textPanel, BoxLayout.Y_AXIS));
            textPanel.setBackground(panel.getBackground());
            
            JLabel titleLabel = new JLabel(item.title);
            titleLabel.setFont(new Font("Arial", Font.BOLD, 12));
            titleLabel.setForeground(isSelected ? UIManager.getOnPrimary() : UIManager.getOnSurface());
            textPanel.add(titleLabel);
            
            JLabel messageLabel = new JLabel("<html>" + item.message + "</html>");
            messageLabel.setFont(new Font("Arial", Font.PLAIN, 11));
            messageLabel.setForeground(isSelected ? UIManager.getOnPrimary() : UIManager.getOnBackground());
            textPanel.add(messageLabel);
            
            panel.add(textPanel, BorderLayout.CENTER);
            
            return panel;
        }
    }
}
