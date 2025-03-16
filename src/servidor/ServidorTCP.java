package servidor;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;


public class ServidorTCP {
    public static void main(String[] args) {
        int port = 5000;

        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("Servidor esperando conexiones en el puerto " + port);

            while (true) {
                // Acepta una nueva conexión de cliente
                Socket socket = serverSocket.accept();
                System.out.println("Cliente conectado: " + socket.getInetAddress());

                // Crear un nuevo hilo para manejar la comunicación con este cliente
                new Thread(new ClienteHandler(socket)).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

class ClienteHandler implements Runnable {
    private Socket socket;

    public ClienteHandler(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        try (
                BufferedReader input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                PrintWriter output = new PrintWriter(socket.getOutputStream(), true)
        ) {
            String receivedMessage;
            while (true) {
                try {
                    // Intentamos leer datos del cliente. Si no hay datos, el socket estará cerrado o desconectado.
                    if ((receivedMessage = input.readLine()) == null) {
                        // Si recibimos null, significa que el cliente cerró la conexión
                        System.out.println("El cliente " + socket.getInetAddress() + " ha cerrado la conexión.");
                        break;
                    }

                    // Procesar el mensaje recibido
                    System.out.println("Mensaje recibido: " + receivedMessage);

                    // Enviar respuesta al cliente
                    output.println("Hola cliente: " + socket.getInetAddress());

                } catch (IOException e) {
                    // Si hay un error de lectura o de socket, significa que la conexión fue cerrada inesperadamente
                    System.out.println("Error de comunicación con el cliente: " + e.getMessage());
                    break; // Salir del bucle y cerrar la conexión.
                }
            }
        } catch (IOException e) {
            System.out.println("Error al gestionar el socket: " + e.getMessage());
        } finally {
            try {
                if (socket != null && !socket.isClosed()) {
                    socket.close(); // Cerrar el socket al final si se cerró el cliente
                }
            } catch (IOException e) {
                System.out.println("Error al cerrar el socket: " + e.getMessage());
            }
        }
    }
}
