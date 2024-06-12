package com.example.brainbox.note.acceptanceTests;

import androidx.test.core.app.ActivityScenario;
import androidx.test.espresso.Espresso;
import androidx.test.espresso.action.GeneralClickAction;
import androidx.test.espresso.action.Tap;
import androidx.test.espresso.action.ViewActions;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.espresso.action.GeneralLocation;
import androidx.test.espresso.action.Press;

import com.example.brainbox.*;
import com.google.firebase.auth.FirebaseAuth;

import org.junit.Test;

import static androidx.test.espresso.action.ViewActions.actionWithAssertions;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.withText;


/**
 * Classe di test per verificare il comportamento della MainActivity.
 */
public class NewPaginaDiNoteAcceptanceTest {

    /** Oggetto FirebaseAuth per l'autenticazione. */
    FirebaseAuth mAuth;
    /** Scenario per l'attività MainActivity utilizzato nei test. */
    ActivityScenario<MainActivity> scenario;

    /**
     * Test per verificare il click sul pulsante per creare una nuova pagina di note.
     * Verifica se il testo del titolo nella pagina di creazione è corretto.
     */
    @Test
    public void testNuovaPaginaDiNoteButton() {

        // accesso con un account di test
        String testMail = "testNewPagineDiNote@gmail.com";
        String testPassword = "password123";
        mAuth = FirebaseAuth.getInstance();
        mAuth.signInWithEmailAndPassword(testMail, testPassword);
        scenario = ActivityScenario.launch(MainActivity.class);

        Espresso.onView(ViewMatchers.withId(R.id.addNewPaginaDiNote)).perform(ViewActions.click());
        Espresso.onView(ViewMatchers.withId(R.id.inserisci_titolo_paginaNote)).check(matches(withText("Nuova pagina di note")));
    }

    /**
     * Test per verificare la corretta creazione di una pagina di note.
     * Verifica se dopo l'inserimento del titolo e la conferma, MainActivity viene visualizzata correttamente.
     */
    @Test
    public void testSubmitTitle() {

        // accesso con un account di test
        String testMail = "testNewPagineDiNote@gmail.com";
        String testPassword = "password123";
        mAuth = FirebaseAuth.getInstance();
        mAuth.signInWithEmailAndPassword(testMail, testPassword);
        scenario = ActivityScenario.launch(MainActivity.class);


        // creazione di una nuova pagina di note
        Espresso.onView(ViewMatchers.withId(R.id.addNewPaginaDiNote)).perform(ViewActions.click());
        Espresso.onView(ViewMatchers.withId(R.id.titoloPaginaNote)).perform(ViewActions.typeText("Test"), ViewActions.closeSoftKeyboard());
        Espresso.onView(ViewMatchers.withId(R.id.submit_title)).perform(ViewActions.click());
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Espresso.onView(ViewMatchers.withId(R.id.main)).check(matches(ViewMatchers.isDisplayed()));
    }

    /**
     * Test per verificare la corretta chiusura del dialog per la creazione di una nuova pagina di note.
     * Verifica se il dialog viene chiuso quando viene cliccato al di fuori di esso.
     */
    @Test
    public void testDismissDialog() {

        String testMail = "testNewPagineDiNote@gmail.com";
        String testPassword = "password123";
        mAuth = FirebaseAuth.getInstance();
        mAuth.signInWithEmailAndPassword(testMail, testPassword);
        scenario = ActivityScenario.launch(MainActivity.class);

        // Dialog per la creazione di una nuova pagina di note
        Espresso.onView(ViewMatchers.withId(R.id.addNewPaginaDiNote)).perform(ViewActions.click());

        // Click al di fuori del dialog per chiuderlo
        Espresso.onView(ViewMatchers.isRoot()).perform(actionWithAssertions(
                new GeneralClickAction(Tap.SINGLE, GeneralLocation.TOP_CENTER, Press.FINGER, 0,0)));

        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        // Verifica che la MainActivity sia visualizzata correttamente
        Espresso.onView(ViewMatchers.isRoot()).check(matches(ViewMatchers.isDisplayed()));
    }

}
