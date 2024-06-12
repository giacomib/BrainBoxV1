package com.example.brainbox.gestioneProfilo;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.RootMatchers.withDecorView;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.test.core.app.ActivityScenario;
import androidx.test.espresso.Espresso;
import androidx.test.espresso.action.ViewActions;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import androidx.test.filters.LargeTest;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.example.brainbox.MainActivity;
import com.example.brainbox.R;
import com.example.brainbox.autenticazione.model.User;
import com.example.brainbox.autenticazione.view.Login;
import com.example.brainbox.autenticazione.view.Registrazione;
import com.example.brainbox.gestioneProfilo.view.OpzioniUtente;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Objects;

public class GestioneProfiloAcceptanceTests {

    @Rule
    public ActivityScenarioRule<Registrazione> activityScenarioRule =
            new ActivityScenarioRule<>(Registrazione.class);

    @Test
    public void registerLoginChangeValidUsername() {

        String username1 = "usrOpzUtenteTest01";
        String username2 = "usrOpzUtenteTest02";
        String email = "usrOpzUtenteTest01@gmail.com";
        String psw = "password123";

        //registrazione
        Espresso.onView(ViewMatchers.withId(R.id.editTextUsername))
                .perform(ViewActions.replaceText(username1));
        Espresso.onView(ViewMatchers.withId(R.id.editTextTextEmailAddress))
                .perform(ViewActions.replaceText(email));
        Espresso.onView(ViewMatchers.withId(R.id.editTextTextPassword))
                .perform(ViewActions.replaceText(psw));
        Espresso.onView(ViewMatchers.withId(R.id.editTextConfirmPassword))
                .perform(ViewActions.replaceText(psw));
        Espresso.onView(ViewMatchers.withId(R.id.button))
                .perform(ViewActions.click());

        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        //login
        Espresso.onView(ViewMatchers.withId(R.id.editTextTextEmailAddress2))
                .perform(ViewActions.replaceText(email));
        Espresso.onView(ViewMatchers.withId(R.id.editTextTextPassword2))
                .perform(ViewActions.replaceText(psw));
        Espresso.onView(ViewMatchers.withId(R.id.loginButton))
                .perform(ViewActions.click());

        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        //naviga nell'activity delle opzioni utente
        Espresso.onView(withId(R.id.usr_opt_icon)).perform(ViewActions.click());

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        //sostituzione username
        onView(withId(R.id.username_text)).perform(ViewActions.replaceText(username2));
        onView(withId(R.id.saveBtn)).perform(ViewActions.click());

        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        onView(withId(R.id.username_text)).check(matches(withText(username2)));

        //eliminazione utente sia da firebase authentication che real time db
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

    @Test
    public void registerLoginChangeEmptyUsername() {

        String username1 = "usrOpzUtenteTest01";
        String username2 = "";
        String email = "usrOpzUtenteTest01@gmail.com";
        String psw = "password123";

        //registrazione
        Espresso.onView(ViewMatchers.withId(R.id.editTextUsername))
                .perform(ViewActions.replaceText(username1));
        Espresso.onView(ViewMatchers.withId(R.id.editTextTextEmailAddress))
                .perform(ViewActions.replaceText(email));
        Espresso.onView(ViewMatchers.withId(R.id.editTextTextPassword))
                .perform(ViewActions.replaceText(psw));
        Espresso.onView(ViewMatchers.withId(R.id.editTextConfirmPassword))
                .perform(ViewActions.replaceText(psw));
        Espresso.onView(ViewMatchers.withId(R.id.button))
                .perform(ViewActions.click());

        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        //login
        Espresso.onView(ViewMatchers.withId(R.id.editTextTextEmailAddress2))
                .perform(ViewActions.replaceText(email));
        Espresso.onView(ViewMatchers.withId(R.id.editTextTextPassword2))
                .perform(ViewActions.replaceText(psw));
        Espresso.onView(ViewMatchers.withId(R.id.loginButton))
                .perform(ViewActions.click());

        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        //naviga nell'activity delle opzioni utente
        Espresso.onView(withId(R.id.usr_opt_icon)).perform(ViewActions.click());

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        //sostituzione username
        onView(withId(R.id.username_text)).perform(ViewActions.replaceText(username2));
        onView(withId(R.id.saveBtn)).perform(ViewActions.click());

        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        onView(withId(R.id.username_text)).check(matches(withText("")));

        //eliminazione utente sia da firebase authentication che real time db
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

/*

    @Test
    public void registerLoginChangeSpaceUsername() {

        String username1 = "usrOpzUtenteTest01";
        String username2 = "user name";
        String email = "usrOpzUtenteTest01@gmail.com";
        String psw = "password123";

        //registrazione
        Espresso.onView(ViewMatchers.withId(R.id.editTextUsername))
                .perform(ViewActions.replaceText(username1));
        Espresso.onView(ViewMatchers.withId(R.id.editTextTextEmailAddress))
                .perform(ViewActions.replaceText(email));
        Espresso.onView(ViewMatchers.withId(R.id.editTextTextPassword))
                .perform(ViewActions.replaceText(psw));
        Espresso.onView(ViewMatchers.withId(R.id.editTextConfirmPassword))
                .perform(ViewActions.replaceText(psw));
        Espresso.onView(ViewMatchers.withId(R.id.button))
                .perform(ViewActions.click());

        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        //login
        Espresso.onView(ViewMatchers.withId(R.id.editTextTextEmailAddress2))
                .perform(ViewActions.replaceText(email));
        Espresso.onView(ViewMatchers.withId(R.id.editTextTextPassword2))
                .perform(ViewActions.replaceText(psw));
        Espresso.onView(ViewMatchers.withId(R.id.loginButton))
                .perform(ViewActions.click());

        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        //naviga nell'activity delle opzioni utente
        Espresso.onView(withId(R.id.menu_icon)).perform(ViewActions.click());

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        //sostituzione username
        onView(withId(R.id.username_text)).perform(ViewActions.replaceText(username2));
        onView(withId(R.id.saveBtn)).perform(ViewActions.click());

        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        onView(withId(R.id.username_text)).check(matches(withText("")));

        //eliminazione utente sia da firebase authentication che real time db
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
*/

}
