package com.example.brainbox.autenticazione.viewModel;

import android.content.Context;
import android.content.Intent;

import androidx.lifecycle.ViewModel;
import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import com.example.brainbox.autenticazione.model.User;
import com.example.brainbox.autenticazione.view.Login;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

/**
 * ViewModel per la gestione dell'autenticazione degli utenti.
 */
public class AuthenticationViewModel extends ViewModel {
    /** LiveData per indicare il successo della registrazione. */
    private MutableLiveData<Boolean> registrationSuccess = new MutableLiveData<>();
    /** LiveData per indicare il successo del login. */
    private MutableLiveData<Boolean> loginSuccess = new MutableLiveData<>();
    /** LiveData per indicare se l'username è unico. */
    private MutableLiveData<Boolean> uniqueUser = new MutableLiveData<>();
    /** Riferimento al database Firebase. */
    private DatabaseReference mDatabase;

    /**
     * Restituisce LiveData per il successo della registrazione.
     * @return LiveData per il successo della registrazione.
     */
    public LiveData<Boolean> getRegistrationSuccess() {
        return registrationSuccess;
    }
    /**
     * Restituisce LiveData per il successo del login.
     * @return LiveData per il successo del login.
     */
    public LiveData<Boolean> getLoginSuccess() {
        return loginSuccess;
    }
    /**
     * Restituisce LiveData per indicare se l'utente è unico.
     * @return LiveData per indicare se l'utente è unico.
     */
    public LiveData<Boolean> getUniqueUser() {
        return uniqueUser;
    }

    /**
     * Procedura di registrazione dell'utente.
     * @param email L'email dell'utente.
     * @param password La password dell'utente.
     * @param username Il nome utente dell'utente.
     * @param mAuth L'istanza di FirebaseAuth per l'autenticazione.
     */
    public void registrationProcedure(String email, String password, String username, FirebaseAuth mAuth){
        mDatabase = FirebaseDatabase.getInstance("https://brainbox-37414-default-rtdb.europe-west1.firebasedatabase.app/").getReference();
        DatabaseReference usersRef = mDatabase.child("users");
        List<String> usernames = new ArrayList<>();

        usersRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                    String user_name = userSnapshot.child("username").getValue(String.class);
                    usernames.add(user_name);
                }
                if(!(usernames.contains(username))){
                    uniqueUser.setValue(true);
                    registerUser(email, password, username, mAuth);
                }
                else{
                    uniqueUser.setValue(false);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        });
    }

    /**
     * Registra l'utente nel database Firebase.
     * @param email L'email dell'utente.
     * @param password La password dell'utente.
     * @param username Il nome utente dell'utente.
     * @param mAuth L'istanza di FirebaseAuth per l'autenticazione.
     */
    public void registerUser(String email, String password, String username, FirebaseAuth mAuth){
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser user = mAuth.getCurrentUser();
                            String userId = user.getUid();
                            mDatabase = FirebaseDatabase.getInstance("https://brainbox-37414-default-rtdb.europe-west1.firebasedatabase.app/").getReference();
                            User newUser = new User(username, email);
                            mDatabase.child("users").child(userId).setValue(newUser);
                            registrationSuccess.setValue(true);
                        } else {
                            registrationSuccess.setValue(false);
                        }
                    }
                });
    }

    /**
     * Effettua il login dell'utente.
     * @param email L'email dell'utente.
     * @param password La password dell'utente.
     * @param mAuth L'istanza di FirebaseAuth per l'autenticazione.
     */
    public void logUser(String email, String password, FirebaseAuth mAuth){
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            loginSuccess.setValue(true);
                        } else {
                            loginSuccess.setValue(false);
                        }
                    }
                });
    }

    /**
     * Verifica che l'input (email, password, username) non sia vuoto.
     * @param email L'email dell'utente.
     * @param password La password dell'utente.
     * @param username Il nome utente dell'utente.
     * @param confirmPassword conferma della password inserita
     * @return true se l'input non è vuoto, altrimenti false.
     */
    public boolean checkInput (String email, String password, String username, String confirmPassword) {
        if(email.isEmpty() || password.isEmpty() || username.isEmpty() || confirmPassword.isEmpty())
            return false;
        return true;
    }

    public Intent logoutUsr(Context context, FirebaseAuth mAuth) {
        mAuth.signOut();
        Intent intent = new Intent(context, Login.class);
        return intent;
    }


}
