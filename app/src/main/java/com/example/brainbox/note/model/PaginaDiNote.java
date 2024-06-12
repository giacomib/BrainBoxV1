package com.example.brainbox.note.model;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

/**
 * La classe PaginaDiNote rappresenta una pagina di appunti con un titolo, una data e una lista
 * degli elementi di cui è composta.
 */
public class PaginaDiNote {

    /** Il titolo della pagina di note. */
    private String titolo;
    /** La data di creazione della pagina di note. */
    private String data;
    /** La lista di elementi presenti nella pagina di note. */
    private ArrayList<Elemento> elementi;

    /**
     * Costruttore predefinito che inizializza la data alla data corrente.
     */
    public PaginaDiNote(){
        LocalDate date = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        this.data = date.format(formatter);
    }

    /**
     * Costruttore che inizializza una pagina di note con un titolo specificato.
     * La data viene impostata sulla data corrente e la lista di elementi viene inizializzata vuota.
     * @param titolo Il titolo della pagina di note.
     */
    public PaginaDiNote(String titolo){
        this();
        this.titolo = titolo;
        this.elementi = new ArrayList<>();
    }

    /**
     * Restituisce la data di creazione della pagina di note.
     * @return La data di creazione della pagina di note.
     */
    public String getData() {
        return data;
    }
    /**
     * Restituisce il titolo della pagina di note.
     * @return Il titolo della pagina di note.
     */
    public String getTitolo() {
        return titolo;
    }
    /**
     * Restituisce la lista di elementi presenti nella pagina di note.
     * @return La lista di elementi presenti nella pagina di note.
     */
    public ArrayList<Elemento> getElementi() {
        return elementi;
    }
}
