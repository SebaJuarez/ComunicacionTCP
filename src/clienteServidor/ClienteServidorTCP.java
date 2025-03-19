package clienteServidor;

import utils.Mapper;
import utils.Mensaje;
import utils.TipoMensaje;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;

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
        new Thread(() -> iniciarCliente(miIP, ipRemota, puertoRemoto)).start();
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

    private static void iniciarCliente(String miIP, String ip, int puerto) {
        int retryDelay = BASE_RETRY_DELAY;
        while (true) {
            try (Socket socket = new Socket(ip, puerto);
                 PrintWriter output = new PrintWriter(socket.getOutputStream(), true);
                 BufferedReader input = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {

                sysoConHora("Conectado a " + ip + ":" + puerto);
                retryDelay = BASE_RETRY_DELAY;

                // Crear y enviar mensaje JSON
                Mensaje mensaje = new Mensaje(miIP, ip, "Hola desde " + miIP, TipoMensaje.SALUDO);
                output.println(Mapper.toJson(mensaje));

                // Recibir respuesta
                String response = input.readLine();
                if (response != null) {
                    Mensaje mensajeRecibido = Mapper.fromJson(response);
                    sysoConHora("Respuesta del servidor: " + mensajeRecibido.toString());
                }

                // Escuchar mensajes del servidor
                while (true) {
                    String serverMessage = input.readLine();
                    if (serverMessage == null) break;
                    Mensaje msg = Mapper.fromJson(serverMessage);
                    sysoConHora("Mensaje del servidor: " + msg.toString());
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

    public ClienteHandler(Socket socket) throws SocketException {
        this.socket = socket;
    }

    @Override
    public void run() {
        try (BufferedReader input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             PrintWriter output = new PrintWriter(socket.getOutputStream(), true)) {

            String mensajeJson;
            while ((mensajeJson = input.readLine()) != null) {
                Mensaje mensaje = Mapper.fromJson(mensajeJson);
                sysoConHora("Mensaje recibido: " + mensaje.toString());

                // Enviar respuesta JSON con IP origen y destino
                Mensaje respuesta = new Mensaje(socket.getLocalAddress().getHostAddress(), mensaje.getIpOrigen(), "Hola desde " + socket.getLocalAddress().getHostAddress(), TipoMensaje.SALUDO);
                output.println(Mapper.toJson(respuesta));
            }
        } catch (IOException e) {
            sysoConHora("Error de comunicación: " + e.getMessage());
        }
    }
}