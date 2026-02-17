package com.securephone.client.ui.frames;

import javax.swing.*;
import java.awt.*;
import com.securephone.client.SecurePhoneApp;
import com.securephone.client.ui.UIManager;
import com.securephone.client.ui.components.Sidebar;
import com.securephone.client.ui.components.NotificationsPopup;
import com.securephone.client.ui.components.SearchContactsPopup;
import com.securephone.client.ui.components.SettingsPopup;
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
    private Sidebar sidebar;
    private NotificationsPopup notificationsPopup;
    private SearchContactsPopup searchContactsPopup;
    private SettingsPopup settingsPopup;
    
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
        setupPopups();
        setupSidebarActions();
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
        
        // Ajouter la sidebar sur la gauche
        sidebar = new Sidebar();
        contentPane.add(sidebar, BorderLayout.WEST);
        
        setContentPane(contentPane);
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
    
    private void setupPopups() {
        // Notifications popup
        notificationsPopup = new NotificationsPopup(this);
        
        // Search contacts popup
        searchContactsPopup = new SearchContactsPopup(this);
        
        // Settings popup
        settingsPopup = new SettingsPopup(this);
    }
    
    private void setupSidebarActions() {
        // Home button - show app page
        sidebar.getHomeButton().addActionListener(e -> {
            showAppPage("");
            notificationsPopup.setVisible(false);
            searchContactsPopup.setVisible(false);
            settingsPopup.setVisible(false);
        });
        
        // Notifications button - toggle notification popup
        sidebar.getNotificationsButton().addActionListener(e -> {
            if (notificationsPopup.isVisible()) {
                notificationsPopup.setVisible(false);
            } else {
                notificationsPopup.setVisible(true);
            }
        });
        
        // Contacts button - toggle search contacts popup
        sidebar.getContactsButton().addActionListener(e -> {
            if (searchContactsPopup.isVisible()) {
                searchContactsPopup.setVisible(false);
            } else {
                searchContactsPopup.setVisible(true);
                searchContactsPopup.clearSearch();
            }
        });
        
        // Settings button - toggle settings popup
        sidebar.getSettingsButton().addActionListener(e -> {
            if (settingsPopup.isVisible()) {
                settingsPopup.setVisible(false);
            } else {
                settingsPopup.setVisible(true);
            }
        });
        
        // Search contacts handler
        searchContactsPopup.setOnSearch(query -> {
            if (!query.isEmpty()) {
                SecurePhoneApp.getConnectionManager().searchContacts(query, results -> {
                    searchContactsPopup.setSearchResults(results.stream()
                        .map(r -> new SearchContactsPopup.ContactResult(
                            r.id, r.username, r.email, r.status, r.isContact
                        ))
                        .collect(java.util.stream.Collectors.toList())
                    );
                });
            }
        });
        
        // Add contact handler
        searchContactsPopup.setOnAddContact(contact -> {
            SecurePhoneApp.getConnectionManager().addContact(contact.id, contact.username, (success, message) -> {
                if (success) {
                    JOptionPane.showMessageDialog(this, "Contact ajouté avec succès!", "Succès", JOptionPane.INFORMATION_MESSAGE);
                    searchContactsPopup.clearSearch();
                } else {
                    JOptionPane.showMessageDialog(this, "Erreur: " + message, "Erreur", JOptionPane.ERROR_MESSAGE);
                }
            });
        });
        
        // Theme toggle in settings
        settingsPopup.setOnThemeToggle(() -> {
            toggleTheme();
        });
        
        // Logout in settings
        settingsPopup.setOnLogout(() -> {
            // Logout du serveur
            SecurePhoneApp.getConnectionManager().logout();
            
            // Retour au login
            showLoginPage();
            notificationsPopup.setVisible(false);
            searchContactsPopup.setVisible(false);
            settingsPopup.setVisible(false);
        });
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
    
    public AppPage getAppFrame() {
        return appPage;
    }
    
    public Sidebar getSidebar() {
        return sidebar;
    }
    
    public NotificationsPopup getNotificationsPopup() {
        return notificationsPopup;
    }
    
    public SearchContactsPopup getSearchContactsPopup() {
        return searchContactsPopup;
    }
    
    public SettingsPopup getSettingsPopup() {
        return settingsPopup;
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
    
    public void showAppPage(String username) {
        appPage.setUsername(username);
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
        sidebar.setBackground(UIManager.getPrimary());
        getContentPane().setBackground(UIManager.getBackground());
        
        // Mettre à jour récursivement
        UIManager.applyThemeRecursively(this);
        
        // Force refresh
        SwingUtilities.updateComponentTreeUI(this);
        repaint();
    }
}
