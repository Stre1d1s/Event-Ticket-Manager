package Client.src;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Date;

public class AdminClientGUI extends ClientGUI {
    private JTextField usernameField, passwordField;
    private JButton loginButton, logoutButton;
    private JTabbedPane tabbedPane;
    private boolean loggedIn = false;
    private UserInfo currentUser;
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            AdminClientGUI gui = new AdminClientGUI();
            gui.setVisible(true);
        });
    }

    public AdminClientGUI() {
        super("Σύστημα Κρατήσεων - Διαχειριστής");
        initializeComponents();
        setupLayout();
        setupEventHandlers();
        updateUIState();
    }
    
    private void initializeComponents() {
        usernameField = new JTextField(15);
        passwordField = new JPasswordField(15);
        loginButton = new JButton("Σύνδεση");
        logoutButton = new JButton("Αποσύνδεση");
        
        tabbedPane = new JTabbedPane();
    }
    
    private void setupLayout() {
        JPanel loginPanel = new JPanel(new GridLayout(3, 2, 5, 5));
        loginPanel.add(new JLabel("Όνομα χρήστη:"));
        loginPanel.add(usernameField);
        loginPanel.add(new JLabel("Κωδικός:"));
        loginPanel.add(passwordField);
        loginPanel.add(new JLabel());
        loginPanel.add(loginButton);
        
        setLayout(new BorderLayout());
        add(loginPanel, BorderLayout.NORTH);
        add(tabbedPane, BorderLayout.CENTER);
        add(logoutButton, BorderLayout.SOUTH);
    }
    
    private void setupEventHandlers() {
        loginButton.addActionListener(e -> login());
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
            setupAdminTabs();
            showSuccess("Επιτυχής σύνδεση ως διαχειριστής!");
        } catch (Exception e) {
            showError("Σφάλμα σύνδεσης: " + e.getMessage());
        }
    }
    
    private void logout() {
        try {
            server.userLogout(currentUser);
            loggedIn = false;
            currentUser = null;
            updateUIState();
            tabbedPane.removeAll();
            showSuccess("Επιτυχής αποσύνδεση!");
        } catch (Exception e) {
            showError("Σφάλμα αποσύνδεσης: " + e.getMessage());
        }
    }
    
    private void setupAdminTabs() {
        tabbedPane.removeAll();
        
        // Καρτέλα δημιουργίας εκδήλωσης
        JPanel createEventPanel = new JPanel(new GridLayout(6, 2, 5, 5));
        
        JTextField eventTitleField = new JTextField();
        JComboBox<String> eventTypeCombo = new JComboBox<>(new String[]{"Θεατρική παράσταση", "Συναυλία", "Αθλητικό γεγονός"});
        JTextField eventDateField = new JTextField();
        JTextField eventHourField = new JTextField();
        JTextField eventCostField = new JTextField();
        JButton createEventButton = new JButton("Δημιουργία Εκδήλωσης");
        
        createEventPanel.add(new JLabel("Τίτλος:"));
        createEventPanel.add(eventTitleField);
        createEventPanel.add(new JLabel("Τύπος:"));
        createEventPanel.add(eventTypeCombo);
        createEventPanel.add(new JLabel("Ημερομηνία (yyyy-MM-dd):"));
        createEventPanel.add(eventDateField);
        createEventPanel.add(new JLabel("Ώρα:"));
        createEventPanel.add(eventHourField);
        createEventPanel.add(new JLabel("Κόστος:"));
        createEventPanel.add(eventCostField);
        createEventPanel.add(new JLabel());
        createEventPanel.add(createEventButton);
        
        createEventButton.addActionListener(e -> {
            try {
                StartingDate startingDate = new StartingDate(
                    new Date(), // Θα πρέπει να μετατραπεί από το eventDateField
                    eventHourField.getText(),
                    eventCostField.getText()
                );
                
                EventInfo newEvent = new EventInfo(
                    eventTitleField.getText(),
                    (String) eventTypeCombo.getSelectedItem(),
                    startingDate,
                    0 // Αρχικό balance
                );
                
                server.createEvent(newEvent);
                showSuccess("Εκδήλωση δημιουργήθηκε επιτυχώς!");
            } catch (Exception ex) {
                showError("Σφάλμα δημιουργίας εκδήλωσης: " + ex.getMessage());
            }
        });
        
        // Καρτέλα διαχείρισης χρηστών
        JPanel usersPanel = new JPanel(new BorderLayout());
        // Προσθέστε περιεχόμενο για διαχείριση χρηστών
        
        tabbedPane.addTab("Δημιουργία Εκδήλωσης", createEventPanel);
        tabbedPane.addTab("Διαχείριση Χρηστών", usersPanel);
    }
    
    private void updateUIState() {
        loginButton.setEnabled(!loggedIn);
        logoutButton.setEnabled(loggedIn);
        usernameField.setEnabled(!loggedIn);
        passwordField.setEnabled(!loggedIn);
        tabbedPane.setEnabled(loggedIn);
    }
}
