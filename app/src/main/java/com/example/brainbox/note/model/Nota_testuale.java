package com.example.brainbox.note.model;

/**
 * La classe {@code Nota_testuale} rappresenta una nota testuale, estende la classe {@link Elemento}.
 * Contiene un titolo e un contenuto testuale.
 */
public class Nota_testuale extends Elemento{

    /**
     * Titolo della nota testuale
     */
    private String titolo;
    /**
     * Contenuto della nota testuale
     */
    private String contenuto;

    /**
     * Costruttore predefinito per la classe {@code Nota_testuale}.
     */
    public Nota_testuale(){}

    /**
     * Costruttore che inizializza una nuova nota testuale con un titolo e un contenuto specificati.
     *
     * @param titolo    il titolo della nota testuale
     * @param contenuto il contenuto della nota testuale
     */
    public Nota_testuale(String titolo, String contenuto){
        super("Nota testuale");
        this.titolo = titolo;
        this.contenuto = contenuto;

    }

    /**
     * Restituisce il titolo della nota testuale.
     *
     * @return il titolo della nota testuale
     */
    public String getTitolo() {
        return titolo;
    }


    /**
     * Restituisce il contenuto della nota testuale.
     *
     * @return il contenuto della nota testuale
     */
    public String getContenuto() {
        return contenuto;
    }

    /**
     * Imposta il titolo della nota testuale.
     *
     * @param titolo il nuovo titolo della nota testuale
     */
    public void setTitolo(String titolo) {
        this.titolo = titolo;
    }

    /**
     * Imposta il contenuto della nota testuale.
     *
     * @param contenuto il nuovo contenuto della nota testuale
     */
    public void setContenuto(String contenuto) {
        this.contenuto = contenuto;
    }


}

