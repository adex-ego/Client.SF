package com.securephone.client.ui.pages;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import com.securephone.client.models.Contact;
import com.securephone.client.ui.UIManager;
import com.securephone.client.ui.frames.MainFrame;
import com.securephone.client.SecurePhoneApp;
import com.securephone.client.network.ConnectionManager;

/**
 * AppPage - Page principale de l'application après connexion
 * Affiche: Contacts, Chat, Boutons d'appel
 */
public class AppPage extends JPanel {
    
    private MainFrame mainFrame;
    private String currentUsername = "";
    private JLabel statusLabel;
    private DefaultListModel<String> contactsListModel;
    private JList<String> contactsList;
    private JScrollPane contactsScrollPane;
    
    // Chat components
    private JTextArea chatArea;
    private JTextField messageInput;
    private JButton sendButton;
    private JButton audioCallButton;
    private JButton videoCallButton;
    private JLabel selectedContactLabel;
    
    // Track selected contact
    private String selectedContactName = null;
    private List<Contact> allContacts = new ArrayList<>();
    
    public AppPage(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
        initUI();
        setupListeners();
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
        
        // Panneau du haut: contact sélectionné + boutons
        JPanel topPanel = createTopControlPanel();
        chatArea.add(topPanel, BorderLayout.NORTH);
        
        // Panneau du milieu: zone de chat
        JPanel messagePanel = createMessagePanel();
        chatArea.add(messagePanel, BorderLayout.CENTER);
        
        // Panneau du bas: entrée de message
        JPanel inputPanel = createInputPanel();
        chatArea.add(inputPanel, BorderLayout.SOUTH);
        
        return chatArea;
    }
    
    private JPanel createTopControlPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(UIManager.getSurface());
        panel.setBorder(new EmptyBorder(5, 5, 5, 5));
        
        // Contact name label
        selectedContactLabel = new JLabel("Sélectionnez un contact");
        selectedContactLabel.setFont(new Font("Arial", Font.BOLD, 14));
        selectedContactLabel.setForeground(UIManager.getOnSurface());
        panel.add(selectedContactLabel, BorderLayout.WEST);
        
        // Boutons d'appel
        JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonsPanel.setBackground(UIManager.getSurface());
        
        audioCallButton = new JButton("🎙️ Appel Audio");
        audioCallButton.setFont(new Font("Arial", Font.PLAIN, 12));
        audioCallButton.setEnabled(false);
        audioCallButton.addActionListener(e -> initiateAudioCall());
        buttonsPanel.add(audioCallButton);
        
        videoCallButton = new JButton("📹 Appel Vidéo");
        videoCallButton.setFont(new Font("Arial", Font.PLAIN, 12));
        videoCallButton.setEnabled(false);
        videoCallButton.addActionListener(e -> initiateVideoCall());
        buttonsPanel.add(videoCallButton);
        
        panel.add(buttonsPanel, BorderLayout.EAST);
        
        return panel;
    }
    
    private JPanel createMessagePanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(UIManager.getBackground());
        panel.setBorder(new EmptyBorder(5, 5, 5, 5));
        
        // Chat area with scroll
        chatArea = new JTextArea();
        chatArea.setEditable(false);
        chatArea.setBackground(UIManager.getSurface());
        chatArea.setForeground(UIManager.getOnSurface());
        chatArea.setFont(new Font("Monospaced", Font.PLAIN, 11));
        chatArea.setLineWrap(true);
        chatArea.setWrapStyleWord(true);
        chatArea.setBorder(new EmptyBorder(5, 5, 5, 5));
        
        JScrollPane scrollPane = new JScrollPane(chatArea);
        scrollPane.getViewport().setBackground(UIManager.getSurface());
        scrollPane.setBorder(BorderFactory.createLineBorder(UIManager.getBorder()));
        
        panel.add(scrollPane, BorderLayout.CENTER);
        
        return panel;
    }
    
    private JPanel createInputPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(UIManager.getBackground());
        panel.setBorder(new EmptyBorder(5, 5, 5, 5));
        
        // Input field
        messageInput = new JTextField();
        messageInput.setBackground(UIManager.getSurface());
        messageInput.setForeground(UIManager.getOnSurface());
        messageInput.setEnabled(false);
        messageInput.setFont(new Font("Arial", Font.PLAIN, 12));
        messageInput.setBorder(BorderFactory.createLineBorder(UIManager.getBorder()));
        messageInput.addKeyListener(new java.awt.event.KeyAdapter() {
            @Override
            public void keyPressed(java.awt.event.KeyEvent e) {
                if (e.getKeyCode() == java.awt.event.KeyEvent.VK_ENTER && !messageInput.getText().isEmpty()) {
                    sendMessage();
                }
            }
        });
        
        panel.add(messageInput, BorderLayout.CENTER);
        
        // Send button
        sendButton = new JButton("Envoyer");
        sendButton.setEnabled(false);
        sendButton.addActionListener(e -> sendMessage());
        
        panel.add(sendButton, BorderLayout.EAST);
        
        return panel;
    }
    
    private void setupListeners() {
        // Contact selection listener
        contactsList.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && contactsList.getSelectedIndex() >= 0) {
                int index = contactsList.getSelectedIndex();
                if (index < allContacts.size()) {
                    Contact contact = allContacts.get(index);
                    selectContact(contact.getName());
                }
            }
        });
        
        // Chat listener - for incoming messages
        ConnectionManager cm = SecurePhoneApp.getConnectionManager();
        if (cm != null) {
            cm.setChatListener(message -> {
                SwingUtilities.invokeLater(() -> {
                    appendMessage("[" + message.getSender() + "]: " + message.getContent());
                });
            });
        }
    }
    
    private void selectContact(String contactName) {
        this.selectedContactName = contactName;
        selectedContactLabel.setText("Chat avec: " + contactName);
        chatArea.setText("");
        messageInput.setEnabled(true);
        sendButton.setEnabled(true);
        audioCallButton.setEnabled(true);
        videoCallButton.setEnabled(true);
    }
    
    private void initiateAudioCall() {
        if (selectedContactName == null) {
            JOptionPane.showMessageDialog(this, "Sélectionnez un contact", "Erreur", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        try {
            ConnectionManager cm = SecurePhoneApp.getConnectionManager();
            cm.initiateCall(selectedContactName, "audio");
            appendMessage("[SYSTEM]: Appel audio initialisé avec " + selectedContactName + "...");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Erreur appel audio: " + ex.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void initiateVideoCall() {
        if (selectedContactName == null) {
            JOptionPane.showMessageDialog(this, "Sélectionnez un contact", "Erreur", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        try {
            ConnectionManager cm = SecurePhoneApp.getConnectionManager();
            cm.initiateCall(selectedContactName, "video");
            appendMessage("[SYSTEM]: Appel vidéo initialisé avec " + selectedContactName + "...");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Erreur appel vidéo: " + ex.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void sendMessage() {
        if (selectedContactName == null || messageInput.getText().isEmpty()) {
            return;
        }
        
        String message = messageInput.getText();
        try {
            ConnectionManager cm = SecurePhoneApp.getConnectionManager();
            cm.sendChatMessage(message, selectedContactName);
            appendMessage("[Vous]: " + message);
            messageInput.setText("");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Erreur envoi: " + ex.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void appendMessage(String text) {
        chatArea.append(text + "\n");
        // Auto scroll to bottom
        chatArea.setCaretPosition(chatArea.getDocument().getLength());
    }
    
    public void setUsername(String username) {
        this.currentUsername = username;
        if (statusLabel != null) {
            statusLabel.setText("Connected as: " + username);
        }
    }
    
    public void displayContacts(List<Contact> contacts) {
        contactsListModel.clear();
        this.allContacts = new ArrayList<>(contacts);
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
