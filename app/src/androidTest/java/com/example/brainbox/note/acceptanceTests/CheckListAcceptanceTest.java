package com.example.brainbox.note.acceptanceTests;

import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import android.widget.GridView;

import androidx.annotation.NonNull;
import androidx.test.core.app.ActivityScenario;
import androidx.test.espresso.Espresso;
import androidx.test.espresso.action.ViewActions;
import androidx.test.espresso.matcher.ViewMatchers;

import com.example.brainbox.R;
import com.example.brainbox.autenticazione.model.User;
import com.example.brainbox.note.view.Pagina_di_note;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * Questa classe contiene i casi di test di accettazione per la funzionalità di gestione delle checklist dell'applicazione.
 * I casi di test includono la verifica della creazione di checklist vuote e con contenuto, la gestione dei titoli duplicati e la validazione dei campi obbligatori.
 */
public class CheckListAcceptanceTest {


    /**
     * Scenario dell'attività per l'esecuzione dei test.
     */
    ActivityScenario<Pagina_di_note> scenario;

    /**
     * Metodo di inizializzazione eseguito prima dell'esecuzione dei casi di test di accettazione.
     * Crea un nuovo utente di test nel database Firebase.
     *
     * @throws InterruptedException se si verifica un'interruzione durante l'attesa delle operazioni asincrone.
     */
    @Before
    public void setUpAcceptanceTest() throws InterruptedException {
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        CountDownLatch latch = new CountDownLatch(1);

        mAuth.createUserWithEmailAndPassword("acceptancetest@gmail.com", "password123")
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser user = mAuth.getCurrentUser();
                        if (user != null) {
                            String userId = user.getUid();
                            DatabaseReference mDatabase = FirebaseDatabase.getInstance("https://brainbox-37414-default-rtdb.europe-west1.firebasedatabase.app/").getReference();
                            User newUser = new User("acceptancetest@gmail.com", "password123");
                            mDatabase.child("users").child(userId).setValue(newUser).addOnCompleteListener(dbTask -> {
                                latch.countDown();
                            });
                        }
                    } else {
                        latch.countDown();
                    }
                });

        if (!latch.await(10, TimeUnit.SECONDS)) {
            throw new RuntimeException("Setup timeout");
        }

    }

    /**
     * Metodo di pulizia eseguito dopo l'esecuzione dei casi di test di accettazione.
     * Elimina l'utente di test e i dati correlati dal database Firebase.
     */
    @After
    public void clearTest(){
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        mAuth.signInWithEmailAndPassword("acceptancetest@gmail.com", "password123");

        FirebaseUser user = mAuth.getCurrentUser();
        String userId = user.getUid();

        user.delete()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {}
                    }
                });

        DatabaseReference mDatabase = FirebaseDatabase.getInstance("https://brainbox-37414-default-rtdb.europe-west1.firebasedatabase.app/").getReference();
        mDatabase.child("users").child(userId).setValue(null);
    }

    /**
     * Test di accettazione per visualizzazione della pagina principale vuota.
     * Verifica che una nuova checklist vuota sia creata correttamente nell'interfaccia utente.
     */
    @Test
    public void emptyChecklist(){
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        mAuth.signInWithEmailAndPassword("acceptancetest@gmail.com", "password123");
        scenario = ActivityScenario.launch(Pagina_di_note.class);

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        Espresso.onView(withId(R.id.pagina_di_note)).check(matches(ViewMatchers.isDisplayed()));
        scenario.onActivity(activity -> {
            GridView gridView = activity.findViewById(R.id.elements_grid_view);
            int itemCount = gridView.getAdapter().getCount();
            assertThat(itemCount, is(0));
        });
    }

    /**
     * Test di accettazione per la gestione dei titoli duplicati durante la creazione di una checklist.
     * Verifica che il sistema rilevi correttamente i titoli duplicati e impedisca la creazione di checklist con titoli già esistenti.
     */
    @Test
    public void titoloNonDisponibileTest(){
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        mAuth.signInWithEmailAndPassword("acceptancetest@gmail.com", "password123");
        scenario = ActivityScenario.launch(Pagina_di_note.class);

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        Espresso.onView(withId(R.id.pagina_di_note)).check(matches(ViewMatchers.isDisplayed()));
        Espresso.onView(withId(R.id.addElemento)).perform(ViewActions.click());
        Espresso.onView(withId(R.id.element_checklist)).perform(ViewActions.click());
        Espresso.onView(withId(R.id.titolo_checklist)).perform(ViewActions.typeText("Acceptance test 1"), ViewActions.closeSoftKeyboard());
        Espresso.onView(withId(R.id.contenuto_checklist)).perform(ViewActions.typeText("contenuto\nchecklist"), ViewActions.closeSoftKeyboard());
        Espresso.onView(withId(R.id.crea_checklist)).perform(ViewActions.click());

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        scenario.onActivity(activity -> {
            GridView gridView = activity.findViewById(R.id.elements_grid_view);
            int itemCount = gridView.getAdapter().getCount();
            assertThat(itemCount, is(1));
        });

        Espresso.onView(withId(R.id.addElemento)).perform(ViewActions.click());
        Espresso.onView(withId(R.id.element_checklist)).perform(ViewActions.click());
        Espresso.onView(withId(R.id.titolo_checklist)).perform(ViewActions.typeText("Acceptance test 1"), ViewActions.closeSoftKeyboard());
        Espresso.onView(withId(R.id.contenuto_checklist)).perform(ViewActions.typeText("altro\ncontenuto\nchecklist"), ViewActions.closeSoftKeyboard());
        Espresso.onView(withId(R.id.crea_checklist)).perform(ViewActions.click());

        Espresso.onView(withId(R.id.titolo_checklist)).check(matches(withText("")));
        scenario.onActivity(activity -> {
            GridView gridView = activity.findViewById(R.id.elements_grid_view);
            int itemCount = gridView.getAdapter().getCount();
            assertThat(itemCount, is(1));
        });
    }

    /**
     * Test di accettazione per la creazione di una checklist senza titolo.
     * Verifica che il sistema impedisca la creazione di checklist senza specificare un titolo.
     */
    @Test
    public void emptyTitleTest(){
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        mAuth.signInWithEmailAndPassword("acceptancetest@gmail.com", "password123");
        scenario = ActivityScenario.launch(Pagina_di_note.class);

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        Espresso.onView(withId(R.id.pagina_di_note)).check(matches(ViewMatchers.isDisplayed()));
        Espresso.onView(withId(R.id.addElemento)).perform(ViewActions.click());
        Espresso.onView(withId(R.id.element_checklist)).perform(ViewActions.click());
        Espresso.onView(withId(R.id.contenuto_checklist)).perform(ViewActions.typeText("contenuto\nchecklist"), ViewActions.closeSoftKeyboard());
        Espresso.onView(withId(R.id.crea_checklist)).perform(ViewActions.click());

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        Espresso.onView(withId(R.id.titolo_checklist)).check(matches(withText("")));
        scenario.onActivity(activity -> {
            GridView gridView = activity.findViewById(R.id.elements_grid_view);
            int itemCount = gridView.getAdapter().getCount();
            assertThat(itemCount, is(0));
        });
    }

    /**
     * Test di accettazione per la creazione di una checklist senza contenuto.
     * Verifica che il sistema impedisca la creazione di checklist senza specificare un contenuto.
     */
    @Test
    public void emptyContenutoTest(){
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        mAuth.signInWithEmailAndPassword("acceptancetest@gmail.com", "password123");
        scenario = ActivityScenario.launch(Pagina_di_note.class);

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        Espresso.onView(withId(R.id.pagina_di_note)).check(matches(ViewMatchers.isDisplayed()));
        Espresso.onView(withId(R.id.addElemento)).perform(ViewActions.click());
        Espresso.onView(withId(R.id.element_checklist)).perform(ViewActions.click());
        Espresso.onView(withId(R.id.titolo_checklist)).perform(ViewActions.typeText("Acceptance test 1"), ViewActions.closeSoftKeyboard());
        Espresso.onView(withId(R.id.crea_checklist)).perform(ViewActions.click());

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        Espresso.onView(withId(R.id.contenuto_checklist)).check(matches(withText("")));
        scenario.onActivity(activity -> {
            GridView gridView = activity.findViewById(R.id.elements_grid_view);
            int itemCount = gridView.getAdapter().getCount();
            assertThat(itemCount, is(0));
        });
    }

    /**
     * Test di accettazione per la creazione di checklist con titolo e contenuto.
     * Verifica che le checklist con titolo e contenuto specificati vengano create correttamente nell'interfaccia utente.
     */
    @Test
    public void creazioneChecklistTest(){
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        mAuth.signInWithEmailAndPassword("acceptancetest@gmail.com", "password123");
        scenario = ActivityScenario.launch(Pagina_di_note.class);

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        Espresso.onView(withId(R.id.pagina_di_note)).check(matches(ViewMatchers.isDisplayed()));
        Espresso.onView(withId(R.id.addElemento)).perform(ViewActions.click());
        Espresso.onView(withId(R.id.element_checklist)).perform(ViewActions.click());
        Espresso.onView(withId(R.id.titolo_checklist)).perform(ViewActions.typeText("Acceptance test 1"), ViewActions.closeSoftKeyboard());
        Espresso.onView(withId(R.id.contenuto_checklist)).perform(ViewActions.typeText("contenuto\nchecklist"), ViewActions.closeSoftKeyboard());
        Espresso.onView(withId(R.id.crea_checklist)).perform(ViewActions.click());

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        scenario.onActivity(activity -> {
            GridView gridView = activity.findViewById(R.id.elements_grid_view);
            int itemCount = gridView.getAdapter().getCount();
            assertThat(itemCount, is(1));
        });

        Espresso.onView(withId(R.id.addElemento)).perform(ViewActions.click());
        Espresso.onView(withId(R.id.element_checklist)).perform(ViewActions.click());
        Espresso.onView(withId(R.id.titolo_checklist)).perform(ViewActions.typeText("Acceptance test 2"), ViewActions.closeSoftKeyboard());
        Espresso.onView(withId(R.id.contenuto_checklist)).perform(ViewActions.typeText("altro\ncontenuto\nchecklist"), ViewActions.closeSoftKeyboard());
        Espresso.onView(withId(R.id.crea_checklist)).perform(ViewActions.click());

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        scenario.onActivity(activity -> {
            GridView gridView = activity.findViewById(R.id.elements_grid_view);
            int itemCount = gridView.getAdapter().getCount();
            assertThat(itemCount, is(2));
        });
    }
}
