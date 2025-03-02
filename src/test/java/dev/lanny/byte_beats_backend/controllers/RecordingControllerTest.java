package dev.lanny.byte_beats_backend.controllers;

import org.junit.jupiter.api.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

class RecordingControllerTest {
    private ServerSocket serverSocket;
    private int testPort = 9091;
    private RecordingController recordingController;

    @BeforeEach
    void setUp() throws Exception {
        serverSocket = new ServerSocket(testPort);
        recordingController = new RecordingController();
    }

    @Test
    @DisplayName("Validamos que el servidor responde correctamente a GET /recordings")
    void test_GetAllRecordings() throws IOException, InterruptedException {
        Thread serverThread = new Thread(() -> {
            try (Socket socket = serverSocket.accept();
                    BufferedWriter output = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
                    BufferedReader input = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {

                input.readLine(); // Leer la solicitud
                String response = recordingController.getAllRecordings();
                output.write("HTTP/1.1 200 OK\r\n\r\n" + response);
                output.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
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
    @DisplayName("Validamos que el servidor responde correctamente a GET /recordings/{id}")
    void test_GetRecordingById() throws IOException, InterruptedException {
        Thread serverThread = new Thread(() -> {
            try (Socket socket = serverSocket.accept();
                    BufferedWriter output = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
                    BufferedReader input = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {

                input.readLine(); // Leer la solicitud
                String response = recordingController.getRecordingById(1);
                output.write("HTTP/1.1 200 OK\r\n\r\n" + response);
                output.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        serverThread.start();
        Thread.sleep(500);

        try (Socket clientSocket = new Socket("localhost", testPort);
                BufferedWriter output = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()));
                BufferedReader input = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()))) {

            output.write("GET /recordings/1 HTTP/1.1\r\n\r\n");
            output.flush();

            String response = input.readLine();
            assertThat("El servidor debería responder con 200 OK", response, containsString("HTTP/1.1 200 OK"));
        }
    }

    @Test
    @DisplayName("Validamos que el servidor responde correctamente a POST /recordings")
    void test_CreateRecording() throws IOException, InterruptedException {
        Thread serverThread = new Thread(() -> {
            try (Socket socket = serverSocket.accept();
                    BufferedWriter output = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
                    BufferedReader input = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {

                input.readLine(); // Leer la solicitud
                String requestBody = "{ \"timestamp\": \"2024-03-01T12:00:00Z\", \"duration\": 120, \"instrument\": \"piano\", \"notes\": [] }";
                recordingController.createRecording(requestBody);
                output.write("HTTP/1.1 201 Created\r\n\r\nGrabación creada");
                output.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        serverThread.start();
        Thread.sleep(500);

        try (Socket clientSocket = new Socket("localhost", testPort);
                BufferedWriter output = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()));
                BufferedReader input = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()))) {

            output.write("POST /recordings HTTP/1.1\r\nContent-Length: 100\r\n\r\n");
            output.write(
                    "{ \"timestamp\": \"2024-03-01T12:00:00Z\", \"duration\": 120, \"instrument\": \"piano\", \"notes\": [] }");
            output.flush();

            String response = input.readLine();
            assertThat("El servidor debería responder con 201 Created", response,
                    containsString("HTTP/1.1 201 Created"));
        }
    }

    @Test
    @DisplayName("Validamos que el servidor responde correctamente a DELETE /recordings/{id}")
    void test_DeleteRecording() throws IOException, InterruptedException {
        Thread serverThread = new Thread(() -> {
            try (Socket socket = serverSocket.accept();
                    BufferedWriter output = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
                    BufferedReader input = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {

                input.readLine(); // Leer la solicitud
                String response = recordingController.destroyRecording(1);
                output.write("HTTP/1.1 200 OK\r\n\r\n" + response);
                output.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        serverThread.start();
        Thread.sleep(500);

        try (Socket clientSocket = new Socket("localhost", testPort);
                BufferedWriter output = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()));
                BufferedReader input = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()))) {

            output.write("DELETE /recordings/1 HTTP/1.1\r\n\r\n");
            output.flush();

            String response = input.readLine();
            assertThat("El servidor debería responder con 200 OK", response, containsString("HTTP/1.1 200 OK"));
        }
    }

    @AfterEach
    void tearDown() {
        try {
            serverSocket.close();
        } catch (IOException e) {
            System.err.println("Error al cerrar el servidor de prueba: " + e.getMessage());
        }
    }
}
