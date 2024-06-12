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
 * Classe per effettuare gli acceptance test per la funzionalità delle note testuali nell'applicazione.
 */
public class NoteTestualiAcceptanceTest {
    /** Scenario per l'attività Pagina_di_note utilizzato nei test. */
    ActivityScenario<Pagina_di_note> scenario;

    /**
     * Configura il test di accettazione creando un nuovo utente nel database Firebase.
     *
     * @throws InterruptedException se il thread viene interrotto mentre aspetta.
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
     * Pulisce i dati di test eliminando l'utente e i rispettivi dati contenuti nel database Firebase.
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
     * Test che verifica il comportamento della view in assenza di note testuali nella pagina.
     */
    @Test
    public void emptyNoteTestualiTest(){
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        mAuth.signInWithEmailAndPassword("acceptancetest@gmail.com", "password123");
        scenario = ActivityScenario.launch(Pagina_di_note.class);

        try {
            Thread.sleep(3000);
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
     * Test che verifica il comportamento quando si tenta di creare una nota testuale con un titolo già esistente.
     */
    @Test
    public void titoloNonDisponibileTest(){
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        mAuth.signInWithEmailAndPassword("acceptancetest@gmail.com", "password123");
        scenario = ActivityScenario.launch(Pagina_di_note.class);

        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        Espresso.onView(withId(R.id.pagina_di_note)).check(matches(ViewMatchers.isDisplayed()));

        Espresso.onView(withId(R.id.addElemento)).perform(ViewActions.click());
        Espresso.onView(withId(R.id.element_nota_testuale)).perform(ViewActions.click());
        Espresso.onView(withId(R.id.titolo_nota_testuale)).perform(ViewActions.typeText("Acceptance test 1"), ViewActions.closeSoftKeyboard());
        Espresso.onView(withId(R.id.contenuto_nota_testuale)).perform(ViewActions.typeText("contenuto"), ViewActions.closeSoftKeyboard());
        Espresso.onView(withId(R.id.crea_nota_testuale)).perform(ViewActions.click());

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        Espresso.onView(withId(R.id.nota_testuale_contenuto)).check(matches(withText("contenuto")));

        scenario.onActivity(activity -> {
            GridView gridView = activity.findViewById(R.id.elements_grid_view);
            int itemCount = gridView.getAdapter().getCount();
            assertThat(itemCount, is(1));
        });

        Espresso.onView(withId(R.id.addElemento)).perform(ViewActions.click());
        Espresso.onView(withId(R.id.element_nota_testuale)).perform(ViewActions.click());
        Espresso.onView(withId(R.id.titolo_nota_testuale)).perform(ViewActions.typeText("Acceptance test 1"), ViewActions.closeSoftKeyboard());
        Espresso.onView(withId(R.id.contenuto_nota_testuale)).perform(ViewActions.typeText("altro contenuto"), ViewActions.closeSoftKeyboard());
        Espresso.onView(withId(R.id.crea_nota_testuale)).perform(ViewActions.click());

        Espresso.onView(withId(R.id.titolo_nota_testuale)).check(matches(withText("")));
        scenario.onActivity(activity -> {
            GridView gridView = activity.findViewById(R.id.elements_grid_view);
            int itemCount = gridView.getAdapter().getCount();
            assertThat(itemCount, is(1));
        });
    }

    /**
     * Test che verifica il comportamento quando si tenta di creare una nota testuale senza un titolo.
     */
    @Test
    public void emptyTitleTest(){
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        mAuth.signInWithEmailAndPassword("acceptancetest@gmail.com", "password123");
        scenario = ActivityScenario.launch(Pagina_di_note.class);

        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        Espresso.onView(withId(R.id.pagina_di_note)).check(matches(ViewMatchers.isDisplayed()));

        Espresso.onView(withId(R.id.addElemento)).perform(ViewActions.click());
        Espresso.onView(withId(R.id.element_nota_testuale)).perform(ViewActions.click());
        Espresso.onView(withId(R.id.titolo_nota_testuale)).perform(ViewActions.typeText(""), ViewActions.closeSoftKeyboard());
        Espresso.onView(withId(R.id.contenuto_nota_testuale)).perform(ViewActions.typeText("contenuto"), ViewActions.closeSoftKeyboard());
        Espresso.onView(withId(R.id.crea_nota_testuale)).perform(ViewActions.click());

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        Espresso.onView(withId(R.id.titolo_nota_testuale)).check(matches(withText("")));
        scenario.onActivity(activity -> {
            GridView gridView = activity.findViewById(R.id.elements_grid_view);
            int itemCount = gridView.getAdapter().getCount();
            assertThat(itemCount, is(0));
        });
    }

    /**
     * Test che verifica la corretta creazione di note testuali.
     */
    @Test
    public void creazioneNoteTestualiTest(){
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        mAuth.signInWithEmailAndPassword("acceptancetest@gmail.com", "password123");
        scenario = ActivityScenario.launch(Pagina_di_note.class);

        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        Espresso.onView(withId(R.id.pagina_di_note)).check(matches(ViewMatchers.isDisplayed()));

        Espresso.onView(withId(R.id.addElemento)).perform(ViewActions.click());
        Espresso.onView(withId(R.id.element_nota_testuale)).perform(ViewActions.click());
        Espresso.onView(withId(R.id.titolo_nota_testuale)).perform(ViewActions.typeText("Acceptance test 1"), ViewActions.closeSoftKeyboard());
        Espresso.onView(withId(R.id.contenuto_nota_testuale)).perform(ViewActions.typeText("contenuto"), ViewActions.closeSoftKeyboard());
        Espresso.onView(withId(R.id.crea_nota_testuale)).perform(ViewActions.click());

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        Espresso.onView(withId(R.id.nota_testuale_contenuto)).check(matches(withText("contenuto")));
        scenario.onActivity(activity -> {
            GridView gridView = activity.findViewById(R.id.elements_grid_view);
            int itemCount = gridView.getAdapter().getCount();
            assertThat(itemCount, is(1));
        });

        Espresso.onView(withId(R.id.addElemento)).perform(ViewActions.click());
        Espresso.onView(withId(R.id.element_nota_testuale)).perform(ViewActions.click());
        Espresso.onView(withId(R.id.titolo_nota_testuale)).perform(ViewActions.typeText("Acceptance test 2"), ViewActions.closeSoftKeyboard());
        Espresso.onView(withId(R.id.contenuto_nota_testuale)).perform(ViewActions.typeText("Altro contenuto"), ViewActions.closeSoftKeyboard());
        Espresso.onView(withId(R.id.crea_nota_testuale)).perform(ViewActions.click());

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
