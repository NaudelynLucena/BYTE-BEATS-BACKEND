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
    private boolean running;

    public HomeController(ServerSocket serverSocket) {
        this.serverSocket = serverSocket;
        this.recordingController = new RecordingController();
        this.running = true;
    }

    public void run() {
        System.out.println("Servidor iniciado en el puerto " + serverSocket.getLocalPort());
    
        while (running) {
            try {
                if (serverSocket.isClosed()) {
                    System.out.println("Servidor cerrado. Deteniendo...");
                    break;
                }
    
                System.out.println("Esperando conexión...");
                Socket clientSocket = serverSocket.accept();
                System.out.println("Cliente conectado desde: " + clientSocket.getInetAddress());
    
                Thread clientThread = new Thread(() -> handleClientCommunication(clientSocket));
                clientThread.start();
    
            } catch (IOException e) {
                System.err.println("Error aceptando conexión: " + e.getMessage());
                break; 
            }
        }
    
        System.out.println("Servidor detenido.");
        stop();
    }
    
    private void handleClientCommunication(Socket clientSocket) {
        try (
            BufferedReader input = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            BufferedWriter output = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()))
        ) {
            String requestInfo = input.readLine();
            if (requestInfo == null || requestInfo.isEmpty()) {
                System.out.println("Petición vacía. Ignorando...");
                return;
            }
    
            System.out.println("Petición recibida: " + requestInfo);
    
            String[] requestParts = requestInfo.split(" ");
            if (requestParts.length < 2) {
                output.write("HTTP/1.1 400 Bad Request\r\nContent-Type: text/plain\r\n\r\nFormato incorrecto");
                output.flush();
                return;
            }
    
            String response = recordingController.processRecordingRequest(requestParts[0], requestParts[1], input);
            output.write("HTTP/1.1 200 OK\r\nContent-Type: application/json\r\n\r\n" + response);
            output.flush();
    
        } catch (IOException e) {
            System.err.println("Error en la comunicación con cliente: " + e.getMessage());
        } finally {
            try {
                clientSocket.close();
            } catch (IOException e) {
                System.err.println("No se pudo cerrar el socket: " + e.getMessage());
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
            System.err.println("Error cerrando el servidor: " + e.getMessage());
        }
    }

   
}
