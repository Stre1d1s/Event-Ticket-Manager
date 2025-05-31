//ICSD21028 -- Konstantinos Katsaros

package DataBaseServer.src;

import java.io.*;
import java.net.*;
import java.util.concurrent.*;

public class DataBaseServer {
    private static final int PORT = 54321;
    private static final int MAX_THREADS = 10;

    public static void main(String[] args) {
        ExecutorService threadPool = Executors.newFixedThreadPool(MAX_THREADS);
        DataBaseManager dbManager = new DataBaseManager();
        
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("Database Server started on port " + PORT);
            
            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("New connection from: " + clientSocket.getInetAddress());
                
                threadPool.execute(new RequestHandler(clientSocket, dbManager));
            }
        } catch (IOException e) {
            System.err.println("Server exception: " + e.getMessage());
        } finally {
            threadPool.shutdown();
        }
    }
}