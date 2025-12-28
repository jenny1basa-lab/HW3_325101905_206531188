package HW3;

import java.net.ServerSocket;
import java.net.Socket;

public class RuppinRegistrationServer {

    public static void main(String[] args) {
        try (ServerSocket serverSocket = new ServerSocket(4445)) {
            System.out.println("Ruppin Registration Server running on port 4445...");

            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("New registration client connected: " + clientSocket.getRemoteSocketAddress());

                Protocol protocol = new RuppinRegistrationProtocol();
                clientHandler handler = new clientHandler(clientSocket, protocol);
                handler.start();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
