package HW3;
import java.net.*;
import java.io.*;

public class clientHandler extends Thread{
	private Socket clientSocket;
    private Protocol protocol;

    public clientHandler(Socket socket, Protocol protocol) {
        this.clientSocket = socket;
        this.protocol = protocol;
    }

    public void run() {
        try {
            PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(
                    new InputStreamReader(clientSocket.getInputStream()));

            String inputLine, outputLine;

            outputLine = protocol.processInput(null);
            out.println(outputLine);

            while ((inputLine = in.readLine()) != null) {
                outputLine = protocol.processInput(inputLine);
                out.println(outputLine);

                if (protocol.isFinished()) {
                    clientSocket.close();
                    break;
                }
            }

            out.close();
            in.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

	}

