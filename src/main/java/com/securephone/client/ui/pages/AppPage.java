package com.securephone.client.ui.pages;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.List;
import com.securephone.client.models.Contact;
import com.securephone.client.ui.UIManager;
import com.securephone.client.ui.frames.MainFrame;

/**
 * AppPage - Page principale de l'application après connexion
 * Affiche: Header, Navigation fixe, Colonne de contacts (1/4), Zone de contenu
 */
public class AppPage extends JPanel {
    
    private MainFrame mainFrame;
    private String currentUsername = "";
    private JLabel statusLabel;
    private DefaultListModel<String> contactsListModel;
    private JList<String> contactsList;
    private JScrollPane contactsScrollPane;
    
    public AppPage(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
        initUI();
    }
    
    private void initUI() {
        setLayout(new BorderLayout());
        setBackground(UIManager.getBackground());
        
        // Header
        add(createHeader(), BorderLayout.NORTH);
        
        // Main content with contacts sidebar
        add(createMainContent(), BorderLayout.CENTER);
    }
    
    private JPanel createHeader() {
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(UIManager.getPrimary());
        header.setBorder(new EmptyBorder(10, 15, 10, 15));
        
        // Title
        JLabel titleLabel = new JLabel("SecurePhone");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        titleLabel.setForeground(UIManager.getOnPrimary());
        header.add(titleLabel, BorderLayout.WEST);
        
        // Status
        statusLabel = new JLabel("Connected as: " + currentUsername);
        statusLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        statusLabel.setForeground(UIManager.getOnPrimary());
        header.add(statusLabel, BorderLayout.EAST);
        
        return header;
    }
    
    private JPanel createMainContent() {
        JPanel mainContent = new JPanel(new BorderLayout());
        mainContent.setBackground(UIManager.getBackground());
        
        // Sidebar des contacts (1/4 de la largeur)
        mainContent.add(createContactsSidebar(), BorderLayout.WEST);
        
        // Zone de contenu principal (3/4)
        mainContent.add(createChatArea(), BorderLayout.CENTER);
        
        return mainContent;
    }
    
    private JPanel createContactsSidebar() {
        JPanel sidebar = new JPanel(new BorderLayout());
        sidebar.setBackground(UIManager.getSurface());
        sidebar.setBorder(BorderFactory.createMatteBorder(0, 0, 0, 1, UIManager.getBorder()));
        sidebar.setPreferredSize(new Dimension(250, 0));
        
        // Header de la colonne
        JLabel contactsTitle = new JLabel("Contacts");
        contactsTitle.setFont(new Font("Arial", Font.BOLD, 14));
        contactsTitle.setForeground(UIManager.getOnSurface());
        contactsTitle.setBorder(new EmptyBorder(10, 10, 10, 10));
        contactsTitle.setBackground(UIManager.getSurface());
        contactsTitle.setOpaque(true);
        sidebar.add(contactsTitle, BorderLayout.NORTH);
        
        // Liste des contacts
        contactsListModel = new DefaultListModel<>();
        contactsList = new JList<>(contactsListModel);
        contactsList.setBackground(UIManager.getSurface());
        contactsList.setForeground(UIManager.getOnSurface());
        contactsList.setSelectionBackground(UIManager.getPrimary());
        contactsList.setSelectionForeground(UIManager.getOnPrimary());
        contactsList.setCellRenderer(new ContactListRenderer());
        
        contactsScrollPane = new JScrollPane(contactsList);
        contactsScrollPane.getViewport().setBackground(UIManager.getSurface());
        contactsScrollPane.setBorder(BorderFactory.createEmptyBorder());
        contactsScrollPane.getVerticalScrollBar().setBackground(UIManager.getSurface());
        contactsScrollPane.getHorizontalScrollBar().setBackground(UIManager.getSurface());
        sidebar.add(contactsScrollPane, BorderLayout.CENTER);
        
        return sidebar;
    }
    
    private JPanel createChatArea() {
        JPanel chatArea = new JPanel(new BorderLayout());
        chatArea.setBackground(UIManager.getBackground());
        chatArea.setBorder(new EmptyBorder(10, 10, 10, 10));
        
        // Message pour l'utilisateur
        JLabel welcomeLabel = new JLabel("Sélectionnez un contact pour démarrer une conversation");
        welcomeLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        welcomeLabel.setForeground(UIManager.getOnBackground());
        welcomeLabel.setHorizontalAlignment(SwingConstants.CENTER);
        chatArea.add(welcomeLabel, BorderLayout.CENTER);
        
        return chatArea;
    }
    
    public void setUsername(String username) {
        this.currentUsername = username;
        if (statusLabel != null) {
            statusLabel.setText("Connected as: " + username);
        }
    }
    
    public void displayContacts(List<Contact> contacts) {
        contactsListModel.clear();
        for (Contact contact : contacts) {
            String status = contact.getStatus() != null ? " (" + contact.getStatus() + ")" : "";
            contactsListModel.addElement(contact.getName() + status);
        }
    }
    
    // Custom renderer pour les contacts avec bonnes couleurs
    private class ContactListRenderer extends DefaultListCellRenderer {
        @Override
        public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
            Component c = super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
            
            if (isSelected) {
                c.setBackground(UIManager.getPrimary());
                c.setForeground(UIManager.getOnPrimary());
            } else {
                c.setBackground(UIManager.getSurface());
                c.setForeground(UIManager.getOnSurface());
            }
            
            return c;
        }
    }
    
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        setBackground(UIManager.getBackground());
    }
}
