package com.securephone.client.utils;

import com.securephone.client.ui.UIManager;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * NotificationManager - Gestionnaire de notifications desktop
 * 
 * Responsabilités:
 * - Afficher des notifications popup Swing
 * - Gérer différents types de notifications
 * - Jouer les sons associés
 * - Auto-fermeture après délai
 * 
 * Types de notifications:
 * - MESSAGE: Nouveau message reçu
 * - CALL: Appel entrant
 * - ERROR: Erreur réseau ou système
 * - INFO: Information générale
 * - WARNING: Avertissement
 * 
 * @author Hatsu
 */
public class NotificationManager {
    
    // ========== CONSTANTES ==========
    private static final int NOTIFICATION_WIDTH = 350;
    private static final int NOTIFICATION_HEIGHT = 100;
    private static final int NOTIFICATION_SPACING = 10;
    private static final int DEFAULT_DURATION = 5000; // 5 secondes
    private static final int ANIMATION_STEPS = 20;
    private static final int ANIMATION_DELAY = 10; // ms
    
    // ========== SINGLETON ==========
    private static NotificationManager instance;
    
    // ========== VARIABLES D'INSTANCE ==========
    private boolean notificationsEnabled = true;
    private int currentYOffset = 0; // Décalage Y pour empiler les notifications
    
    // ========== CONSTRUCTEUR PRIVÉ ==========
    private NotificationManager() {
    }
    
    /**
     * Retourne l'instance unique du NotificationManager
     */
    public static synchronized NotificationManager getInstance() {
        if (instance == null) {
            instance = new NotificationManager();
        }
        return instance;
    }
    
    // ========== TYPES DE NOTIFICATIONS ==========
    public enum NotificationType {
        MESSAGE(UIManager.DarkTheme.INFO, "💬"),
        CALL(UIManager.DarkTheme.PRIMARY, "📞"),
        ERROR(UIManager.DarkTheme.ERROR, "❌"),
        INFO(UIManager.DarkTheme.INFO, "ℹ️"),
        WARNING(UIManager.DarkTheme.WARNING, "⚠️"),
        SUCCESS(UIManager.DarkTheme.SUCCESS, "✓");
        
        private final Color color;
        private final String icon;
        
        NotificationType(Color color, String icon) {
            this.color = color;
            this.icon = icon;
        }
        
        public Color getColor() {
            return color;
        }
        
        public String getIcon() {
            return icon;
        }
    }
    
    // ========== MÉTHODES PUBLIQUES ==========
    
    /**
     * Affiche une notification de message
     */
    public void showMessageNotification(String from, String message) {
        if (!notificationsEnabled) return;
        
        showNotification(
            NotificationType.MESSAGE,
            "Nouveau message de " + from,
            truncateMessage(message, 50),
            DEFAULT_DURATION,
            true // Jouer le son
        );
    }
    
    /**
     * Affiche une notification d'appel entrant
     */
    public void showCallNotification(String from, String callType) {
        if (!notificationsEnabled) return;
        
        showNotification(
            NotificationType.CALL,
            "Appel " + callType + " entrant",
            from + " vous appelle...",
            10000, // 10 secondes pour un appel
            true
        );
    }
    
    /**
     * Affiche une notification d'erreur
     */
    public void showErrorNotification(String title, String message) {
        if (!notificationsEnabled) return;
        
        showNotification(
            NotificationType.ERROR,
            title,
            message,
            DEFAULT_DURATION,
            true
        );
    }
    
    /**
     * Affiche une notification d'information
     */
    public void showInfoNotification(String title, String message) {
        if (!notificationsEnabled) return;
        
        showNotification(
            NotificationType.INFO,
            title,
            message,
            DEFAULT_DURATION,
            false
        );
    }
    
    /**
     * Affiche une notification d'avertissement
     */
    public void showWarningNotification(String title, String message) {
        if (!notificationsEnabled) return;
        
        showNotification(
            NotificationType.WARNING,
            title,
            message,
            DEFAULT_DURATION,
            false
        );
    }
    
    /**
     * Affiche une notification de succès
     */
    public void showSuccessNotification(String title, String message) {
        if (!notificationsEnabled) return;
        
        showNotification(
            NotificationType.SUCCESS,
            title,
            message,
            3000, // Plus court pour succès
            false
        );
    }
    
    /**
     * Active ou désactive les notifications
     */
    public void setNotificationsEnabled(boolean enabled) {
        this.notificationsEnabled = enabled;
    }
    
    /**
     * Vérifie si les notifications sont activées
     */
    public boolean isNotificationsEnabled() {
        return notificationsEnabled;
    }
    
    // ========== MÉTHODE PRINCIPALE ==========
    
    /**
     * Affiche une notification personnalisée
     */
    private void showNotification(NotificationType type, String title, String message, 
                                  int duration, boolean playSound) {
        
        SwingUtilities.invokeLater(() -> {
            // Créer la fenêtre de notification
            JWindow notification = createNotificationWindow(type, title, message);
            
            // Positionner la notification
            positionNotification(notification);
            
            // Animer l'apparition
            animateIn(notification);
            
            // Jouer le son si demandé
            if (playSound) {
                playNotificationSound(type);
            }
            
            // Auto-fermeture après le délai
            Timer closeTimer = new Timer(duration, e -> {
                animateOut(notification);
            });
            closeTimer.setRepeats(false);
            closeTimer.start();
        });
    }
    
    /**
     * Crée la fenêtre de notification
     */
    private JWindow createNotificationWindow(NotificationType type, String title, String message) {
        JWindow window = new JWindow();
        window.setSize(NOTIFICATION_WIDTH, NOTIFICATION_HEIGHT);
        window.setAlwaysOnTop(true);
        
        // Panel principal
        JPanel mainPanel = new JPanel(new BorderLayout(10, 5));
        mainPanel.setBackground(UIManager.getSurface());
        mainPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(type.getColor(), 3),
            BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));
        
        // Icône
        JLabel iconLabel = new JLabel(type.getIcon());
        iconLabel.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 32));
        mainPanel.add(iconLabel, BorderLayout.WEST);
        
        // Contenu texte
        JPanel textPanel = new JPanel();
        textPanel.setLayout(new BoxLayout(textPanel, BoxLayout.Y_AXIS));
        textPanel.setBackground(UIManager.getSurface());
        
        JLabel titleLabel = new JLabel(title);
        UIManager.styleSubtitle(titleLabel);
        titleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        JLabel messageLabel = new JLabel("<html>" + message + "</html>");
        UIManager.styleBody(messageLabel);
        messageLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        textPanel.add(titleLabel);
        textPanel.add(Box.createVerticalStrut(5));
        textPanel.add(messageLabel);
        
        mainPanel.add(textPanel, BorderLayout.CENTER);
        
        // Bouton fermer
        JButton closeButton = new JButton("✕");
        closeButton.setFont(new Font("Arial", Font.PLAIN, 16));
        closeButton.setForeground(UIManager.getTextSecondary());
        closeButton.setBackground(UIManager.getSurface());
        closeButton.setBorderPainted(false);
        closeButton.setFocusPainted(false);
        closeButton.setContentAreaFilled(false);
        closeButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        closeButton.addActionListener(e -> animateOut(window));
        
        mainPanel.add(closeButton, BorderLayout.EAST);
        
        // Click pour fermer
        mainPanel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                animateOut(window);
            }
        });
        
        window.add(mainPanel);
        return window;
    }
    
    /**
     * Positionne la notification en bas à droite de l'écran
     */
    private void positionNotification(JWindow notification) {
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        GraphicsDevice gd = ge.getDefaultScreenDevice();
        Rectangle screenBounds = gd.getDefaultConfiguration().getBounds();
        
        Insets insets = Toolkit.getDefaultToolkit().getScreenInsets(gd.getDefaultConfiguration());
        
        int x = screenBounds.width - NOTIFICATION_WIDTH - 20 - insets.right;
        int y = screenBounds.height - NOTIFICATION_HEIGHT - 20 - insets.bottom - currentYOffset;
        
        notification.setLocation(x, y);
        currentYOffset += NOTIFICATION_HEIGHT + NOTIFICATION_SPACING;
    }
    
    /**
     * Animation d'apparition (slide in from right)
     */
    private void animateIn(JWindow notification) {
        final Point finalPosition = notification.getLocation();
        final int startX = finalPosition.x + NOTIFICATION_WIDTH;
        
        notification.setLocation(startX, finalPosition.y);
        notification.setVisible(true);
        
        Timer timer = new Timer(ANIMATION_DELAY, null);
        final int[] step = {0};
        
        timer.addActionListener(e -> {
            step[0]++;
            float progress = (float) step[0] / ANIMATION_STEPS;
            int currentX = startX - (int) ((startX - finalPosition.x) * progress);
            
            notification.setLocation(currentX, finalPosition.y);
            
            if (step[0] >= ANIMATION_STEPS) {
                timer.stop();
            }
        });
        
        timer.start();
    }
    
    /**
     * Animation de disparition (fade out)
     */
    private void animateOut(JWindow notification) {
        Timer timer = new Timer(ANIMATION_DELAY, null);
        final float[] opacity = {1.0f};
        
        timer.addActionListener(e -> {
            opacity[0] -= 0.05f;
            
            if (opacity[0] <= 0) {
                timer.stop();
                notification.dispose();
                currentYOffset = Math.max(0, currentYOffset - NOTIFICATION_HEIGHT - NOTIFICATION_SPACING);
            } else {
                notification.setOpacity(opacity[0]);
            }
        });
        
        timer.start();
    }
    
    /**
     * Joue le son approprié pour le type de notification
     */
    private void playNotificationSound(NotificationType type) {
        SoundPlayer soundPlayer = SoundPlayer.getInstance();
        
        switch (type) {
            case MESSAGE:
                soundPlayer.playMessageReceived();
                break;
            case CALL:
                soundPlayer.playCallIncoming();
                break;
            case ERROR:
                soundPlayer.playError();
                break;
            case SUCCESS:
            case INFO:
            case WARNING:
                soundPlayer.playNotification();
                break;
        }
    }
    
    /**
     * Tronque un message trop long
     */
    private String truncateMessage(String message, int maxLength) {
        if (message == null) return "";
        if (message.length() <= maxLength) return message;
        return message.substring(0, maxLength - 3) + "...";
    }
}