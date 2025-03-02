package dev.lanny.byte_beats_backend.models;

import java.util.List;

public class Recording {
    private int id;
    private String title;
    private String instrument;
    private long duration;
    private List<Note> notes;

    public Recording(int id, String title, String instrument, long duration, List<Note> notes) {
        this.id = id;
        this.title = title;
        this.instrument = instrument;
        this.duration = duration;
        this.notes = notes;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getInstrument() {
        return instrument;
    }

    public long getDuration() {
        return duration;
    }

    public List<Note> getNotes() {
        return notes;
    }

    @Override
    public String toString() {
        return "Recording{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", instrument='" + instrument + '\'' +
                ", duration=" + duration +
                ", notes=" + notes +
                '}';
    }
}
