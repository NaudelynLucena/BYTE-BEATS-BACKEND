package dev.lanny.byte_beats_backend.controllers;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import dev.lanny.byte_beats_backend.dtos.RecordingDto;
import dev.lanny.byte_beats_backend.repository.RecordingRepository;
import utils.JsonManager;

public class RecordingController {
    private static final Logger LOGGER = Logger.getLogger(RecordingController.class.getName());
    private final RecordingRepository recordingRepository;

    public RecordingController() {
        this.recordingRepository = new RecordingRepository();
    }

    public String processRecordingRequest(String method, String path, BufferedReader input) throws IOException {
        try {
            if ("GET".equalsIgnoreCase(method)) {
                return handleGetRequest(path);
            } else if ("POST".equalsIgnoreCase(method) && path.equals("/recordings")) {
                return createRecording(readRequestBody(input));
            } else if ("PUT".equalsIgnoreCase(method)) {
                return handlePutRequest(path, input);
            } else if ("DELETE".equalsIgnoreCase(method)) {
                return handleDeleteRequest(path);
            }
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error procesando la petición", e);
            return formatHttpResponse(500, "Error interno del servidor");
        }

        return formatHttpResponse(404, "Recurso no encontrado");
    }

    private String handleGetRequest(String path) {
        if (path.equals("/recordings")) {
            return formatHttpResponse(200, getAllRecordings());
        } else if (path.startsWith("/recordings/")) {
            try {
                int id = Integer.parseInt(path.substring(12));
                return formatHttpResponse(200, getRecordingById(id));
            } catch (NumberFormatException e) {
                return formatHttpResponse(400, "Formato de ID inválido");
            }
        }
        return formatHttpResponse(404, "Recurso no encontrado");
    }

    private String handlePutRequest(String path, BufferedReader input) throws IOException {
        if (path.startsWith("/recordings/")) {
            try {
                int id = Integer.parseInt(path.substring(12));
                return formatHttpResponse(200, updateRecording(id, readRequestBody(input)));
            } catch (NumberFormatException e) {
                return formatHttpResponse(400, "Formato de ID inválido");
            }
        }
        return formatHttpResponse(404, "Recurso no encontrado");
    }

    private String handleDeleteRequest(String path) {
        if (path.startsWith("/recordings/")) {
            try {
                int id = Integer.parseInt(path.substring(12));
                return destroyRecording(id);
            } catch (NumberFormatException e) {
                return formatHttpResponse(400, "Formato de ID inválido");
            }
        }
        return formatHttpResponse(404, "Recurso no encontrado");
    }

    public String createRecording(String requestBody) {
        LOGGER.info("Recibido JSON: " + requestBody);
        try {
            Map<String, Object> jsonMap = JsonManager.fromJsonToMap(requestBody);
            int id = ((Double) jsonMap.get("id")).intValue();
            String title = (String) jsonMap.get("title");
            double duration = (double) jsonMap.get("duration");

            RecordingDto recordingDto = new RecordingDto(id, title, duration);
            recordingRepository.saveRecording(recordingDto);
            return formatHttpResponse(201, "Grabación creada exitosamente");
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error al procesar JSON", e);
            return formatHttpResponse(400, "Formato JSON inválido");
        }
    }

    public String getRecordingById(int id) {
        RecordingDto recording = recordingRepository.getRecordingById(id);
        return recording != null
                ? JsonManager.toJson(recording)
                : formatHttpResponse(404, "Grabación no encontrada");
    }

    public String getAllRecordings() {
        List<RecordingDto> recordings = recordingRepository.getAllRecordings();
        return JsonManager.toJson(recordings);
    }

    public String updateRecording(int id, String requestBody) {
        try {
            Map<String, Object> jsonMap = JsonManager.fromJsonToMap(requestBody);
            String title = (String) jsonMap.getOrDefault("title", "Título por defecto");
            double duration = ((Number) jsonMap.getOrDefault("duration", 0.0)).doubleValue();

            RecordingDto recordingDto = new RecordingDto(id, title, duration);
            boolean updated = recordingRepository.updateRecording(id, recordingDto);

            return updated
                    ? formatHttpResponse(200, "Grabación actualizada exitosamente")
                    : formatHttpResponse(404, "Grabación no encontrada");
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error al actualizar grabación", e);
            return formatHttpResponse(400, "Formato JSON inválido");
        }
    }

    public String destroyRecording(int id) {
        boolean deleted = recordingRepository.destroyRecording(id);
        return deleted
                ? formatHttpResponse(200, "Grabación eliminada correctamente")
                : formatHttpResponse(404, "Grabación no encontrada");
    }

    private String readRequestBody(BufferedReader input) throws IOException {
        StringBuilder requestBody = new StringBuilder();
        String line;
        while ((line = input.readLine()) != null && !line.isEmpty()) {
            LOGGER.info("Header: " + line);
        }
        while (input.ready()) {
            requestBody.append((char) input.read());
        }
        return requestBody.toString().trim();
    }

    private String formatHttpResponse(int statusCode, String message) {
        return "HTTP/1.1 " + statusCode + " \r\n\r\n" + message;
    }
}
