package com.securephone.client.ui.pages;

import javax.swing.*;
import java.awt.*;
import com.securephone.client.ui.UIManager;
import com.securephone.client.ui.frames.MainFrame;

/**
 * RegisterPage - Page d'inscription
 * 
 * Champs:
 * - username (VARCHAR(50) UNIQUE)
 * - email (VARCHAR(100) UNIQUE)
 * - password (mot de passe)
 * - confirmPassword (confirmation)
 * 
 * @author Hatsu
 */
public class RegisterPage extends JPanel {
    
    private MainFrame mainFrame;
    private JTextField usernameField;
    private JTextField emailField;
    private JPasswordField passwordField;
    private JPasswordField confirmPasswordField;
    private JButton registerButton;
    private JButton loginButton;
    private JLabel errorLabel;
    
    public RegisterPage(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
        setLayout(new BorderLayout());
        initComponents();
        setupLayout();
    }
    
    private void initComponents() {
        // Les composants seront créés dans setupLayout
    }
    
    private void setupLayout() {
        setBackground(UIManager.getBackground());
        
        // ScrollPane pour supporter les petites fenêtres
        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setBackground(UIManager.getBackground());
        scrollPane.getViewport().setBackground(UIManager.getBackground());
        scrollPane.setBorder(null);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        
        // Panel principal
        JPanel mainContent = new JPanel();
        mainContent.setLayout(new GridBagLayout());
        mainContent.setBackground(UIManager.getBackground());
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.gridwidth = 2;
        gbc.gridx = 0;
        
        // Titre
        JLabel titleLabel = new JLabel("Créer un compte");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 28));
        titleLabel.setForeground(UIManager.getPrimary());
        gbc.gridy = 0;
        mainContent.add(titleLabel, gbc);
        
        // Sous-titre
        JLabel subtitleLabel = new JLabel("Rejoignez SecurePhone");
        subtitleLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        subtitleLabel.setForeground(UIManager.getOnBackground());
        gbc.gridy = 1;
        mainContent.add(subtitleLabel, gbc);
        
        // Username label
        JLabel usernameLabel = new JLabel("Nom d'utilisateur:");
        usernameLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        gbc.gridy = 2;
        gbc.anchor = GridBagConstraints.WEST;
        mainContent.add(usernameLabel, gbc);
        
        // Username field
        usernameField = new JTextField();
        usernameField.setPreferredSize(new Dimension(300, 40));
        usernameField.setFont(new Font("Arial", Font.PLAIN, 14));
        usernameField.setBorder(BorderFactory.createLineBorder(UIManager.getPrimary(), 1));
        usernameField.setBackground(UIManager.getSurface());
        usernameField.setForeground(UIManager.getOnSurface());
        usernameField.setCaretColor(UIManager.getPrimary());
        gbc.gridy = 3;
        gbc.anchor = GridBagConstraints.CENTER;
        mainContent.add(usernameField, gbc);
        
        // Email label
        JLabel emailLabel = new JLabel("Email:");
        emailLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        gbc.gridy = 4;
        gbc.anchor = GridBagConstraints.WEST;
        mainContent.add(emailLabel, gbc);
        
        // Email field
        emailField = new JTextField();
        emailField.setPreferredSize(new Dimension(300, 40));
        emailField.setFont(new Font("Arial", Font.PLAIN, 14));
        emailField.setBorder(BorderFactory.createLineBorder(UIManager.getPrimary(), 1));
        emailField.setBackground(UIManager.getSurface());
        emailField.setForeground(UIManager.getOnSurface());
        emailField.setCaretColor(UIManager.getPrimary());
        gbc.gridy = 5;
        gbc.anchor = GridBagConstraints.CENTER;
        mainContent.add(emailField, gbc);
        
        // Password label
        JLabel passwordLabel = new JLabel("Mot de passe:");
        passwordLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        gbc.gridy = 6;
        gbc.anchor = GridBagConstraints.WEST;
        mainContent.add(passwordLabel, gbc);
        
        // Password field
        passwordField = new JPasswordField();
        passwordField.setPreferredSize(new Dimension(300, 40));
        passwordField.setFont(new Font("Arial", Font.PLAIN, 14));
        passwordField.setBorder(BorderFactory.createLineBorder(UIManager.getPrimary(), 1));
        passwordField.setBackground(UIManager.getSurface());
        passwordField.setForeground(UIManager.getOnSurface());
        passwordField.setCaretColor(UIManager.getPrimary());
        gbc.gridy = 7;
        gbc.anchor = GridBagConstraints.CENTER;
        mainContent.add(passwordField, gbc);
        
        // Confirm Password label
        JLabel confirmPasswordLabel = new JLabel("Confirmer le mot de passe:");
        confirmPasswordLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        gbc.gridy = 8;
        gbc.anchor = GridBagConstraints.WEST;
        mainContent.add(confirmPasswordLabel, gbc);
        
        // Confirm Password field
        confirmPasswordField = new JPasswordField();
        confirmPasswordField.setPreferredSize(new Dimension(300, 40));
        confirmPasswordField.setFont(new Font("Arial", Font.PLAIN, 14));
        confirmPasswordField.setBorder(BorderFactory.createLineBorder(UIManager.getPrimary(), 1));
        confirmPasswordField.setBackground(UIManager.getSurface());
        confirmPasswordField.setForeground(UIManager.getOnSurface());
        confirmPasswordField.setCaretColor(UIManager.getPrimary());
        gbc.gridy = 9;
        gbc.anchor = GridBagConstraints.CENTER;
        mainContent.add(confirmPasswordField, gbc);
        
        // Error label
        errorLabel = new JLabel();
        errorLabel.setForeground(UIManager.getError());
        errorLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        errorLabel.setVisible(false);
        gbc.gridy = 10;
        mainContent.add(errorLabel, gbc);
        
        // Register button
        registerButton = new JButton("S'inscrire");
        registerButton.setPreferredSize(new Dimension(300, 40));
        registerButton.setFont(new Font("Arial", Font.BOLD, 14));
        registerButton.setBackground(UIManager.getPrimary());
        registerButton.setForeground(UIManager.getOnPrimary());
        registerButton.setBorderPainted(false);
        registerButton.setFocusPainted(false);
        registerButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        registerButton.addActionListener(e -> handleRegister());
        gbc.gridy = 11;
        mainContent.add(registerButton, gbc);
        
        // Login button
        loginButton = new JButton("Vous avez déjà un compte? Se connecter");
        loginButton.setPreferredSize(new Dimension(300, 40));
        loginButton.setFont(new Font("Arial", Font.PLAIN, 12));
        loginButton.setBackground(UIManager.getBackground());
        loginButton.setForeground(UIManager.getPrimary());
        loginButton.setBorderPainted(false);
        loginButton.setFocusPainted(false);
        loginButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        loginButton.addActionListener(e -> mainFrame.showLoginPage());
        gbc.gridy = 12;
        mainContent.add(loginButton, gbc);
        
        scrollPane.setViewportView(mainContent);
        add(scrollPane, BorderLayout.CENTER);
    }
    
    private void handleRegister() {
        String username = usernameField.getText().trim();
        String email = emailField.getText().trim();
        String password = new String(passwordField.getPassword());
        String confirmPassword = new String(confirmPasswordField.getPassword());
        
        // Validation
        if (username.isEmpty() || email.isEmpty() || password.isEmpty()) {
            showError("Veuillez remplir tous les champs");
            return;
        }
        
        if (username.length() < 3 || username.length() > 50) {
            showError("Le nom d'utilisateur doit faire entre 3 et 50 caractères");
            return;
        }
        
        if (!email.contains("@") || email.length() > 100) {
            showError("Adresse email invalide");
            return;
        }
        
        if (password.length() < 8) {
            showError("Le mot de passe doit faire au moins 8 caractères");
            return;
        }
        
        if (!password.equals(confirmPassword)) {
            showError("Les mots de passe ne correspondent pas");
            return;
        }
        
        // Envoyer la requête d'inscription
        try {
            var connectionManager = com.securephone.client.SecurePhoneApp.getConnectionManager();
            
            if (connectionManager != null) {
                // Connecter d'abord au serveur
                boolean connected = connectionManager.connect();
                if (!connected) {
                    showError("Impossible de se connecter au serveur");
                    return;
                }
                
                // Envoyer la requête d'inscription
                connectionManager.register(username, password, email);
            } else {
                showError("Erreur d'initialisation");
            }
        } catch (Exception e) {
            showError("Erreur: " + e.getMessage());
        }
    }
    
    public void setError(String message) {
        showError(message);
    }
    
    private void showError(String message) {
        errorLabel.setText(message);
        errorLabel.setVisible(true);
    }
    
    public void clearError() {
        errorLabel.setVisible(false);
    }
}


