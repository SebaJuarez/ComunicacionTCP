package cliente;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ClienteTCP {
    public static void main(String[] args) {

        String serverAddress = System.getenv("DIRECCION-SERVIDOR"); // Set from environment variable
        if (serverAddress == null) {
            serverAddress = "localhost"; // Default value if environment variable is not set
        }
        int port = 5000;

        try (Socket socket = new Socket(serverAddress, port)) {
            PrintWriter output = new PrintWriter(socket.getOutputStream(), true);
            BufferedReader input = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            output.println("Hola, servidor!");
            String response = input.readLine();
            System.out.println("Respuesta del servidor: " + response);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
