package dbserver;

import java.io.*;
import java.net.*;

/**
 *
 * @author kkkatsar
 */
public class DBServer {

    public static void main(String[] args) {
        try {
            try (Socket sock = new Socket("localhost", 8888)) {
                BufferedReader input = new BufferedReader(new InputStreamReader(sock.getInputStream()));
                BufferedWriter response = new BufferedWriter(new OutputStreamWriter(sock.getOutputStream()));
                System.out.println("Sending Messages to the Server...");
                System.out.println("Connecting to " + sock.getInetAddress() + " and port " + sock.getPort());
                System.out.println("Local Address :" + sock.getLocalAddress() + " Port:" + sock.getLocalPort());

                /*
                
                code goes here at some point idk lel
                
                 */
                input.close();
                response.close();
            }
            System.out.println("Closing connection...");
        } catch (IOException ex) {
            System.err.println("Connection refused");
        }
    }

}
