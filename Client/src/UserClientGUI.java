package Client.src;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class UserClientGUI extends ClientGUI {
    private JTextField usernameField, passwordField;
    private JButton loginButton, signupButton, logoutButton;
    private JPanel mainPanel, eventPanel;
    private boolean loggedIn = false;
    private UserInfo currentUser;
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            UserClientGUI gui = new UserClientGUI();
            gui.setVisible(true);
        });
    }

    public UserClientGUI() {
        super("Σύστημα Κρατήσεων - Χρήστης");
        initializeComponents();
        setupLayout();
        setupEventHandlers();
        updateUIState();
    }
    
    private void initializeComponents() {
        usernameField = new JTextField(15);
        passwordField = new JPasswordField(15);
        loginButton = new JButton("Σύνδεση");
        signupButton = new JButton("Εγγραφή");
        logoutButton = new JButton("Αποσύνδεση");
        
        mainPanel = new JPanel(new BorderLayout());
        eventPanel = new JPanel(); // Θα γεμίσει με τα events αργότερα
    }
    
    private void setupLayout() {
        JPanel loginPanel = new JPanel(new GridLayout(3, 2, 5, 5));
        loginPanel.add(new JLabel("Όνομα χρήστη:"));
        loginPanel.add(usernameField);
        loginPanel.add(new JLabel("Κωδικός:"));
        loginPanel.add(passwordField);
        loginPanel.add(signupButton);
        loginPanel.add(loginButton);
        
        mainPanel.add(loginPanel, BorderLayout.NORTH);
        mainPanel.add(new JScrollPane(eventPanel), BorderLayout.CENTER);
        mainPanel.add(logoutButton, BorderLayout.SOUTH);
        
        add(mainPanel);
    }
    
    private void setupEventHandlers() {
        loginButton.addActionListener(e -> login());
        signupButton.addActionListener(e -> signup());
        logoutButton.addActionListener(e -> logout());
    }
    
    private void login() {
        try {
            UserInfo user = new UserInfo();
            user.setUsername(usernameField.getText());
            user.setPassword(passwordField.getText());
            
            server.userLogin(user);
            currentUser = user;
            loggedIn = true;
            updateUIState();
            loadEvents();
            showSuccess("Επιτυχής σύνδεση!");
        } catch (Exception e) {
            showError("Σφάλμα σύνδεσης: " + e.getMessage());
        }
    }
    
    private void signup() {
        // Δημιουργία παραθύρου για εγγραφή νέου χρήστη
        JPanel panel = new JPanel(new GridLayout(7, 2, 5, 5));
        
        JTextField fullnameField = new JTextField();
        JTextField phoneField = new JTextField();
        JTextField emailField = new JTextField();
        JTextField addressField = new JTextField();
        JTextField newUsernameField = new JTextField();
        JPasswordField newPasswordField = new JPasswordField();
        
        panel.add(new JLabel("Ονοματεπώνυμο:"));
        panel.add(fullnameField);
        panel.add(new JLabel("Τηλέφωνο:"));
        panel.add(phoneField);
        panel.add(new JLabel("Email:"));
        panel.add(emailField);
        panel.add(new JLabel("Διεύθυνση:"));
        panel.add(addressField);
        panel.add(new JLabel("Όνομα χρήστη:"));
        panel.add(newUsernameField);
        panel.add(new JLabel("Κωδικός:"));
        panel.add(newPasswordField);
        
        int result = JOptionPane.showConfirmDialog(this, panel, "Εγγραφή νέου χρήστη", 
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        
        if (result == JOptionPane.OK_OPTION) {
            try {
                UserInfo newUser = new UserInfo(
                    fullnameField.getText(),
                    Integer.parseInt(phoneField.getText()),
                    emailField.getText(),
                    addressField.getText(),
                    newUsernameField.getText(),
                    new String(newPasswordField.getPassword()),
                    false
                );
                
                server.userSignUp(newUser);
                showSuccess("Επιτυχής εγγραφή! Μπορείτε τώρα να συνδεθείτε.");
            } catch (Exception e) {
                showError("Σφάλμα εγγραφής: " + e.getMessage());
            }
        }
    }
    
    private void logout() {
        try {
            server.userLogout(currentUser);
            loggedIn = false;
            currentUser = null;
            updateUIState();
            showSuccess("Επιτυχής αποσύνδεση!");
        } catch (Exception e) {
            showError("Σφάλμα αποσύνδεσης: " + e.getMessage());
        }
    }
    
    private void loadEvents() {
        // Υλοποίηση φόρτωσης events από τον server
        eventPanel.removeAll();
        eventPanel.setLayout(new BoxLayout(eventPanel, BoxLayout.Y_AXIS));
        
        try {
            // Αυτό είναι ένα παράδειγμα - θα πρέπει να προσαρμόσετε με τα πραγματικά δεδομένα
            EventInfo sampleEvent = new EventInfo();
            sampleEvent.setTitle("Δείγμα Εκδήλωσης");
            sampleEvent.setType("Συναυλία");
            
            JPanel eventCard = createEventCard(sampleEvent);
            eventPanel.add(eventCard);
            
            eventPanel.revalidate();
            eventPanel.repaint();
        } catch (Exception e) {
            showError("Σφάλμα φόρτωσης εκδηλώσεων: " + e.getMessage());
        }
    }
    
    private JPanel createEventCard(EventInfo event) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBorder(BorderFactory.createEtchedBorder());
        
        JLabel titleLabel = new JLabel(event.getTitle());
        JLabel typeLabel = new JLabel("Τύπος: " + event.getType());
        
        JPanel infoPanel = new JPanel(new GridLayout(2, 1));
        infoPanel.add(titleLabel);
        infoPanel.add(typeLabel);
        
        JButton bookButton = new JButton("Κράτηση");
        bookButton.addActionListener(e -> bookEvent(event));
        
        card.add(infoPanel, BorderLayout.CENTER);
        card.add(bookButton, BorderLayout.EAST);
        
        return card;
    }
    
    private void bookEvent(EventInfo event) {
        if (!loggedIn) {
            showError("Πρέπει να συνδεθείτε πρώτα!");
            return;
        }
        
        // Υλοποίηση κράτησης
        try {
            server.requestEventInfo(event); // Αυτό είναι ένα παράδειγμα
            showSuccess("Κράτηση ολοκληρώθηκε!");
        } catch (Exception e) {
            showError("Σφάλμα κατά την κράτηση: " + e.getMessage());
        }
    }
    
    private void updateUIState() {
        loginButton.setEnabled(!loggedIn);
        signupButton.setEnabled(!loggedIn);
        logoutButton.setEnabled(loggedIn);
        usernameField.setEnabled(!loggedIn);
        passwordField.setEnabled(!loggedIn);
    }
}