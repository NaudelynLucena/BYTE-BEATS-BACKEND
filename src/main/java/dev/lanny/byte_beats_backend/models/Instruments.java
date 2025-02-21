package dev.lanny.byte_beats_backend.models;

public class Instruments {
    private String name;
    private int id;
    private String type;
    private String sound;

    public Instruments(String name, int id, String type, String sound) {
        this.name = name;
        this.id = id;
        this.type = type;
        this.sound = sound;
    }

    public int getId() { return id;}
    public  String getname() { return name;}
    public String getType() { return type;}
    public String getSound() { return sound;}

    
public void sound() { System.out.println(name + " sound" + " is playing" + sound); }

       
    
 


}
