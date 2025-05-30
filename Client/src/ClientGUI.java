package Client.src;

import javax.swing.*;
import java.rmi.*;
import java.rmi.registry.*;

public abstract class ClientGUI extends JFrame {
    protected Operations server;
    
    public ClientGUI(String title) {
        super(title);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        
        try {
            Registry registry = LocateRegistry.getRegistry("localhost");
            server = (Operations) registry.lookup("TicketService");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Σφάλμα σύνδεσης με τον server: " + e.getMessage(), "Σφάλμα", JOptionPane.ERROR_MESSAGE);
            System.exit(1);
        }
    }
    
    protected void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "Σφάλμα", JOptionPane.ERROR_MESSAGE);
    }
    
    protected void showSuccess(String message) {
        JOptionPane.showMessageDialog(this, message, "Επιτυχία", JOptionPane.INFORMATION_MESSAGE);
    }
}