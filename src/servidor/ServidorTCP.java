package servidor;

import java.io.*;
import java.net.*;

public class ServidorTCP {
    public static void main(String[] args) {
        int port = 5000;

        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("Servidor esperando conexiones en el puerto " + port);

            while (true) {
                Socket socket = serverSocket.accept();
                System.out.println("Cliente conectado: " + socket.getInetAddress());

                BufferedReader input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                PrintWriter output = new PrintWriter(socket.getOutputStream(), true);

                String receivedMessage = input.readLine();
                System.out.println("Mensaje recibido: " + receivedMessage);

                output.println("Hola, cliente!");

                socket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}