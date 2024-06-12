package com.example.brainbox;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.SearchView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.Observer;

import com.example.brainbox.autenticazione.viewModel.AuthenticationViewModel;
import com.example.brainbox.gestioneProfilo.view.OpzioniUtente;
import com.example.brainbox.note.view.Pagina_di_note;
import com.example.brainbox.note.viewModel.NoteViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Map;


/**
 * Activity principale dell'applicazione, gestisce l'interfaccia utente per la creazione di
 * nuove pagine di note.
 */
public class MainActivity extends AppCompatActivity {

    /** Floating action button per aggiungere una nuova pagina di note. */
    private FloatingActionButton addPaginaDiNote;
    /** EditText per inserire il titolo della nuova pagina di note. */
    private EditText titoloPaginaNote;
    /** Button per confermare il titolo della nuova pagina di note. */
    private Button submitTitolo;
    /** Dialog per visualizzare l'interfaccia utente per la creazione di una nuova pagina di note. */
    private Dialog newPageDialog;
    /** ViewModel per la gestione delle note. */
    private NoteViewModel noteViewModel;
    /** ViewModel per la gestione dell'autenticazione. */
    private AuthenticationViewModel authenticationViewModel;
    /** GridView per visualizzare le pagine di note. */
    private GridView gridView;
    /** ArrayList contenente le informazioni sulle pagine di note. */
    private ArrayList<Map<String, String>> pagine_di_note;
    /** Oggetto FirebaseAuth per l'autenticazione. */
    private FirebaseAuth mAuth;
    /**
     * ImageView per l'icona di logout nella toolbar.
     */
    private ImageView logout_icon;
    /**
     * ImageView per l'icona delle note nella toolbar.
     */
    private ImageView note_icon;
    /**
     * ImageView per l'icona delle opzioni utente nella toolbar.
     */
    private SimpleAdapter adapter;
    private ImageView usr_opt_icon;
    private SearchView searchView;


    /**
     * Metodo chiamato alla creazione dell'activity.
     * Inizializza l'interfaccia utente e i vari componenti.
     * @param savedInstanceState lo stato dell'activity salvato in precedenza, se presente.
     */
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Inizializzazione del NoteViewModel
        noteViewModel = new NoteViewModel();

        //Inizializzazione del AuthenticationViewModel
        authenticationViewModel = new AuthenticationViewModel();

        // Inizializzazione dei componenti dell'interfaccia utente
        addPaginaDiNote = findViewById(R.id.addNewPaginaDiNote);
        newPageDialog = new Dialog(this);
        newPageDialog.setContentView(R.layout.activity_add_pagina_di_note);
        newPageDialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        submitTitolo = newPageDialog.findViewById(R.id.submit_title);
        titoloPaginaNote = newPageDialog.findViewById(R.id.titoloPaginaNote);

        // inizializzazione elementi della gridview
        gridView = findViewById(R.id.grid_view);
        searchView = findViewById(R.id.search_view);
        pagine_di_note = new ArrayList<>();
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        String currentUserId = currentUser.getUid();
        adapter = new SimpleAdapter(this, pagine_di_note, R.layout.pagina_di_note_item,
                new String[] {"titolo", "data"}, new int[] {R.id.pagina_di_note_titolo, R.id.pagina_di_note_data});


        // Imposta il ViewBinder per la TextView del titolo della pagina di note per lo scroll orizzontale
        adapter.setViewBinder(new SimpleAdapter.ViewBinder() {
            @Override
            public boolean setViewValue(View view, Object data, String textRepresentation) {
                if (view.getId() == R.id.pagina_di_note_titolo) {
                    TextView textView = (TextView) view;
                    textView.setText(textRepresentation);

                    // Imposta il focus e seleziona la TextView per farla scorrere automaticamente
                    textView.requestFocus();
                    textView.setSelected(true);

                    return true;
                }
                return false;
            }
        });

        gridView.setAdapter(adapter);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                MainActivity.this.adapter.getFilter().filter(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                MainActivity.this.adapter.getFilter().filter(newText);
                //adapter.getFilter().filter(newText);
                return false;
            }
        });

        // Recupera le pagine di note dal database Firebase
        noteViewModel.getRealTimePagineDiNote(pagine_di_note, currentUserId, adapter);

        // Listener per il click degli item della gridview
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TextView titoloTextView = view.findViewById(R.id.pagina_di_note_titolo);
                String titolo = titoloTextView.getText().toString();

                Intent intent = new Intent(MainActivity.this, Pagina_di_note.class);
                intent.putExtra("titolo", titolo);
                startActivity(intent);
            }
        });
        gridView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                TextView titoloTextView = view.findViewById(R.id.pagina_di_note_titolo);
                String titolo = titoloTextView.getText().toString();
                PopupMenu popup = new PopupMenu(MainActivity.this, view);
                popup.getMenuInflater().inflate(R.menu.menu_impostazioni_pagina_di_note, popup.getMenu());

                try {
                    Field popupField = popup.getClass().getDeclaredField("mPopup");
                    popupField.setAccessible(true);
                    Object menuPopupHelper = popupField.get(popup);
                    Class<?> classPopupHelper = Class.forName(menuPopupHelper.getClass().getName());
                    Method setForceIcons = classPopupHelper.getMethod("setForceShowIcon", boolean.class);
                    setForceIcons.invoke(menuPopupHelper, true);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                // Gestione degli elementi del menu da implementare
                popup.show();
                return true;
            }
        });

        // Gestione del click sul floating action button per aggiungere una nuova pagina di note
        addPaginaDiNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                newPageDialog.show();
            }
        });

        // Gestione del click sul pulsante di conferma del titolo della nuova pagina di note
        submitTitolo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String titolo_pagina_note = titoloPaginaNote.getText().toString();

                if (!titolo_pagina_note.isEmpty()) {
                    noteViewModel.newPaginaDiNote(titolo_pagina_note);
                }else {
                    Toast.makeText(MainActivity.this, "Inserire un titolo", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Osservazione del LiveData per il successo dell'aggiunta di una nuova pagina di note
        noteViewModel.getAddPageSuccess().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean newPageSuccess) {
                if(newPageSuccess){
                    Toast.makeText(MainActivity.this, "Pagina di note creata", Toast.LENGTH_SHORT).show();
                    titoloPaginaNote.setText("");
                    newPageDialog.dismiss();
                } else{
                    Toast.makeText(MainActivity.this, "Titolo già esistente", Toast.LENGTH_SHORT).show();
                    titoloPaginaNote.setText("");
                }
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
                Toast.makeText(MainActivity.this, "Pagina relativa alle note", Toast.LENGTH_SHORT).show();
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

    }
}