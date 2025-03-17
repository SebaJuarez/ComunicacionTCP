package clienteServidor;

import java.io.*;
import java.net.*;
import static logger.Logger.sysoConHora;

public class ClienteServidorTCP {
    private static final int BASE_RETRY_DELAY = 1000;
    private static final int MAX_RETRY_DELAY = 30000;

    public static void main(String[] args) {
        if (args.length != 4) {
            System.out.println("Uso: java ClienteServidorTCP <miIP> <miPuerto> <IPRemoto> <PuertoRemoto>");
            return;
        }

        String miIP = args[0];
        int miPuerto = Integer.parseInt(args[1]);
        String ipRemota = args[2];
        int puertoRemoto = Integer.parseInt(args[3]);

        new Thread(() -> iniciarServidor(miPuerto)).start();
        new Thread(() -> iniciarCliente(ipRemota, puertoRemoto)).start();
    }

    private static void iniciarServidor(int puerto) {
        try (ServerSocket serverSocket = new ServerSocket(puerto)) {
            sysoConHora("Servidor escuchando en el puerto " + puerto);
            while (true) {
                Socket socket = serverSocket.accept();
                sysoConHora("Cliente conectado desde: " + socket.getInetAddress());
                new Thread(new ClienteHandler(socket)).start();
            }
        } catch (IOException e) {
            sysoConHora("Error en el servidor: " + e.getMessage());
        }
    }

    private static void iniciarCliente(String ip, int puerto) {
        int retryDelay = BASE_RETRY_DELAY;
        while (true) {
            try (Socket socket = new Socket(ip, puerto);
                 PrintWriter output = new PrintWriter(socket.getOutputStream(), true);
                 BufferedReader input = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {

                sysoConHora("Conectado a " + ip + ":" + puerto);
                retryDelay = BASE_RETRY_DELAY;
                output.println("Hola desde " + socket.getLocalAddress());
                String response = input.readLine();
                sysoConHora("Respuesta del servidor: " + response);
                while (true) {
                    String serverMessage = input.readLine();
                    if (serverMessage == null) break;
                    sysoConHora("Mensaje del servidor: " + serverMessage);
                }
            } catch (IOException e) {
                sysoConHora("Error conectando con " + ip + ": " + e.getMessage());
                try {
                    sysoConHora("Reintentando conexión...");
                    Thread.sleep(retryDelay);
                } catch (InterruptedException ie) {
                    Thread.currentThread().interrupt();
                    return;
                }
                retryDelay = Math.min(retryDelay * 2, MAX_RETRY_DELAY);
            }
        }
    }
}

class ClienteHandler implements Runnable {
    private Socket socket;

    public ClienteHandler(Socket socket) throws SocketException { this.socket = socket;}
    @Override
    public void run() {

        try (BufferedReader input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             PrintWriter output = new PrintWriter(socket.getOutputStream(), true)) {
            String mensaje;
            while ((mensaje = input.readLine()) != null) {
                sysoConHora("Mensaje recibido: " + mensaje);
                output.println("Hola desde " + socket.getLocalAddress());
            }
        } catch (IOException e) {
            sysoConHora("Error de comunicación: " + e.getMessage());
        }
    }
}