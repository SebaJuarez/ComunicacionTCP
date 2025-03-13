package clienteServidor;
import java.io.*;
import java.net.*;


public class ClienteServidorTCP {

    public static void main(String[] args) {
        if (args.length != 4) {
            System.out.println("Uso: java ClienteServidorTCP <IP_servidor> <Puerto_servidor> <IP_cliente> <Puerto_cliente>");
            return;
        }

        String serverAddress = args[0];  // Dirección IP para escuchar como servidor
        int serverPort = Integer.parseInt(args[1]); // Puerto para escuchar como servidor
        String clientAddress = args[2];  // Dirección IP del otro nodo al que se conectará
        int clientPort = Integer.parseInt(args[3]); // Puerto del otro nodo para la conexión cliente

        // Hilo para el servidor
        Thread serverThread = new Thread(() -> startServer(serverPort));

        // Hilo para el cliente
        Thread clientThread = new Thread(() -> startClient(clientAddress, clientPort));

        // Iniciar ambos hilos
        serverThread.start();
        clientThread.start();
    }

    public static void startServer(int port) {
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("Servidor esperando conexiones en el puerto " + port);

            while (true) {
                Socket socket = serverSocket.accept();
                System.out.println("Cliente conectado: " + socket.getInetAddress());

                BufferedReader input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                PrintWriter output = new PrintWriter(socket.getOutputStream(), true);

                String receivedMessage = input.readLine();
                System.out.println("Mensaje recibido: " + receivedMessage);

                output.println("Hola, cliente!"); // Responder al cliente

                socket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static void startClient(String serverAddress, int port) {
        while (true) {
            try (Socket socket = new Socket(serverAddress, port)) {
                PrintWriter output = new PrintWriter(socket.getOutputStream(), true);
                BufferedReader input = new BufferedReader(new InputStreamReader(socket.getInputStream()));

                output.println("Hola, servidor!");  // Enviar saludo

                String response = input.readLine();
                System.out.println("Respuesta del servidor: " + response); // Recibir respuesta

                break;  // Salir después de recibir la respuesta
            } catch (IOException e) {
                System.out.println("Error al intentar conectar o durante la comunicación. Intentando nuevamente...");

                try {
                    Thread.sleep(5000); // Esperar 5 segundos antes de intentar reconectar
                } catch (InterruptedException ie) {
                    Thread.currentThread().interrupt();
                    System.out.println("Error durante la espera de reconexión");
                }
            }
        }
    }
}