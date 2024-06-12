package com.example.brainbox.note.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.brainbox.R;
import com.example.brainbox.note.model.Immagine;
import com.example.brainbox.note.model.checklist.Checklist;
import com.example.brainbox.note.model.Elemento;
import com.example.brainbox.note.model.Nota_testuale;
import com.example.brainbox.note.viewModel.NoteViewModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Adapter per visualizzare una lista di elementi in una pagina di note.
 * Supporta tutti i diversi tipi di elementi, ad ora: Nota testuale e Check List.
 */
public class ElementsAdapter extends BaseAdapter implements Filterable {

    /**
     * Il contesto dell'applicazione.
     */
    private Context context;
    /**
     * La lista di elementi da visualizzare.
     */
    private List<Elemento> elementi;
    /**
     * L'inflater per creare le viste dagli XML di layout.
     */
    private LayoutInflater inflater;
    /**
     * L'ID dell'utente corrente.
     */
    private String currentUserId;
    /**
     * Il titolo della pagina di note.
     */
    private String titoloPaginaDiNote;

    /**
     * Riferimento al viewmodel
     */
    private NoteViewModel noteViewModel;

    /**
     * Costruttore dell'adapter.
     *
     * @param context Il contesto dell'applicazione.
     * @param elementi La lista di elementi da visualizzare.
     * @param currentUserId L'ID dell'utente corrente.
     * @param titoloPaginaDiNote Il titolo della pagina di note.
     */
    public ElementsAdapter(Context context, List<Elemento> elementi, String currentUserId, String titoloPaginaDiNote) {
        this.context = context;
        this.elementi = elementi;
        this.inflater = LayoutInflater.from(context);
        this.currentUserId = currentUserId;
        this.titoloPaginaDiNote = titoloPaginaDiNote;
    }

    /**
     * Restituisce il numero di elementi nella lista.
     *
     * @return Numero di elementi.
     */
    @Override
    public int getCount() {
        return elementi.size();
    }

    /**
     * Restituisce l'elemento alla posizione specificata.
     *
     * @param position Posizione dell'elemento.
     * @return Elemento alla posizione specificata.
     */
    @Override
    public Object getItem(int position) {
        return elementi.get(position);
    }

    /**
     * Restituisce l'ID dell'elemento alla posizione specificata.
     *
     * @param position Posizione dell'elemento.
     * @return ID dell'elemento.
     */
    @Override
    public long getItemId(int position) {
        return position;
    }

    /**
     * Restituisce la tipologia dell'elemento alla posizione specificata.
     *
     * @param position Posizione dell'elemento.
     * @return Tipologia dell'elemento.
     */
    public String getTipologiaItem(int position){
        return elementi.get(position).getTipologia();
    }

    /**
     * Restituisce il tipo di vista per l'elemento alla posizione specificata.
     *
     * @param position Posizione dell'elemento.
     * @return Tipo di vista.
     */
    @Override
    public int getItemViewType(int position) {
        String tipo = getTipologiaItem(position);
        if (tipo.equals("Nota testuale")) {
            return 0; // Tipo 0 per Nota testuale
        } else if (tipo.equals("Check List")) {
            return 1; // Tipo 1 per Check List
        } else if (tipo.equals("Immagine")) {
            return 2; // Tipo 2 per Immagine
        }
        return -1; // Caso di fallback, non dovrebbe succedere
    }

    /**
     * Restituisce il numero di tipi di viste supportati dall'adapter.
     *
     * @return Il numero di tipi di viste.
     */
    @Override
    public int getViewTypeCount() {
        return 3;
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

        int viewType = getItemViewType(position);
        noteViewModel = new NoteViewModel();

        if(convertView == null){
            if(viewType==0){
                convertView = inflater.inflate(R.layout.nota_testuale_item, parent, false);
            } else if (viewType==1) {
                convertView = inflater.inflate(R.layout.checklist_item, parent, false);
            } else if (viewType==2) {
                convertView = inflater.inflate(R.layout.image_item, parent, false);
            }
        }
        if (viewType==0){
            TextView textView = convertView.findViewById(R.id.nota_testuale_titolo);
            textView.setText(((Nota_testuale)elementi.get(position)).getTitolo());
            TextView textView1 = convertView.findViewById(R.id.nota_testuale_contenuto);
            textView1.setText(((Nota_testuale)elementi.get(position)).getContenuto());
        } else if (viewType==1) {
            TextView textView = convertView.findViewById(R.id.checklist_titolo);
            ListView listView = convertView.findViewById(R.id.checklist_items);

            Checklist checklist = (Checklist) elementi.get(position);
            textView.setText(checklist.getTitolo());

            ChecklistItemAdapter checklistAdapter = new ChecklistItemAdapter(context, checklist.getChecks(), currentUserId, titoloPaginaDiNote, checklist.getTitolo());
            listView.setAdapter(checklistAdapter);
            Utility.setListViewHeightBasedOnItems(listView);
        } else if(viewType == 2) {
            ImageView imageview = convertView.findViewById(R.id.image_content);
            noteViewModel.scaricaMostraImmagine(imageview, ((Immagine) (elementi.get(position) ) ).getUri(), titoloPaginaDiNote);

            // Per evitare che immagini troppo grandi
            // rendano il formato della card sproporzionato
            imageview.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
        }
        return convertView;
    }

    /**
     * Restituisce un filtro per filtrare gli elementi in base a un criterio di ricerca.
     * Il filtro può essere applicato su elementi di tipo {@link Nota_testuale} e {@link Checklist}.
     *
     * @return un filtro che può essere utilizzato per filtrare gli elementi in base a un criterio di ricerca.
     */
    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults results = new FilterResults();
                List<Elemento> filteredList = new ArrayList<>();
                if (constraint == null || constraint.length() == 0){
                    filteredList.addAll(elementi);
                    results.values = filteredList;
                    results.count = filteredList.size();
                } else{
                    String filterPattern = constraint.toString().toLowerCase().trim();
                    for (Elemento item : elementi) {
                        if (item instanceof Nota_testuale) {
                            if (((Nota_testuale) item).getTitolo().toLowerCase().contains(filterPattern) ||
                                    ((Nota_testuale) item).getContenuto().toLowerCase().contains(filterPattern)) {
                                filteredList.add(item);
                            }
                        } else if (item instanceof Checklist) {
                            if (((Checklist) item).getTitolo().toLowerCase().contains(filterPattern)) {
                                filteredList.add(item);
                            }
                        }
                    }
                    results.values = filteredList;
                    results.count = filteredList.size();
                }
                return results;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                elementi.clear();
                elementi.addAll((List) results.values);
                notifyDataSetChanged();
            }
        };
    }
}
