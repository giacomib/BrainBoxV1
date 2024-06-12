package com.example.brainbox.note.acceptanceTests;

import androidx.annotation.NonNull;
import androidx.test.core.app.ActivityScenario;
import androidx.test.espresso.Espresso;
import androidx.test.espresso.action.ViewActions;
import androidx.test.espresso.assertion.ViewAssertions;
import androidx.test.espresso.matcher.ViewMatchers;

import com.example.brainbox.MainActivity;
import com.example.brainbox.R;
import com.example.brainbox.autenticazione.model.User;
import com.example.brainbox.note.model.PaginaDiNote;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.hamcrest.CoreMatchers;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public class MenuImpostazioniAcceptanceTest {

    ActivityScenario<MainActivity> scenario;

    /**
     * Metodo di inizializzazione eseguito prima dell'esecuzione dei casi di test di accettazione.
     * Crea un nuovo utente e una nuova pagina di note di test nel database Firebase.
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
                            mDatabase.child("users").child(userId).child("Pagine di note").child("Pagina di note: pagina test").setValue(new PaginaDiNote("test")).addOnCompleteListener(dbTask -> {
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
     * Test di accettazione per il menu delle opzioni relative alle pagine di note della schermata principale.
     * Verifica che, al fare un long click (tenendo premuto) su una pagina di note, vengano visualizzate correttamente le opzioni del menu contestuale.
     */
    @Test
    public void menuElementiTest(){
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        mAuth.signInWithEmailAndPassword("acceptancetest@gmail.com", "password123");
        scenario = ActivityScenario.launch(MainActivity.class);

        Espresso.onData(CoreMatchers.anything())
                .inAdapterView(ViewMatchers.withId(R.id.grid_view))
                .atPosition(0)
                .perform(ViewActions.longClick());

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        Espresso.onView(ViewMatchers.withText("Modifica"))
                .check(ViewAssertions.matches(ViewMatchers.isDisplayed()));
        Espresso.onView(ViewMatchers.withText("Primo piano"))
                .check(ViewAssertions.matches(ViewMatchers.isDisplayed()));
        Espresso.onView(ViewMatchers.withText("Notifiche"))
                .check(ViewAssertions.matches(ViewMatchers.isDisplayed()));
        Espresso.onView(ViewMatchers.withText("Condividi"))
                .check(ViewAssertions.matches(ViewMatchers.isDisplayed()));
        Espresso.onView(ViewMatchers.withText("Elimina"))
                .check(ViewAssertions.matches(ViewMatchers.isDisplayed()));

    }


}
