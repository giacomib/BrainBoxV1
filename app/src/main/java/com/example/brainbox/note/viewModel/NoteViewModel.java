package com.example.brainbox.note.viewModel;

import android.net.Uri;
import android.util.Log;
import android.widget.ImageView;
import android.widget.SimpleAdapter;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.brainbox.note.model.Elemento;
import com.example.brainbox.note.model.Immagine;
import com.example.brainbox.note.model.Nota_testuale;
import com.example.brainbox.note.model.PaginaDiNote;
import com.example.brainbox.note.model.checklist.Check;
import com.example.brainbox.note.model.checklist.Checklist;
import com.example.brainbox.note.view.ElementsAdapter;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
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
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * ViewModel per la gestione delle pagine di note dell'utente.
 */
public class NoteViewModel extends ViewModel {

    /** LiveData per indicare il successo dell'aggiunta di una nuova pagina di note. */
    private MutableLiveData<Boolean> addPageSuccess = new MutableLiveData<>();

    /** LiveData per indicare il successo dell'aggiunta di una nuova nota testuale. */
    private MutableLiveData<Boolean> addNotaTestualeSuccess = new MutableLiveData<>();

    /** LiveData per indicare il successo dell'aggiunta di una nuova Checklist. */
    private MutableLiveData<Boolean> addChecklistSuccess = new MutableLiveData<>();

    /** LiveData per indicare il successo dell'aggiunta di una nuova immagine. */
    private MutableLiveData<Boolean> addImageSuccess = new MutableLiveData<>();

    /** Riferimento al database Firebase. */
    private DatabaseReference mDatabase;

    /**
     * Restituisce LiveData per il successo dell'aggiunta di una nuova pagina di note.
     * @return LiveData per il successo dell'aggiunta di una nuova pagina di note.
     */
    public MutableLiveData<Boolean> getAddPageSuccess() {
        return addPageSuccess;
    }
    /**
     * Restituisce LiveData per il successo dell'aggiunta di una nota testuale.
     * @return LiveData per il successo dell'aggiunta di una nuova nota testuale.
     */
    public MutableLiveData<Boolean> getNotaTestualeSuccess() {
        return addNotaTestualeSuccess;
    }
    /**
     * Restituisce LiveData per il successo dell'aggiunta di una nuova checklist.
     * @return LiveData per il successo dell'aggiunta di una nuova checklist.
     */
    public MutableLiveData<Boolean> getChecklistSuccess() {
        return addChecklistSuccess;
    }

    /**
     * Restituisce LiveData per il successo dell'aggiunta di una nuova checklist.
     * @return LiveData per il successo dell'aggiunta di una nuova checklist.
     */
    public LiveData<Boolean> getAddImageSuccess() {
        return addImageSuccess;
    }


    // Pagine di note

    /**
     * Ottiene gli elementi in tempo reale da Firebase e aggiorna l'adapter della pagina di note.
     *
     * @param currentUserId       ID dell'utente corrente.
     * @param titolo_pagina_di_note Titolo della pagina di note.
     * @param elementiList        Lista degli elementi da aggiornare.
     * @param adapter             Adapter da notificare dei cambiamenti.
     */
    public void getRealtimeElements(String currentUserId, String titolo_pagina_di_note, List<Elemento> elementiList, ElementsAdapter adapter){
        FirebaseDatabase database = FirebaseDatabase.getInstance("https://brainbox-37414-default-rtdb.europe-west1.firebasedatabase.app/");
        DatabaseReference reference = database.getReference("users").child(currentUserId).child("Pagine di note")
                .child("Pagina di note: " + titolo_pagina_di_note).child("elementi");

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                elementiList.clear(); // Pulisce la lista prima di aggiungere nuovi dati
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    String tipologia = snapshot.child("tipologia").getValue().toString();
                    if (tipologia.equals("Nota testuale")) {
                        String titolo = snapshot.child("titolo").getValue().toString();
                        String contenuto = snapshot.child("contenuto").getValue().toString();
                        elementiList.add(new Nota_testuale(titolo, contenuto));
                    }
                    else if (tipologia.equals("Check List")) {
                        ArrayList<Check> checks = new ArrayList<>();
                        String titolo = snapshot.child("titolo").getValue().toString();
                        for (DataSnapshot checkSnapshot : snapshot.child("checks").getChildren()) {
                            String item = checkSnapshot.child("item").getValue(String.class);
                            boolean isChecked = checkSnapshot.child("checked").getValue(Boolean.class);
                            checks.add(new Check(item, isChecked));
                        }
                        elementiList.add(new Checklist(titolo, checks));
                    } else if(tipologia.equals("Immagine")) {
                        String uri = snapshot.child("uri").getValue().toString();
                        elementiList.add(new Immagine(uri));
                    }
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Gestisci possibili errori
            }
        });
    }

    /**
     * Metodo per aggiungere una nuova pagina di note.
     * Controlla se il titolo della nuova pagina di note esiste già nel database, quindi aggiunge
     * la pagina di note se il titolo è unico richiamando il metodo addPaginaDiNote
     *
     * @see #addPaginaDiNote(PaginaDiNote, String)
     * @param titolo Il titolo della nuova pagina di note.
     */
    public void newPaginaDiNote(String titolo){
        // Creazione della pagina di note
        PaginaDiNote paginaDiNote = new PaginaDiNote(titolo);
        // Ottenere l'istanza di FirebaseAuth per ottenere l'utente corrente
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        String userID = user.getUid();

        // controllo sul titolo
        mDatabase = FirebaseDatabase.getInstance("https://brainbox-37414-default-rtdb.europe-west1.firebasedatabase.app/")
                .getReference("users").child(userID).child("Pagine di note");
        List<String> pagine_di_note = new ArrayList<>();
        mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    String p = dataSnapshot.child("titolo").getValue().toString();
                    pagine_di_note.add(p);
                }
                if (pagine_di_note.contains(paginaDiNote.getTitolo())){
                    addPageSuccess.setValue(false);
                }
                else{
                    addPaginaDiNote(paginaDiNote, userID);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        });
    }

    /**
     * Metodo per aggiungere effettivamente la nuova pagina di note al database.
     * @param paginaDiNote La nuova pagina di note da aggiungere.
     * @param userID L'ID dell'utente corrente.
     */
    public void addPaginaDiNote(PaginaDiNote paginaDiNote, String userID){
        // Ottieni il riferimento al nodo dell'utente nel database Firebase
        mDatabase = FirebaseDatabase.getInstance("https://brainbox-37414-default-rtdb.europe-west1.firebasedatabase.app/")
                .getReference("users").child(userID);
        // Creazione del riferimento per la nuova pagina di note e salvataggio nel database
        mDatabase = mDatabase.child("Pagine di note").child("Pagina di note: "+ paginaDiNote.getTitolo());
        mDatabase.setValue(paginaDiNote);
        // Imposta il successo dell'operazione di aggiunta della pagina di note
        addPageSuccess.setValue(true);
    }

    /**
     * Metodo per ottenere in tempo reale le pagine di note dell'utente dal database Firebase.
     * Aggiorna l'interfaccia utente quando le pagine di note cambiano.
     * @param pagine_di_note L'ArrayList che contiene le informazioni sulle pagine di note.
     * @param userId L'ID dell'utente corrente.
     * @param adapter L'adattatore per la visualizzazione delle pagine di note.
     */
    public void getRealTimePagineDiNote(ArrayList<Map<String, String>> pagine_di_note, String userId, SimpleAdapter adapter){
        mDatabase = FirebaseDatabase.getInstance("https://brainbox-37414-default-rtdb.europe-west1.firebasedatabase.app/")
                .getReference().child("users").child(userId).child("Pagine di note");
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot datasnapshot) {
                pagine_di_note.clear();
                for(DataSnapshot snapshot : datasnapshot.getChildren()){
                    String titolo = snapshot.child("titolo").getValue().toString();
                    String data = snapshot.child("data").getValue().toString();
                    Map<String, String> map= new HashMap<String, String>(2);
                    map.put("titolo", titolo);
                    map.put("data", data);
                    pagine_di_note.add(map);
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error){}
        });
    }

    // Note testuali

    /**
     * Metodo per aggiungere una nuova nota testuale.
     * Controlla se il titolo della nuova nota testuale esiste già nel database, quindi aggiunge
     * la nota testuale se il titolo è unico richiamando il metodo {@link #addNotaTestuale(String, String, String)}.
     *
     * @param titolo Il titolo della nuova nota testuale.
     * @param contenuto Il contenuto della nuova nota testuale.
     * @param titolo_pagina_di_note Il titolo della pagina di note a cui appartiene la nota testuale.
     */
    public void newNotaTestuale(String titolo, String contenuto, String titolo_pagina_di_note){
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        String userID = user.getUid();

        // controllo sul titolo
        mDatabase = FirebaseDatabase.getInstance("https://brainbox-37414-default-rtdb.europe-west1.firebasedatabase.app/")
                .getReference("users").child(userID).child("Pagine di note").child("Pagina di note: "+titolo_pagina_di_note)
                .child("elementi");
        List<String> note_testuali = new ArrayList<>();

        mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    String p = dataSnapshot.child("titolo").getValue().toString();
                    note_testuali.add(p);
                }
                if (note_testuali.contains(titolo)){
                    addNotaTestualeSuccess.setValue(false);
                }
                else{
                    addNotaTestuale(titolo, contenuto, titolo_pagina_di_note);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        });
    }

    /**
     * Metodo per aggiungere effettivamente la nuova nota testuale al database.
     * @param titolo Il titolo della nuova nota testuale.
     * @param contenuto Il contenuto della nuova nota testuale.
     * @param titolo_pagina_di_note Il titolo della pagina di note a cui appartiene la nota testuale.
     */
    public void addNotaTestuale(String titolo, String contenuto, String titolo_pagina_di_note){
        Nota_testuale newNotaTestuale = new Nota_testuale(titolo, contenuto);

        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        String userID = user.getUid();

        mDatabase = FirebaseDatabase.getInstance("https://brainbox-37414-default-rtdb.europe-west1.firebasedatabase.app/")
                .getReference("users").child(userID).child("Pagine di note")
                .child("Pagina di note: " + titolo_pagina_di_note).child("elementi").child("Nota testuale: " + titolo);

        mDatabase.setValue(newNotaTestuale);
        addNotaTestualeSuccess.setValue(true);
    }

    // Checklist

    /**
     * Metodo per aggiungere una nuova checklist.
     * Controlla se il titolo della nuova checklist esiste già nel database, quindi elabora l'input della view
     * mediante il metodo {@link #elaborateContenutoChecklist(String)} ed aggiunge
     * la checklist se il titolo è unico richiamando il metodo {@link #addChecklist(String, ArrayList, String)}.
     *
     * @param titolo Titolo della nuova checklist.
     * @param contenuto Contenuto della nuova checklist.
     * @param titolo_pagina_di_note Titolo della pagina di note a cui appartiene la checklist.
     */
    public void newChecklist(String titolo, String contenuto, String titolo_pagina_di_note){
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        String userID = user.getUid();

        // controllo sul titolo
        mDatabase = FirebaseDatabase.getInstance("https://brainbox-37414-default-rtdb.europe-west1.firebasedatabase.app/")
                .getReference("users").child(userID).child("Pagine di note").child("Pagina di note: "+titolo_pagina_di_note)
                .child("elementi");
        List<String> elementi = new ArrayList<>();

        mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    String p = dataSnapshot.child("titolo").getValue().toString();
                    elementi.add(p);
                }
                if (elementi.contains(titolo)){
                    addChecklistSuccess.setValue(false);
                }
                else{
                    // elaborazione contenuto checklist
                    ArrayList<String> checks = elaborateContenutoChecklist(contenuto);
                    addChecklist(titolo, checks, titolo_pagina_di_note);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        });
    }

    /**
     * Elabora il contenuto della checklist, suddividendo il contenuto in elementi separati.
     *
     * @param contenuto Contenuto della checklist da elaborare.
     * @return Lista di stringhe rappresentanti gli elementi della checklist.
     */
    public ArrayList<String> elaborateContenutoChecklist(String contenuto){
        ArrayList<String> elab_contenuto = new ArrayList<>();
        String[] checks = contenuto.split("\n");
        for (String check : checks) {
            elab_contenuto.add(check);
        }

        return elab_contenuto;
    }

    /**
     * Aggiunge una nuova checklist al database Firebase.
     *
     * @param titolo Titolo della checklist.
     * @param contenuto Lista degli elementi della checklist.
     * @param titolo_pagina_di_note Titolo della pagina di note a cui appartiene la checklist.
     */
    public void addChecklist(String titolo, ArrayList<String> contenuto, String titolo_pagina_di_note){
        ArrayList<Check> checks= new ArrayList<>();
        for (String c: contenuto) {
            checks.add(new Check(c));
        }
        Checklist x = new Checklist(titolo, checks);
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        String userID = user.getUid();
        mDatabase = FirebaseDatabase.getInstance("https://brainbox-37414-default-rtdb.europe-west1.firebasedatabase.app/")
                .getReference("users").child(userID).child("Pagine di note")
                .child("Pagina di note: " + titolo_pagina_di_note).child("elementi").child("Checklist: " + titolo);

        mDatabase.setValue(x);
        addChecklistSuccess.setValue(true);
    }

    /**
     * Aggiorna lo stato di un elemento della checklist nel database Firebase.
     *
     * @param checkItem          Elemento della checklist da aggiornare.
     * @param userID             ID dell'utente corrente.
     * @param titolo_pagina_di_note  Titolo della pagina di note a cui appartiene la checklist.
     * @param titoloChecklist    Titolo della checklist.
     * @param position           Posizione dell'elemento nella checklist.
     */
    public void updateCheck(Check checkItem, String userID, String titolo_pagina_di_note, String titoloChecklist, int position){
        mDatabase = FirebaseDatabase.getInstance("https://brainbox-37414-default-rtdb.europe-west1.firebasedatabase.app/")
                .getReference("users").child(userID).child("Pagine di note")
                .child("Pagina di note: " + titolo_pagina_di_note).child("elementi").child("Checklist: "+titoloChecklist);
        String itemKey = String.valueOf(position);
        mDatabase.child("checks").child(itemKey).setValue(checkItem);
    }

    public void addImage(Uri uri, String titolo_pagina_di_note) {
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        //String userID = user.getUid();
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storagereference = storage.getReference();
        if(user != null) {
            String userId = user.getUid();
            Log.d("debug notes img", "path: " + uri.getLastPathSegment());
            StorageReference imgRef = storagereference.child("Notes/" + userId + "/" + titolo_pagina_di_note + "/" + uri.getLastPathSegment());
            imgRef.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Log.i("Notes img", "Caricamento immagine completato");
                    addImageSuccess.setValue(true);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.d("Notes img - err msg: errore durante caricamento immagine utente", Objects.requireNonNull(e.getMessage()));
                }
            });
        }
    }

    public void setupImage(Uri uri, String titolo_pagina_di_note) {

        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();

        String userID = null;
        if (user != null) {
            userID = user.getUid();
        }

        mDatabase = FirebaseDatabase.getInstance("https://brainbox-37414-default-rtdb.europe-west1.firebasedatabase.app/")
                .getReference("users").child(userID).child("Pagine di note")
                .child("Pagina di note: " + titolo_pagina_di_note).child("elementi").child("Immagine:" + uri.getLastPathSegment());

        mDatabase.setValue(new Immagine(uri.toString()));
    }

    public void scaricaMostraImmagine(ImageView imageView, String immagine, String titoloPaginaDiNote) {
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser =mAuth.getCurrentUser();
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storagereference = FirebaseStorage.getInstance().getReference();

        Uri uri = Uri.parse(immagine);

        StorageReference newRef = storagereference.child("Notes/" + currentUser.getUid() + "/" + titoloPaginaDiNote + "/" + uri.getLastPathSegment());
        newRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.get().load(uri).into(imageView);
                Log.d("Notes img", "Immagine scaricata con successo");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d("err msg: errore nello scaricare l'immagine dell'utente", Objects.requireNonNull(e.getMessage()));
            }
        });
    }

}
