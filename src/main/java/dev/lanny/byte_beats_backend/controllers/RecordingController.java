package dev.lanny.byte_beats_backend.controllers;

import java.io.BufferedReader;

import dev.lanny.byte_beats_backend.repository.RecordingRepository;


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
                return updateRecordingById(id , readRequestBody(input));
            } catch (NumberFormatException e) {
                return "HTTP/1.1 400 Bad Request\r\n\r\nInvalid ID format";
            }
        }

        if ("DELETE".equalsIgnoreCase(method) && path.startsWith("/recordings/")) {
            try {
                int id = Integer.parseInt(path.substring(12));
                return destroyRecordingById(id);
            } catch (NumberFormatException e) {
                return "HTTP/1.1 400 Bad Request\r\n\r\nInvalid ID format";
            }
        }
        return "HTTP/1.1 404 Not Found\r\n\r\n";
    }

}


