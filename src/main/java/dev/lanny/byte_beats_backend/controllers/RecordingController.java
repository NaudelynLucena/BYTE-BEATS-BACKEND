package dev.lanny.byte_beats_backend.controllers;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.ArrayList;

import dev.lanny.byte_beats_backend.dtos.NoteDto;
import dev.lanny.byte_beats_backend.dtos.RecordingDto;
import dev.lanny.byte_beats_backend.repository.RecordingRepository;
import utils.JsonManager;


public class RecordingController {

    private final RecordingRepository recordingRepository;

    public RecordingController() {
        this.recordingRepository = new RecordingRepository();
    }

    public String processRecordingRequest(String method, String path, BufferedReader input) throws IOException {
        if ("GET".equalsIgnoreCase(method) && path.equals("/recordings")) {
            return getAllRecordings();
        }
        if ("GET".equalsIgnoreCase(method) && path.startsWith("/recordings/")) {
            try {
                int id = Integer.parseInt(path.substring(12));
                return getRecordingById(id);
            } catch (NumberFormatException e) {
                return "HTTP/1.1 400 Bad Request\r\n\r\nID inválido";
            }
        }
        if ("POST".equalsIgnoreCase(method) && path.equals("/recordings")) {
            createRecording(readRequestBody(input));
            return "HTTP/1.1 201 Created\r\n\r\nGrabación creada"; 
        }
        if ("PUT".equalsIgnoreCase(method) && path.startsWith("/recordings/")) {
            try {
                int recordingId = Integer.parseInt(path.substring(12));
                return updateRecording(recordingId, readRequestBody(input));
            } catch (NumberFormatException e) {
                return "HTTP/1.1 400 Bad Request\r\n\r\nID inválido";
            }
        }
        if ("DELETE".equalsIgnoreCase(method) && path.startsWith("/recordings/")) {
            try {
                int id = Integer.parseInt(path.substring(12));
                return destroyRecording(id);
            } catch (NumberFormatException e) {
                return "HTTP/1.1 400 Bad Request\r\n\r\nID inválido";
            }
        }
        return "HTTP/1.1 404 Not Found\r\n\r\n";
    }

    public void createRecording(String requestBody) {
        try {
            Map<String, Object> jsonMap = JsonManager.fromJsonToMap(requestBody);
            String timestamp = (String) jsonMap.get("timestamp");
            long duration = ((Number) jsonMap.get("duration")).longValue();
            String instrument = (String) jsonMap.get("instrument");    
            
            Object notesObj = jsonMap.get("notes");
            if (!(notesObj instanceof List<?>)) {
                System.out.println("Error: Formato incorrecto de notas");
                return;
            }
    
            List<NoteDto> notes = new ArrayList<>();
            for (Object item : (List<?>) notesObj) {
                if (item instanceof Map<?, ?> map) {
                    int midi = ((Number) map.get("midi")).intValue();
                    long startTime = ((Number) map.get("startTime")).longValue();
                    long stopTime = ((Number) map.get("stopTime")).longValue();
                    long noteDuration = ((Number) map.get("duration")).longValue();
    
                    notes.add(new NoteDto(midi, startTime, stopTime, noteDuration));
                }
            }
                
            RecordingDto recordingDto = new RecordingDto(0, timestamp, duration, instrument, notes);
            recordingRepository.saveRecording(recordingDto);
    
            System.out.println("Grabación creada correctamente.");
            
        } catch (Exception e) {
            System.out.println("Error al procesar la grabación: " + e.getMessage());
        }
    }
    
    public String getRecordingById(int recordingId) {
        RecordingDto recording = recordingRepository.getRecordingById(recordingId);
        return recording != null ? JsonManager.toJson(recording)
                : "HTTP/1.1 404 Not Found\r\n\r\nGrabación no encontrada";
    }

    public String getAllRecordings() {
        List<RecordingDto> recordings = recordingRepository.getAllRecordings();
        return JsonManager.toJson(recordings);
    }

    public String updateRecording(int recordingId, String requestBody) {
        try {
            Map<String, Object> jsonMap = JsonManager.fromJsonToMap(requestBody);
            String timestamp = (String) jsonMap.get("timestamp");
            long duration = ((Number) jsonMap.get("duration")).longValue();
            String instrument = (String) jsonMap.get("instrument");

            RecordingDto recordingDto = new RecordingDto(recordingId, timestamp, duration, instrument, List.of());
            boolean updated = recordingRepository.updateRecording(recordingId, recordingDto);

            return updated ? "HTTP/1.1 200 OK\r\n\r\nGrabación actualizada"
                    : "HTTP/1.1 404 Not Found\r\n\r\nNo encontrada";
        } catch (Exception e) {
            return "HTTP/1.1 400 Bad Request\r\n\r\nFormato JSON inválido";
        }
    }

    public String destroyRecording(int recordingId) {
        boolean deleted = recordingRepository.destroyRecording(recordingId);
        return deleted ? "HTTP/1.1 200 OK\r\n\r\nGrabación eliminada" : "HTTP/1.1 404 Not Found\r\n\r\nNo encontrada";
    }

    private String readRequestBody(BufferedReader input) throws IOException {
        StringBuilder requestBody = new StringBuilder();
        String line;

        while ((line = input.readLine()) != null && !line.isEmpty()) {
            System.out.println("Header: " + line);
        }
        while (input.ready()) {
            requestBody.append((char) input.read());
        }

        return requestBody.toString().trim();
    }

}

