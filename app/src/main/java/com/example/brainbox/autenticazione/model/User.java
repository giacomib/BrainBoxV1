package com.example.brainbox.autenticazione.model;

/**
 * La classe User rappresenta un utente con un nome utente e un'email.
 */
public class User {

    /** Email dell'utente. */
    private String email;
    /** L'username dell'utente. */
    private String username;

    /**
     * Costruttore vuoto per la classe User.
     */
    public User(){}
    /**
     * Costruttore che inizializza un nuovo oggetto User con il nome utente e l'email specificati.
     * @param username Il nome utente dell'utente.
     * @param email La email dell'utente.
     */
    public User(String username, String email){
        this.username = username;
        this.email = email;
    }
    /**
     * Restituisce l'username dell'utente.
     * @return l'username dell'utente.
     */
    public String getUsername() {
        return username;
    }
    /**
     * Restituisce l'email dell'utente.
     * @return L'email dell'utente.
     */
    public String getEmail() {
        return email;
    }
    /**
     * Imposta l'email dell'utente.
     * @param email La nuova email dell'utente.
     */
    public void setEmail(String email) {
        this.email = email;
    }
    /**
     * Imposta il nome utente dell'utente.
     * @param username Il nuovo username dell'utente.
     */
    public void setUsername(String username){
        this.username = username;
    }
}
