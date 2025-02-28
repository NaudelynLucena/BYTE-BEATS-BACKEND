package dev.lanny.byte_beats_backend.dtos;

public record NoteDto(
    int midi,
    long startTime,
    long stopTime,
    long duration
) {}
