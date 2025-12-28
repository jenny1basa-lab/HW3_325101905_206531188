package HW3;

import java.net.ServerSocket;
import java.net.Socket;

public class KnockKnockServer {

    public static void main(String[] args) {
        try (ServerSocket serverSocket = new ServerSocket(4444)) {
            System.out.println("KnockKnock Server running on port 4444...");

            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("New KnockKnock client connected: " + clientSocket.getRemoteSocketAddress());

                Protocol protocol = new KnockKnockProtocol();
                clientHandler handler = new clientHandler(clientSocket, protocol);
                handler.start();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
