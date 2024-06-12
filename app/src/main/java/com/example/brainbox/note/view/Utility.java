package com.example.brainbox.note.view;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;

/**
 * Classe di utilità che contiene metodi ausiliari per le la visualizzazione degli elementi delle checklist su ListView.
 */
public class Utility {

    /**
     * Imposta l'altezza di un ListView basata sugli elementi contenuti al suo interno.
     * Questo metodo misura ciascun elemento della ListView e imposta l'altezza totale della ListView
     * in modo che tutti gli elementi siano visibili senza scorrere.
     *
     * @param listView ListView di cui si vuole impostare l'altezza.
     */
    public static void setListViewHeightBasedOnItems(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            return;
        }

        int totalHeight = 0;
        int desiredWidth = View.MeasureSpec.makeMeasureSpec(listView.getWidth(), View.MeasureSpec.UNSPECIFIED);
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() + 15));
        listView.setLayoutParams(params);
        listView.requestLayout();
    }
}

