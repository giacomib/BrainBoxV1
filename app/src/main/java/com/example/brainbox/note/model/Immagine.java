package com.example.brainbox.note.model;

public class Immagine extends Elemento{

    private String uri;
    public Immagine() {}

    public Immagine(String uri) {
        super("Immagine");
        this.uri = uri;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }
}
