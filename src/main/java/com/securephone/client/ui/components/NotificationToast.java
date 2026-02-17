package com.securephone.client.ui.components;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.Timer;
import java.util.TimerTask;
import com.securephone.client.ui.UIManager;

/**
 * NotificationToast - Notification popup en haut à droite de l'écran
 * Disappears automatiquement après 5 secondes
 */
public class NotificationToast extends JFrame {
    
    public enum Type {
        INFO, SUCCESS, WARNING, ERROR
    }
    
    private Type type;
    private String title;
    private String message;
    
    public NotificationToast(String title, String message, Type type) {
        this.title = title;
        this.message = message;
        this.type = type;
        
        initUI();
        showAndAutoHide();
    }
    
    private void initUI() {
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setUndecorated(true);
        setAlwaysOnTop(true);
        setFocusable(false);
        
        // Panel principal
        JPanel mainPanel = new JPanel(new BorderLayout(10, 5));
        mainPanel.setBorder(new EmptyBorder(10, 15, 10, 15));
        mainPanel.setBackground(getBackgroundColor());
        
        // Icône + titre
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(getBackgroundColor());
        
        JLabel iconLabel = new JLabel(getIcon());
        iconLabel.setFont(new Font("Arial", Font.BOLD, 18));
        headerPanel.add(iconLabel, BorderLayout.WEST);
        
        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 14));
        titleLabel.setForeground(getForegroundColor());
        headerPanel.add(titleLabel, BorderLayout.CENTER);
        
        mainPanel.add(headerPanel, BorderLayout.NORTH);
        
        // Message
        JLabel messageLabel = new JLabel("<html>" + message + "</html>");
        messageLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        messageLabel.setForeground(getForegroundColor());
        mainPanel.add(messageLabel, BorderLayout.CENTER);
        
        // Bouton fermer
        JButton closeButton = new JButton("✕");
        closeButton.setFont(new Font("Arial", Font.PLAIN, 12));
        closeButton.setBackground(getBackgroundColor());
        closeButton.setForeground(getForegroundColor());
        closeButton.setBorderPainted(false);
        closeButton.setFocusPainted(false);
        closeButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        closeButton.addActionListener(e -> dispose());
        mainPanel.add(closeButton, BorderLayout.EAST);
        
        setContentPane(mainPanel);
        setSize(350, 100);
        
        // Positionner en haut à droite
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        setLocation(screenSize.width - getWidth() - 20, 20);
    }
    
    private void showAndAutoHide() {
        setVisible(true);
        
        // Disparaître après 5 secondes
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                SwingUtilities.invokeLater(() -> {
                    fadeOut();
                });
            }
        }, 5000);
    }
    
    private void fadeOut() {
        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            float opacity = 1.0f;
            
            @Override
            public void run() {
                opacity -= 0.05f;
                if (opacity <= 0) {
                    timer.cancel();
                    dispose();
                } else {
                    SwingUtilities.invokeLater(() -> {
                        setOpacity(opacity);
                    });
                }
            }
        }, 0, 20);
    }
    
    private String getIcon() {
        if (type == Type.SUCCESS) return "✅";
        if (type == Type.ERROR) return "❌";
        if (type == Type.WARNING) return "⚠️";
        return "ℹ️";
    }
    
    private Color getBackgroundColor() {
        if (type == Type.SUCCESS) return new Color(76, 175, 80);   // Vert
        if (type == Type.ERROR) return new Color(244, 67, 54);     // Rouge
        if (type == Type.WARNING) return new Color(255, 193, 7);   // Orange
        return new Color(33, 150, 243);   // Bleu (INFO)
    }
    
    private Color getForegroundColor() {
        return Color.WHITE;
    }
}
