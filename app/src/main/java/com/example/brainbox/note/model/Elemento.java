package com.example.brainbox.note.model;

/**
 * La classe Elemento rappresenta un elemento, caratterizzato da una specifica tipologia, con la
 * quale personalizzare la propria pagina di note.
 */
public class Elemento {
    /** La tipologia dell'elemento. */
    private String tipologia;

    /**
     * Costruttore predefinito di Elemento.
     * Crea un elemento senza specificare la tipologia.
     */
    public Elemento(){}
    /**
     * Costruttore che inizializza un elemento con la tipologia specificata.
     * @param tipologia La tipologia dell'elemento.
     */
    public  Elemento(String tipologia){
        this.tipologia = tipologia;
    }

    /**
     * Restituisce la tipologia dell'elemento.
     * @return La tipologia dell'elemento.
     */
    public String getTipologia() {
        return tipologia;
    }
}
