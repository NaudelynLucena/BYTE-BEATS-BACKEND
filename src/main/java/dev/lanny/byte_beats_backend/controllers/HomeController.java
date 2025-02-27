package dev.lanny.byte_beats_backend.controllers;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

public class HomeController {
    private static final Logger LOGGER = Logger.getLogger(HomeController.class.getName());
    private final ServerSocket serverSocket;
    private final RecordingController recordingController;
    private boolean running = true;

    public HomeController(ServerSocket serverSocket) {
        this.serverSocket = serverSocket;
        this.recordingController = new RecordingController();
    }

    public void run() {
        LOGGER.info("Servidor iniciado en el puerto " + serverSocket.getLocalPort());

        while (running) {
            try {
                if (serverSocket.isClosed()) {
                    LOGGER.warning("El servidor ha sido cerrado. Saliendo del bucle.");
                    break;
                }

                Socket clientSocket = serverSocket.accept();
                LOGGER.info("Cliente conectado: " + clientSocket.getInetAddress());

                new Thread(() -> handleClientCommunication(clientSocket)).start();

            } catch (IOException e) {
                if (running) {
                    LOGGER.log(Level.SEVERE, "Error al aceptar conexión del cliente", e);
                }
            }
        }
        stop();
    }

    private void handleClientCommunication(Socket clientSocket) {
        try (BufferedReader input = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            BufferedWriter output = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()))) {

            String requestLine = input.readLine();
            if (requestLine == null) {
                return;
            }

            LOGGER.info("Petición recibida: " + requestLine);

            String[] requestParts = requestLine.split(" ");
            if (requestParts.length < 2) {
                output.write("HTTP/1.1 400 Bad Request\r\n\r\nInvalid request format");
                output.flush();
                return;
            }

            String method = requestParts[0];
            String path = requestParts[1];

            String response = recordingController.processRecordingRequest(method, path, input);
            output.write("HTTP/1.1 200 OK\r\nContent-Type: application/json\r\n\r\n" + response);
            output.flush();

        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Error al manejar la comunicación con el cliente", e);
        } finally {
            try {
                clientSocket.close();
            } catch (IOException e) {
                LOGGER.log(Level.WARNING, "Error al cerrar el socket del cliente", e);
            }
        }
    }

    public void stop() {
        running = false;
        try {
            if (!serverSocket.isClosed()) {
                serverSocket.close();
                LOGGER.info("Servidor cerrado correctamente.");
            }
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Error al cerrar el servidor", e);
        }
    }
}
