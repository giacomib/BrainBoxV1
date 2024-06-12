package com.example.brainbox.note.view;

import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.Observer;

import com.example.brainbox.MainActivity;
import com.example.brainbox.R;
import com.example.brainbox.autenticazione.viewModel.AuthenticationViewModel;
import com.example.brainbox.gestioneProfilo.view.OpzioniUtente;
import com.example.brainbox.note.model.Elemento;
import com.example.brainbox.note.viewModel.NoteViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Activity per visualizzare e interagire con una singola pagina di note.
 */
public class Pagina_di_note extends AppCompatActivity {

    /**
     * ViewModel per gestire gli elementi della pagina di note
     */
    private NoteViewModel noteViewModel;
    /**
     * FloatingActionButton per aprire il menu degli elementi da inserire nella pagina di note
     */
    private FloatingActionButton addElemento;
    /**
     * Dialog in cui è contenuto il menu degli elementi da inserire nella pagina di note
     */
    private Dialog elementsDialog;
    /**
     * TextView per l'elemento "Nota testuale" nel dialog degli elementi.
     */
    private TextView element_nota_testuale;
    /**
     * Dialog per la creazione di una nuova nota testuale.
     */
    private Dialog noteTestualiDialog;
    /**
     * Riferimento al bottone per confermare la creazione di una nuova nota testuale.
     */
    private Button crea_nota_testuale;
    /**
     * EditText per inserire il titolo della nota testuale.
     */
    private EditText titolo_nota_testuale;
    /**
     * EditText per inserire il contenuto della nota testuale.
     */
    private EditText contenuto_nota_testuale;
    /**
     * GridView per visualizzare gli elementi della pagina di note.
     */
    private GridView elements_grid_view;
    /**
     * Arraylist contenente le informazioni delle note testuali.
     */
    private ArrayList<Map<String, String>> elements;
    /**
     * Istanza di FirebaseAuth per gestire l'autenticazione dell'utente.
     */
    private FirebaseAuth mAuth;
    /**
     * ImageView per l'icona delle note nella toolbar.
     */
    private ImageView note_icon;
    /**
     * ImageView per l'icona delle opzioni utente nella toolbar.
     */
    private ImageView usr_opt_icon;
    /**
     * ImageView per l'icona di logout nella toolbar.
     */
    private ImageView logout_icon;
    /**
     * TextView per l'elemento Checklist nel Dialog elementsDialog
     */
    private TextView element_checklist;
    /**
     * Dialog per la creazione di nuove checklist
     */
    private Dialog checklistDialog;
    /**
     * EditText per l'inserimento del titolo di una nuova checklist
     */
    private EditText titolo_checklist;
    /**
     * EditText per l'inserimento del contenuto della nuova checklist
     */
    private EditText contenuto_checklist;
    /**
     * Button per la creazione di una nuova checklist
     */
    private Button crea_checklist;
    /**
     * Adapter per la visualizzazione degli elementi della pagina di note
     */
    private ElementsAdapter adapter;
    /**
     * Arraylist di oggetti di tipo Elemento della pagina di note
     */
    private List<Elemento> elementiList = new ArrayList<>();
    /**
     * TextView per l'elemento "Immagine" nel dialog degli elementi.
     */
    private TextView element_immagine;
    /**
     * Stringa per il titolo della pagina di note
     */
    private String titolo;
    /**
     * Searchview per la ricerca di contenuti testuali
     */
    private SearchView searchView;

    /**
     * Metodo chiamato alla creazione dell'Activity.
     * @param savedInstanceState Istanza salvata dello stato precedente dell'Activity.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_pagina_di_note);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.pagina_di_note), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // inizializzazione titolo e userID
        Intent intent = getIntent();
        titolo = intent.getStringExtra("titolo");
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        String currentUserId = currentUser.getUid();

        // inizializzazione viewmodel e adapter
        AuthenticationViewModel authenticationViewModel = new AuthenticationViewModel();
        noteViewModel = new NoteViewModel();
        adapter = new ElementsAdapter(this, elementiList, currentUserId, titolo);

        // Inizializzazione elementi della GridView
        elements_grid_view = findViewById(R.id.elements_grid_view);
        searchView = findViewById(R.id.searchView_elementi);

        elements_grid_view.setAdapter(adapter);
        // listener query Searchbar
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                adapter.getFilter().filter(newText);
                return false;
            }
        });
        searchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                noteViewModel.getRealtimeElements(currentUserId, titolo, elementiList, adapter);
                return false;
            }
        });

        // get elementi del db firebase
        noteViewModel.getRealtimeElements(currentUserId, titolo, elementiList, adapter);

        // inizializzazione icone Toolbar
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
                Intent intent = new Intent(getApplicationContext(), OpzioniUtente.class);
                startActivity(intent);
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

        // Inizializzazione FloatingButton e Dialog per la creazione di nuovi elementi
        addElemento = findViewById(R.id.addElemento);
        elementsDialog = new Dialog(this);
        elementsDialog.setContentView(R.layout.activity_add_new_element);
        elementsDialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        // listener per il click del FloatingButton
        addElemento.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                elementsDialog.show();
            }
        });

        // Inizializzazione Dialog per la creazione note testuali e dei suoi elementi
        element_nota_testuale = elementsDialog.findViewById(R.id.element_nota_testuale);
        noteTestualiDialog = new Dialog(this);
        noteTestualiDialog.setContentView(R.layout.activity_add_nota_testuale);
        noteTestualiDialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        // listener per l'azione di click della voce "Nota testuale" nell'elementsDialog
        element_nota_testuale.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                elementsDialog.dismiss();
                noteTestualiDialog.show();
            }
        });

        titolo_nota_testuale = noteTestualiDialog.findViewById(R.id.titolo_nota_testuale);
        contenuto_nota_testuale = noteTestualiDialog.findViewById(R.id.contenuto_nota_testuale);
        crea_nota_testuale = noteTestualiDialog.findViewById(R.id.crea_nota_testuale);

        // Listener per l'azione di click del bottone noteTestualiDialog
        crea_nota_testuale.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // funzione in NoteViewModel
                if(!titolo_nota_testuale.getText().toString().isEmpty()) {
                    noteViewModel.newNotaTestuale(titolo_nota_testuale.getText().toString(), contenuto_nota_testuale.getText().toString(), titolo);
                } else
                    Toast.makeText(Pagina_di_note.this, "Inserire un titolo", Toast.LENGTH_SHORT).show();

            }
        });

        noteViewModel.getNotaTestualeSuccess().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean notaTestualeSuccess) {
                if(!notaTestualeSuccess){
                    Toast.makeText(Pagina_di_note.this, "Titolo già in uso", Toast.LENGTH_SHORT).show();
                    titolo_nota_testuale.setText("");
                } else {
                    Toast.makeText(Pagina_di_note.this, "Nota testuale creata ", Toast.LENGTH_SHORT).show();
                    titolo_nota_testuale.setText("");
                    contenuto_nota_testuale.setText("");
                    noteTestualiDialog.dismiss();
                }
            }
        });

        // Inizializzazione Dialog per la creazione di Checklist e dei suoi elementi
        element_checklist = elementsDialog.findViewById(R.id.element_checklist);
        checklistDialog = new Dialog(this);
        checklistDialog.setContentView(R.layout.activity_add_new_checklist);
        checklistDialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        // Listener per checklistDialog al click della voce "checklist"
        element_checklist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                elementsDialog.dismiss();
                checklistDialog.show();
            }
        });

        titolo_checklist = checklistDialog.findViewById(R.id.titolo_checklist);
        contenuto_checklist = checklistDialog.findViewById(R.id.contenuto_checklist);
        crea_checklist = checklistDialog.findViewById(R.id.crea_checklist);

        // Listener per l'azione di click del bottone crea_checklist
        crea_checklist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (titolo_checklist.getText().toString().isEmpty() || contenuto_checklist.getText().toString().isEmpty()){
                    Toast.makeText(Pagina_di_note.this, "Completare tutti i campi", Toast.LENGTH_SHORT).show();
                } else {
                    noteViewModel.newChecklist(titolo_checklist.getText().toString(),
                            contenuto_checklist.getText().toString(),
                            titolo);
                }
            }
        });

        noteViewModel.getChecklistSuccess().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean checklistSuccess) {
                if (!checklistSuccess){
                    Toast.makeText(Pagina_di_note.this, "Titolo già in uso", Toast.LENGTH_SHORT).show();
                    titolo_checklist.setText("");
                } else {
                    Toast.makeText(Pagina_di_note.this, "Check List creata ", Toast.LENGTH_SHORT).show();
                    titolo_checklist.setText("");
                    contenuto_checklist.setText("");
                    checklistDialog.dismiss();
                }
            }
        });

        // binding bottone "Immagine" nella dialog
        // relativa all'aggiunta degli elementi
        element_immagine = elementsDialog.findViewById(R.id.element_immagine);

        // Listener per l'azione di click della voce "Immagine"
        // nella dialog relativa all'aggiunta degli elementi
        element_immagine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                elementsDialog.dismiss();
                Intent galleryIntent = new Intent();
                galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
                galleryIntent.setType("image/*");
                startActivityForResult(galleryIntent, 22);
            }
        });

        // Listener per verificare il corretto
        // caricamento dell'immagine o meno
        noteViewModel.getAddImageSuccess().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean success) {
                if (success) {
                    Toast.makeText(Pagina_di_note.this, "Immagine aggiunta", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(Pagina_di_note.this, "Errore nel caricamento dell'immagine", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }// End onCreate method

    /**
     * Controlla che l'immagine selezionata dall'utente sia valida
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
            Uri uri = data.getData();

            // Setup db realt time
            noteViewModel.setupImage(uri, titolo);

            // Caricaremento immagine su firebase storage
            noteViewModel.addImage(uri, titolo);

            Log.d("Notes igm", "Immagine caricata correttamente");

        } else {
            Log.d("Notes igm", "Errore durante la selezione dell'immagine");
        }
    }

}