import java.net.*;
import java.io.*;

public class MainServer {
    public static void main(String[] args) throws Exception {
        try{
            Socket sock = new Socket("localhost", 5550);

            BufferedReader instream = new BufferedReader (new InputStreamReader(sock.getInputStream()));
            BufferedWriter outstream = new BufferedWriter(new OutputStreamWriter(sock.getOutputStream()));

            System.out.println("Connecting to "+ sock.getInetAddress()+ " and port "+sock.getPort());
            System.out.println("Local Address :"+sock.getLocalAddress()+" Port:"+sock.getLocalPort());
            String strin, strout;
            strin = instream.readLine();

            instream.close();
            outstream.close();
            sock.close();
            System.out.println("Connection Closing...");
        }
        catch (IOException ex){
            System.out.println("Connection Refused!!!");
        }
    }
}
