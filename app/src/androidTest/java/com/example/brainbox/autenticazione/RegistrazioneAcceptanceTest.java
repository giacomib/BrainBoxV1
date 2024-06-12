package com.example.brainbox.autenticazione;

import static androidx.test.espresso.assertion.ViewAssertions.doesNotExist;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;

import androidx.annotation.NonNull;
import androidx.test.espresso.Espresso;
import androidx.test.espresso.action.ViewActions;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.rules.ActivityScenarioRule;

import com.example.brainbox.R;
import com.example.brainbox.autenticazione.view.Registrazione;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.junit.Rule;
import org.junit.Test;

/**
 * Classe di test per verificare le funzionalità di registrazione dell'utente
 */
public class RegistrazioneAcceptanceTest {

    /**
     * Regola per l'esecuzione di test a partire dalla Activity Registrazione.
     */
    @Rule
    public ActivityScenarioRule<Registrazione> activityScenarioRule =
            new ActivityScenarioRule<>(Registrazione.class);

    /**
     * Test per verificare il tentativo di registrazione con un username non disponibile (già in uso).
     * Verifica che il layout di login non sia visualizzato dopo il tentativo di registrazione con un username non disponibile.
     * Verifica che il layout di registrazione sia ancora visualizzato dopo il tentativo di accesso con un username non disponibile.
     */
    @Test
    public void invalidUserTest(){
        Espresso.onView(ViewMatchers.withId(R.id.editTextUsername))
                .perform(ViewActions.replaceText("Test"), ViewActions.closeSoftKeyboard());
        Espresso.onView(ViewMatchers.withId(R.id.editTextTextEmailAddress))
                .perform(ViewActions.replaceText("test@gmail.com"), ViewActions.closeSoftKeyboard());
        Espresso.onView(ViewMatchers.withId(R.id.editTextTextPassword))
                .perform(ViewActions.replaceText("password123"), ViewActions.closeSoftKeyboard());
        Espresso.onView(ViewMatchers.withId(R.id.editTextConfirmPassword))
                .perform(ViewActions.replaceText("password123"), ViewActions.closeSoftKeyboard());
        Espresso.onView(ViewMatchers.withId(R.id.button)).perform(ViewActions.click());




        // Asserzione che il layout di login non sia mostrato
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Espresso.onView(ViewMatchers.withId(R.id.login)).check(doesNotExist());
        Espresso.onView(ViewMatchers.withId(R.id.register)).check(matches(isDisplayed()));
    }

    /**
     * Test per verificare il tentativo di registrazione con un'email non valida.
     * Verifica che il layout di login non sia visualizzato dopo il tentativo di registrazione con un'email non valida.
     * Verifica che il layout di registrazione sia ancora visualizzato dopo il tentativo di accesso con un'email non valida.
     */
    @Test
    public void invalidEmailTest(){
        Espresso.onView(ViewMatchers.withId(R.id.editTextUsername))
                .perform(ViewActions.replaceText("validUsername"), ViewActions.closeSoftKeyboard());
        Espresso.onView(ViewMatchers.withId(R.id.editTextTextEmailAddress))
                .perform(ViewActions.replaceText("invalidEmail"), ViewActions.closeSoftKeyboard());
        Espresso.onView(ViewMatchers.withId(R.id.editTextTextPassword))
                .perform(ViewActions.replaceText("password123"), ViewActions.closeSoftKeyboard());
        Espresso.onView(ViewMatchers.withId(R.id.editTextConfirmPassword))
                .perform(ViewActions.replaceText("password123"), ViewActions.closeSoftKeyboard());
        Espresso.onView(ViewMatchers.withId(R.id.button)).perform(ViewActions.click());


        // Asserzione che il layout di login non sia mostrato
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Espresso.onView(ViewMatchers.withId(R.id.login)).check(doesNotExist());
        Espresso.onView(ViewMatchers.withId(R.id.register)).check(matches(isDisplayed()));
    }

    /**
     * Test per verificare il tentativo di registrazione con il campo del nome utente vuoto.
     * Verifica che il layout di login non sia visualizzato dopo il tentativo di registrazione con il campo del nome utente vuoto.
     * Verifica che il layout di registrazione sia ancora visualizzato dopo il tentativo di accesso con il campo del nome utente vuoto.
     */
    @Test
    public void emptyUsernameFieldTest(){
        Espresso.onView(ViewMatchers.withId(R.id.editTextUsername))
                .perform(ViewActions.replaceText(""), ViewActions.closeSoftKeyboard());
        Espresso.onView(ViewMatchers.withId(R.id.editTextTextEmailAddress))
                .perform(ViewActions.replaceText("test@gmail.com"), ViewActions.closeSoftKeyboard());
        Espresso.onView(ViewMatchers.withId(R.id.editTextTextPassword))
                .perform(ViewActions.replaceText("password123"), ViewActions.closeSoftKeyboard());
        Espresso.onView(ViewMatchers.withId(R.id.editTextConfirmPassword))
                .perform(ViewActions.replaceText("password123"), ViewActions.closeSoftKeyboard());
        Espresso.onView(ViewMatchers.withId(R.id.button)).perform(ViewActions.click());


        // Asserzione che il layout di login non sia mostrato
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Espresso.onView(ViewMatchers.withId(R.id.login)).check(doesNotExist());
        Espresso.onView(ViewMatchers.withId(R.id.register)).check(matches(isDisplayed()));
    }

    /**
     * Test per verificare il tentativo di registrazione con il campo dell'email vuoto.
     * Verifica che il layout di login non sia visualizzato dopo il tentativo di registrazione con il campo dell'email vuoto.
     * Verifica che il layout di registrazione sia ancora visualizzato dopo il tentativo di accesso con il campo dell'email vuoto.
     */
    @Test
    public void emptyEmailFieldTest(){
        Espresso.onView(ViewMatchers.withId(R.id.editTextUsername))
                .perform(ViewActions.replaceText("ValidUsername"), ViewActions.closeSoftKeyboard());
        Espresso.onView(ViewMatchers.withId(R.id.editTextTextEmailAddress))
                .perform(ViewActions.replaceText(""), ViewActions.closeSoftKeyboard());
        Espresso.onView(ViewMatchers.withId(R.id.editTextTextPassword))
                .perform(ViewActions.replaceText("password123"), ViewActions.closeSoftKeyboard());
        Espresso.onView(ViewMatchers.withId(R.id.editTextConfirmPassword))
                .perform(ViewActions.replaceText("password123"), ViewActions.closeSoftKeyboard());
        Espresso.onView(ViewMatchers.withId(R.id.button)).perform(ViewActions.click());


        // Asserzione che il layout di login non sia mostrato
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Espresso.onView(ViewMatchers.withId(R.id.login)).check(doesNotExist());
        Espresso.onView(ViewMatchers.withId(R.id.register)).check(matches(isDisplayed()));
    }

    /**
     * Test per verificare il tentativo di registrazione con il campo della password vuoto.
     * Verifica che il layout di login non sia visualizzato dopo il tentativo di registrazione con il campo della password vuoto.
     * Verifica che il layout di registrazione sia ancora visualizzato dopo il tentativo di accesso con il campo della password vuoto.
     */
    @Test
    public void emptyPasswordFieldTest(){
        Espresso.onView(ViewMatchers.withId(R.id.editTextUsername))
                .perform(ViewActions.replaceText("ValidUsername"), ViewActions.closeSoftKeyboard());
        Espresso.onView(ViewMatchers.withId(R.id.editTextTextEmailAddress))
                .perform(ViewActions.replaceText("test@gmail.com"), ViewActions.closeSoftKeyboard());
        Espresso.onView(ViewMatchers.withId(R.id.editTextTextPassword))
                .perform(ViewActions.replaceText(""), ViewActions.closeSoftKeyboard());
        Espresso.onView(ViewMatchers.withId(R.id.editTextConfirmPassword))
                .perform(ViewActions.replaceText(""), ViewActions.closeSoftKeyboard());
        Espresso.onView(ViewMatchers.withId(R.id.button)).perform(ViewActions.click());


        // Asserzione che il layout di login non sia mostrato
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Espresso.onView(ViewMatchers.withId(R.id.login)).check(doesNotExist());
        Espresso.onView(ViewMatchers.withId(R.id.register)).check(matches(isDisplayed()));
    }

    /**
     * Test per verificare il tentativo di registrazione con il campo conferma password vuoto.
     * Verifica che il layout di login non sia visualizzato dopo il tentativo di registrazione con il campo conferma password vuoto.
     * Verifica che il layout di registrazione sia ancora visualizzato dopo il tentativo di accesso con il campo conferma password vuoto.
     */
    @Test
    public void emptyConfirmPasswordFieldTest(){
        Espresso.onView(ViewMatchers.withId(R.id.editTextUsername))
                .perform(ViewActions.replaceText("ValidUsername"), ViewActions.closeSoftKeyboard());
        Espresso.onView(ViewMatchers.withId(R.id.editTextTextEmailAddress))
                .perform(ViewActions.replaceText("test@gmail.com"), ViewActions.closeSoftKeyboard());
        Espresso.onView(ViewMatchers.withId(R.id.editTextTextPassword))
                .perform(ViewActions.replaceText("password123"), ViewActions.closeSoftKeyboard());
        Espresso.onView(ViewMatchers.withId(R.id.editTextConfirmPassword))
                .perform(ViewActions.replaceText(""), ViewActions.closeSoftKeyboard());
        Espresso.onView(ViewMatchers.withId(R.id.button)).perform(ViewActions.click());


        // Asserzione che il layout di login non sia mostrato
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Espresso.onView(ViewMatchers.withId(R.id.login)).check(doesNotExist());
        Espresso.onView(ViewMatchers.withId(R.id.register)).check(matches(isDisplayed()));
    }

    /**
     * Test per verificare il tentativo di registrazione con il campo conferma password errato (non corrispondente alla password inserita).
     * Verifica che il layout di login non sia visualizzato dopo il tentativo di registrazione con il campo conferma password errato.
     * Verifica che il layout di registrazione sia ancora visualizzato dopo il tentativo di accesso con il campo conferma password errato.
     */
    @Test
    public void wrongConfirmPasswordTest(){
        Espresso.onView(ViewMatchers.withId(R.id.editTextUsername))
                .perform(ViewActions.replaceText("ValidUsername"), ViewActions.closeSoftKeyboard());
        Espresso.onView(ViewMatchers.withId(R.id.editTextTextEmailAddress))
                .perform(ViewActions.replaceText("test@gmail.com"), ViewActions.closeSoftKeyboard());
        Espresso.onView(ViewMatchers.withId(R.id.editTextTextPassword))
                .perform(ViewActions.replaceText("password123"), ViewActions.closeSoftKeyboard());
        Espresso.onView(ViewMatchers.withId(R.id.editTextConfirmPassword))
                .perform(ViewActions.replaceText("password124"), ViewActions.closeSoftKeyboard());
        Espresso.onView(ViewMatchers.withId(R.id.button)).perform(ViewActions.click());


        // Asserzione che il layout di login non sia mostrato
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Espresso.onView(ViewMatchers.withId(R.id.login)).check(doesNotExist());
        Espresso.onView(ViewMatchers.withId(R.id.register)).check(matches(isDisplayed()));
    }

    /**
     * Test per verificare la registrazione utente riuscita.
     * Verifica che il layout di login sia visualizzato dopo una registrazione utente riuscita.
     * Successivamente viene eliminato l'account di test creato.
     */
    @Test
    public void successfulRegistrationTest(){
        Espresso.onView(ViewMatchers.withId(R.id.editTextUsername))
                .perform(ViewActions.replaceText("ValidUsername"), ViewActions.closeSoftKeyboard());
        Espresso.onView(ViewMatchers.withId(R.id.editTextTextEmailAddress))
                .perform(ViewActions.replaceText("validEmail@gmail.com"), ViewActions.closeSoftKeyboard());
        Espresso.onView(ViewMatchers.withId(R.id.editTextTextPassword))
                .perform(ViewActions.replaceText("password123"), ViewActions.closeSoftKeyboard());
        Espresso.onView(ViewMatchers.withId(R.id.editTextConfirmPassword))
                .perform(ViewActions.replaceText("password123"), ViewActions.closeSoftKeyboard());
        Espresso.onView(ViewMatchers.withId(R.id.button)).perform(ViewActions.click());


        // Asserzione che il layout di login non sia mostrato
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Espresso.onView(ViewMatchers.withId(R.id.login)).check(matches(isDisplayed()));

        // eliminazione User da firebaseAuth
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
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
}