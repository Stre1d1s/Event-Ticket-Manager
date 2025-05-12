import java.io.*;
import java.net.*;

public class DBServer {

    public static void main(String[] args) {
        try (ServerSocket srvr = new ServerSocket(8888)) {

            while (true) {

                try (Socket sock = srvr.accept()) {

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
                    sock.close();

                } catch (IOException ex) {
                    System.err.println(ex);
                }
            }

        } catch (IOException e) {
            System.err.println(e);
        }
    }
}
