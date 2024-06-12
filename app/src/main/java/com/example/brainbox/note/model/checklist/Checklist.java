package com.example.brainbox.note.model.checklist;

import com.example.brainbox.note.model.Elemento;

import java.util.ArrayList;

/**
 * Classe che rappresenta una checklist, un tipo di {@link Elemento}.
 * Una checklist contiene un titolo e una lista di elementi {@link Check}.
 */
public class Checklist extends Elemento {

    /**
     * Il titolo della checklist.
     */
    private String titolo;
    /**
     * La lista di elementi {@link Check} nella checklist.
     */
    private ArrayList<Check> checks;

    /**
     * Costruttore di default che crea una checklist vuota.
     */
    public Checklist(){
        super();
    }

    /**
     * Costruttore che crea una checklist con un titolo specificato.
     *
     * @param titolo Titolo della checklist.
     */
    public Checklist(String titolo){
        super("Check List");
        this.titolo = titolo;
        this.checks = new ArrayList<>();
    }

    /**
     * Costruttore che crea una checklist con un titolo specificato e una lista di elementi {@link Check}.
     *
     * @param titolo Titolo della checklist.
     * @param checks Lista di elementi {@link Check}.
     */
    public Checklist(String titolo, ArrayList<Check> checks){
        super("Check List");
        this.titolo = titolo;
        this.checks = checks;
    }

    /**
     * Restituisce il titolo della checklist.
     *
     * @return Titolo della checklist.
     */
    public String getTitolo() {
        return titolo;
    }

    /**
     * Restituisce la lista di elementi {@link Check} della checklist.
     *
     * @return Lista di elementi {@link Check}.
     */
    public ArrayList<Check> getChecks() {
        return checks;
    }
}
