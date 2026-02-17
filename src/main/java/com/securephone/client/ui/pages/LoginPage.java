package com.securephone.client.ui.pages;

import javax.swing.*;
import java.awt.*;
import com.securephone.client.ui.UIManager;
import com.securephone.client.ui.frames.MainFrame;

/**
 * LoginPage - Page de connexion avec email et mot de passe
 * 
 * @author Hatsu
 */
public class LoginPage extends JPanel {
    
    private MainFrame mainFrame;
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton loginButton;
    private JButton registerButton;
    private JLabel errorLabel;
    
    public LoginPage(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
        initComponents();
        setupLayout();
    }
    
    private void initComponents() {
        // Titre
        JLabel titleLabel = new JLabel("SecurePhone Login");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 28));
        titleLabel.setForeground(UIManager.getPrimary());
        
        // Sous-titre
        JLabel subtitleLabel = new JLabel("Connectez-vous à votre compte");
        subtitleLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        subtitleLabel.setForeground(UIManager.getOnBackground());
        
        // Champ username
        JLabel usernameLabel = new JLabel("Nom d'utilisateur:");
        usernameLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        usernameField = new JTextField();
        usernameField.setPreferredSize(new Dimension(300, 40));
        usernameField.setFont(new Font("Arial", Font.PLAIN, 14));
        usernameField.setBorder(BorderFactory.createLineBorder(UIManager.getPrimary(), 1));
        usernameField.setBackground(UIManager.getSurface());
        usernameField.setForeground(UIManager.getOnSurface());
        usernameField.setCaretColor(UIManager.getPrimary());
        
        // Champ mot de passe
        JLabel passwordLabel = new JLabel("Mot de passe:");
        passwordLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        passwordField = new JPasswordField();
        passwordField.setPreferredSize(new Dimension(300, 40));
        passwordField.setFont(new Font("Arial", Font.PLAIN, 14));
        passwordField.setBorder(BorderFactory.createLineBorder(UIManager.getPrimary(), 1));
        passwordField.setBackground(UIManager.getSurface());
        passwordField.setForeground(UIManager.getOnSurface());
        passwordField.setCaretColor(UIManager.getPrimary());
        
        // Label d'erreur
        errorLabel = new JLabel();
        errorLabel.setForeground(UIManager.getError());
        errorLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        errorLabel.setVisible(false);
        
        // Bouton de connexion
        loginButton = new JButton("Connexion");
        loginButton.setPreferredSize(new Dimension(300, 40));
        loginButton.setFont(new Font("Arial", Font.BOLD, 14));
        loginButton.setBackground(UIManager.getPrimary());
        loginButton.setForeground(UIManager.getOnPrimary());
        loginButton.setBorderPainted(false);
        loginButton.setFocusPainted(false);
        loginButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        loginButton.addActionListener(e -> handleLogin());
        
        // Bouton register
        registerButton = new JButton("Pas de compte? S'inscrire");
        registerButton.setPreferredSize(new Dimension(300, 40));
        registerButton.setFont(new Font("Arial", Font.PLAIN, 12));
        registerButton.setBackground(UIManager.getSurface());
        registerButton.setForeground(UIManager.getPrimary());
        registerButton.setBorderPainted(false);
        registerButton.setFocusPainted(false);
        registerButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        registerButton.addActionListener(e -> mainFrame.showRegisterPage());
        
        // Ajouter les composants (on les ajoute directement dans setupLayout)
    }
    
    private void setupLayout() {
        setLayout(new GridBagLayout());
        setBackground(UIManager.getBackground());
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.anchor = GridBagConstraints.CENTER;
        
        // Titre
        JLabel titleLabel = new JLabel("SecurePhone Login");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 28));
        titleLabel.setForeground(UIManager.getPrimary());
        gbc.gridwidth = 2;
        gbc.gridx = 0;
        gbc.gridy = 0;
        add(titleLabel, gbc);
        
        // Sous-titre
        JLabel subtitleLabel = new JLabel("Connectez-vous à votre compte");
        subtitleLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        subtitleLabel.setForeground(UIManager.getOnBackground());
        gbc.gridy = 1;
        add(subtitleLabel, gbc);
        
        // Email label
        JLabel emailLabel = new JLabel("Nom d'utilisateur:");
        emailLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        gbc.gridwidth = 2;
        gbc.gridy = 2;
        gbc.anchor = GridBagConstraints.WEST;
        add(emailLabel, gbc);
        
        // Email field
        gbc.gridy = 3;
        gbc.anchor = GridBagConstraints.CENTER;
        add(usernameField, gbc);
        
        // Password label
        JLabel passwordLabel = new JLabel("Mot de passe:");
        passwordLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        gbc.gridy = 4;
        gbc.anchor = GridBagConstraints.WEST;
        add(passwordLabel, gbc);
        
        // Password field
        gbc.gridy = 5;
        gbc.anchor = GridBagConstraints.CENTER;
        add(passwordField, gbc);
        
        // Error label
        errorLabel.setForeground(UIManager.getError());
        errorLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        errorLabel.setVisible(false);
        gbc.gridy = 6;
        add(errorLabel, gbc);
        
        // Login button
        gbc.gridy = 7;
        add(loginButton, gbc);
        
        // Register button
        gbc.gridy = 8;
        add(registerButton, gbc);
    }
    
    private void handleLogin() {
        String username = usernameField.getText().trim();
        String password = new String(passwordField.getPassword());
        
        if (username.isEmpty() || password.isEmpty()) {
            showError("Veuillez remplir tous les champs");
            return;
        }
        
        // Connecter au serveur et login
        try {
            var connectionManager = com.securephone.client.SecurePhoneApp.getConnectionManager();
            
            if (connectionManager != null) {
                // Connecter d'abord au serveur
                boolean connected = connectionManager.connect();
                if (!connected) {
                    showError("Impossible de se connecter au serveur");
                    return;
                }
                
                // Puis envoyer la requête de login
                connectionManager.login(username, password, "");
            } else {
                showError("Erreur d'initialisation");
            }
        } catch (Exception e) {
            showError("Erreur: " + e.getMessage());
        }
    }
    
    public void setError(String message) {
        errorLabel.setText(message);
        errorLabel.setVisible(true);
    }
    
    private void showError(String message) {
        setError(message);
    }
    
    public void clearError() {
        errorLabel.setVisible(false);
    }
}
