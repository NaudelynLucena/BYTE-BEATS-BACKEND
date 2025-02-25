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
  //  private final RecordingsController recordingsController;
   // private final SongsController songsController;
    private boolean running = true;

    public HomeController(ServerSocket serverSocket) throws Exception {
        this.serverSocket = serverSocket;
      //  this.recordingsController = recordingsController;
      //  this.songsController = songsController;
        run();
    }

    public void run() {
        System.out.println("Servidor iniciando en el puerto" + serverSocket.getLocalPort());
        while (running) {
            try {
                Socket clienSocket = serverSocket.accept();
                System.out.println("Cliente conectado: " + clienSocket.getInetAddress());
                // MAnejar cada cliente en un hilo diferente
                new Thread(() -> handleClient(clienSocket)).start();

            } catch (IOException e) {
                if (running) {
                    System.err.println("Error al aceptar la conexión del cliente: " + e.getMessage());
                }
            }
        }
        try {
            serverSocket.close();
            System.out.println("Servidor cerrado");
        } catch (IOException e) {
            System.err.println("Error al cerrar el servidor: " + e.getMessage());
        }
    }

    private void handleClient(Socket clientSocket) {
        try (BufferedReader in = new BufferedReader(
                new InputStreamReader(clientSocket.getInputStream()));
                BufferedWriter out = new BufferedWriter(
                        new OutputStreamWriter(clientSocket.getOutputStream()))) {
            String requestLine = in.readLine();
            if (requestLine == null) {
                return;
            }
            System.out.println("Petición recibida: " + requestLine);
            String resposnse = handleRequest(requestLine);
            out.write(resposnse);
            out.flush();

        } catch (IOException e) {
            System.err.println("Error al cerrar el socket del cliente: " + e.getMessage());
        }
    }

    private String handleRequest(String requestLine) {
        String response = "";
        if (requestLine.startsWith("GET /songs")) {
          //  response = songController.getAllSongs();
        } else if (requestLine.startsWith("GET /recordings")) {
          //  response = recordingsController.getAllRecordings();
        } else if (requestLine.startsWith("GET /salir")) {
            running = false;
            response = "HTTP/1.1 200 OK\r\n\r\nServidor finalizando";
        } else {
            response = "HTTP/1.1 404 Not Found\r\n\r\n";
        }
        return response;
    }
    public void stop() {
        running = false;
        try{
            serverSocket.close();
        } catch (IOException e) {
            System.err.println("Error al cerrar el servidor: " + e.getMessage());
        }
    }
}