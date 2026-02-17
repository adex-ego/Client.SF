package com.securephone.client;

import com.securephone.client.ui.UIManager;
import com.securephone.client.utils.NotificationManager;
import com.securephone.client.utils.SoundPlayer;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

/**
 * MainUIDemo - Interface de démonstration professionnelle
 * 
 * Vitrine complète du système de design SecurePhone
 * 
 * @author Hatsu
 * @version 1.0.0
 */
public class MainUIDemo {
    
    private static final int WINDOW_WIDTH = 1000;
    private static final int WINDOW_HEIGHT = 700;
    
    public static void main(String[] args) {
        // Activer l'antialiasing pour un rendu plus lisse
        System.setProperty("awt.useSystemAAFontSettings", "on");
        System.setProperty("swing.aatext", "true");
        
        SwingUtilities.invokeLater(() -> {
            createDemoWindow();
        });

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            UIManager.saveTheme(UIManager.getCurrentTheme());
        }));
    }
    
    private static void createDemoWindow() {
        // ========== FENÊTRE PRINCIPALE ==========
        JFrame frame = new JFrame("SecurePhone Design System");
        UIManager.styleFrame(frame);
        frame.setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
        frame.setLocationRelativeTo(null);
        
        // ========== LAYOUT PRINCIPAL ==========
        JPanel rootPanel = new JPanel(new BorderLayout());
        UIManager.stylePanel(rootPanel);
        
        // ========== SIDEBAR NAVIGATION ==========
        JPanel sidebar = createSidebar();
        rootPanel.add(sidebar, BorderLayout.WEST);
        
        // ========== CONTENU PRINCIPAL ==========
        JPanel contentArea = new JPanel(new BorderLayout());
        UIManager.stylePanel(contentArea);
        
        // Header
        JPanel header = createHeader();
        contentArea.add(header, BorderLayout.NORTH);
        
        // Scrollable content
        JPanel mainContent = createMainContent(frame);  // Pass frame here
        JScrollPane scrollPane = new JScrollPane(mainContent);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        contentArea.add(scrollPane, BorderLayout.CENTER);
        
        rootPanel.add(contentArea, BorderLayout.CENTER);
        
        frame.add(rootPanel);
        frame.setVisible(true);
        
        // ========== LOG CONSOLE ==========
        logSystemInfo();
    }
    
    /**
     * Crée la sidebar de navigation
     */
    private static JPanel createSidebar() {
        JPanel sidebar = new JPanel();
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));
        sidebar.setBackground(UIManager.getSurfaceVariant());
        sidebar.setPreferredSize(new Dimension(220, 0));
        sidebar.setBorder(new EmptyBorder(20, 0, 20, 0));
        
        // Logo / Titre
        JPanel logoPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 10));
        logoPanel.setBackground(UIManager.getSurfaceVariant());
        JLabel logoLabel = new JLabel("SECUREPHONE");
        logoLabel.setFont(UIManager.Fonts.SUBTITLE);
        logoLabel.setForeground(UIManager.getPrimary());
        logoPanel.add(logoLabel);
        sidebar.add(logoPanel);
        
        sidebar.add(Box.createVerticalStrut(10));
        sidebar.add(createDividerHorizontal());
        sidebar.add(Box.createVerticalStrut(20));
        
        // Menu items
        String[] menuItems = {
            "Typography",
            "Buttons", 
            "Input Fields",
            "Status Colors",
            "Notifications",
            "Audio System",
            "Theme Settings"
        };
        
        for (String item : menuItems) {
            JButton menuButton = createMenuButton(item);
            sidebar.add(menuButton);
            sidebar.add(Box.createVerticalStrut(5));
        }
        
        sidebar.add(Box.createVerticalGlue());
        
        // Version info en bas
        JPanel versionPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 5));
        versionPanel.setBackground(UIManager.getSurfaceVariant());
        JLabel versionLabel = new JLabel("Design System v1.0");
        UIManager.styleCaption(versionLabel);
        versionPanel.add(versionLabel);
        sidebar.add(versionPanel);
        
        return sidebar;
    }
    
    /**
     * Crée un bouton de menu
     */
    private static JButton createMenuButton(String text) {
        JButton button = new JButton(text);
        button.setFont(UIManager.Fonts.BODY);
        button.setForeground(UIManager.getTextPrimary());
        button.setBackground(UIManager.getSurfaceVariant());
        button.setHorizontalAlignment(SwingConstants.LEFT);
        button.setBorder(new EmptyBorder(10, 20, 10, 20));
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setContentAreaFilled(true);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setMaximumSize(new Dimension(220, 40));
        
        // Hover effect
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(UIManager.getHover());
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(UIManager.getSurfaceVariant());
            }
        });
        
        return button;
    }
    
    /**
     * Crée le header
     */
    private static JPanel createHeader() {
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(UIManager.getBackground());
        header.setBorder(new EmptyBorder(30, 40, 20, 40));
        
        // Titre et description
        JPanel titlePanel = new JPanel();
        titlePanel.setLayout(new BoxLayout(titlePanel, BoxLayout.Y_AXIS));
        titlePanel.setBackground(UIManager.getBackground());
        
        JLabel title = new JLabel("Design System Overview");
        UIManager.styleTitle(title);
        title.setAlignmentX(Component.LEFT_ALIGNMENT);
        titlePanel.add(title);
        
        titlePanel.add(Box.createVerticalStrut(8));
        
        JLabel subtitle = new JLabel("Complete UI component library for SecurePhone application");
        UIManager.styleBody(subtitle);
        subtitle.setForeground(UIManager.getTextSecondary());
        subtitle.setAlignmentX(Component.LEFT_ALIGNMENT);
        titlePanel.add(subtitle);
        
        header.add(titlePanel, BorderLayout.WEST);
        
        // Actions
        JPanel actionsPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        actionsPanel.setBackground(UIManager.getBackground());
        
        JButton refreshBtn = new JButton("Refresh");
        UIManager.styleSecondaryButton(refreshBtn);
        actionsPanel.add(refreshBtn);
        
        JButton exportBtn = new JButton("Export Specs");
        UIManager.stylePrimaryButton(exportBtn);
        actionsPanel.add(exportBtn);
        
        header.add(actionsPanel, BorderLayout.EAST);
        
        return header;
    }
    
    /**
     * Crée le contenu principal
     */
    private static JPanel createMainContent(JFrame frame) {
        JPanel content = new JPanel();
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
        content.setBorder(new EmptyBorder(0, 40, 40, 40));
        UIManager.stylePanel(content);
        
        // Section 1: Typography
        content.add(createTypographySection());
        content.add(Box.createVerticalStrut(40));
        
        // Section 2: Buttons
        content.add(createButtonsSection());
        content.add(Box.createVerticalStrut(40));
        
        // Section 3: Input Fields
        content.add(createInputSection());
        content.add(Box.createVerticalStrut(40));
        
        // Section 4: Status Colors
        content.add(createStatusSection());
        content.add(Box.createVerticalStrut(40));
        
        // Section 5: Notifications
        content.add(createNotificationsSection());
        content.add(Box.createVerticalStrut(40));
        
        // Section 6: Audio
        content.add(createAudioSection());
        content.add(Box.createVerticalStrut(40));
        
        // Section 7: Theme
        content.add(createThemeSection(frame));
        
        return content;
    }
    
    /**
     * Section Typography
     */
    private static JPanel createTypographySection() {
        JPanel section = createSection("Typography", "Font styles and text hierarchy");
        
        JPanel grid = new JPanel(new GridLayout(0, 1, 0, 15));
        grid.setBackground(UIManager.getSurface());
        
        // Title
        JPanel titleRow = createTypographyRow("Title", "Used for main headings and page titles");
        grid.add(titleRow);
        
        // Subtitle
        JPanel subtitleRow = createTypographyRow("Subtitle", "Used for section headers");
        grid.add(subtitleRow);
        
        // Body
        JPanel bodyRow = createTypographyRow("Body Text", "Standard text for content and descriptions");
        grid.add(bodyRow);
        
        // Caption
        JPanel captionRow = createTypographyRow("Caption", "Small text for metadata and annotations");
        grid.add(captionRow);
        
        section.add(grid);
        return section;
    }
    
    private static JPanel createTypographyRow(String sample, String description) {
        JPanel row = new JPanel(new BorderLayout(20, 5));
        row.setBackground(UIManager.getSurface());
        
        JLabel sampleLabel = new JLabel(sample);
        if (sample.equals("Title")) UIManager.styleTitle(sampleLabel);
        else if (sample.equals("Subtitle")) UIManager.styleSubtitle(sampleLabel);
        else if (sample.equals("Caption")) UIManager.styleCaption(sampleLabel);
        else UIManager.styleBody(sampleLabel);
        
        JLabel descLabel = new JLabel(description);
        UIManager.styleCaption(descLabel);
        
        row.add(sampleLabel, BorderLayout.WEST);
        row.add(descLabel, BorderLayout.CENTER);
        
        return row;
    }
    
    /**
     * Section Buttons
     */
    private static JPanel createButtonsSection() {
        JPanel section = createSection("Buttons", "Interactive button components");
        
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 15));
        buttonPanel.setBackground(UIManager.getSurface());
        
        JButton primaryBtn = new JButton("Primary Action");
        UIManager.stylePrimaryButton(primaryBtn);
        buttonPanel.add(primaryBtn);
        
        JButton secondaryBtn = new JButton("Secondary Action");
        UIManager.styleSecondaryButton(secondaryBtn);
        buttonPanel.add(secondaryBtn);
        
        JButton disabledBtn = new JButton("Disabled State");
        UIManager.styleSecondaryButton(disabledBtn);
        disabledBtn.setEnabled(false);
        buttonPanel.add(disabledBtn);
        
        section.add(buttonPanel);
        return section;
    }
    
    /**
     * Section Input Fields
     */
    private static JPanel createInputSection() {
        JPanel section = createSection("Input Fields", "Form input components");
        
        JPanel inputGrid = new JPanel();
        inputGrid.setLayout(new BoxLayout(inputGrid, BoxLayout.Y_AXIS));
        inputGrid.setBackground(UIManager.getSurface());
        
        // Text Field
        JLabel textLabel = new JLabel("Username");
        UIManager.styleBody(textLabel);
        textLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        inputGrid.add(textLabel);
        inputGrid.add(Box.createVerticalStrut(8));
        
        JTextField textField = new JTextField("alice@securephone.com");
        UIManager.styleTextField(textField);
        textField.setMaximumSize(new Dimension(400, 40));
        textField.setAlignmentX(Component.LEFT_ALIGNMENT);
        inputGrid.add(textField);
        inputGrid.add(Box.createVerticalStrut(20));
        
        // Password Field
        JLabel passLabel = new JLabel("Password");
        UIManager.styleBody(passLabel);
        passLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        inputGrid.add(passLabel);
        inputGrid.add(Box.createVerticalStrut(8));
        
        JPasswordField passField = new JPasswordField("securepassword123");
        UIManager.stylePasswordField(passField);
        passField.setMaximumSize(new Dimension(400, 40));
        passField.setAlignmentX(Component.LEFT_ALIGNMENT);
        inputGrid.add(passField);
        inputGrid.add(Box.createVerticalStrut(20));
        
        // 2FA Code
        JLabel codeLabel = new JLabel("Two-Factor Authentication Code");
        UIManager.styleBody(codeLabel);
        codeLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        inputGrid.add(codeLabel);
        inputGrid.add(Box.createVerticalStrut(8));
        
        JTextField codeField = new JTextField();
        UIManager.styleTextField(codeField);
        codeField.setMaximumSize(new Dimension(150, 40));
        codeField.setAlignmentX(Component.LEFT_ALIGNMENT);
        inputGrid.add(codeField);
        
        section.add(inputGrid);
        return section;
    }
    
    /**
     * Section Status Colors
     */
   /**
 * Section Status Colors
 */
private static JPanel createStatusSection() {
    JPanel section = createSection("Status Indicators", "Semantic color system");
    
    JPanel colorGrid = new JPanel(new GridLayout(2, 2, 20, 20));
    colorGrid.setBackground(UIManager.getSurface());
    
    // Utiliser les getters dynamiques qui se mettent à jour avec le thème
    colorGrid.add(createColorCard("Success", 
        UIManager.getSuccess(), 
        "Positive actions and confirmations"));
    
    colorGrid.add(createColorCard("Warning", 
        UIManager.getWarning(), 
        "Cautionary messages"));
    
    colorGrid.add(createColorCard("Error", 
        UIManager.getError(), 
        "Critical errors and failures"));
    
    colorGrid.add(createColorCard("Info", 
        UIManager.getInfo(), 
        "Informational messages"));
    
    section.add(colorGrid);
    return section;
}
    
    private static JPanel createColorCard(String title, Color initialColor, String description) {
    JPanel card = new JPanel(new BorderLayout(10, 10));
    card.setBackground(UIManager.getSurface());
    card.setBorder(BorderFactory.createCompoundBorder(
        BorderFactory.createLineBorder(UIManager.getBorder(), 1),
        new EmptyBorder(15, 15, 15, 15)
    ));
    
    // Color swatch - doit se mettre à jour dynamiquement
    JPanel swatch = new JPanel() {
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            // Recalculer la couleur basée sur le thème actuel
            Color statusColor = getStatusColorForTitle(title);
            setBackground(statusColor);
        }
    };
    swatch.setPreferredSize(new Dimension(60, 60));
    card.add(swatch, BorderLayout.WEST);
    
    // Info
    JPanel info = new JPanel();
    info.setLayout(new BoxLayout(info, BoxLayout.Y_AXIS));
    info.setBackground(UIManager.getSurface());
    
    JLabel titleLabel = new JLabel(title);
    UIManager.styleSubtitle(titleLabel);
    titleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
    info.add(titleLabel);
    
    info.add(Box.createVerticalStrut(5));
    
    JLabel descLabel = new JLabel(description);
    UIManager.styleCaption(descLabel);
    descLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
    info.add(descLabel);
    
    card.add(info, BorderLayout.CENTER);
    
    return card;
}

// Méthode utilitaire pour obtenir la bonne couleur selon le titre et le thème
private static Color getStatusColorForTitle(String title) {
    switch (title) {
        case "Success":
            return UIManager.getSuccess();
        case "Warning":
            return UIManager.getWarning();
        case "Error":
            return UIManager.getError();
        case "Info":
            return UIManager.getInfo();
        default:
            return Color.GRAY;
    }
}
    
    /**
     * Section Notifications
     */
    private static JPanel createNotificationsSection() {
        JPanel section = createSection("Notification System", "Toast notifications and alerts");
        
        JPanel notifPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 15));
        notifPanel.setBackground(UIManager.getSurface());
        
        JButton msgBtn = new JButton("Show Message");
        UIManager.stylePrimaryButton(msgBtn);
        msgBtn.addActionListener(e -> 
            NotificationManager.getInstance().showMessageNotification("Alice Cooper", "Meeting scheduled for 3 PM")
        );
        notifPanel.add(msgBtn);
        
        JButton callBtn = new JButton("Show Call");
        UIManager.styleSecondaryButton(callBtn);
        callBtn.addActionListener(e -> 
            NotificationManager.getInstance().showCallNotification("Bob Wilson", "video")
        );
        notifPanel.add(callBtn);
        
        JButton errorBtn = new JButton("Show Error");
        UIManager.styleSecondaryButton(errorBtn);
        errorBtn.addActionListener(e -> 
            NotificationManager.getInstance().showErrorNotification("Connection Failed", "Unable to reach server")
        );
        notifPanel.add(errorBtn);
        
        JButton successBtn = new JButton("Show Success");
        UIManager.styleSecondaryButton(successBtn);
        successBtn.addActionListener(e -> 
            NotificationManager.getInstance().showSuccessNotification("Message Sent", "Your message was delivered successfully")
        );
        notifPanel.add(successBtn);
        
        section.add(notifPanel);
        return section;
    }
    
    /**
     * Section Audio
     */
    private static JPanel createAudioSection() {
        JPanel section = createSection("Audio Feedback", "Sound notifications and alerts");
        
        JPanel audioPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 15));
        audioPanel.setBackground(UIManager.getSurface());
        
        JButton msgSound = new JButton("Message Received");
        UIManager.stylePrimaryButton(msgSound);
        msgSound.addActionListener(e -> SoundPlayer.getInstance().playMessageReceived());
        audioPanel.add(msgSound);
        
        JButton callSound = new JButton("Incoming Call");
        UIManager.styleSecondaryButton(callSound);
        callSound.addActionListener(e -> SoundPlayer.getInstance().playCallIncoming());
        audioPanel.add(callSound);
        
        JButton errorSound = new JButton("Error Alert");
        UIManager.styleSecondaryButton(errorSound);
        errorSound.addActionListener(e -> SoundPlayer.getInstance().playError());
        audioPanel.add(errorSound);
        
        JLabel note = new JLabel("Audio files must be present in resources/sounds/");
        UIManager.styleCaption(note);
        audioPanel.add(note);
        
        section.add(audioPanel);
        return section;
    }
    
    /**
     * Section Theme
     */
    private static JPanel createThemeSection(JFrame frame) {
        JPanel section = createSection("Theme Configuration", "Color scheme preferences");
        
        JPanel themePanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 15));
        themePanel.setBackground(UIManager.getSurface());
        
        JLabel currentLabel = new JLabel("Active Theme: " + UIManager.getCurrentTheme());
        UIManager.styleBody(currentLabel);
        themePanel.add(currentLabel);
        
        JButton darkBtn = new JButton("Dark Mode");
        UIManager.stylePrimaryButton(darkBtn);
        darkBtn.addActionListener(e -> {
            UIManager.setTheme(UIManager.Theme.DARK);
            UIManager.applyThemeRecursively(frame);
        });
        themePanel.add(darkBtn);
        
        JButton lightBtn = new JButton("Light Mode");
        UIManager.styleSecondaryButton(lightBtn);
        lightBtn.addActionListener(e -> {
            UIManager.setTheme(UIManager.Theme.LIGHT);
            UIManager.applyThemeRecursively(frame);
        });
        themePanel.add(lightBtn);
        
        section.add(themePanel);
        return section;
    }
    
    /**
     * Crée une section avec titre et description
     */
    private static JPanel createSection(String title, String description) {
        JPanel section = UIManager.createSurfacePanel();
        section.setLayout(new BoxLayout(section, BoxLayout.Y_AXIS));
        section.setAlignmentX(Component.LEFT_ALIGNMENT);
        section.setMaximumSize(new Dimension(Integer.MAX_VALUE, Integer.MAX_VALUE));
        
        // Header
        JLabel titleLabel = new JLabel(title);
        UIManager.styleSubtitle(titleLabel);
        titleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        section.add(titleLabel);
        
        section.add(Box.createVerticalStrut(5));
        
        JLabel descLabel = new JLabel(description);
        UIManager.styleCaption(descLabel);
        descLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        section.add(descLabel);
        
        section.add(Box.createVerticalStrut(20));
        
        return section;
    }
    
    /**
     * Crée un diviseur horizontal
     */
    private static JSeparator createDividerHorizontal() {
        JSeparator separator = new JSeparator(SwingConstants.HORIZONTAL);
        separator.setForeground(UIManager.getBorder());
        separator.setMaximumSize(new Dimension(Integer.MAX_VALUE, 1));
        return separator;
    }
    
    /**
     * Log des informations système
     */
    private static void logSystemInfo() {
        System.out.println("=====================================");
        System.out.println("  SECUREPHONE DESIGN SYSTEM");
        System.out.println("=====================================");
        System.out.println("Status: Initialized");
        System.out.println("Theme: " + UIManager.getCurrentTheme());
        System.out.println("Sound: " + (SoundPlayer.getInstance().isSoundEnabled() ? "Enabled" : "Disabled"));
        System.out.println("Notifications: " + (NotificationManager.getInstance().isNotificationsEnabled() ? "Enabled" : "Disabled"));
        System.out.println("=====================================");
        System.out.println();
        System.out.println("Component Status:");
        System.out.println("  [OK] UIManager");
        System.out.println("  [OK] SoundPlayer");
        System.out.println("  [OK] NotificationManager");
        System.out.println();
        System.out.println("Interface ready for demonstration.");
    }
}