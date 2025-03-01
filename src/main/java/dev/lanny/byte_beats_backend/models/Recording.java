package dev.lanny.byte_beats_backend.models;

public class Recording {
    private int id;
    private String instrument;   
    private long  duration;

    public Recording(int id, String title, String instrument, long duration) {
        this.id = id;      
        this.instrument = instrument;
        this.duration = duration;
    }

    public int getId() {
        return id;
    }  

    public String getInstrument() { 
        return instrument;
    }

    public long getDuration() {
        return duration;
    }

    @Override
    public String toString() {
        return "Recording{" +
                "id=" + id +                
                ", instrument='" + instrument + '\'' +
                ", duration=" + duration +
                '}';
    }
}


