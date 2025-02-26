package dev.lanny.byte_beats_backend;

import java.io.IOException;
import java.net.ServerSocket;

import dev.lanny.byte_beats_backend.controllers.HomeController;

public final class App {
    private App() {
    }

    
    public static void main(String[] args) throws Exception {
        int port = 8080;

        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("Servidor iniciado en el puerto " + serverSocket.getLocalPort());

            HomeController homeController = new HomeController(serverSocket);

            new Thread(homeController::run).start();

        } catch (IOException e) {
            System.err.println("Error al iniciar el servidor en el puerto " + port + ": " + e.getMessage());
            e.printStackTrace();
        }
    }

}