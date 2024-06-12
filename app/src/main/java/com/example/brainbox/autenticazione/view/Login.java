package com.example.brainbox.autenticazione.view;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
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

import com.example.brainbox.MainActivity;
import com.example.brainbox.*;
import com.example.brainbox.autenticazione.viewModel.AuthenticationViewModel;
import com.google.firebase.auth.FirebaseAuth;

/**
 * Activity per l'autenticazione dell'utente.
 */
public class Login extends AppCompatActivity {

    /**
     * Riferimento all'editText della mail dell'utente nel form di login
     */
    private EditText emailEditText;
    /**
     * Riferimento all'editText della password dell'utente nel form di login
     */
    private EditText passwordEditText;
    /**
     * Riferimento al bottone per l'autenticazione dell'utente nel form di login
     */
    private Button loginButton;
    /**
     * Riferimento all'AuthenticationViewModel
     */
    private AuthenticationViewModel authenticationViewModel;
    /**
     * Riferimento al FirebaseAuth per l'autenticazione
     */
    private FirebaseAuth mAuth;
    /**
     * Riferimento al TextView nel form di login
     */
    private TextView goToRegister;

    /**
     * Metodo invocato alla creazione della View di login
     *
     * @param savedInstanceState Se l'attività viene reinizializzata dopo essere stata
     *                           precedentemente chiusa, questo bundle contiene i dati forniti più
     *                           recentemente in {@link #onSaveInstanceState}.
     *                           <b><i>Note: altrimenti è null.</i></b>
     *
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.login), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        mAuth = FirebaseAuth.getInstance();

        emailEditText = findViewById(R.id.editTextTextEmailAddress2);
        passwordEditText = findViewById(R.id.editTextTextPassword2);
        loginButton = findViewById(R.id.loginButton);
        goToRegister = findViewById(R.id.registerNow);

        authenticationViewModel = new ViewModelProvider(this).get(AuthenticationViewModel.class);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String email = emailEditText.getText().toString();
                String password = passwordEditText.getText().toString();

                if(TextUtils.isEmpty(email) || TextUtils.isEmpty(password)){
                    Toast.makeText(Login.this, "Completare tutti i campi", Toast.LENGTH_SHORT).show();
                    return;
                }
                authenticationViewModel.logUser(email, password, mAuth);
            }
        });

        authenticationViewModel.getLoginSuccess().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean isSuccess) {
                if (isSuccess) {
                    Intent intent = new Intent(Login.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(getApplicationContext(), "Login failed", Toast.LENGTH_SHORT).show();
                }
            }
        });

        goToRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Registrazione.class);
                startActivity(intent);
                finish();
            }
        });

    }
}