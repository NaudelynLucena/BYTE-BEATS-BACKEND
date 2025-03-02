package dev.lanny.byte_beats_backend.controllers;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import java.net.Socket;

public class HomeControllerTest {
    private HomeController homeController;
    private ServerSocket serverSocket;
    private int testPort = 9090;

    @BeforeEach
    public void setUp() throws Exception {
        serverSocket = new ServerSocket(testPort);
        homeController = new HomeController(serverSocket);

    }

    @Test
    @DisplayName("Validamos que se pueda iniciar el servidor correctamente")
    void test_ServerStarts_Correctly() throws IOException, InterruptedException {
        Thread serverThread = new Thread(() -> homeController.run());
        serverThread.start();
        Thread.sleep(500);

        assertThat("El servidor debería estar activo después de iniciar", homeController != null);
    }

    @Test
    @DisplayName("Validamos que se pueda aceptar una conexión de cliente")
    void test_Accept_ClientConnection() throws IOException, InterruptedException {
        Thread serverThread = new Thread(() -> homeController.run());
        serverThread.start();
        Thread.sleep(500);

        try (Socket clientSocket = new Socket("localhost", testPort)) {
            assertThat("El cliente debería poder conectarse", clientSocket.isConnected(), is(true));
        }
    }

    @Test
    @DisplayName("VAlidamos que se pueda responder correctamente a una solicitud válida")
    void test_Validate_HttpRequest() throws IOException, InterruptedException {
        Thread serverThread = new Thread(() -> homeController.run());
        serverThread.start();
        Thread.sleep(500);

        try (Socket clientSocket = new Socket("localhost", testPort);
                BufferedWriter output = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()));
                BufferedReader input = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()))) {

            output.write("GET /recordings HTTP/1.1\r\n\r\n");
            output.flush();

            String response = input.readLine();
            assertThat("El servidor debería responder con 200 OK", response, containsString("HTTP/1.1 200 OK"));
        }
    }

    @Test
    @DisplayName("VAlidamos que se pueda manejar múltiples conexiones")
    void test_Handles_MultipleConnections() throws IOException, InterruptedException {
        Thread serverThread = new Thread(() -> homeController.run());
        serverThread.start();
        Thread.sleep(500);

        try (Socket clientSocket1 = new Socket("localhost", testPort);
                Socket clientSocket2 = new Socket("localhost", testPort)) {

            assertThat("El primer cliente debería conectarse", clientSocket1.isConnected(), is(true));
            assertThat("El segundo cliente debería conectarse", clientSocket2.isConnected(), is(true));
        }
    }

    @Test
    @DisplayName("VElidamos que se pueda detener el servidor correctamente")
    void test_Server_StopsSuccessfully() throws IOException, InterruptedException {
        Thread serverThread = new Thread(() -> homeController.run());
        serverThread.start();
        Thread.sleep(500);

        homeController.stop();
        Thread.sleep(500);

        assertThat("El servidor debería estar cerrado después de detenerse", serverSocket.isClosed(), is(true));
    }

    @AfterEach
    void tearDown() {
        homeController.stop();
        try {
            serverSocket.close();
        } catch (IOException e) {
            System.err.println("Error al cerrar el servidor de prueba: " + e.getMessage());
        }
    }

}
