//ICSD21028 -- Konstantinos Katsaros
//ICSD21049 -- Aristeidis Papadopoulos

package Client;

import DataBaseServer.Event;
import DataBaseServer.EventType;
import DataBaseServer.Performance;
import DataBaseServer.Reservation;
import DataBaseServer.Response;
import DataBaseServer.ResponseStatus;
import DataBaseServer.User;
import DataBaseServer.UserRole;
import DataBaseServer.src.*;
import MainServer.ReservationSystem;

import javax.swing.*;
import java.awt.*;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.time.LocalDateTime;
import java.util.List;

public class ClientGUI extends JFrame {
    private ReservationSystem server;
    private String currentUser;
    private UserRole currentRole;

    // GUI Components
    private JTabbedPane tabbedPane;
    private JPanel loginPanel;
    private JPanel userPanel;
    private JPanel adminPanel;

    public ClientGUI() {
        super("Ticket Reservation System");
        initializeUI();
        connectToServer();
    }

    private void connectToServer() {
        try {
            server = (ReservationSystem) Naming.lookup("rmi://localhost/ReservationSystem");
            System.out.println("Connected to server");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error connecting to server: " + e.getMessage(), 
                "Connection Error", JOptionPane.ERROR_MESSAGE);
            System.exit(1);
        }
    }

    private void initializeUI() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null);

        // Create tabs
        tabbedPane = new JTabbedPane();
        
        // Login Panel
        loginPanel = createLoginPanel();
        tabbedPane.addTab("Login", loginPanel);

        // User Panel (hidden initially)
        userPanel = createUserPanel();
        
        // Admin Panel (hidden initially)
        adminPanel = createAdminPanel();

        add(tabbedPane);
    }

    private JPanel createLoginPanel() {
        JPanel panel = new JPanel(new GridLayout(4, 2, 5, 5));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JTextField usernameField = new JTextField();
        JPasswordField passwordField = new JPasswordField();
        JButton loginButton = new JButton("Login");
        JButton registerButton = new JButton("Register");

        panel.add(new JLabel("Username:"));
        panel.add(usernameField);
        panel.add(new JLabel("Password:"));
        panel.add(passwordField);
        panel.add(loginButton);
        panel.add(registerButton);

        loginButton.addActionListener(e -> {
            String username = usernameField.getText();
            String password = new String(passwordField.getPassword());

            try {
                Response response = server.authenticateUser(username, password);
                if (response.getStatus() == ResponseStatus.SUCCESS) {
                    currentUser = username;
                    currentRole = (UserRole) response.getData();
                    JOptionPane.showMessageDialog(this, "Login successful!", "Success", JOptionPane.INFORMATION_MESSAGE);
                    
                    // Show appropriate panel based on role
                    tabbedPane.removeAll();
                    if (currentRole == UserRole.ADMIN) {
                        tabbedPane.addTab("Admin", adminPanel);
                    } else {
                        tabbedPane.addTab("User", userPanel);
                    }
                    tabbedPane.addTab("Logout", createLogoutPanel());
                } else {
                    JOptionPane.showMessageDialog(this, response.getMessage(), "Login Failed", JOptionPane.ERROR_MESSAGE);
                }
            } catch (RemoteException ex) {
                JOptionPane.showMessageDialog(this, "Error communicating with server: " + ex.getMessage(), 
                    "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        registerButton.addActionListener(e -> {
            // Create registration form
            showRegistrationDialog();
        });

        return panel;
    }

    private void showRegistrationDialog() {
        JDialog dialog = new JDialog(this, "User Registration", true);
        dialog.setSize(400, 400);
        dialog.setLayout(new GridLayout(7, 2, 5, 5));

        JTextField usernameField = new JTextField();
        JPasswordField passwordField = new JPasswordField();
        JTextField fullNameField = new JTextField();
        JTextField emailField = new JTextField();
        JTextField phoneField = new JTextField();
        JComboBox<UserRole> roleCombo = new JComboBox<>(UserRole.values());
        
        JButton registerButton = new JButton("Register");

        dialog.add(new JLabel("Username:"));
        dialog.add(usernameField);
        dialog.add(new JLabel("Password:"));
        dialog.add(passwordField);
        dialog.add(new JLabel("Full Name:"));
        dialog.add(fullNameField);
        dialog.add(new JLabel("Email:"));
        dialog.add(emailField);
        dialog.add(new JLabel("Phone:"));
        dialog.add(phoneField);
        dialog.add(new JLabel("Role:"));
        dialog.add(roleCombo);
        dialog.add(new JLabel(""));
        dialog.add(registerButton);

        registerButton.addActionListener(e -> {
            User user = new User(
                usernameField.getText(),
                new String(passwordField.getPassword()),
                fullNameField.getText(),
                emailField.getText(),
                phoneField.getText(),
                (UserRole) roleCombo.getSelectedItem()
            );

            try {
                Response response = server.registerUser(user);
                JOptionPane.showMessageDialog(dialog, response.getMessage(), 
                    response.getStatus() == ResponseStatus.SUCCESS ? "Success" : "Error",
                    response.getStatus() == ResponseStatus.SUCCESS ? JOptionPane.INFORMATION_MESSAGE : JOptionPane.ERROR_MESSAGE);
                
                if (response.getStatus() == ResponseStatus.SUCCESS) {
                    dialog.dispose();
                }
            } catch (RemoteException ex) {
                JOptionPane.showMessageDialog(dialog, "Error communicating with server: " + ex.getMessage(), 
                    "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        dialog.setVisible(true);
    }

    private JPanel createUserPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        
        // Event list
        DefaultListModel<Event> eventListModel = new DefaultListModel<>();
        JList<Event> eventList = new JList<>(eventListModel);
        JScrollPane scrollPane = new JScrollPane(eventList);
        
        // Details and reservation components
        JPanel detailsPanel = new JPanel(new GridLayout(0, 1));
        JTextArea eventDetails = new JTextArea(10, 30);
        eventDetails.setEditable(false);
        
        JButton refreshButton = new JButton("Refresh Events");
        JButton reserveButton = new JButton("Reserve Tickets");
        
        // Add components
        panel.add(scrollPane, BorderLayout.WEST);
        panel.add(detailsPanel, BorderLayout.CENTER);
        detailsPanel.add(new JScrollPane(eventDetails));
        detailsPanel.add(reserveButton);
        
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(refreshButton);
        panel.add(buttonPanel, BorderLayout.SOUTH);
        
        // Load events
        refreshButton.addActionListener(e -> {
            try {
                Response response = server.getActiveEvents();
                if (response.getStatus() == ResponseStatus.SUCCESS) {
                    eventListModel.clear();
                    List<Event> events = (List<Event>) response.getData();
                    events.forEach(eventListModel::addElement);
                } else {
                    JOptionPane.showMessageDialog(this, response.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (RemoteException ex) {
                JOptionPane.showMessageDialog(this, "Error communicating with server: " + ex.getMessage(), 
                    "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
        
        // Show event details
        eventList.addListSelectionListener(e -> {
            Event selected = eventList.getSelectedValue();
            if (selected != null) {
                eventDetails.setText("Title: " + selected.getTitle() + "\n");
                eventDetails.append("Type: " + selected.getType() + "\n");
                eventDetails.append("Performances:\n");
                for (Performance p : selected.getPerformances()) {
                    eventDetails.append("- " + p.getPerformanceId() + ": " + p.getStartTime() + 
                        ", Available seats: " + p.getAvailableSeats() + ", Price: " + p.getPrice() + "\n");
                }
            }
        });
        
        // Reserve tickets
        reserveButton.addActionListener(e -> {
            Event selectedEvent = eventList.getSelectedValue();
            if (selectedEvent == null) {
                JOptionPane.showMessageDialog(this, "Please select an event first", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            showReservationDialog(selectedEvent);
        });
        
        return panel;
    }

    private void showReservationDialog(Event event) {
        JDialog dialog = new JDialog(this, "Make Reservation", true);
        dialog.setLayout(new GridLayout(0, 2, 5, 5));
        
        JComboBox<Performance> performanceCombo = new JComboBox<>();
        event.getPerformances().forEach(performanceCombo::addItem);
        
        JSpinner seatsSpinner = new JSpinner(new SpinnerNumberModel(1, 1, 100, 1));
        
        JButton reserveButton = new JButton("Reserve");
        
        dialog.add(new JLabel("Performance:"));
        dialog.add(performanceCombo);
        dialog.add(new JLabel("Number of seats:"));
        dialog.add(seatsSpinner);
        dialog.add(new JLabel(""));
        dialog.add(reserveButton);
        
        reserveButton.addActionListener(e -> {
            Performance selectedPerformance = (Performance) performanceCombo.getSelectedItem();
            int seats = (int) seatsSpinner.getValue();
            
            String reservationId = "RES-" + System.currentTimeMillis();
            Reservation reservation = new Reservation(
                reservationId,
                currentUser,
                event.getEventId(),
                selectedPerformance.getPerformanceId(),
                seats
            );
            
            try {
                Response response = server.reserveTickets(reservation);
                if (response.getStatus() == ResponseStatus.SUCCESS) {
                    JOptionPane.showMessageDialog(dialog, "Reservation successful! ID: " + reservationId, 
                        "Success", JOptionPane.INFORMATION_MESSAGE);
                    dialog.dispose();
                    
                    // Process payment
                    processPayment(reservationId);
                } else {
                    JOptionPane.showMessageDialog(dialog, response.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (RemoteException ex) {
                JOptionPane.showMessageDialog(dialog, "Error communicating with server: " + ex.getMessage(), 
                    "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
        
        dialog.pack();
        dialog.setVisible(true);
    }

    private void processPayment(String reservationId) {
        try {
            Response response = server.processPayment(reservationId);
            if (response.getStatus() == ResponseStatus.SUCCESS) {
                JOptionPane.showMessageDialog(this, "Payment processed successfully!", 
                    "Success", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, response.getMessage(), "Payment Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (RemoteException e) {
            JOptionPane.showMessageDialog(this, "Error processing payment: " + e.getMessage(), 
                "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private JPanel createAdminPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        
        // Tabbed pane for different admin functions
        JTabbedPane adminTabs = new JTabbedPane();
        
        // Add Event Tab
        JPanel addEventPanel = new JPanel(new GridLayout(0, 2, 5, 5));
        JTextField eventIdField = new JTextField();
        JTextField titleField = new JTextField();
        JComboBox<EventType> typeCombo = new JComboBox<>(EventType.values());
        JButton addEventButton = new JButton("Add Event");
        
        addEventPanel.add(new JLabel("Event ID:"));
        addEventPanel.add(eventIdField);
        addEventPanel.add(new JLabel("Title:"));
        addEventPanel.add(titleField);
        addEventPanel.add(new JLabel("Type:"));
        addEventPanel.add(typeCombo);
        addEventPanel.add(new JLabel(""));
        addEventPanel.add(addEventButton);
        
        addEventButton.addActionListener(e -> {
            Event event = new Event(
                eventIdField.getText(),
                titleField.getText(),
                (EventType) typeCombo.getSelectedItem()
            );
            
            // Add performances dialog
            addPerformancesToEvent(event);
        });
        
        adminTabs.addTab("Add Event", addEventPanel);
        
        // Deactivate Event Tab
        JPanel deactivatePanel = new JPanel(new BorderLayout());
        DefaultListModel<Event> eventListModel = new DefaultListModel<>();
        JList<Event> eventList = new JList<>(eventListModel);
        JButton refreshButton = new JButton("Refresh Events");
        JButton deactivateButton = new JButton("Deactivate Selected");
        
        refreshButton.addActionListener(e -> {
            try {
                Response response = server.getActiveEvents();
                if (response.getStatus() == ResponseStatus.SUCCESS) {
                    eventListModel.clear();
                    List<Event> events = (List<Event>) response.getData();
                    events.forEach(eventListModel::addElement);
                } else {
                    JOptionPane.showMessageDialog(this, response.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (RemoteException ex) {
                JOptionPane.showMessageDialog(this, "Error communicating with server: " + ex.getMessage(), 
                    "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
        
        deactivateButton.addActionListener(e -> {
            Event selected = eventList.getSelectedValue();
            if (selected != null) {
                try {
                    Response response = server.deactivateEvent(selected.getEventId());
                    JOptionPane.showMessageDialog(this, response.getMessage(), 
                        response.getStatus() == ResponseStatus.SUCCESS ? "Success" : "Error",
                        response.getStatus() == ResponseStatus.SUCCESS ? JOptionPane.INFORMATION_MESSAGE : JOptionPane.ERROR_MESSAGE);
                    
                    if (response.getStatus() == ResponseStatus.SUCCESS) {
                        refreshButton.doClick();
                    }
                } catch (RemoteException ex) {
                    JOptionPane.showMessageDialog(this, "Error communicating with server: " + ex.getMessage(), 
                        "Error", JOptionPane.ERROR_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(this, "Please select an event first", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
        
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(refreshButton);
        buttonPanel.add(deactivateButton);
        
        deactivatePanel.add(new JScrollPane(eventList), BorderLayout.CENTER);
        deactivatePanel.add(buttonPanel, BorderLayout.SOUTH);
        
        adminTabs.addTab("Deactivate Event", deactivatePanel);
        
        panel.add(adminTabs, BorderLayout.CENTER);
        return panel;
    }

    private void addPerformancesToEvent(Event event) {
        JDialog dialog = new JDialog(this, "Add Performances", true);
        dialog.setLayout(new BorderLayout());
        
        DefaultListModel<Performance> performanceListModel = new DefaultListModel<>();
        JList<Performance> performanceList = new JList<>(performanceListModel);
        
        JPanel addPanel = new JPanel(new GridLayout(0, 2, 5, 5));
        JTextField performanceIdField = new JTextField();
        JTextField dateTimeField = new JTextField("yyyy-MM-dd HH:mm");
        JSpinner totalSeatsSpinner = new JSpinner(new SpinnerNumberModel(100, 1, 1000, 1));
        JSpinner priceSpinner = new JSpinner(new SpinnerNumberModel(20.0, 5.0, 500.0, 5.0));
        JButton addPerformanceButton = new JButton("Add Performance");
        
        addPanel.add(new JLabel("Performance ID:"));
        addPanel.add(performanceIdField);
        addPanel.add(new JLabel("Date & Time (yyyy-MM-dd HH:mm):"));
        addPanel.add(dateTimeField);
        addPanel.add(new JLabel("Total Seats:"));
        addPanel.add(totalSeatsSpinner);
        addPanel.add(new JLabel("Price:"));
        addPanel.add(priceSpinner);
        addPanel.add(new JLabel(""));
        addPanel.add(addPerformanceButton);
        
        JButton saveEventButton = new JButton("Save Event");
        
        addPerformanceButton.addActionListener(e -> {
            try {
                LocalDateTime dateTime = LocalDateTime.parse(dateTimeField.getText().replace(" ", "T"));
                Performance performance = new Performance(
                    performanceIdField.getText(),
                    dateTime,
                    (int) totalSeatsSpinner.getValue(),
                    (double) priceSpinner.getValue()
                );
                performanceListModel.addElement(performance);
                event.addPerformance(performance);
                
                // Clear fields
                performanceIdField.setText("");
                dateTimeField.setText("yyyy-MM-dd HH:mm");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(dialog, "Invalid date/time format", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        Event clientEvent = new Event(currentUser, getTitle(), null);
        
        saveEventButton.addActionListener(e -> {
            try {
                Response response = server.addEvent(event);
                JOptionPane.showMessageDialog(dialog, response.getMessage(), 
                    response.getStatus() == ResponseStatus.SUCCESS ? "Success" : "Error",
                    response.getStatus() == ResponseStatus.SUCCESS ? JOptionPane.INFORMATION_MESSAGE : JOptionPane.ERROR_MESSAGE);
                
                if (response.getStatus() == ResponseStatus.SUCCESS) {
                    dialog.dispose();
                }
            } catch (RemoteException ex) {
                JOptionPane.showMessageDialog(dialog, "Error communicating with server: " + ex.getMessage(), 
                    "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
        
        dialog.add(new JScrollPane(performanceList), BorderLayout.CENTER);
        dialog.add(addPanel, BorderLayout.NORTH);
        dialog.add(saveEventButton, BorderLayout.SOUTH);
        
        dialog.pack();
        dialog.setVisible(true);
    }

    private JPanel createLogoutPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        JButton logoutButton = new JButton("Logout");
        
        logoutButton.addActionListener(e -> {
            currentUser = null;
            currentRole = null;
            tabbedPane.removeAll();
            tabbedPane.addTab("Login", loginPanel);
        });
        
        panel.add(logoutButton, BorderLayout.CENTER);
        return panel;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            ClientGUI client = new ClientGUI();
            client.setVisible(true);
        });
    }
}