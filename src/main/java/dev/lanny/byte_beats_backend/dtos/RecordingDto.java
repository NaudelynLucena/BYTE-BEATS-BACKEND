package dev.lanny.byte_beats_backend.dtos;

import java.util.List;

public record RecordingDto(
    int id,
    String timestamp,
    long duration,
    String instrument,
    List<NoteDto> notes
) {}
