package cliente;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ClienteTCP {
    public static void main(String[] args) {

        String serverAddress = System.getenv("DIRECCION_SERVIDOR");
        if (serverAddress == null) {
            serverAddress = "localhost";
        }
        int port = 5000;

        while (true) {
            try (Socket socket = new Socket(serverAddress, port)) {
                PrintWriter output = new PrintWriter(socket.getOutputStream(), true);
                BufferedReader input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                output.println("Hola, servidor!");
                String response = input.readLine();
                System.out.println("Respuesta del servidor: " + response);
                break;
            } catch (IOException e) {
                System.out.println("Error al intentar conectar o durante la comunicación. Intentando nuevamente...");

                try {
                    Thread.sleep(5000);
                } catch (InterruptedException ie) {
                    Thread.currentThread().interrupt();
                    System.out.println("Error durante la espera de reconexión");
                }
            }
        }
    }
}
