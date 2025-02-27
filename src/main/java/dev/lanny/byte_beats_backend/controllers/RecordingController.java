package dev.lanny.byte_beats_backend.controllers;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import dev.lanny.byte_beats_backend.dtos.RecordingDto;
import dev.lanny.byte_beats_backend.repository.RecordingRepository;
import utils.JsonManager;


public class RecordingController {

    private final RecordingRepository recordingRepository;

    public RecordingController(){
        this.recordingRepository = new RecordingRepository();

    }

    public String processRecordingRequest(String method, String path, BufferedReader input){
        if ("GET".equalsIgnoreCase(method) && path.equals("/recordings")) {
            return getAllRecordings();
        }
        if ("GET".equalsIgnoreCase(method) && path.startsWith("/recordings/")) {
            try {
                int id = Integer.parseInt(path.substring(12));
                return getRecordingById(id);
            } catch (NumberFormatException e) {
                return "HTTP/1.1 400 Bad Request\r\n\r\nInvalid ID format";
            }
        }
        if ("POST".equalsIgnoreCase(method) && path.equals("/recordings")) {
            return createRecording(readRequestBody(input));
        }
        if ("PUT".equalsIgnoreCase(method) && path.startsWith("/recordings/")) {
            try{
                int id = Integer.parseInt(path.substring(12));
                return updateRecording(id , readRequestBody(input));
            } catch (NumberFormatException e) {
                return "HTTP/1.1 400 Bad Request\r\n\r\nInvalid ID format";
            }
        }

        if ("DELETE".equalsIgnoreCase(method) && path.startsWith("/recordings/")) {
            try {
                int id = Integer.parseInt(path.substring(12));
                destroyRecording(id);
            } catch (NumberFormatException e) {
                return "HTTP/1.1 400 Bad Request\r\n\r\nInvalid ID format";
            }
        }
        return "HTTP/1.1 404 Not Found\r\n\r\n";
    }

    public String createRecording(String requestBody) {
        System.out.println("Recibido JSON: " + requestBody);

        try {
            Map<String, Object> jsonMap = JsonManager.fromJsonToMap(requestBody);
            int id = ((Double) jsonMap.get("id")).intValue();
            String title = (String) jsonMap.get("title");
            double duration = (double) jsonMap.get("duration");

            RecordingDto recordingDto = new RecordingDto(id, title, duration);
            recordingRepository.saveRecording(recordingDto);
            return "HTTP/1.1 201 Created\r\n\r\nRecording created successfully";
        } catch (Exception e) {
            e.printStackTrace();
            return "HTTP/1.1 400 Bad Request\r\n\r\nInvalid JSON format";
        }
    }

    public String getRecordingById(int id) {
        RecordingDto recording = recordingRepository.getRecordingById(id);
        return recording != null ? JsonManager.toJson(recording) : "HTTP/1.1 404 Not Found\r\n\r\nRecording not found";
    }

    public String getAllRecordings() {
        List<RecordingDto> recordings = recordingRepository.getAllRecordings();
        return JsonManager.toJson(recordings);
    }

    public String updateRecording(int id, String requestBody) {
        try {
            Map<String, Object> jsonMap = JsonManager.fromJsonToMap(requestBody);
            String title = jsonMap.get("title") != null ? jsonMap.get("title").toString() : "Default Title";
            double duration = jsonMap.get("duration") != null ? ((Number) jsonMap.get("duration")).doubleValue() : 0.0;

            RecordingDto recordingDto = new RecordingDto(id, title, duration);
            boolean updated = recordingRepository.updateRecording(id, recordingDto);

            return updated ? "HTTP/1.1 200 OK\r\n\r\nRecording updated successfully" : "HTTP/1.1 404 Not Found\r\n\r\nRecording not found";

        } catch (Exception e) {
            e.printStackTrace();
            return "HTTP/1.1 400 Bad Request\r\n\r\nInvalid JSON format";
        }
    }

    public void destroyRecording(int id) {
        recordingRepository.destroyRecording(id);
    }

    private String readRequestBody(BufferedReader in) throws IOException {
        StringBuilder requestBody = new StringBuilder();
        String line;

        while ((line = in.readLine()) != null && !line.isEmpty()) {
            System.out.println("Header: " + line);
        }

        while (in.ready()) {
            requestBody.append((char) in.read());
        }

        String json = requestBody.toString().trim();
        System.out.println("Cuerpo de la petición recibido: " + json);

        return json;
    }
}
