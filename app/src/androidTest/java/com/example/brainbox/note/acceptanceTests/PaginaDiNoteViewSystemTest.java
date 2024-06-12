package com.example.brainbox.note.acceptanceTests;

import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import android.widget.GridView;
import androidx.test.core.app.ActivityScenario;
import androidx.test.espresso.Espresso;
import androidx.test.espresso.action.ViewActions;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.rules.ActivityScenarioRule;

import com.example.brainbox.MainActivity;
import com.example.brainbox.R;
import com.example.brainbox.autenticazione.view.Registrazione;

import org.junit.Rule;
import org.junit.Test;

/**
 * Classe di test per verificare l'integrazione della visualizzazione delle pagine di note.
 */
public class PaginaDiNoteViewSystemTest {

    /**
     * Regola per l'esecuzione di test a partire dalla Activity Registrazione.
     */
    @Rule
    public ActivityScenarioRule<Registrazione> activityScenarioRule =
            new ActivityScenarioRule<>(Registrazione.class);

    /**
     * Test per verificare l'integrazione della visualizzazione delle pagine di note.
     * Il test include la registrazione di un nuovo utente, il login e la creazione di pagine di note.
     * Verifica infine che la gridview delle pagine di note contenga il numero corretto di elementi.
     */
    @Test
    public void integTestNewUserPaginaDiNote(){

        // Parte 1: Registrazione

        Espresso.onView(ViewMatchers.withId(R.id.editTextUsername))
                .perform(ViewActions.replaceText("integTest1"), ViewActions.closeSoftKeyboard());
        Espresso.onView(ViewMatchers.withId(R.id.editTextTextEmailAddress))
                .perform(ViewActions.replaceText("integTest1@gmail.com"), ViewActions.closeSoftKeyboard());
        Espresso.onView(ViewMatchers.withId(R.id.editTextTextPassword))
                .perform(ViewActions.replaceText("password123"), ViewActions.closeSoftKeyboard());
        Espresso.onView(ViewMatchers.withId(R.id.editTextConfirmPassword))
                .perform(ViewActions.replaceText("password123"), ViewActions.closeSoftKeyboard());
        Espresso.onView(ViewMatchers.withId(R.id.button)).perform(ViewActions.click());

        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Espresso.onView(ViewMatchers.withId(R.id.login)).check(matches(isDisplayed()));

        // Parte 2: Login

        Espresso.onView(ViewMatchers.withId(R.id.editTextTextEmailAddress2))
                .perform(ViewActions.replaceText("integTest1@gmail.com"), ViewActions.closeSoftKeyboard());
        Espresso.onView(ViewMatchers.withId(R.id.editTextTextPassword2))
                .perform(ViewActions.replaceText("password123"), ViewActions.closeSoftKeyboard());
        Espresso.onView(ViewMatchers.withId(R.id.loginButton)).perform(ViewActions.click());

        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Espresso.onView(ViewMatchers.withId(R.id.main)).check(matches(isDisplayed()));

        // Parte 3: creazione pagine di note
        Espresso.onView(ViewMatchers.withId(R.id.addNewPaginaDiNote)).perform(ViewActions.click());
        Espresso.onView(ViewMatchers.withId(R.id.titoloPaginaNote)).perform(ViewActions.typeText("integTest"), ViewActions.closeSoftKeyboard());
        Espresso.onView(ViewMatchers.withId(R.id.submit_title)).perform(ViewActions.click());

        Espresso.onView(ViewMatchers.withId(R.id.addNewPaginaDiNote)).perform(ViewActions.click());
        Espresso.onView(ViewMatchers.withId(R.id.titoloPaginaNote)).perform(ViewActions.typeText("integTest1"), ViewActions.closeSoftKeyboard());
        Espresso.onView(ViewMatchers.withId(R.id.submit_title)).perform(ViewActions.click());

        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // assert che la gridview delle pagine di note contenga 2 item
        ActivityScenario<MainActivity> scenario = ActivityScenario.launch(MainActivity.class);
        scenario.onActivity(activity -> {
            GridView gridView = activity.findViewById(R.id.grid_view);
            int itemCount = gridView.getAdapter().getCount();
            assertThat(itemCount, is(2));
        });
    }
}
