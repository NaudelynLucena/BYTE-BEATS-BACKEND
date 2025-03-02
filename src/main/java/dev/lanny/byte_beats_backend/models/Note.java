package dev.lanny.byte_beats_backend.models;

public class Note {
    private int midi;
    private long startTime;
    private long stopTime;
    private long duration;

    public Note(int midi, long startTime, long stopTime, long duration) {
        this.midi = midi;
        this.startTime = startTime;
        this.stopTime = stopTime;
        this.duration = duration;
    }

    public int getMidi() {
        return midi;
    }

    public long getStartTime() {
        return startTime;
    }

    public long getStopTime() {
        return stopTime;
    }

    public long getDuration() {
        return duration;
    }

    @Override
    public String toString() {
        return "Note{" +
                "midi=" + midi +
                ", startTime=" + startTime +
                ", stopTime=" + stopTime +
                ", duration=" + duration +
                '}';
    }
}
