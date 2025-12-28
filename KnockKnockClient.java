package HW3;

import java.io.*;
import java.net.*;
import java.util.Scanner;

public class KnockKnockClient {
	public static void main(String[] args) throws IOException {

		Scanner scanner = new Scanner(System.in);

		System.out.println("Choose protocol:");
		System.out.println("1 - Knock Knock");
		System.out.println("2 - Ruppin Registration");

		int choice = scanner.nextInt();
		scanner.nextLine(); // consume newline

		int port;

		if (choice == 1)
		    port = 4444;
		else
		    port = 4445;

		 try {
	            Socket kkSocket = new Socket("127.0.0.1", port);

	            BufferedReader in = new BufferedReader(
	                    new InputStreamReader(kkSocket.getInputStream()));

	            PrintWriter out = new PrintWriter(
	                    kkSocket.getOutputStream(), true);

	            BufferedReader stdIn = new BufferedReader(
	                    new InputStreamReader(System.in));
	            new Thread(() -> {
	                try {
	                    String fromServer;
	                    while ((fromServer = in.readLine()) != null) {
	                        System.out.println("Server: " + fromServer);
	                        if (fromServer.equals("Bye."))
	                            break;
	                    }
	                } catch (IOException e) {
	                    e.printStackTrace();
	                }
	            }).start();
	            new Thread(() -> {
	                try {
	                    String fromUser;
	                    while ((fromUser = stdIn.readLine()) != null) {
	                        System.out.println("Client: " + fromUser);
	                        out.println(fromUser);
	                    }
	                } catch (IOException e) {
	                    e.printStackTrace();
	                }
	            }).start();

	        } catch (IOException e) {
	            e.printStackTrace();
	        }
	    }
}
