package com.example.brainbox.gestioneProfilo.unitTests;

import static org.junit.jupiter.api.Assertions.*;

import com.example.brainbox.gestioneProfilo.viewModel.OpzioniUtenteViewModel;

import org.junit.Test;

public class OpzioniUtenteViewModelTest {
    @Test
    public void ValidEmail() {
        String email = "gmotta99@gmail.com";

        OpzioniUtenteViewModel myModel = new OpzioniUtenteViewModel();
        assertTrue(myModel.emailValidation(email));
    }

    @Test
    public void InvalidEmail01() {
        String email = "";

        OpzioniUtenteViewModel myModel = new OpzioniUtenteViewModel();
        assertFalse(myModel.emailValidation(email));
    }

    @Test
    public void InvalidEmail02() {
        String email = "gmotta99 @gmail.com";

        OpzioniUtenteViewModel myModel = new OpzioniUtenteViewModel();
        assertFalse(myModel.emailValidation(email));
    }

    @Test
    public void InvalidEmail03() {
        String email = "gmotta99gmail.com";

        OpzioniUtenteViewModel myModel = new OpzioniUtenteViewModel();
        assertFalse(myModel.emailValidation(email));
    }

    @Test
    public void InvalidEmail04() {
        String email = "gmotta99£@gmail.com";

        OpzioniUtenteViewModel myModel = new OpzioniUtenteViewModel();
        assertFalse(myModel.emailValidation(email));
    }

    @Test
    public void InvalidEmail05() {
        String email = "gmotta99£@ gmail.com";

        OpzioniUtenteViewModel myModel = new OpzioniUtenteViewModel();
        assertFalse(myModel.emailValidation(email));
    }

    @Test
    public void ValidUsername() {
        String username = "pippopluto";

        OpzioniUtenteViewModel myModel = new OpzioniUtenteViewModel();
        assertTrue(myModel.usernameValidation(username));
    }

    @Test
    public void InvalidUsername01() {
        String username = "";

        OpzioniUtenteViewModel myModel = new OpzioniUtenteViewModel();
        assertFalse(myModel.usernameValidation(username));
    }

    @Test
    public void InvalidUsername02() {
        String username = "pippo pluto";

        OpzioniUtenteViewModel myModel = new OpzioniUtenteViewModel();
        assertFalse(myModel.usernameValidation(username));
    }
}