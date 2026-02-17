package com.securephone.client;

import javax.swing.*;
import com.securephone.client.ui.frames.MainFrame;
import com.securephone.client.network.ConnectionManager;
import com.securephone.client.utils.Logger;

public class SecurePhoneApp {
    
    private static ConnectionManager connectionManager;
    private static MainFrame mainFrame;
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                // Créer la frame
                mainFrame = new MainFrame();
                
                // Initialiser la connexion au serveur
                connectionManager = new ConnectionManager();
                setupConnectionListeners();
                
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
                    mainFrame.showAppPage();
                });
            }

            @Override
            public void onLoginRequires2FA(String message) {
                SwingUtilities.invokeLater(() -> {
                    Logger.warn("⚠️ 2FA requis: " + message);
                    // Extraire le username du message ou afficher la page 2FA
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
        
        // Error listener
        connectionManager.setErrorListener(message -> {
            SwingUtilities.invokeLater(() -> {
                Logger.error("Network Error: " + message);
            });
        });
    }
    
    public static ConnectionManager getConnectionManager() {
        return connectionManager;
    }
}
