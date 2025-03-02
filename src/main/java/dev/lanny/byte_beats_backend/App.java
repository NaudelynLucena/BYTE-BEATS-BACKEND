package dev.lanny.byte_beats_backend;


import java.net.ServerSocket;

import dev.lanny.byte_beats_backend.controllers.HomeController;

public final class App {
    private App() {
    }

    
    public static void main(String[] args) {
        try {
            int port = 8080;
            ServerSocket serverSocket = new ServerSocket(port);
            System.out.println("✅ Servidor iniciado en el puerto " + port);
    
            HomeController homeController = new HomeController(serverSocket);
            homeController.run();
    
        } catch (Exception e) {
            System.err.println("🔥 Error crítico en el servidor: " + e.getMessage());
            e.printStackTrace();
        }
    }
    

}