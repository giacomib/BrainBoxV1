package com.example.brainbox.note.model.checklist;

/**
 * Classe che rappresenta un singolo elemento di una checklist.
 * Ogni elemento ha un nome (item) e uno stato (isChecked) che indica se è checkato.
 */
public class Check {

    /**
     * Il nome dell'elemento della checklist.
     */
    private String item;
    /**
     * Stato dell'elemento della checklist, indica se l'item è stato checkato.
     */
    private boolean isChecked;

    /**
     * Costruttore di default che crea un elemento vuoto non completato.
     */
    public Check(){
    }
    /**
     * Costruttore che crea un elemento con un nome specificato e non completato.
     *
     * @param item Nome dell'elemento.
     */
    public Check(String item) {
        this.item = item;
        this.isChecked = false;
    }

    /**
     * Costruttore che crea un elemento con un nome specificato e uno stato specificato.
     *
     * @param item Nome dell'elemento.
     * @param isChecked Stato dell'elemento, indica se è stato completato.
     */
    public Check(String item, boolean isChecked) {
        this.item = item;
        this.isChecked = isChecked;
    }

    /**
     * Restituisce il nome dell'elemento della checklist.
     *
     * @return Nome dell'elemento.
     */
    public String getItem() {
        return item;
    }

    /**
     * Restituisce lo stato dell'elemento della checklist.
     *
     * @return {@code true} se l'elemento è stato completato, altrimenti {@code false}.
     */
    public boolean isChecked() {
        return isChecked;
    }

    /**
     * Imposta il nome dell'elemento della checklist.
     *
     * @param item Nome dell'elemento.
     */
    public void setItem(String item) {
        this.item = item;
    }

    /**
     * Imposta lo stato dell'elemento della checklist.
     *
     * @param checked {@code true} se l'elemento è stato completato, altrimenti {@code false}.
     */
    public void setChecked(boolean checked) {
        isChecked = checked;
    }
}
