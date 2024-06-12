package com.example.brainbox.note.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import com.example.brainbox.R;
import com.example.brainbox.note.model.checklist.Check;
import com.example.brainbox.note.viewModel.NoteViewModel;

import java.util.List;

/**
 * Adapter per visualizzare gli elementi di una Check List.
 */
public class ChecklistItemAdapter extends ArrayAdapter<Check> {
    /**
     * Il contesto dell'applicazione.
     */
    private Context context;
    /**
     * La lista di elementi della Check List.
     */
    private List<Check> items;
    /**
     * L'ID dell'utente corrente.
     */
    private String currentUserId;
    /**
     * Il titolo della pagina di note.
     */
    private String titoloPaginaDiNote;
    /**
     * Il titolo della Check List.
     */
    private String titolo;

    /**
     * Costruttore dell'adapter.
     *
     * @param context Contesto dell'applicazione.
     * @param items Lista di elementi della Check List.
     * @param currentUserId ID dell'utente corrente.
     * @param titoloPaginaDiNote Titolo della pagina di note.
     * @param titolo Titolo della Check List.
     */
    public ChecklistItemAdapter(Context context, List<Check> items, String currentUserId, String titoloPaginaDiNote, String titolo) {
        super(context, 0, items);
        this.context = context;
        this.items = items;
        this.currentUserId = currentUserId;
        this.titoloPaginaDiNote = titoloPaginaDiNote;
        this.titolo = titolo;
    }

    /**
     * Restituisce la vista per l'elemento alla posizione specificata.
     *
     * @param position Posizione dell'elemento.
     * @param convertView Vista riutilizzabile.
     * @param parent Gruppo di viste a cui la nuova vista sarà aggiunta.
     * @return Vista per l'elemento alla posizione specificata.
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.checklist_item_element, parent, false);
        }

        Check checkItem = getItem(position);
        CheckBox checkBox = convertView.findViewById(R.id.checklist_item_checkbox);
        TextView textView = convertView.findViewById(R.id.checklist_item_text);

        textView.setText(checkItem.getItem());
        checkBox.setChecked(checkItem.isChecked());

        checkBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            checkItem.setChecked(isChecked);
            NoteViewModel noteViewModel = new NoteViewModel();
            noteViewModel.updateCheck(checkItem, currentUserId, titoloPaginaDiNote, titolo, position);
        });

        return convertView;
    }
}

