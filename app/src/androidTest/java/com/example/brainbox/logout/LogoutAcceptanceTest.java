package com.example.brainbox.logout;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.test.espresso.Espresso;
import androidx.test.espresso.action.ViewActions;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.rules.ActivityScenarioRule;

import com.example.brainbox.R;
import com.example.brainbox.autenticazione.view.Login;
import com.example.brainbox.autenticazione.view.Registrazione;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.junit.Rule;
import org.junit.Test;

public class LogoutAcceptanceTest {

    @Rule
    public ActivityScenarioRule<Registrazione> activityScenarioRule =
            new ActivityScenarioRule<>(Registrazione.class);

    @Test
    public void registerLoginLogout() {

        String username1 = "usrTest01";
        String email = "usrTest01@gmail.com";
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

        //salvo l'id dell'utente prima di effettuare il logout
        // altrimenti non posso eliminare l'account a posteriori
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String userId = user.getUid();

        //logout
        Espresso.onView(withId(R.id.logout_icon)).perform(ViewActions.click());

        //check logout effettuato
        onView(withId(R.id.login)).check(matches(isDisplayed()));

        //eliminazione utente sia da firebase authentication che real time db

        user.delete()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Log.i("info DB", "utente rimosso correttamente");
                        }
                    }
                });

        DatabaseReference mDatabase = FirebaseDatabase.getInstance("https://brainbox-37414-default-rtdb.europe-west1.firebasedatabase.app/").getReference();
        mDatabase.child("users").child(userId).setValue(null);

    }

    @Test
    public void registerLoginUsroptLogout() {

        String username1 = "usrTest01";
        String email = "usrTest01@gmail.com";
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

        //salvo l'id dell'utente prima di effettuare il logout
        // altrimenti non posso eliminare l'account a posteriori
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String userId = user.getUid();

        //logout
        Espresso.onView(withId(R.id.logout_icon)).perform(ViewActions.click());

        //check logout effettuato
        onView(withId(R.id.login)).check(matches(isDisplayed()));

        //eliminazione utente sia da firebase authentication che real time db

        user.delete()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Log.i("info DB", "utente rimosso correttamente");
                        }
                    }
                });

        DatabaseReference mDatabase = FirebaseDatabase.getInstance("https://brainbox-37414-default-rtdb.europe-west1.firebasedatabase.app/").getReference();
        mDatabase.child("users").child(userId).setValue(null);

    }

    @Test
    public void registerLoginNoteLogout() {

        String username1 = "usrTest01";
        String email = "usrTest01@gmail.com";
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

        //creazione nuova pagina di note
        Espresso.onView(withId(R.id.addNewPaginaDiNote)).perform(ViewActions.click());
        Espresso.onView(ViewMatchers.withId(R.id.titoloPaginaNote)).perform(ViewActions.typeText("Test"));
        Espresso.onView(ViewMatchers.withId(R.id.submit_title)).perform(ViewActions.click());

        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        //Navigazione nella pagina di note creata
        //Espresso.onView(withId(R.id.usr_opt_icon)).perform(ViewActions.click());
        Espresso.onView(withText("Test")).perform(ViewActions.click());
        //Espresso.onView(ViewMatchers.withId(R.id.titoloPaginaNote)).perform(ViewActions.click());

        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        //salvo l'id dell'utente prima di effettuare il logout
        // altrimenti non posso eliminare l'account a posteriori
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String userId = user.getUid();

        //logout
        Espresso.onView(withId(R.id.logout_icon)).perform(ViewActions.click());

        //check logout effettuato
        onView(withId(R.id.login)).check(matches(isDisplayed()));

        //eliminazione utente sia da firebase authentication che real time db

        user.delete()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Log.i("info DB", "utente rimosso correttamente");
                        }
                    }
                });

        DatabaseReference mDatabase = FirebaseDatabase.getInstance("https://brainbox-37414-default-rtdb.europe-west1.firebasedatabase.app/").getReference();
        mDatabase.child("users").child(userId).setValue(null);

    }
}
