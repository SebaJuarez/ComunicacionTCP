package cliente;

import java.io.*;
import java.net.*;
import static logger.Logger.sysoConHora;

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
                sysoConHora("Intentando conectar al servidor en " + serverAddress + ":" + port);

                // Crear socket sin conexión
                socket = new Socket();
                // Intentar conectar con un timeout de 5 segundos
                socket.connect(new InetSocketAddress(serverAddress, port), 1000);

                output = new PrintWriter(socket.getOutputStream(), true);
                input = new BufferedReader(new InputStreamReader(socket.getInputStream()));

                sysoConHora("Conectado al servidor!");

                // Reiniciar el tiempo de espera en caso de éxito
                retryDelay = BASE_RETRY_DELAY;

                // Enviar mensaje inicial
                output.println("Hola, servidor!");

                // Recibir respuesta del servidor
                String response = input.readLine();
                sysoConHora("Respuesta del servidor: " + response);

                // Mantener la conexión y leer mensajes
                while (true) {
                    try {
                        String serverMessage = input.readLine();
                        if (serverMessage == null) {
                            sysoConHora("El servidor ha cerrado la conexión.");
                            break;
                        }
                        sysoConHora("Mensaje del servidor: " + serverMessage);
                    } catch (SocketException se) {
                        sysoConHora("Error de comunicación, la conexión fue cerrada.");
                        break;
                    }
                }

            } catch (IOException e) {
                sysoConHora("Error al conectar con el servidor: " + e.getMessage());

                // Esperar antes de reintentar con backoff exponencial
                try {
                    sysoConHora("Reintentando en " + (retryDelay / 1000) + " segundos...");
                    Thread.sleep(retryDelay);
                } catch (InterruptedException ie) {
                    Thread.currentThread().interrupt();
                    sysoConHora("Hilo interrumpido durante la espera de reconexión.");
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
                    sysoConHora("Error cerrando el socket.");
                }
            }
        }
    }
}
