package com.example.brainbox.note.acceptanceTests;

import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.espresso.Espresso;
import androidx.test.espresso.action.ViewActions;
import androidx.test.espresso.matcher.ViewMatchers;

import com.example.brainbox.R;
import com.example.brainbox.autenticazione.view.Registrazione;
import org.junit.Rule;
import org.junit.Test;

import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;

/**
 * Classe di test di integrazione per la creazione di una nuova pagina di note.
 * Questo test integra le fasi di registrazione, accesso e creazione di una nuova pagina di note
 * per un nuovo utente.
 */
public class NewPaginaDiNoteSystemTest {

    /**
     * Regola per l'esecuzione di test a partire dalla Activity Registrazione.
     */
    @Rule
    public ActivityScenarioRule<Registrazione> activityScenarioRule =
            new ActivityScenarioRule<>(Registrazione.class);

    /**
     * Test di integrazione per la creazione di una nuova pagina di note da parte di un nuovo utente.
     * Questo test comprende le seguenti fasi:
     * 1. Effettua la registrazione di un nuovo utente.
     * 2. Effettua il login con le credenziali del nuovo utente.
     * 3. Crea una nuova pagina di note.
     * Il test verifica che la procedura di registrazione, accesso e creazione della pagina di note
     * avvenga correttamente e che l'utente sia reindirizzato alla schermata principale dopo aver creato
     * con successo la nuova pagina di note.
     */
    @Test
    public void integTestNewUserPaginaDiNote(){
        // Parte 1: Registrazione

        Espresso.onView(ViewMatchers.withId(R.id.editTextUsername))
                .perform(ViewActions.replaceText("integTest0"), ViewActions.closeSoftKeyboard());
        Espresso.onView(ViewMatchers.withId(R.id.editTextTextEmailAddress))
                .perform(ViewActions.replaceText("integTest0@gmail.com"), ViewActions.closeSoftKeyboard());
        Espresso.onView(ViewMatchers.withId(R.id.editTextTextPassword))
                .perform(ViewActions.replaceText("password123"), ViewActions.closeSoftKeyboard());
        Espresso.onView(ViewMatchers.withId(R.id.editTextConfirmPassword))
                .perform(ViewActions.replaceText("password123"), ViewActions.closeSoftKeyboard());
        Espresso.onView(ViewMatchers.withId(R.id.button)).perform(ViewActions.click());

        // Asserzione che il layout di login sia mostrato
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Espresso.onView(ViewMatchers.withId(R.id.login)).check(matches(isDisplayed()));

        // Parte 2: Login

        Espresso.onView(ViewMatchers.withId(R.id.editTextTextEmailAddress2))
                .perform(ViewActions.replaceText("integTest0@gmail.com"), ViewActions.closeSoftKeyboard());
        Espresso.onView(ViewMatchers.withId(R.id.editTextTextPassword2))
                .perform(ViewActions.replaceText("password123"), ViewActions.closeSoftKeyboard());
        Espresso.onView(ViewMatchers.withId(R.id.loginButton)).perform(ViewActions.click());

        // Asserisci che il layout principale sia mostrato
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Espresso.onView(ViewMatchers.withId(R.id.main)).check(matches(isDisplayed()));

        // Parte 3: Click su "addNewPaginaDiNote"
        Espresso.onView(ViewMatchers.withId(R.id.addNewPaginaDiNote)).perform(ViewActions.click());

        Espresso.onView(ViewMatchers.withId(R.id.titoloPaginaNote)).perform(ViewActions.typeText("integTest"), ViewActions.closeSoftKeyboard());
        Espresso.onView(ViewMatchers.withId(R.id.submit_title)).perform(ViewActions.click());

        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // assert di essere tornati a visualizzare la mainActivity
        Espresso.onView(ViewMatchers.withId(R.id.main)).check(matches(ViewMatchers.isDisplayed()));

    }

}
