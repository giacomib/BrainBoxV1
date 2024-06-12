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
import com.example.brainbox.autenticazione.view.Login;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;

/**
 * Classe di test per verificare le funzionalità di Login dell'utente
 */
public class LoginAcceptanceTests {

    /**
     * Regola per l'esecuzione di test a partire dalla Activity Login.
     */
    @Rule
    public ActivityScenarioRule<Login> activityScenarioRule =
            new ActivityScenarioRule<>(Login.class);

    /**
     * Configura l'ambiente per il test di accettazione del login.
     * Crea un nuovo account utente con email e password specificate per il test.
     */
    @BeforeClass
    public static void setUpLoginAcceptanceTest(){
        FirebaseAuth mAuth = FirebaseAuth.getInstance();

        mAuth.createUserWithEmailAndPassword("loginAcceptancetest@gmail.com", "password123")
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {}
                        else {}
                    }
                });
    }

    /**
     * Test per verificare il comportamento quando viene inserita un'email errata durante il login.
     * Verifica che la pagina principale dell'applicazione non sia visualizzato dopo il tentativo di accesso
     * con un'email errata, rimanendo sulla pagina di Login.
     */
    @Test
    public void wrongEmailTest(){

        Espresso.onView(ViewMatchers.withId(R.id.editTextTextEmailAddress2))
                .perform(ViewActions.replaceText("wrongEmail@gmail.com"), ViewActions.closeSoftKeyboard());
        Espresso.onView(ViewMatchers.withId(R.id.editTextTextPassword2))
                .perform(ViewActions.replaceText("password123"), ViewActions.closeSoftKeyboard());
        Espresso.onView(ViewMatchers.withId(R.id.loginButton)).perform(ViewActions.click());

        // Asserisci che il layout principale sia mostrato
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Espresso.onView(ViewMatchers.withId(R.id.main)).check(doesNotExist());
        Espresso.onView(ViewMatchers.withId(R.id.login)).check(matches(isDisplayed()));

    }

    /**
     * Test per verificare il comportamento quando viene inserita una password errata durante il login.
     * Verifica che la pagina principale dell'applicazione non sia visualizzata dopo il tentativo di accesso con una password errata.
     * Verifica che il layout di login sia ancora visualizzato dopo il tentativo di accesso con una password errata.
     */
    @Test
    public void wrongPasswordTest(){

        Espresso.onView(ViewMatchers.withId(R.id.editTextTextEmailAddress2))
                .perform(ViewActions.replaceText("loginAcceptancetest@gmail.com"), ViewActions.closeSoftKeyboard());
        Espresso.onView(ViewMatchers.withId(R.id.editTextTextPassword2))
                .perform(ViewActions.replaceText("wrongPassword"), ViewActions.closeSoftKeyboard());
        Espresso.onView(ViewMatchers.withId(R.id.loginButton)).perform(ViewActions.click());

        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Espresso.onView(ViewMatchers.withId(R.id.main)).check(doesNotExist());
        Espresso.onView(ViewMatchers.withId(R.id.login)).check(matches(isDisplayed()));
    }

    /**
     * Test per verificare il comportamento quando il campo dell'email è vuoto durante la procedura di login.
     * Verifica che la pagina principale dell'applicazione non sia visualizzato dopo il tentativo di accesso con un campo email vuoto.
     * Verifica che il layout di login sia ancora visualizzato dopo il tentativo di accesso con un campo email vuoto.
     */
    @Test
    public void emptyEmailFieldTest(){

        Espresso.onView(ViewMatchers.withId(R.id.editTextTextEmailAddress2))
                .perform(ViewActions.replaceText(""), ViewActions.closeSoftKeyboard());
        Espresso.onView(ViewMatchers.withId(R.id.editTextTextPassword2))
                .perform(ViewActions.replaceText("password123"), ViewActions.closeSoftKeyboard());
        Espresso.onView(ViewMatchers.withId(R.id.loginButton)).perform(ViewActions.click());

        // Asserisci che il layout principale sia mostrato
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Espresso.onView(ViewMatchers.withId(R.id.main)).check(doesNotExist());
        Espresso.onView(ViewMatchers.withId(R.id.login)).check(matches(isDisplayed()));
    }

    /**
     * Test per verificare il comportamento quando il campo della password è vuoto durante il login.
     * Verifica che la pagina principale dell'applicazione non sia visualizzata dopo il tentativo di accesso con un campo password vuoto.
     * Verifica che il layout di login sia ancora visualizzato dopo il tentativo di accesso con un campo password vuoto.
     */
    @Test
    public void emptyPasswordFieldTest(){

        Espresso.onView(ViewMatchers.withId(R.id.editTextTextEmailAddress2))
                .perform(ViewActions.replaceText("loginAcceptancetest@gmail.com"), ViewActions.closeSoftKeyboard());
        Espresso.onView(ViewMatchers.withId(R.id.editTextTextPassword2))
                .perform(ViewActions.replaceText(""), ViewActions.closeSoftKeyboard());
        Espresso.onView(ViewMatchers.withId(R.id.loginButton)).perform(ViewActions.click());

        // Asserisci che il layout principale sia mostrato
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Espresso.onView(ViewMatchers.withId(R.id.main)).check(doesNotExist());
        Espresso.onView(ViewMatchers.withId(R.id.login)).check(matches(isDisplayed()));
    }

    /**
     * Test per verificare il login con credenziali corrette.
     * Verifica che la pagina principale dell'applicazione sia visualizzata dopo il login con credenziali corrette.
     * Elimina successivamente l'account di test creato.
     */
    @Test
    public void successfulLoginTest(){

        Espresso.onView(ViewMatchers.withId(R.id.editTextTextEmailAddress2))
                .perform(ViewActions.replaceText("loginAcceptancetest@gmail.com"), ViewActions.closeSoftKeyboard());
        Espresso.onView(ViewMatchers.withId(R.id.editTextTextPassword2))
                .perform(ViewActions.replaceText("password123"), ViewActions.closeSoftKeyboard());
        Espresso.onView(ViewMatchers.withId(R.id.loginButton)).perform(ViewActions.click());

        // Asserisci che il layout principale sia mostrato
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Espresso.onView(ViewMatchers.withId(R.id.main)).check(matches(isDisplayed()));

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        user.delete()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {}
                    }
                });

    }

}