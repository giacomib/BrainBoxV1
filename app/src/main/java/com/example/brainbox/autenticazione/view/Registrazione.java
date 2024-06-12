package com.example.brainbox.autenticazione.view;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.brainbox.*;
import com.example.brainbox.autenticazione.viewModel.AuthenticationViewModel;
import com.google.firebase.auth.FirebaseAuth;

/**
 * Activity per la registrazione di un nuovo utente.
 */
public class Registrazione extends AppCompatActivity {

    /**
     * Riferimento all'editText della mail dell'utente nel form di registrazione
     */
    private EditText emailEditText;
    /**
     * Riferimento all'editText della password dell'utente nel form di registrazione
     */
    private EditText passwordEditText;
    /**
     * Riferimento all'editText del campo conferma password nel form di registrazione
     */
    private EditText confirmPasswordEditText;
    /**
     * Riferimento all'editText dell'username dell'utente nel form di registrazione
     */
    private EditText usernameEditText;
    /**
     * Riferimento al bottone per la registrazione dell'utente nel form di registrazione
     */
    private Button registerButton;
    /**
     * Riferimento all'AuthenticationViewModel
     */
    private AuthenticationViewModel authenticationViewModel;
    /**
     * Riferimento al FirebaseAuth per l'autenticazione
     */
    private FirebaseAuth mAuth;
    /**
     * Riferimento al TextView nel form di registrazione
     */
    private TextView goToLogin;

    /**
     * Metodo invocato alla creazione della View di registrazione
     *
     * @param savedInstanceState Se l'attività viene reinizializzata dopo essere stata
     *                           precedentemente chiusa, questo bundle contiene i dati forniti più
     *                           recentemente in {@link #onSaveInstanceState}.
     *
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_registrazione);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.register), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        mAuth = FirebaseAuth.getInstance();

        emailEditText = findViewById(R.id.editTextTextEmailAddress);
        passwordEditText = findViewById(R.id.editTextTextPassword);
        confirmPasswordEditText = findViewById(R.id.editTextConfirmPassword);
        usernameEditText = findViewById(R.id.editTextUsername);
        registerButton = findViewById(R.id.button);
        goToLogin = findViewById(R.id.loginNow);

        authenticationViewModel = new ViewModelProvider(this).get(AuthenticationViewModel.class);

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String email = emailEditText.getText().toString();
                String password = passwordEditText.getText().toString();
                String confirmPassword = confirmPasswordEditText.getText().toString();
                String username = usernameEditText.getText().toString();

                if(!authenticationViewModel.checkInput(email, password, username, confirmPassword)) {
                    Toast.makeText(Registrazione.this, "Completare tutti i campi", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(!confirmPassword.equals(password)){
                    Toast.makeText(Registrazione.this, "Le password inserite non corrispondono", Toast.LENGTH_SHORT).show();
                } else
                    authenticationViewModel.registrationProcedure(email, password, username, mAuth);
            }
        });

        authenticationViewModel.getUniqueUser().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean isUnique) {
                if (!isUnique) {
                    Toast.makeText(Registrazione.this, "Username non disponibile", Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(Registrazione.this, "Username disponibile", Toast.LENGTH_SHORT).show();
                }
            }
        });

        authenticationViewModel.getRegistrationSuccess().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean isSuccess) {
                if (isSuccess) {
                    Intent intent = new Intent(Registrazione.this, Login.class);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(getApplicationContext(), "Registration failed", Toast.LENGTH_SHORT).show();
                }
            }
        });

        goToLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Registrazione.this, Login.class);
                startActivity(intent);
                finish();
            }
        });

    }
}