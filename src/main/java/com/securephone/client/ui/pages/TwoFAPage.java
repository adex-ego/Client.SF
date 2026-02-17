package com.securephone.client.ui.pages;

import javax.swing.*;
import java.awt.*;
import com.securephone.client.ui.UIManager;
import com.securephone.client.ui.frames.MainFrame;

/**
 * TwoFAPage - Page pour entrer le code d'authentification à deux facteurs
 * 
 * @author Hatsu
 */
public class TwoFAPage extends JPanel {
    
    private MainFrame mainFrame;
    private JTextField codeField;
    private JButton verifyButton;
    private JButton backButton;
    private JLabel errorLabel;
    private JLabel emailLabel;
    private String currentEmail;
    
    public TwoFAPage(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
        initComponents();
        setupLayout();
    }
    
    private void initComponents() {
        // Les composants seront initialisés dans setupLayout
    }
    
    private void setupLayout() {
        setLayout(new GridBagLayout());
        setBackground(UIManager.getBackground());
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.anchor = GridBagConstraints.CENTER;
        
        // Titre
        JLabel titleLabel = new JLabel("Authentification à deux facteurs");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 28));
        titleLabel.setForeground(UIManager.getPrimary());
        gbc.gridwidth = 2;
        gbc.gridx = 0;
        gbc.gridy = 0;
        add(titleLabel, gbc);
        
        // Sous-titre
        JLabel subtitleLabel = new JLabel("Entrez le code envoyé à votre email");
        subtitleLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        subtitleLabel.setForeground(UIManager.getOnBackground());
        gbc.gridy = 1;
        add(subtitleLabel, gbc);
        
        // Email label
        emailLabel = new JLabel();
        emailLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        emailLabel.setForeground(UIManager.getOnBackground());
        gbc.gridy = 2;
        add(emailLabel, gbc);
        
        // Code label
        JLabel codeLabel = new JLabel("Code de vérification:");
        codeLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        gbc.gridy = 3;
        gbc.anchor = GridBagConstraints.WEST;
        add(codeLabel, gbc);
        
        // Code field
        codeField = new JTextField();
        codeField.setPreferredSize(new Dimension(300, 40));
        codeField.setFont(new Font("Arial", Font.PLAIN, 24));
        codeField.setBorder(BorderFactory.createLineBorder(UIManager.getPrimary(), 1));
        codeField.setBackground(UIManager.getSurface());
        codeField.setForeground(UIManager.getOnSurface());
        codeField.setCaretColor(UIManager.getPrimary());
        codeField.setHorizontalAlignment(JTextField.CENTER);
        codeField.setDocument(new javax.swing.text.PlainDocument() {
            @Override
            public void insertString(int offs, String str, javax.swing.text.AttributeSet a) 
                    throws javax.swing.text.BadLocationException {
                // Limiter à 6 caractères numériques
                if ((getLength() + str.length()) <= 6 && str.matches("[0-9]*")) {
                    super.insertString(offs, str, a);
                }
            }
        });
        gbc.gridy = 4;
        gbc.anchor = GridBagConstraints.CENTER;
        add(codeField, gbc);
        
        // Error label
        errorLabel = new JLabel();
        errorLabel.setForeground(UIManager.getError());
        errorLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        errorLabel.setVisible(false);
        gbc.gridy = 5;
        add(errorLabel, gbc);
        
        // Verify button
        verifyButton = new JButton("Vérifier");
        verifyButton.setPreferredSize(new Dimension(300, 40));
        verifyButton.setFont(new Font("Arial", Font.BOLD, 14));
        verifyButton.setBackground(UIManager.getPrimary());
        verifyButton.setForeground(UIManager.getOnPrimary());
        verifyButton.setBorderPainted(false);
        verifyButton.setFocusPainted(false);
        verifyButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        verifyButton.addActionListener(e -> handleVerify());
        gbc.gridy = 6;
        add(verifyButton, gbc);
        
        // Back button
        backButton = new JButton("Retour");
        backButton.setPreferredSize(new Dimension(300, 40));
        backButton.setFont(new Font("Arial", Font.PLAIN, 12));
        backButton.setBackground(UIManager.getSurface());
        backButton.setForeground(UIManager.getPrimary());
        backButton.setBorderPainted(false);
        backButton.setFocusPainted(false);
        backButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        backButton.addActionListener(e -> mainFrame.showLoginPage());
        gbc.gridy = 7;
        add(backButton, gbc);
    }
    
    private void handleVerify() {
        String code = codeField.getText().trim();
        
        if (code.isEmpty()) {
            showError("Veuillez entrer le code");
            return;
        }
        
        if (code.length() != 6) {
            showError("Le code doit faire 6 caractères");
            return;
        }
        
        // Envoyer le code 2FA au serveur
        try {
            var connectionManager = com.securephone.client.SecurePhoneApp.getConnectionManager();
            
            if (connectionManager != null) {
                // Vérifier que la connexion est active
                if (!connectionManager.isConnected()) {
                    showError("Connexion perdue, reconnexion...");
                    // Essayer de reconnecter
                    if (!connectionManager.connect()) {
                        showError("Impossible de se connecter au serveur");
                        return;
                    }
                }
                
                // Envoyer le code 2FA pour vérification
                connectionManager.verify2FA(code);
            } else {
                showError("Erreur d'initialisation");
            }
        } catch (Exception e) {
            showError("Erreur: " + e.getMessage());
        }
    }
    
    private void showError(String message) {
        errorLabel.setText(message);
        errorLabel.setVisible(true);
    }
    
    public void clearError() {
        errorLabel.setVisible(false);
    }
    
    public void setEmail(String email) {
        this.currentEmail = email;
        emailLabel.setText("Code envoyé à: " + email);
        codeField.setText("");
        clearError();
    }
}
