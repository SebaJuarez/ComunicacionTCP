package registro;

import utils.Mensaje;
import utils.Mapper;
import utils.TipoMensaje;
import static logger.Logger.sysoConHora;

import java.io.*;
import java.net.*;
import java.util.concurrent.ConcurrentLinkedQueue;

public class RegistroContactos {
    private static final int PUERTO = 4000;
    private static final ConcurrentLinkedQueue<String> nodos = new ConcurrentLinkedQueue<>();

    public static void main(String[] args) {
        try (ServerSocket serverSocket = new ServerSocket(PUERTO)) {
            sysoConHora("RegistroContactos escuchando en el puerto " + PUERTO);
            while (true) {
                Socket socket = serverSocket.accept();
                new Thread(new ClienteHandler(socket)).start();
            }
        } catch (IOException e) {
            sysoConHora("Error en RegistroContactos: " + e.getMessage());
        }
    }

    private static class ClienteHandler implements Runnable {
        private final Socket socket;

        public ClienteHandler(Socket socket) {
            this.socket = socket;
        }

        @Override
        public void run() {
            try (BufferedReader input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                 PrintWriter output = new PrintWriter(socket.getOutputStream(), true)) {

                String mensajeJson = input.readLine();
                if (mensajeJson != null) {
                    Mensaje mensaje = Mapper.fromJson(mensajeJson);
                    String nodoInfo = mensaje.getIpOrigen() + ":" + mensaje.getMensaje();

                    if (!nodos.contains(nodoInfo)) {
                        if(nodos.removeIf(n -> n.startsWith(mensaje.getIpOrigen()))){
                            sysoConHora("Solo se puede tener una conexión por IP");
                        }
                        nodos.add(nodoInfo);
                        sysoConHora("Nodo registrado: " + nodoInfo);
                    }


                    Mensaje respuesta = new Mensaje("RegistroContactos", mensaje.getIpOrigen(), String.join(",", nodos), TipoMensaje.CONFIGURACION);
                    output.println(Mapper.toJson(respuesta));
                }
            } catch (IOException e) {
                sysoConHora("Error manejando cliente: " + e.getMessage());
            }
        }
    }
}
