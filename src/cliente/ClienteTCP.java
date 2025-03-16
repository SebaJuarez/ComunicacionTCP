package cliente;

import java.io.*;
import java.net.*;

public class ClienteTCP {
    private static final int BASE_RETRY_DELAY = 1000; // 1 segundo inicial
    private static final int MAX_RETRY_DELAY = 30000; // 30 segundos máximo

    public static void main(String[] args) {
        String serverAddress = System.getenv("DIRECCION_SERVIDOR");
        if (serverAddress == null) {
            serverAddress = "localhost";
        }
        int port = 5000;

        int retryDelay = BASE_RETRY_DELAY; // Tiempo de espera inicial

        while (true) {
            Socket socket = null;
            PrintWriter output = null;
            BufferedReader input = null;

            try {
                System.out.println("Intentando conectar al servidor en " + serverAddress + ":" + port);

                // Crear socket sin conexión
                socket = new Socket();
                // Intentar conectar con un timeout de 5 segundos
                socket.connect(new InetSocketAddress(serverAddress, port), 1000);

                output = new PrintWriter(socket.getOutputStream(), true);
                input = new BufferedReader(new InputStreamReader(socket.getInputStream()));

                System.out.println("Conectado al servidor!");

                // Reiniciar el tiempo de espera en caso de éxito
                retryDelay = BASE_RETRY_DELAY;

                // Enviar mensaje inicial
                output.println("Hola, servidor!");

                // Recibir respuesta del servidor
                String response = input.readLine();
                System.out.println("Respuesta del servidor: " + response);

                // Mantener la conexión y leer mensajes
                while (true) {
                    try {
                        String serverMessage = input.readLine();
                        if (serverMessage == null) {
                            System.out.println("El servidor ha cerrado la conexión.");
                            break;
                        }
                        System.out.println("Mensaje del servidor: " + serverMessage);
                    } catch (SocketException se) {
                        System.out.println("Error de comunicación, la conexión fue cerrada.");
                        break;
                    }
                }

            } catch (IOException e) {
                System.out.println("Error al conectar con el servidor: " + e.getMessage());

                // Esperar antes de reintentar con backoff exponencial
                try {
                    System.out.println("Reintentando en " + (retryDelay / 1000) + " segundos...");
                    Thread.sleep(retryDelay);
                } catch (InterruptedException ie) {
                    Thread.currentThread().interrupt();
                    System.out.println("Hilo interrumpido durante la espera de reconexión.");
                    return;
                }

                // Incrementar el tiempo de espera exponencialmente, hasta un máximo
                retryDelay = Math.min(retryDelay * 2, MAX_RETRY_DELAY);
            } finally {
                try {
                    if (socket != null && !socket.isClosed()) {
                        socket.close();
                    }
                } catch (IOException e) {
                    System.out.println("Error cerrando el socket.");
                }
            }
        }
    }
}
