package com.example.brainbox.note.unitTests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.mockito.Mockito.*;

import androidx.annotation.NonNull;
import androidx.test.core.app.ActivityScenario;

import com.example.brainbox.autenticazione.model.User;
import com.example.brainbox.note.model.PaginaDiNote;
import com.example.brainbox.note.model.checklist.Check;
import com.example.brainbox.note.view.Pagina_di_note;
import com.example.brainbox.note.viewModel.NoteViewModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;



/**
 * Questa classe contiene i casi di test unitari per le funzionalità di gestione delle checklist della classe NoteViewModel.
 * I casi di test includono la verifica della creazione di nuove checklist, del loro contenuto e della gestione dei titoli duplicati.
 */
public class ChecklistUnitTest {

    /**
     * Scenario dell'attività per l'esecuzione dei test.
     */
    ActivityScenario<Pagina_di_note> scenario;

    /**
     * Metodo di inizializzazione eseguito prima dell'esecuzione di ogni caso di test.
     * Viene utilizzato per impostare l'ambiente di test e i dati di esempio nel database Firebase.
     *
     * @throws InterruptedException se si verifica un'interruzione durante l'attesa delle operazioni asincrone.
     */
    @Before
    public void setUpTest() throws InterruptedException {
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        CountDownLatch latch = new CountDownLatch(1);

        mAuth.createUserWithEmailAndPassword("unitTest@gmail.com", "password123")
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser user = mAuth.getCurrentUser();
                        if (user != null) {
                            String userId = user.getUid();
                            DatabaseReference mDatabase = FirebaseDatabase.getInstance("https://brainbox-37414-default-rtdb.europe-west1.firebasedatabase.app/").getReference();
                            User newUser = new User("unitTest", "unitTest@gmail.com");
                            mDatabase.child("users").child(userId).setValue(newUser).addOnCompleteListener(dbTask -> {
                                latch.countDown();
                            });
                            PaginaDiNote pagina = new PaginaDiNote("Pagina 1");
                            mDatabase.child("users").child(userId).child("Pagine di note")
                                    .child("Pagina di note: " + pagina.getTitolo()).setValue(pagina).addOnCompleteListener(dbTask -> {
                                latch.countDown();
                            });

                        }
                    } else {
                        latch.countDown();
                    }
                });

        if (!latch.await(10, TimeUnit.SECONDS)) {
            throw new RuntimeException("Setup timeout");
        }
    }

    /**
     * Metodo di pulizia eseguito dopo l'esecuzione di ogni caso di test.
     * Viene utilizzato per ripristinare lo stato dell'ambiente di test e pulire i dati di esempio dal database Firebase.
     *
     * @throws InterruptedException se si verifica un'interruzione durante l'attesa delle operazioni asincrone.
     */
    @After
    public void clearTest() throws InterruptedException {
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        CountDownLatch latch = new CountDownLatch(2);

        mAuth.signInWithEmailAndPassword("unitTest@gmail.com", "password123")
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser user = mAuth.getCurrentUser();
                        if (user != null) {
                            String userId = user.getUid();

                            // Cancellazione dell'utente
                            user.delete().addOnCompleteListener(deleteTask -> {
                                if (deleteTask.isSuccessful()) {
                                    latch.countDown();
                                } else {
                                    latch.countDown(); // Anche se la cancellazione fallisce, sblocchiamo il latch
                                }
                            });

                            // Cancellazione dei dati nel database
                            DatabaseReference mDatabase = FirebaseDatabase.getInstance("https://brainbox-37414-default-rtdb.europe-west1.firebasedatabase.app/").getReference();
                            mDatabase.child("users").child(userId).setValue(null).addOnCompleteListener(dbTask -> {
                                if (dbTask.isSuccessful()) {
                                    latch.countDown();
                                } else {
                                    latch.countDown(); // Anche se la cancellazione fallisce, sblocchiamo il latch
                                }
                            });

                            // Cancellazione esplicita del nodo "Pagine di note"
                            mDatabase.child("users").child(userId).child("Pagine di note").setValue(null).addOnCompleteListener(noteTask -> {
                                if (noteTask.isSuccessful()) {
                                    latch.countDown();
                                } else {
                                    latch.countDown(); // Anche se la cancellazione fallisce, sblocchiamo il latch
                                }
                            });
                        } else {
                            latch.countDown();
                        }
                    } else {
                        latch.countDown();
                    }
                });

        if (!latch.await(10, TimeUnit.SECONDS)) {
            throw new RuntimeException("Clear test timeout");
        }
    }

    /**
     * Test unitario per verificare la creazione di una nuova checklist nel database Firebase.
     * Viene verificato se il titolo della checklist corrisponde a quello specificato e se i suoi elementi sono correttamente memorizzati.
     */
    @Test
    public void newChecklistTitleUnitTest(){
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        mAuth.signInWithEmailAndPassword("unitTest@gmail.com", "password123");
        scenario = ActivityScenario.launch(Pagina_di_note.class);
        NoteViewModel noteViewModel = new NoteViewModel();
        noteViewModel.newChecklist("Checklist 1", "contenuto\nchecklist", "Pagina 1");

        FirebaseUser user = mAuth.getCurrentUser();
        String userID = user.getUid();
        DatabaseReference mDatabase = FirebaseDatabase.getInstance("https://brainbox-37414-default-rtdb.europe-west1.firebasedatabase.app/")
                .getReference("users").child(userID).child("Pagine di note").child("Pagina di note: Pagina 1")
                .child("elementi");

        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists() && snapshot.hasChildren()) {
                    DataSnapshot firstElementSnapshot = snapshot.getChildren().iterator().next();
                    if (firstElementSnapshot.getKey().equals("Checklist: Checklist 1")) {
                        String titolo_checklist = firstElementSnapshot.child("titolo").getValue(String.class);
                        assertEquals("Checklist 1", titolo_checklist);
                    } else {
                    }
                } else {
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        });
    }

    /**
     * Test unitario per verificare la creazione di una nuova checklist nel database Firebase, controllando il contenuto dei suoi elementi.
     * Viene verificato se il contenuto della checklist corrisponde a quello specificato e se gli elementi sono correttamente memorizzati.
     */
    @Test
    public void newCheckListContentUnitTest(){
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        mAuth.signInWithEmailAndPassword("unitTest@gmail.com", "password123");
        scenario = ActivityScenario.launch(Pagina_di_note.class);
        NoteViewModel noteViewModel = new NoteViewModel();
        noteViewModel.newChecklist("Checklist 1", "contenuto\nchecklist", "Pagina 1");

        FirebaseUser user = mAuth.getCurrentUser();
        String userID = user.getUid();
        DatabaseReference mDatabase = FirebaseDatabase.getInstance("https://brainbox-37414-default-rtdb.europe-west1.firebasedatabase.app/")
                .getReference("users").child(userID).child("Pagine di note").child("Pagina di note: Pagina 1")
                .child("elementi");

        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists() && snapshot.hasChildren()) {
                    DataSnapshot firstElementSnapshot = snapshot.getChildren().iterator().next();
                    if (firstElementSnapshot.getKey().equals("Checklist: Checklist 1")) {
                        ArrayList<Check> checks = new ArrayList<>();
                        for (DataSnapshot checkSnapshot : firstElementSnapshot.child("checks").getChildren()) {
                            Check check = checkSnapshot.getValue(Check.class);
                            checks.add(check);
                        }
                        assertEquals("contenuto", checks.get(0).getItem());
                        assertFalse(checks.get(0).isChecked());
                        assertEquals("checklist", checks.get(1).getItem());
                        assertFalse(checks.get(1).isChecked());
                    } else {
                    }
                } else {
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        });
    }

    /**
     * Test unitario per verificare la gestione dei titoli duplicati durante la creazione di una nuova checklist nel database Firebase.
     * Viene verificato se il sistema rileva correttamente il titolo già esistente e non crea una nuova checklist duplicata.
     */
    @Test
    public void titoloNonDisponibileChecklistTest(){
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        mAuth.signInWithEmailAndPassword("unitTest@gmail.com", "password123");
        scenario = ActivityScenario.launch(Pagina_di_note.class);
        NoteViewModel noteViewModel = new NoteViewModel();
        noteViewModel.newChecklist("Checklist 1", "contenuto\nchecklist", "Pagina 1");
        noteViewModel.newChecklist("Checklist 1", "altro\ncontenuto\nchecklist", "Pagina 1");

        FirebaseUser user = mAuth.getCurrentUser();
        String userID = user.getUid();
        DatabaseReference mDatabase = FirebaseDatabase.getInstance("https://brainbox-37414-default-rtdb.europe-west1.firebasedatabase.app/")
                .getReference("users").child(userID).child("Pagine di note").child("Pagina di note: Pagina 1")
                .child("elementi");


        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists() && snapshot.hasChildren()) {
                    long numberOfCheckLists = snapshot.getChildrenCount();
                    assertEquals(1, numberOfCheckLists);
                } else {
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        });

    }

    /**
     * Test unitario per verificare l'elaborazione del contenuto di una checklist.
     * Viene verificato se il contenuto della checklist viene correttamente elaborato in una lista di stringhe separate.
     */
    @Test
    public void elaborateContenutoChecklistTest(){
        NoteViewModel noteViewModel = new NoteViewModel();
        String contenuto_da_elaborare = "questo\ncontenuto\nè\nda\nelaborare";
        ArrayList<String> contenuto_elaborato = noteViewModel.elaborateContenutoChecklist(contenuto_da_elaborare);

        assertEquals(5, contenuto_elaborato.size());

        assertEquals("questo", contenuto_elaborato.get(0));
        assertEquals("contenuto", contenuto_elaborato.get(1));
        assertEquals("è", contenuto_elaborato.get(2));
        assertEquals("da", contenuto_elaborato.get(3));
        assertEquals("elaborare", contenuto_elaborato.get(4));
    }

}
