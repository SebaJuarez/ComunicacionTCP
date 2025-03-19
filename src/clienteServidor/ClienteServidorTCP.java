package clienteServidor;

import utils.Mapper;
import utils.Mensaje;
import utils.TipoMensaje;
import static logger.Logger.sysoConHora;

import java.io.*;
import java.net.*;
import java.util.*;

public class ClienteServidorTCP {
    public static void main(String[] args) {
        if (args.length != 2) {
            System.out.println("Uso: java ClienteServidorTCP <IPRegistroContactos> <PuertoRegistroContactos>");
            return;
        }

        String ipRegistro = args[0];
        int puertoRegistro = Integer.parseInt(args[1]);
        int puertoLocal = new Random().nextInt(10000) + 1024;

        new Thread(() -> iniciarServidor(puertoLocal)).start();
        new Thread(() -> registrarYConectar(ipRegistro, puertoRegistro, puertoLocal)).start();
    }

    private static void iniciarServidor(int puerto) {
        try (ServerSocket serverSocket = new ServerSocket(puerto)) {
            sysoConHora("Servidor escuchando en el puerto " + puerto);
            while (true) {
                Socket socket = serverSocket.accept();
                new Thread(new ClienteHandler(socket)).start();
            }
        } catch (IOException e) {
            sysoConHora("Error en el servidor: " + e.getMessage());
        }
    }

    private static void registrarYConectar(String ipRegistro, int puertoRegistro, int puertoLocal) {
        int intentos = 0;
        int maxIntentos = 5;
        int espera = 1000; // 1 segundo inicial

        while (intentos < maxIntentos) {
            try (Socket socket = new Socket(ipRegistro, puertoRegistro);
                 PrintWriter output = new PrintWriter(socket.getOutputStream(), true);
                 BufferedReader input = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {

                String miIP = InetAddress.getLocalHost().getHostAddress();
                Mensaje mensaje = new Mensaje(miIP, "RegistroContactos", String.valueOf(puertoLocal), TipoMensaje.CONFIGURACION);
                output.println(Mapper.toJson(mensaje));

                String respuesta = input.readLine();
                if (respuesta != null) {
                    Mensaje mensajeRecibido = Mapper.fromJson(respuesta);
                    sysoConHora("Nodos recibidos: " + mensajeRecibido.getMensaje());
                    conectarANodos(mensajeRecibido.getMensaje());
                }
                return; // Éxito, salimos del bucle
            } catch (IOException e) {
                intentos++;
                sysoConHora("Intento " + intentos + " de " + maxIntentos + ": Error registrando nodo: " + e.getMessage());
                if (intentos < maxIntentos) {
                    try {
                        Thread.sleep(espera);
                        espera *= 2; // Incremento exponencial
                    } catch (InterruptedException ex) {
                        Thread.currentThread().interrupt();
                    }
                }
            }
        }
        sysoConHora("No se pudo conectar al RegistroContactos después de " + maxIntentos + " intentos.");
    }

    private static void conectarANodos(String nodos) {
        try {
            String miIP = InetAddress.getLocalHost().getHostAddress();
            for (String nodo : nodos.split(",")) {
                String[] datos = nodo.split(":");
                if (datos.length == 2) {
                    String ip = datos[0];
                    int puerto = Integer.parseInt(datos[1]);

                    // Evitar conectarse a sí mismo
                    if (!ip.equals(miIP)) {
                        new Thread(() -> conectarNodo(ip, puerto)).start();
                    } else {
                        sysoConHora("Evitando conexión a sí mismo: " + ip + ":" + puerto);
                    }
                }
            }
        } catch (UnknownHostException e) {
            sysoConHora("Error obteniendo la IP local: " + e.getMessage());
        }
    }


    private static void conectarNodo(String ip, int puerto) {
        try (Socket socket = new Socket(ip, puerto);
             PrintWriter output = new PrintWriter(socket.getOutputStream(), true);
             BufferedReader input = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {

            String miIP = InetAddress.getLocalHost().getHostAddress();
            Mensaje mensaje = new Mensaje(miIP, ip, "Hola desde " + miIP, TipoMensaje.SALUDO);
            output.println(Mapper.toJson(mensaje));

            String respuesta = input.readLine();
            if (respuesta != null) {
                Mensaje mensajeRecibido = Mapper.fromJson(respuesta);
                sysoConHora("Respuesta de " + ip + ": " + mensajeRecibido.getMensaje());
            }
        } catch (IOException e) {
            sysoConHora("Error conectando a " + ip + ": " + e.getMessage());
        }
    }

    static class ClienteHandler implements Runnable {
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
                    sysoConHora("Mensaje recibido: " + mensaje.getMensaje());

                    // Enviar respuesta JSON con IP origen y destino
                    Mensaje respuesta = new Mensaje(socket.getLocalAddress().getHostAddress(), mensaje.getIpOrigen(), "Hola desde " + socket.getLocalAddress().getHostAddress(), TipoMensaje.SALUDO);
                    output.println(Mapper.toJson(respuesta));
                }
            } catch (IOException e) {
                sysoConHora("Error de comunicación: " + e.getMessage());
            }
        }
    }
}
