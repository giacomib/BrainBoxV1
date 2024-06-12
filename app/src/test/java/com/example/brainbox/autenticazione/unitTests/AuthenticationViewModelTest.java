package com.example.brainbox.autenticazione.unitTests;

import static org.junit.jupiter.api.Assertions.*;

import com.example.brainbox.autenticazione.viewModel.AuthenticationViewModel;

import org.junit.Test;

/**
 * Classe di test per verificare diversi comportamenti del metodo checkInput della classe AuthenticationViewModel.
 */
public class AuthenticationViewModelTest {

    /**
     * Test per verificare l'input valido.
     * Verifica se l'input composto da un'email, una password e un username non vuoti è valido.
     */
    @Test
    public void checkValidInput() {
        String email = "gmotta99@gmail.com";
        String password = "myPsw";
        String confirmPassword = "myPsw";
        String username = "myUsr";

        AuthenticationViewModel myModel = new AuthenticationViewModel();
        assertTrue(myModel.checkInput(email, password, username, confirmPassword));
    }

    /**
     * Test per verificare l'input con email non valida.
     * Verifica se l'input con un'email vuota è considerato non valido.
     */
    @Test
    public void checkInvalidEmail() {
        String email = "";
        String password = "myPsw";
        String confirmPassword = "myPsw";
        String username = "myUsr";

        AuthenticationViewModel myModel = new AuthenticationViewModel();
        assertFalse(myModel.checkInput(email, password, username, confirmPassword));
    }

    /**
     * Test per verificare l'input con password non valida.
     * Verifica se l'input con una password vuota è considerato non valido.
     */
    @Test
    public void checkInvalidPsw() {
        String email = "gmotta99@gmail.com";
        String password = "";
        String confirmPassword = "";
        String username = "myUsr";

        AuthenticationViewModel myModel = new AuthenticationViewModel();
        assertFalse(myModel.checkInput(email, password, username, confirmPassword));
    }

    /**
     * Test per verificare l'input con conferma password non valida.
     * Verifica se l'input con conferma password vuota è considerato non valido.
     */
    @Test
    public void checkInvalidConfirmPsw() {
        String email = "gmotta99@gmail.com";
        String password = "password123";
        String confirmPassword = "";
        String username = "myUsr";

        AuthenticationViewModel myModel = new AuthenticationViewModel();
        assertFalse(myModel.checkInput(email, password, username, confirmPassword));
    }

    /**
     * Test per verificare l'input con username non valido.
     * Verifica se l'input con un username vuoto è considerato non valido.
     */
    @Test
    public void checkInvalidUsr() {
        String email = "gmotta99@gmail.com";
        String password = "myPsw";
        String confirmPassword = "myPsw";
        String username = "";

        AuthenticationViewModel myModel = new AuthenticationViewModel();
        assertFalse(myModel.checkInput(email, password, username, confirmPassword));
    }

}