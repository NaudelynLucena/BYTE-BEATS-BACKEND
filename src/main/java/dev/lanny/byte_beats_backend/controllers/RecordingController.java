package dev.lanny.byte_beats_backend.controllers;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.List;
import java.util.Map;

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
                return "HTTP/1.1 400 Bad Request\r\n\r\nInvalid ID format";
            }
        }
        if ("POST".equalsIgnoreCase(method) && path.equals("/recordings")) {
            return createRecording(readRequestBody(input));
        }
        if ("PUT".equalsIgnoreCase(method) && path.startsWith("/recordings/")) {
            try {
                int recordingId = Integer.parseInt(path.substring(12));
                return updateRecording(recordingId, readRequestBody(input));
            } catch (NumberFormatException e) {
                return "HTTP/1.1 400 Bad Request\r\n\r\nInvalid ID format (must be a number)";
            }
        }

        if ("DELETE".equalsIgnoreCase(method) && path.startsWith("/recordings/")) {
            try {
                int id = Integer.parseInt(path.substring(12));
                return destroyRecording(id);
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
            String timestamp = (String) jsonMap.get("timestamp");
            long duration = ((Number) jsonMap.get("duration")).longValue();
            String instrument = (String) jsonMap.get("instrument");

            Object notesObj = jsonMap.get("notes");
            if (!(notesObj instanceof List<?>)) {
                return "HTTP/1.1 400 Bad Request\r\n\r\nInvalid notes format";
            }
            List<Map<String, Object>> notesList = ((List<?>) notesObj).stream()
                    .filter(item -> item instanceof Map<?, ?>)
                    .map(item -> {
                        if (item instanceof Map<?, ?> map) {
                            return (Map<String, Object>) map;
                        } else {
                            throw new IllegalArgumentException("Invalid notes format");
                        }
                    })
                    .toList();
            List<NoteDto> notes = notesList.stream().map(note -> new NoteDto(
                    ((Number) note.get("midi")).intValue(),
                    ((Number) note.get("startTime")).longValue(),
                    ((Number) note.get("stopTime")).longValue(),
                    ((Number) note.get("duration")).longValue())).toList();

            RecordingDto recordingDto = new RecordingDto(0, timestamp, duration, instrument, notes);
            recordingRepository.saveRecording(recordingDto);
            return "HTTP/1.1 201 Created\r\n\r\nRecording created successfully";
        } catch (Exception e) {
            e.printStackTrace();
            return "HTTP/1.1 400 Bad Request\r\n\r\nInvalid JSON format";
        }
    }

    public String getRecordingById(int recordingId) {
        RecordingDto recording = recordingRepository.getRecordingById(recordingId);
        return recording != null ? JsonManager.toJson(recording) : "HTTP/1.1 404 Not Found\r\n\r\nRecording not found";
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

            Object notesObj = jsonMap.get("notes");
            if (!(notesObj instanceof List<?>)) {
                return "HTTP/1.1 400 Bad Request\r\n\r\nInvalid notes format";
            }
            List<?> notesRawList = (List<?>) notesObj;
            List<Map<String, Object>> notesList = notesRawList.stream()
                    .filter(item -> item instanceof Map<?, ?>)
                    .map(item -> (Map<String, Object>) item)
                    .toList();
            List<NoteDto> notes = notesList.stream().map(note -> new NoteDto(
                    ((Number) note.get("midi")).intValue(),
                    ((Number) note.get("startTime")).longValue(),
                    ((Number) note.get("stopTime")).longValue(),
                    ((Number) note.get("duration")).longValue())).toList();

            RecordingDto recordingDto = new RecordingDto(recordingId, timestamp, duration, instrument, notes);
            boolean updated = recordingRepository.updateRecording(recordingId, recordingDto);

            return updated ? "HTTP/1.1 200 OK\r\n\r\nRecording updated successfully"
                    : "HTTP/1.1 404 Not Found\r\n\r\nRecording not found";

        } catch (Exception e) {
            e.printStackTrace();
            return "HTTP/1.1 400 Bad Request\r\n\r\nInvalid JSON format";
        }
    }

    public String destroyRecording(int recordingId) {
        boolean deleted = recordingRepository.destroyRecording(recordingId);
        return deleted ? "HTTP/1.1 200 OK\r\n\r\nRecording deleted successfully"
                : "HTTP/1.1 404 Not Found\r\n\r\nRecording not found";
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

        String json = requestBody.toString().trim();
        System.out.println("Cuerpo de la petición recibido: " + json);

        return json;
    }
}
