package com.securephone.client.ui.frames;

import javax.swing.*;
import java.awt.*;
import com.securephone.client.ui.UIManager;
import com.securephone.client.ui.pages.AppPage;
import com.securephone.client.ui.pages.LoginPage;
import com.securephone.client.ui.pages.RegisterPage;
import com.securephone.client.ui.pages.TwoFAPage;

/**
 * MainFrame - Fenêtre principale de l'application
 * 
 * Responsabilités:
 * - Conteneur principal de toutes les pages
 * - Gestion du switching entre pages (login, register, 2FA, app)
 * - Gestion des thèmes (light/dark)
 * - Bouton de paramètres
 * 
 * @author Hatsu
 */
public class MainFrame extends JFrame {
    
    // ========== COMPOSANTS ==========
    private JPanel mainPanel;
    private CardLayout cardLayout;
    private JPanel settingsPanel;
    
    // ========== PAGES ==========
    private LoginPage loginPage;
    private RegisterPage registerPage;
    private TwoFAPage twoFAPage;
    private AppPage appPage;
    
    // ========== PAGES IDS ==========
    public static final String LOGIN_PAGE = "login";
    public static final String REGISTER_PAGE = "register";
    public static final String TWOFА_PAGE = "twofa";
    public static final String APP_PAGE = "app";
    
    // ========== CONSTANTES ==========
    private static final int WINDOW_WIDTH = 800;
    private static final int WINDOW_HEIGHT = 600;
    private boolean isFullscreen = false;

    // ========== CONSTRUCTEUR ==========
    public MainFrame() {
        initWindow();
        setupLayout();
        initPages();
        setupSettingsPanel();
        showLoginPage();
    }
    
    // ========== INITIALISATION ==========
    private void initWindow() {
        setTitle("SecurePhone");
        setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(true);
        
        // Icône de la fenêtre (optionnel)
        try {
            setIconImage(new ImageIcon(
                getClass().getResource("/images/icon.png")
            ).getImage());
        } catch (Exception e) {
            System.err.println("   ⚠ Impossible de charger l'icône");
        }
        
        // Shortcut fullscreen avec F11
        KeyboardFocusManager.getCurrentKeyboardFocusManager().addKeyEventDispatcher(e -> {
            if (e.getKeyCode() == java.awt.event.KeyEvent.VK_F11 && e.getID() == java.awt.event.KeyEvent.KEY_PRESSED) {
                toggleFullscreen();
                return true;
            }
            return false;
        });
    }
    
    private void toggleFullscreen() {
        GraphicsEnvironment env = GraphicsEnvironment.getLocalGraphicsEnvironment();
        GraphicsDevice device = env.getDefaultScreenDevice();
        
        if (!isFullscreen) {
            isFullscreen = true;
            dispose();
            setUndecorated(true);
            device.setFullScreenWindow(this);
        } else {
            isFullscreen = false;
            device.setFullScreenWindow(null);
            setUndecorated(false);
            setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
            setLocationRelativeTo(null);
            setVisible(true);
        }
    }
    
    private void setupLayout() {
        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);
        mainPanel.setBackground(UIManager.getBackground());
        
        // Ajouter le content pane
        JPanel contentPane = new JPanel(new BorderLayout());
        contentPane.add(mainPanel, BorderLayout.CENTER);
        
        // Ajouter le settings panel sur la droite (petit panneau)
        setupSettingsPanel();
        contentPane.add(settingsPanel, BorderLayout.EAST);
        
        setContentPane(contentPane);
    }
    
    private void setupSettingsPanel() {
        settingsPanel = new JPanel();
        settingsPanel.setLayout(new BoxLayout(settingsPanel, BoxLayout.Y_AXIS));
        settingsPanel.setBackground(UIManager.getSurface());
        settingsPanel.setPreferredSize(new Dimension(60, getHeight()));
        settingsPanel.setBorder(BorderFactory.createMatteBorder(0, 1, 0, 0, UIManager.getBorder()));
        
        // Bouton pour basculer le thème
        JButton themeToggleButton = new JButton("🌓");
        themeToggleButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        themeToggleButton.setMaximumSize(new Dimension(50, 50));
        themeToggleButton.setMargin(new Insets(10, 10, 10, 10));
        themeToggleButton.setBackground(UIManager.getPrimary());
        themeToggleButton.setForeground(UIManager.getOnPrimary());
        themeToggleButton.setBorderPainted(false);
        themeToggleButton.setFocusPainted(false);
        themeToggleButton.setToolTipText("Toggle light/dark mode");
        
        themeToggleButton.addActionListener(e -> toggleTheme());
        
        settingsPanel.add(Box.createVerticalStrut(10));
        settingsPanel.add(themeToggleButton);
        settingsPanel.add(Box.createVerticalGlue());
    }
    
    private void initPages() {
        loginPage = new LoginPage(this);
        registerPage = new RegisterPage(this);
        twoFAPage = new TwoFAPage(this);
        appPage = new AppPage(this);
        
        mainPanel.add(loginPage, LOGIN_PAGE);
        mainPanel.add(registerPage, REGISTER_PAGE);
        mainPanel.add(twoFAPage, TWOFА_PAGE);
        mainPanel.add(appPage, APP_PAGE);
    }
    
    // ========== GETTERS ==========
    public LoginPage getLoginFrame() {
        return loginPage;
    }
    
    public RegisterPage getRegisterFrame() {
        return registerPage;
    }
    
    public TwoFAPage getTwoFAFrame() {
        return twoFAPage;
    }
    
    // ========== NAVIGATION ==========
    public void showLoginPage() {
        cardLayout.show(mainPanel, LOGIN_PAGE);
    }
    
    public void showRegisterPage() {
        cardLayout.show(mainPanel, REGISTER_PAGE);
    }
    
    public void showTwoFAPage(String email) {
        twoFAPage.setEmail(email);
        cardLayout.show(mainPanel, TWOFА_PAGE);
    }
    
    public void showAppPage() {
        cardLayout.show(mainPanel, APP_PAGE);
    }
    
    // ========== THÈME ==========
    private void toggleTheme() {
        UIManager.toggleTheme();
        updateTheme();
    }
    
    private void updateTheme() {
        // Mettre à jour les couleurs de tous les composants
        mainPanel.setBackground(UIManager.getBackground());
        settingsPanel.setBackground(UIManager.getSurface());
        settingsPanel.setBorder(BorderFactory.createMatteBorder(0, 1, 0, 0, UIManager.getBorder()));
        getContentPane().setBackground(UIManager.getBackground());
        
        // Mettre à jour récursivement
        UIManager.applyThemeRecursively(this);
        
        // Force refresh
        SwingUtilities.updateComponentTreeUI(this);
        repaint();
    }
}
