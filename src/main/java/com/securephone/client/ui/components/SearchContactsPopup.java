package com.securephone.client.ui.components;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.List;
import com.securephone.client.ui.UIManager;

/**
 * SearchContactsPopup - Popup pour rechercher et ajouter des contacts
 * Avec barre de recherche et liste de résultats
 */
public class SearchContactsPopup extends JDialog {
    
    private JTextField searchField;
    private DefaultListModel<ContactResult> resultsModel;
    private JList<ContactResult> resultsList;
    private SearchContactListener onAddContact;
    private SearchListener onSearch;
    
    public SearchContactsPopup(Frame owner) {
        super(owner, "Add Contacts", false);
        initUI();
    }
    
    private void initUI() {
        setDefaultCloseOperation(JDialog.HIDE_ON_CLOSE);
        setSize(400, 450);
        setLocationRelativeTo(getOwner());
        setResizable(true);
        
        // Panel principal
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBackground(UIManager.getBackground());
        mainPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        
        // Header
        JLabel headerLabel = new JLabel("Find & Add Contacts");
        headerLabel.setFont(new Font("Arial", Font.BOLD, 16));
        headerLabel.setForeground(UIManager.getOnBackground());
        mainPanel.add(headerLabel, BorderLayout.NORTH);
        
        // Search panel
        JPanel searchPanel = new JPanel(new BorderLayout(5, 0));
        searchPanel.setBackground(UIManager.getBackground());
        
        searchField = new JTextField();
        searchField.setBackground(UIManager.getSurface());
        searchField.setForeground(UIManager.getOnSurface());
        searchField.setBorder(BorderFactory.createLineBorder(UIManager.getBorder(), 1));
        searchField.setPreferredSize(new Dimension(0, 30));
        searchField.addActionListener(e -> performSearch());
        searchPanel.add(searchField, BorderLayout.CENTER);
        
        JButton searchButton = new JButton("🔍");
        searchButton.setBackground(UIManager.getPrimary());
        searchButton.setForeground(UIManager.getOnPrimary());
        searchButton.setBorderPainted(false);
        searchButton.setFocusPainted(false);
        searchButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        searchButton.setPreferredSize(new Dimension(35, 30));
        searchButton.addActionListener(e -> performSearch());
        searchPanel.add(searchButton, BorderLayout.EAST);
        
        mainPanel.add(searchPanel, BorderLayout.NORTH);
        
        // Results list
        resultsModel = new DefaultListModel<>();
        resultsList = new JList<>(resultsModel);
        resultsList.setBackground(UIManager.getBackground());
        resultsList.setForeground(UIManager.getOnBackground());
        resultsList.setSelectionBackground(UIManager.getPrimary());
        resultsList.setSelectionForeground(UIManager.getOnPrimary());
        resultsList.setCellRenderer(new ContactResultRenderer());
        
        JScrollPane scrollPane = new JScrollPane(resultsList);
        scrollPane.getViewport().setBackground(UIManager.getBackground());
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        mainPanel.add(scrollPane, BorderLayout.CENTER);
        
        // Button panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 5, 0));
        buttonPanel.setBackground(UIManager.getBackground());
        
        JButton addButton = new JButton("Add Contact");
        addButton.setBackground(UIManager.getPrimary());
        addButton.setForeground(UIManager.getOnPrimary());
        addButton.setBorderPainted(false);
        addButton.setFocusPainted(false);
        addButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        addButton.addActionListener(e -> {
            ContactResult selected = resultsList.getSelectedValue();
            if (selected != null && onAddContact != null) {
                onAddContact.onAddContact(selected);
            }
        });
        buttonPanel.add(addButton);
        
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);
        
        setContentPane(mainPanel);
    }
    
    private void performSearch() {
        if (onSearch != null) {
            onSearch.onSearch(getSearchQuery());
        }
    }
    
    public String getSearchQuery() {
        return searchField.getText().trim();
    }
    
    public void setSearchResults(List<ContactResult> results) {
        resultsModel.clear();
        for (ContactResult result : results) {
            resultsModel.addElement(result);
        }
    }
    
    public ContactResult getSelectedContact() {
        return resultsList.getSelectedValue();
    }
    
    public void setOnSearch(SearchListener callback) {
        this.onSearch = callback;
    }
    
    public void setOnAddContact(SearchContactListener callback) {
        this.onAddContact = callback;
    }
    
    public void clearSearch() {
        searchField.setText("");
        resultsModel.clear();
    }
    
    // Listeners
    public interface SearchListener {
        void onSearch(String query);
    }
    
    public interface SearchContactListener {
        void onAddContact(ContactResult contact);
    }
    
    // Model pour les résultats de recherche
    public static class ContactResult {
        public int id;
        public String username;
        public String email;
        public String status;
        public boolean isContact;
        
        public ContactResult(int id, String username, String email, String status, boolean isContact) {
            this.id = id;
            this.username = username;
            this.email = email;
            this.status = status;
            this.isContact = isContact;
        }
        
        @Override
        public String toString() {
            return username + " (" + status + ")";
        }
    }
    
    // Custom renderer pour les résultats
    private class ContactResultRenderer extends DefaultListCellRenderer {
        @Override
        public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
            ContactResult contact = (ContactResult) value;
            
            JPanel panel = new JPanel(new BorderLayout(10, 5));
            panel.setBorder(new EmptyBorder(5, 5, 5, 5));
            
            if (isSelected) {
                panel.setBackground(UIManager.getPrimary());
            } else {
                panel.setBackground(UIManager.getSurface());
            }
            
            // Infos utilisateur
            JPanel infoPanel = new JPanel();
            infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
            infoPanel.setBackground(panel.getBackground());
            
            JLabel usernameLabel = new JLabel(contact.username);
            usernameLabel.setFont(new Font("Arial", Font.BOLD, 12));
            usernameLabel.setForeground(isSelected ? UIManager.getOnPrimary() : UIManager.getOnSurface());
            infoPanel.add(usernameLabel);
            
            JLabel emailLabel = new JLabel(contact.email);
            emailLabel.setFont(new Font("Arial", Font.PLAIN, 10));
            emailLabel.setForeground(isSelected ? UIManager.getOnPrimary() : UIManager.getOnBackground());
            infoPanel.add(emailLabel);
            
            panel.add(infoPanel, BorderLayout.CENTER);
            
            // Status
            JLabel statusLabel = new JLabel(contact.status);
            statusLabel.setFont(new Font("Arial", Font.PLAIN, 10));
            statusLabel.setForeground(isSelected ? UIManager.getOnPrimary() : UIManager.getOnBackground());
            panel.add(statusLabel, BorderLayout.EAST);
            
            // Badge "Already Contact"
            if (contact.isContact) {
                JLabel badgeLabel = new JLabel("✓ Contact");
                badgeLabel.setFont(new Font("Arial", Font.BOLD, 9));
                badgeLabel.setForeground(new Color(76, 175, 80));
                panel.add(badgeLabel, BorderLayout.WEST);
            }
            
            return panel;
        }
    }
}
