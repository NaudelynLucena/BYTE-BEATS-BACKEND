package dev.lanny.byte_beats_backend.models;

public class Piano extends Instruments{

 private int numberOfKeys;

    public Piano( int id,String name, String type, String sound, int numberOfKeys) {
        super(name ,id,type,sound);
        this.numberOfKeys = numberOfKeys;
    }
    
    public  int getNumberOfKeys() {return numberOfKeys;}

    @Override
    public void sound() {
        System.out.println(getname() + "Piano sound" + "with" + numberOfKeys + "keys is playing" + getSound()); }

}
