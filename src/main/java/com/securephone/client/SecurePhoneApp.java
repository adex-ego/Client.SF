package com.securephone.client;

import javax.swing.*;
import com.securephone.client.ui.frames.MainFrame;
import com.securephone.client.ui.components.NotificationToast;
import com.securephone.client.network.ConnectionManager;
import com.securephone.client.webpush.PushManager;
import com.securephone.client.utils.Logger;

public class SecurePhoneApp {
    
    private static ConnectionManager connectionManager;
    private static PushManager pushManager;
    private static MainFrame mainFrame;
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                // Créer la frame
                mainFrame = new MainFrame();
                
                // Initialiser la connexion au serveur
                connectionManager = new ConnectionManager();
                setupConnectionListeners();
                
                // Initialiser le gestionnaire de notifications push
                pushManager = new PushManager();
                setupPushListeners();
                
                // Afficher la frame
                mainFrame.setVisible(true);
                
                Logger.info("✅ Application démarrée");
                
            } catch (Exception e) {
                Logger.error("❌ Erreur au démarrage: " + e.getMessage());
                e.printStackTrace();
                JOptionPane.showMessageDialog(
                    null,
                    "Erreur au démarrage:\n" + e.getMessage(),
                    "Erreur",
                    JOptionPane.ERROR_MESSAGE
                );
                System.exit(1);
            }
        });
    }
    
    private static void setupConnectionListeners() {
        // Login listener
        connectionManager.setAuthListener(new ConnectionManager.AuthListener() {
            @Override
            public void onLoginSuccess(com.securephone.client.models.UserSession session) {
                SwingUtilities.invokeLater(() -> {
                    Logger.info("✅ Login réussi: " + session.getUsername());
                    mainFrame.getLoginFrame().clearError();
                    // Afficher la page principale de l'application
                    mainFrame.showAppPage(session.getUsername());
                    
                    // Charger les contacts
                    connectionManager.requestContacts();
                });
            }

            @Override
            public void onLoginRequires2FA(String message) {
                SwingUtilities.invokeLater(() -> {
                    // Afficher la page 2FA
                    mainFrame.showTwoFAPage("");
                });
            }

            @Override
            public void onLoginFailed(String reason) {
                SwingUtilities.invokeLater(() -> {
                    Logger.error("❌ Login échoué: " + reason);
                    mainFrame.getLoginFrame().setError("Connexion échouée: " + reason);
                });
            }

            @Override
            public void onRegisterSuccess(String message) {
                SwingUtilities.invokeLater(() -> {
                    Logger.info("✅ Inscription réussie");
                    mainFrame.getRegisterFrame().clearError();
                    // Auto switch to login
                    mainFrame.showLoginPage();
                });
            }

            @Override
            public void onRegisterFailed(String reason) {
                SwingUtilities.invokeLater(() -> {
                    Logger.error("❌ Inscription échouée: " + reason);
                    mainFrame.getRegisterFrame().setError("Inscription échouée: " + reason);
                });
            }
        });
        
        // Contact listener
        connectionManager.setContactListener(contacts -> {
            SwingUtilities.invokeLater(() -> {
                mainFrame.getAppFrame().displayContacts(contacts);
            });
        });
        
        // Notification listener
        connectionManager.setNotificationListener((type, title, message, data) -> {
            SwingUtilities.invokeLater(() -> {
                // Afficher toast notification
                NotificationToast.Type toastType = parseNotificationType(type);
                NotificationToast toast = new NotificationToast(title, message, toastType);
                toast.setVisible(true);
                
                // Ajouter à la liste des notifications
                mainFrame.getNotificationsPopup().addNotification(title, message, type);
                
                // Mettre à jour le badge
                int count = mainFrame.getNotificationsPopup().getNotificationCount();
                mainFrame.getSidebar().setNotificationCount(count);
                
                Logger.info("🔔 Notification reçue: " + title);
            });
        });
        
        // Error listener
        connectionManager.setErrorListener(message -> {
            SwingUtilities.invokeLater(() -> {
                Logger.error("Network Error: " + message);
            });
        });
    }
    
    private static void setupPushListeners() {
        pushManager.addListener((title, message, type) -> {
            SwingUtilities.invokeLater(() -> {
                // Afficher toast notification
                NotificationToast toast = new NotificationToast(title, message, parseNotificationType(type));
                toast.setVisible(true);
                
                // Ajouter à la liste des notifications
                mainFrame.getNotificationsPopup().addNotification(title, message, type);
                
                // Mettre à jour le badge
                int count = mainFrame.getNotificationsPopup().getNotificationCount();
                mainFrame.getSidebar().setNotificationCount(count);
                
                Logger.info("🔔 Notification reçue: " + title);
            });
        });
    }
    
    private static NotificationToast.Type parseNotificationType(String type) {
        if (type == null) return NotificationToast.Type.INFO;
        switch(type.toLowerCase()) {
            case "contact_request":
                return NotificationToast.Type.INFO;
            case "message":
                return NotificationToast.Type.INFO;
            case "call":
                return NotificationToast.Type.WARNING;
            case "accepted":
                return NotificationToast.Type.SUCCESS;
            default:
                return NotificationToast.Type.INFO;
        }
    }
    
    public static ConnectionManager getConnectionManager() {
        return connectionManager;
    }
    
    public static PushManager getPushManager() {
        return pushManager;
    }
    
    public static MainFrame getMainFrame() {
        return mainFrame;
    }
}
