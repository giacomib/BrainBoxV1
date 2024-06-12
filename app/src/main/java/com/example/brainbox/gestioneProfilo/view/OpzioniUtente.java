package com.example.brainbox.gestioneProfilo.view;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.bumptech.glide.Glide;
import com.example.brainbox.MainActivity;
import com.example.brainbox.R;
import com.example.brainbox.autenticazione.model.User;
import com.example.brainbox.autenticazione.view.Login;
import com.example.brainbox.autenticazione.viewModel.AuthenticationViewModel;
import com.example.brainbox.gestioneProfilo.viewModel.OpzioniUtenteViewModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;
import java.util.Objects;

public class OpzioniUtente extends AppCompatActivity {

    private EditText username;
    private EditText email;
    private ImageView usrImage;
    private DatabaseReference databaseReference;
    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;
    private User user;
    private Uri imageUri;
    FirebaseStorage storage = FirebaseStorage.getInstance();
    StorageReference storagereference = storage.getReference();
    boolean imgClicked;

    private ImageView logout_icon;
    private ImageView note_icon;
    private ImageView usr_opt_icon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_opzioni_utente);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        username = findViewById(R.id.username_text);
        email = findViewById(R.id.email_text);
        usrImage = findViewById(R.id.userImage);
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();

        Button save_button = findViewById(R.id.saveBtn);
        TextView forgetPass = findViewById(R.id.forgetPass);

        OpzioniUtenteViewModel opzioniUtenteViewModel = new OpzioniUtenteViewModel();
        AuthenticationViewModel authenticationViewModel = new AuthenticationViewModel();

        databaseReference = FirebaseDatabase
                .getInstance("https://brainbox-37414-default-rtdb.europe-west1.firebasedatabase.app/")
                .getReference("users")
                .child(currentUser.getUid());



        if(currentUser == null) {
            Toast.makeText(this, "utente non loggato", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(getApplicationContext(), Login.class);
            startActivity(intent);
            finish();
            return;
        }

        scaricaImmagine();

        /**
         * Aggiornamento view con username e
         * email dell'utente attuale
         */
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                user = snapshot.getValue(User.class);
                if (user != null) {
                    username.setText(user.getUsername());
                    email.setText(user.getEmail());
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        /**
         * Nel momento in cui l'utente clicca sul bottone salva,
         * aggiorna i dati dell'utente:
         * -- username
         * -- email
         * -- immagine utente
         */
        save_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String newUsername = username.getText().toString();
                String newEmail = email.getText().toString();

                if(!newUsername.equals(user.getUsername())) {
                    if(opzioniUtenteViewModel.usernameValidation(newUsername)) {
                        updateUsername(newUsername);
                    }else {
                        Toast.makeText(OpzioniUtente.this, "Username non valida", Toast.LENGTH_SHORT).show();
                    }
                }

                if(!newEmail.equals(user.getEmail())) {
                    if(opzioniUtenteViewModel.emailValidation(newEmail)) {
                        updateEmail(newEmail);
                    }else {
                        Toast.makeText(OpzioniUtente.this, "Email non valida", Toast.LENGTH_SHORT).show();
                    }
                }
                if(imgClicked)
                    uploadToFirebase();
            }
        });

        forgetPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showRecoverPasswordDialog();
            }
        });

        /**
         * Nel momento in cui l'utente clicca sul bottone relativo
         * all'immagine profilo profilo, si apre una nuova attività
         * per permettergli di scegliere una nuova immagine
         */
        usrImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imgClicked = true;
                Intent galleryIntent = new Intent();
                galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
                galleryIntent.setType("image/*");
                startActivityForResult(galleryIntent, 22);
            }
        });

        logout_icon = findViewById(R.id.logout_icon);

        logout_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = authenticationViewModel.logoutUsr(getApplicationContext(), mAuth);
                startActivity(intent);
            }
        });

        note_icon = findViewById(R.id.note_icon);

        note_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
            }
        });

        usr_opt_icon = findViewById(R.id.usr_opt_icon);

        usr_opt_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(OpzioniUtente.this, "Pagina relativa alle opzioni utente", Toast.LENGTH_SHORT).show();
            }
        });

    }

    /**
     * Scarica, se già presente, l'attuale immagine
     * profilo dell'utente, se non ha mai caricato un
     * immagine, allora non succede niente
     */
    public void scaricaImmagine() {
        StorageReference newRef = storagereference.child("ProfileImages/" + currentUser.getUid());
        newRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Glide.with(OpzioniUtente.this)
                        .load(uri)
                        .into(usrImage);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.i("err msg: errore nello scaricare l'immagine dell'utente", Objects.requireNonNull(e.getMessage()));
            }
        });

    }

    /**
     * Controlla che l'immagine selezionata
     * dall'utente sia valida
     * @param requestCode The integer request code originally supplied to
     *                    startActivityForResult(), allowing you to identify who this
     *                    result came from.
     * @param resultCode The integer result code returned by the child activity
     *                   through its setResult().
     * @param data An Intent, which can return result data to the caller
     *               (various data can be attached to Intent "extras").
     *
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == 22 && resultCode == RESULT_OK && data != null && data.getData() != null) {
            imageUri = data.getData();
            usrImage.setImageURI(imageUri);
        }
    }

    /**
     * Effettua il caricamento dell'immagine selezionata
     * su firebase storage; notifica l'utente sia se il
     * caricamento va a buon fine che se fallisce
     */
    public void uploadToFirebase() {

        if(imageUri != null) {

            //Uri file = Uri.fromFile(new File(imageUri.getPath()));
            StorageReference imgRef = storagereference.child("ProfileImages/" + currentUser.getUid());

            imgRef.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Toast.makeText(OpzioniUtente.this, "Caricamento immagine completato", Toast.LENGTH_SHORT).show();
                    Log.d("info", "Caricamento immagine completato");
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(OpzioniUtente.this, "Caricamento immagine fallito", Toast.LENGTH_SHORT).show();
                    Log.i("err msg: errore durante caricamento immagine utente", Objects.requireNonNull(e.getMessage()));
                }
            });
        }
    }

    /**
     * Genera la dialog per recuperare la psw,
     * chiede all'utente di inserire l'email utilizzata
     * per la registrazione nuovamente, se l'utente quindi
     * clicca sul bottone per il recupero, gli verrà inviata
     * una mail per effettuare il recupero.
     */
    public void showRecoverPasswordDialog() {
        AlertDialog.Builder builder=new AlertDialog.Builder(this);
        builder.setTitle("Inserire l'email per il recupero della password");
        LinearLayout linearLayout=new LinearLayout(this);
        final EditText emailet= new EditText(this);
        emailet.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);

        // write the email using which you registered
        emailet.setText("Email");
        emailet.setMinEms(16);
        emailet.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
        linearLayout.addView(emailet);
        linearLayout.setPadding(10,10,10,10);
        builder.setView(linearLayout);

        // Click on Recover and a email will be sent to your registered email id
        builder.setPositiveButton("Recupera", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String email=emailet.getText().toString().trim();
                beginRecovery(email);
            }
        });

        builder.setNegativeButton("Annulla", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.create().show();
    }

    /**
     * Metodo di supporto per il recupero della psw
     * questo metodo avvia la procedura per inviare la
     * mail di recupero password.
     * @param email email a cui verrà inviata la mail per effettuare il recupero password.
     */
    public void beginRecovery(String email) {
        // calling sendPasswordResetEmail
        // open your email and write the new
        // password and then you can login

        mAuth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful())
                {
                    // if isSuccessful then done message will be shown
                    // and you can change the password
                    Toast.makeText(OpzioniUtente.this,"Email di recupero inviata",Toast.LENGTH_LONG).show();
                }
                else {
                    Toast.makeText(OpzioniUtente.this,"Si è verificato un errore",Toast.LENGTH_LONG).show();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(OpzioniUtente.this,"Si è verificato un errore",Toast.LENGTH_LONG).show();
            }
        });
    }

    /**
     * Metodo di supporto per aggiornare l'email sul DB firebase.
     * @param newEmail
     */
    public void updateEmail(String newEmail) {
        //questa prima parte cambia l'email di autenticazione
        currentUser.verifyBeforeUpdateEmail(newEmail).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(OpzioniUtente.this, "Verificare la nuova mail", Toast.LENGTH_SHORT).show();
                }
            }
        });

        //questa seconda parte cambia l'email nel RTDB
        HashMap user = new HashMap();
        user.put("email", newEmail);

        databaseReference.updateChildren(user).addOnCompleteListener(new OnCompleteListener() {
            @Override
            public void onComplete(@NonNull Task task) {
                if(task.isSuccessful()) {
                    Toast.makeText(OpzioniUtente.this, "email aggiornata correttamente", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(OpzioniUtente.this, "email non aggiornata", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void updateUsername(String newUsername) {

        HashMap user = new HashMap();
        user.put("username", newUsername);

        databaseReference.updateChildren(user).addOnCompleteListener(new OnCompleteListener() {
            @Override
            public void onComplete(@NonNull Task task) {
                if(task.isSuccessful()) {
                    Toast.makeText(OpzioniUtente.this, "Username aggiornato correttamente", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(OpzioniUtente.this, "Username non aggiornato", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}