package com.securephone.client.ui;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

/**
 * UIManager - Gestionnaire global des thèmes et styles de l'application
 * 
 * Responsabilités:
 * - Gestion thème sombre/clair
 * - Couleurs globales
 * - Polices système
 * - Méthodes utilitaires de styling
 * 
 * @author Hatsu
 */
public class UIManager {
    
    // ========== THÈME ACTUEL ==========
    private static Theme currentTheme;
    private static final String THEME_FILE = "client/resources/theme.cfg";
    
    // ========== ÉNUMÉRATION DES THÈMES ==========
    public enum Theme {
        DARK, LIGHT
    }
    
    // Bloc d'initialisation statique - charge le thème au démarrage
    static {
        currentTheme = loadTheme();
    }

    // ========== GETTERS POUR COULEURS DE STATUT ==========

    public static Color getSuccess() {
        return currentTheme == Theme.DARK ? DarkTheme.SUCCESS : LightTheme.SUCCESS;
    }

    public static Color getWarning() {
        return currentTheme == Theme.DARK ? DarkTheme.WARNING : LightTheme.WARNING;
    }

    public static Color getError() {
        return currentTheme == Theme.DARK ? DarkTheme.ERROR : LightTheme.ERROR;
    }

    public static Color getInfo() {
        return currentTheme == Theme.DARK ? DarkTheme.INFO : LightTheme.INFO;
    }
    
    // ========== COULEURS THÈME SOMBRE ==========
    public static class DarkTheme {
        // Couleurs principales
        public static final Color BACKGROUND = new Color(18, 18, 18);           // Fond principal noir
        public static final Color SURFACE = new Color(25, 25, 25);              // Surfaces (cartes, panels)
        public static final Color SURFACE_VARIANT = new Color(35, 35, 35);      // Surface alternative
        
        // Couleurs de texte
        public static final Color TEXT_PRIMARY = new Color(255, 255, 255);      // Texte principal blanc
        public static final Color TEXT_SECONDARY = new Color(200, 200, 200);    // Texte secondaire
        public static final Color TEXT_DISABLED = new Color(120, 120, 120);     // Texte désactivé
        
        // Couleurs d'accent
        public static final Color PRIMARY = new Color(100, 200, 255);           // Bleu principal
        public static final Color PRIMARY_VARIANT = new Color(80, 160, 215);    // Bleu variant
        public static final Color SECONDARY = new Color(150, 100, 255);         // Violet secondaire
        
        // Couleurs de statut
        public static final Color SUCCESS = new Color(76, 175, 80);             // Vert (succès)
        public static final Color WARNING = new Color(255, 152, 0);             // Orange (warning)
        public static final Color ERROR = new Color(244, 67, 54);               // Rouge (erreur)
        public static final Color INFO = new Color(33, 150, 243);               // Bleu info
        
        // Bordures et séparateurs
        public static final Color BORDER = new Color(50, 50, 50);
        public static final Color DIVIDER = new Color(40, 40, 40);
        
        // États interactifs
        public static final Color HOVER = new Color(45, 45, 45);
        public static final Color PRESSED = new Color(55, 55, 55);
        public static final Color SELECTED = new Color(100, 200, 255, 30);
    }
    
    // ========== COULEURS THÈME CLAIR ==========
    public static class LightTheme {
        // Couleurs principales
        public static final Color BACKGROUND = new Color(255, 255, 255);        // Fond blanc
        public static final Color SURFACE = new Color(250, 250, 250);           // Surfaces
        public static final Color SURFACE_VARIANT = new Color(245, 245, 245);   // Surface alternative
        
        // Couleurs de texte
        public static final Color TEXT_PRIMARY = new Color(20, 20, 20);         // Texte principal noir
        public static final Color TEXT_SECONDARY = new Color(80, 80, 80);       // Texte secondaire
        public static final Color TEXT_DISABLED = new Color(158, 158, 158);     // Texte désactivé
        
        // Couleurs d'accent
        public static final Color PRIMARY = new Color(33, 150, 243);
        public static final Color PRIMARY_VARIANT = new Color(25, 118, 210);
        public static final Color SECONDARY = new Color(103, 58, 183);
        
        // Couleurs de statut
        public static final Color SUCCESS = new Color(76, 175, 80);
        public static final Color WARNING = new Color(255, 152, 0);
        public static final Color ERROR = new Color(244, 67, 54);
        public static final Color INFO = new Color(33, 150, 243);
        
        // Bordures et séparateurs
        public static final Color BORDER = new Color(230, 230, 230);
        public static final Color DIVIDER = new Color(240, 240, 240);
        
        // États interactifs
        public static final Color HOVER = new Color(240, 240, 240);
        public static final Color PRESSED = new Color(230, 230, 230);
        public static final Color SELECTED = new Color(33, 150, 243, 20);
    }
    
    // ========== POLICES ==========
    public static class Fonts {
        public static final Font TITLE = new Font("Segoe UI", Font.BOLD, 24);
        public static final Font SUBTITLE = new Font("Segoe UI", Font.BOLD, 18);
        public static final Font BODY = new Font("Segoe UI", Font.PLAIN, 14);
        public static final Font BODY_BOLD = new Font("Segoe UI", Font.BOLD, 14);
        public static final Font CAPTION = new Font("Segoe UI", Font.PLAIN, 12);
        public static final Font BUTTON = new Font("Segoe UI", Font.BOLD, 14);
        public static final Font CODE = new Font("Consolas", Font.PLAIN, 13);
    }
    
    // ========== DIMENSIONS ==========
    public static class Dimensions {
        public static final int BORDER_RADIUS = 8;
        public static final int PADDING_SMALL = 8;
        public static final int PADDING_MEDIUM = 16;
        public static final int PADDING_LARGE = 24;
        public static final int SPACING_SMALL = 4;
        public static final int SPACING_MEDIUM = 8;
        public static final int SPACING_LARGE = 16;
        public static final int BUTTON_HEIGHT = 36;
        public static final int INPUT_HEIGHT = 40;
    }
    
    // ========== GETTERS DYNAMIQUES (selon thème actuel) ==========
    
    public static Color getBackground() {
        return currentTheme == Theme.DARK ? DarkTheme.BACKGROUND : LightTheme.BACKGROUND;
    }
    
    public static Color getSurface() {
        return currentTheme == Theme.DARK ? DarkTheme.SURFACE : LightTheme.SURFACE;
    }
    
    public static Color getSurfaceVariant() {
        return currentTheme == Theme.DARK ? DarkTheme.SURFACE_VARIANT : LightTheme.SURFACE_VARIANT;
    }
    
    public static Color getTextPrimary() {
        return currentTheme == Theme.DARK ? DarkTheme.TEXT_PRIMARY : LightTheme.TEXT_PRIMARY;
    }
    
    public static Color getTextSecondary() {
        return currentTheme == Theme.DARK ? DarkTheme.TEXT_SECONDARY : LightTheme.TEXT_SECONDARY;
    }
    
    public static Color getPrimary() {
        return currentTheme == Theme.DARK ? DarkTheme.PRIMARY : LightTheme.PRIMARY;
    }
    
    public static Color getPrimaryVariant() {
        // Couleur un peu plus sombre/claire que le primary pour hover effect
        Color primary = getPrimary();
        if (currentTheme == Theme.DARK) {
            return new Color(
                Math.min(primary.getRed() + 30, 255),
                Math.min(primary.getGreen() + 30, 255),
                Math.min(primary.getBlue() + 30, 255)
            );
        } else {
            return new Color(
                Math.max(primary.getRed() - 30, 0),
                Math.max(primary.getGreen() - 30, 0),
                Math.max(primary.getBlue() - 30, 0)
            );
        }
    }
    
    public static Color getBorder() {
        return currentTheme == Theme.DARK ? DarkTheme.BORDER : LightTheme.BORDER;
    }
    
    public static Color getHover() {
        return currentTheme == Theme.DARK ? DarkTheme.HOVER : LightTheme.HOVER;
    }
    
    public static Color getOnPrimary() {
        return Color.WHITE;
    }
    
    public static Color getOnBackground() {
        return currentTheme == Theme.DARK ? DarkTheme.TEXT_PRIMARY : LightTheme.TEXT_PRIMARY;
    }
    
    public static Color getOnSurface() {
        return currentTheme == Theme.DARK ? DarkTheme.TEXT_PRIMARY : LightTheme.TEXT_PRIMARY;
    }
    
    // ========== MÉTHODES UTILITAIRES ==========
    
    public static void refreshAllWindows() {
    for (Window window : Window.getWindows()) {
        SwingUtilities.updateComponentTreeUI(window);
        window.repaint();
        }
    }


    /**
     * Change le thème de l'application
     */
    public static void setTheme(Theme theme) {
        if (currentTheme == theme) return;

        currentTheme = theme;
        saveTheme(theme);

        // 🔄 rafraîchissement immédiat de toute l'UI
        refreshAllWindows();
    }
    
    /**
     * Bascule le thème (DARK <-> LIGHT)
     */
    public static void toggleTheme() {
        Theme newTheme = currentTheme == Theme.DARK ? Theme.LIGHT : Theme.DARK;
        setTheme(newTheme);
    }

    
    /**
     * Retourne le thème actuel
     */
    public static Theme getCurrentTheme() {
        return currentTheme;
    }
    
    /**
     * Sauvegarde le thème dans un fichier
     */
    public static void saveTheme(Theme theme) {
    try {
        java.io.File file = new java.io.File("client/resources/theme.cfg");
        file.getParentFile().mkdirs();
        java.io.FileWriter writer = new java.io.FileWriter(file);
        writer.write(theme.name());
        writer.close();
        System.out.println("✓ Thème sauvegardé: " + theme);
    } catch (java.io.IOException e) {
        System.err.println("Erreur lors de la sauvegarde du thème: " + e.getMessage());
    }
}

    
    /**
     * Charge le thème depuis le fichier
     */
    private static Theme loadTheme() {
        try {
            java.io.File file = new java.io.File(THEME_FILE);
            if (!file.exists()) {
                System.out.println("Aucun thème sauvegardé, utilisation du thème DARK par défaut");
                return Theme.DARK;
            }
            
            java.io.BufferedReader reader = new java.io.BufferedReader(new java.io.FileReader(file));
            String themeName = reader.readLine();
            reader.close();
            
            Theme loaded = Theme.valueOf(themeName);
            System.out.println("✓ Thème chargé: " + loaded);
            return loaded;
        } catch (Exception e) {
            System.err.println("Erreur lors du chargement du thème, utilisation de DARK: " + e.getMessage());
            return Theme.DARK;
        }
    }
    
    /**
     * Style un JButton avec le style primaire
     */
    public static void stylePrimaryButton(JButton button) {
        button.setFont(Fonts.BUTTON);
        button.setBackground(getPrimary());
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setPreferredSize(new Dimension(button.getPreferredSize().width, Dimensions.BUTTON_HEIGHT));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        // Effet hover
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(currentTheme == Theme.DARK ? 
                    DarkTheme.PRIMARY_VARIANT : LightTheme.PRIMARY_VARIANT);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(getPrimary());
            }
        });
    }
    
    /**
     * Style un JButton avec le style secondaire
     */
    public static void styleSecondaryButton(JButton button) {
        button.setFont(Fonts.BUTTON);
        button.setBackground(getSurfaceVariant());
        button.setForeground(getTextPrimary());
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setPreferredSize(new Dimension(button.getPreferredSize().width, Dimensions.BUTTON_HEIGHT));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        // Effet hover
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(getHover());
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(getSurfaceVariant());
            }
        });
    }
    
    /**
     * Style un JButton icône transparent
     */
    public static void styleIconButton(JButton button) {
        button.setFont(new Font("Segoe UI", Font.PLAIN, 18));
        button.setBackground(new Color(0, 0, 0, 0)); // Transparent
        button.setForeground(getPrimary());
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setContentAreaFilled(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setPreferredSize(new Dimension(40, 40));
    }
    
    /**
     * Style un JTextField
     */
    public static void styleTextField(JTextField field) {
        field.setFont(Fonts.BODY);
        field.setBackground(getSurfaceVariant());
        field.setForeground(getTextPrimary());
        field.setCaretColor(getTextPrimary());
        field.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(getBorder(), 1),
            new EmptyBorder(Dimensions.PADDING_SMALL, Dimensions.PADDING_MEDIUM, 
                          Dimensions.PADDING_SMALL, Dimensions.PADDING_MEDIUM)
        ));
        field.setPreferredSize(new Dimension(field.getPreferredSize().width, Dimensions.INPUT_HEIGHT));
    }
    
    /**
     * Style un JPasswordField
     */
    public static void stylePasswordField(JPasswordField field) {
        field.setFont(Fonts.BODY);
        field.setBackground(getSurfaceVariant());
        field.setForeground(getTextPrimary());
        field.setCaretColor(getTextPrimary());
        field.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(getBorder(), 1),
            new EmptyBorder(Dimensions.PADDING_SMALL, Dimensions.PADDING_MEDIUM, 
                          Dimensions.PADDING_SMALL, Dimensions.PADDING_MEDIUM)
        ));
        field.setPreferredSize(new Dimension(field.getPreferredSize().width, Dimensions.INPUT_HEIGHT));
    }
    
    /**
     * Style un JLabel comme titre
     */
    public static void styleTitle(JLabel label) {
        label.setFont(Fonts.TITLE);
        label.setForeground(getTextPrimary());
    }
    
    /**
     * Style un JLabel comme sous-titre
     */
    public static void styleSubtitle(JLabel label) {
        label.setFont(Fonts.SUBTITLE);
        label.setForeground(getTextPrimary());
    }
    
    /**
     * Style un JLabel comme texte normal
     */
    public static void styleBody(JLabel label) {
        label.setFont(Fonts.BODY);
        label.setForeground(getTextPrimary());
    }
    
    /**
     * Style un JLabel comme caption (petit texte)
     */
    public static void styleCaption(JLabel label) {
        label.setFont(Fonts.CAPTION);
        label.setForeground(getTextSecondary());
    }
    
    /**
     * Style une JFrame
     */
    public static void styleFrame(JFrame frame) {
        frame.getContentPane().setBackground(getBackground());
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
    
    /**
     * Style un JPanel
     */
    public static void stylePanel(JPanel panel) {
        panel.setBackground(getBackground());
    }
    
    /**
     * Crée un panel avec surface (carte)
     */
    public static JPanel createSurfacePanel() {
        JPanel panel = new JPanel();
        panel.setBackground(getSurface());
        panel.setBorder(new EmptyBorder(Dimensions.PADDING_MEDIUM, Dimensions.PADDING_MEDIUM,
                                       Dimensions.PADDING_MEDIUM, Dimensions.PADDING_MEDIUM));
        return panel;
    }
    
    /**
     * Crée un séparateur horizontal
     */
    public static JSeparator createDivider() {
        JSeparator separator = new JSeparator(SwingConstants.HORIZONTAL);
        separator.setForeground(currentTheme == Theme.DARK ? DarkTheme.DIVIDER : LightTheme.DIVIDER);
        return separator;
    }

    public static void applyThemeRecursively(Component comp) {
        if (comp instanceof JFrame) {
            JFrame frame = (JFrame) comp;
            frame.getContentPane().setBackground(getBackground());
        } else if (comp instanceof JPanel) {
            comp.setBackground(getBackground());
        } else if (comp instanceof JButton) {
            JButton btn = (JButton) comp;
            btn.setBackground(getSurfaceVariant());
            btn.setForeground(getTextPrimary());
        } else if (comp instanceof JLabel) {
            comp.setForeground(getTextPrimary());
        } else if (comp instanceof JTextField) {
            JTextField field = (JTextField) comp;
            field.setBackground(getSurfaceVariant());
            field.setForeground(getTextPrimary());
            field.setCaretColor(getTextPrimary());
        } else if (comp instanceof JPasswordField) {
            JPasswordField field = (JPasswordField) comp;
            field.setBackground(getSurfaceVariant());
            field.setForeground(getTextPrimary());
            field.setCaretColor(getTextPrimary());
        }
        
        if (comp instanceof Container) {
            for (Component child : ((Container) comp).getComponents()) {
                applyThemeRecursively(child);
            }
        }
        comp.repaint();
    }

}