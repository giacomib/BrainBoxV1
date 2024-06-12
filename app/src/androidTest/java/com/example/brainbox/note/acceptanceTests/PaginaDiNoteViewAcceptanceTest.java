package com.example.brainbox.note.acceptanceTests;

import androidx.test.core.app.ActivityScenario;
import androidx.test.espresso.Espresso;
import androidx.test.espresso.action.ViewActions;
import androidx.test.espresso.matcher.ViewMatchers;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import com.example.brainbox.*;
import com.google.firebase.auth.FirebaseAuth;
import org.junit.Test;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import android.widget.GridView;

/**
 * Classe di test per verificare il comportamento della visualizzazione delle pagine di note.
 */
public class PaginaDiNoteViewAcceptanceTest {

    /** Oggetto FirebaseAuth per l'autenticazione. */
    FirebaseAuth mAuth;
    /** Scenario dell'activity utilizzato per i test. */
    ActivityScenario<MainActivity> scenario;

    /**
     * Test per verificare la visualizzazione delle pagine di note quando la lista è vuota.
     * Verifica che la gridview delle pagine di note sia vuota.
     */
    @Test
    public void testPagineDiNoteViewEmpty(){

        String testMail = "testPagineDiNoteView@gmail.com";
        String testPassword = "password123";
        mAuth = FirebaseAuth.getInstance();
        mAuth.signInWithEmailAndPassword(testMail, testPassword);
        scenario = ActivityScenario.launch(MainActivity.class);

        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        Espresso.onView(ViewMatchers.withId(R.id.main)).check(matches(ViewMatchers.isDisplayed()));
        scenario.onActivity(activity -> {
            GridView gridView = activity.findViewById(R.id.grid_view);
            int itemCount = gridView.getAdapter().getCount();
            assertThat(itemCount, is(0));
        });
    }

    /**
     * Test per verificare la visualizzazione delle pagine di note dopo la creazione di una nuova pagina.
     * Verifica che la gridview delle pagine di note contenga correttamente una nuova pagina dopo la creazione.
     */
    @Test
    public void testPagineDiNoteView(){

        String testMail = "testPagineDiNoteView@gmail.com";
        String testPassword = "password123";
        mAuth = FirebaseAuth.getInstance();
        mAuth.signInWithEmailAndPassword(testMail, testPassword);
        scenario = ActivityScenario.launch(MainActivity.class);

        Espresso.onView(ViewMatchers.withId(R.id.addNewPaginaDiNote)).perform(ViewActions.click());
        Espresso.onView(ViewMatchers.withId(R.id.titoloPaginaNote)).perform(ViewActions.typeText("Test"), ViewActions.closeSoftKeyboard());
        Espresso.onView(ViewMatchers.withId(R.id.submit_title)).perform(ViewActions.click());
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Espresso.onView(ViewMatchers.withId(R.id.main)).check(matches(ViewMatchers.isDisplayed()));
        Espresso.onView(ViewMatchers.withId(R.id.main)).check(matches(ViewMatchers.isDisplayed()));
        scenario.onActivity(activity -> {
            GridView gridView = activity.findViewById(R.id.grid_view);
            int itemCount = gridView.getAdapter().getCount();
            assertThat(itemCount, is(1));
        });
    }

}
