package dev.lanny.byte_beats_backend.controllers;

import dev.lanny.byte_beats_backend.config.repository.RecordingRepository;


public class RecordingController {

    private final RecordingRepository recordingRepository;

    public RecordingController(){
        this.recordingRepository = new RecordingRepository();
        
    }


}
