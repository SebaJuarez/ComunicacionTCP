package registro;

import utils.Mensaje;
import utils.Mapper;
import utils.TipoMensaje;
import static logger.Logger.sysoConHora;

import java.io.*;
import java.net.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class RegistroContactos {
    private static final int PUERTO = 4000;
    private static final GestorInscripciones gestorInscripciones = new GestorInscripciones();

    public static void main(String[] args) {
        // Programamos la actualización de la ventana cada 60 segundos
        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
        scheduler.scheduleAtFixedRate(gestorInscripciones::actualizarVentana, 10, 20, TimeUnit.SECONDS);

        try (ServerSocket serverSocket = new ServerSocket(PUERTO)) {
            sysoConHora("RegistroContactos escuchando en el puerto " + PUERTO);
            while (true) {
                Socket socket = serverSocket.accept();
                int ventana = gestorInscripciones.getVentanaActual();
                new Thread(new ClienteHandler(socket,ventana,gestorInscripciones)).start();
            }
        } catch (IOException e) {
            sysoConHora("Error en RegistroContactos: " + e.getMessage());
        }
    }

    private static class ClienteHandler implements Runnable {
        private final Socket socket;
        private final int ventana;
        private GestorInscripciones gestorInscripciones;

        public ClienteHandler(Socket socket, int ventana, GestorInscripciones gestorInscripciones) {
            this.socket = socket;
            this.ventana = ventana;
            this.gestorInscripciones = gestorInscripciones;
        }

        @Override
        public void run() {
            try (BufferedReader input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                 PrintWriter output = new PrintWriter(socket.getOutputStream(), true)) {

                String mensajeJson = input.readLine();
                if (mensajeJson != null) {
                    Mensaje mensaje = Mapper.fromJson(mensajeJson);
                    String nodoInfo = mensaje.getIpOrigen() + ":" + mensaje.getMensaje();

                    // Si el nodo no está en la lista de nodos actuales
                    if (!gestorInscripciones.obtenerInscripcionesActuales().contains(nodoInfo)) {
                        // Se evita registrar el mismo nodo en la ventana futura si ya está registrado en la actual
                        gestorInscripciones.registrarNodo(nodoInfo);  // Registro en la ventana futura
                        sysoConHora("Nodo registrado: " + nodoInfo);
                    }

                    while(ventana == gestorInscripciones.getVentanaActual()){
                        // significa que la ventana sigue aceptando contactos
                        Thread.sleep(1000);
                    }

                    String nodosActuales = String.join(",", gestorInscripciones.obtenerInscripcionesActuales());
                    // Respuesta con la lista de nodos activos en la ventana actual
                    Mensaje respuesta = new Mensaje("RegistroContactos", mensaje.getIpOrigen(), nodosActuales, TipoMensaje.CONFIGURACION);
                    output.println(Mapper.toJson(respuesta));
                }
            } catch (IOException e) {
                sysoConHora("Error manejando cliente: " + e.getMessage());
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}