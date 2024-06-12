package com.example.brainbox.navigabilita;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

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

public class NavigabilitaAcceptanceTests {

    @Rule
    public ActivityScenarioRule<Registrazione> activityScenarioRule =
            new ActivityScenarioRule<>(Registrazione.class);

    @Test
    public void registerLoginUsropt() {

        String username1 = "usrOpzUtenteTest01";
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

        //controllo di essere nell'activity giusta
        onView(withId(R.id.forgetPass)).check(matches(isDisplayed()));

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
    public void registerLoginUsroptAndBack() {

        String username1 = "usrOpzUtenteTest01";
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

        //naviga nell'activity principale
        Espresso.onView(withId(R.id.note_icon)).perform(ViewActions.click());

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        onView(withId(R.id.align)).check(matches(isDisplayed()));

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
}
