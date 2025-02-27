package dev.lanny.byte_beats_backend.controllers;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class HomeController {
    private final ServerSocket serverSocket;
    private final RecordingController recordingController;
    private boolean running = true;

    public HomeController(ServerSocket serverSocket) {
        this.serverSocket = serverSocket;
        this.recordingController = new RecordingController();
    }

    public void run() {
        System.out.println("Servidor iniciando en el puerto " + serverSocket.getLocalPort());

        while (running) {
            try {
                if (serverSocket.isClosed()) {
                    System.out.println("El servidor ha sido cerrado. Saliendo del bucle.");
                    break;
                }

                Socket clientSocket = serverSocket.accept();
                System.out.println("Cliente conectado: " + clientSocket.getInetAddress());
                new Thread(() -> handleClientCommunication(clientSocket)).start();

            } catch (IOException e) {
                if (running) {
                    System.err.println("Error al aceptar la conexión del cliente: " + e.getMessage());
                }
            }
        }
        stop();
    }

    private void handleClientCommunication(Socket clientSocket) {
        try (BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                BufferedWriter out = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()))) {

            String requestInfo = in.readLine();
            if (requestInfo == null) {
                return;
            }

            System.out.println("Petición recibida: " + requestInfo);

            String response = recordingController.processRecordingRequest(requestInfo.split(" ")[0],
                    requestInfo.split(" ")[1], in);
            out.write("HTTP/1.1 200 OK\r\nContent-Type: application/json\r\n\r\n" + response);
            out.flush();

        } catch (IOException e) {
            System.err.println("Error al manejar el cliente: " + e.getMessage());
        } finally {
            try {
                clientSocket.close();
            } catch (IOException e) {
                System.err.println("Error al cerrar el socket del cliente: " + e.getMessage());
            }
        }
    }

    public void stop() {
        running = false;
        try {
            if (!serverSocket.isClosed()) {
                serverSocket.close();
                System.out.println("Servidor cerrado correctamente.");
            }
        } catch (IOException e) {
            System.err.println("Error al cerrar el servidor: " + e.getMessage());
        }
    }
}
