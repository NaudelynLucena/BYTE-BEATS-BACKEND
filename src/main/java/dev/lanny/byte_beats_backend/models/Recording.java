package dev.lanny.byte_beats_backend.models;

public class Recording {
    private int id;
    private String title;
    private double duration;

    public Recording(int id, String title, double duration) {
        this.id = id;
        this.title = title;
        this.duration = duration;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public double getDuration() {
        return duration;
    }

    public void startRecording() {
        System.out.println("Recording started");
    }

    public void stopRecording() {
        System.out.println("Recording stopped");
    }

    public void playRecording() {
        System.out.println("Recording playing");
    }

    @Override
    public String toString() {
        return "Recording{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", duration=" + duration +
                '}';
    }

}
