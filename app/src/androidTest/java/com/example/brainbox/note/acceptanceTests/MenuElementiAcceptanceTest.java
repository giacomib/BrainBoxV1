package com.example.brainbox.note.acceptanceTests;

import androidx.test.espresso.Espresso;
import androidx.test.espresso.action.ViewActions;
import androidx.test.espresso.assertion.ViewAssertions;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.rules.ActivityScenarioRule;

import com.example.brainbox.R;
import com.example.brainbox.autenticazione.view.Login;

import org.hamcrest.CoreMatchers;
import org.junit.Rule;
import org.junit.Test;

/**
 * Classe di test per verificare la corretta visualizzazione del menu elementi delle pagine di note.
 */
public class MenuElementiAcceptanceTest {

    /**
     * Regola per avviare l'Activity di Login prima di eseguire i test.
     */
    @Rule
    public ActivityScenarioRule<Login> activityScenarioRule =
            new ActivityScenarioRule<>(Login.class);

    /**
     * Test per verificare la visualizzazione del menu degli elementi.
     * Esegue i seguenti passi:
     * 1. Effettua il login con un account di test.
     * 2. Clicca sulla prima pagina di note.
     * 3. Clicca sul bottone per aggiungere un nuovo elemento.
     * 4. Verifica che il dialog contenente il menu degli elementi sia visualizzato.
     */
    @Test
    public void visualizzazioneElementiMenuElementiTest(){

        // Login sull'account di test
        Espresso.onView(ViewMatchers.withId(R.id.editTextTextEmailAddress2))
                .perform(ViewActions.replaceText("test@gmail.com"), ViewActions.closeSoftKeyboard());
        Espresso.onView(ViewMatchers.withId(R.id.editTextTextPassword2))
                .perform(ViewActions.replaceText("password123"), ViewActions.closeSoftKeyboard());
        Espresso.onView(ViewMatchers.withId(R.id.loginButton)).perform(ViewActions.click());

        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // click sulla prima pagina di note
        Espresso.onData(CoreMatchers.anything())
                .inAdapterView(ViewMatchers.withId(R.id.grid_view))
                .atPosition(0)
                .perform(ViewActions.click());

        // click sul bottone aggiungi elemento
        Espresso.onView(ViewMatchers.withId(R.id.addElemento)).perform(ViewActions.click());

        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        // asserzione sulla visualizzazione del dialog contenente il menu
        Espresso.onView(ViewMatchers.withId(R.id.newElementDialog))
                .check(ViewAssertions.matches(ViewMatchers.isDisplayed()));


    }
}
