package com.raffinato.contactlensreminder;

import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class LibraryDialogAdapter extends RecyclerView.Adapter<LibraryDialogAdapter.ViewHolder> {

    private final static String JODA_TIME = "Joda Time";
    private final static String JODA_TIME_AUTHOR = "jodastephen";
    private final static String JODA_TIME_URL = "http://www.joda.org/joda-time/";

    private final static String PULL_TO_DISMISS = "PullToDismiss";
    private final static String PULL_TO_DISMISS_AUTHOR = "fatihsokmen";
    private final static String PULL_TO_DISMISS_URL = "https://github.com/fatihsokmen/pull-to-dismiss";

    private final static String DATEPICKER_TIMELINE = "Datepicker Timeline";
    private final static String DATEPICKER_TIMELINE_AUTHOR = "badoualy";
    private final static String DATEPICKER_TIMELINE_URL = "https://github.com/badoualy/datepicker-timeline";

    private final static String SEGMENTED_CONTROL = "SegmentedButton";
    private final static String SEGMENTED_CONTROL_AUTHOR = "ceryle";
    private final static String SEGMENTED_CONTROL_URL = "https://github.com/ceryle/SegmentedButton";
    private final List<Library> mDataset;
    private OnLibraryItemClick listener;
    public LibraryDialogAdapter() {
        List<Library> libList = new ArrayList<>();
        libList.add(new Library(JODA_TIME, JODA_TIME_AUTHOR, JODA_TIME_URL));
        libList.add(new Library(PULL_TO_DISMISS, PULL_TO_DISMISS_AUTHOR, PULL_TO_DISMISS_URL));
        libList.add(new Library(DATEPICKER_TIMELINE, DATEPICKER_TIMELINE_AUTHOR, DATEPICKER_TIMELINE_URL));
        libList.add(new Library(SEGMENTED_CONTROL, SEGMENTED_CONTROL_AUTHOR, SEGMENTED_CONTROL_URL));
        this.mDataset = libList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewTipe) {
        View itemView = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.library_item_row, viewGroup, false);
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        final Library l = mDataset.get(i);
        viewHolder.card.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        listener.onClick(l);
                    }
                });

        viewHolder.title.setText(l.getName());
        viewHolder.author.setText(l.getAuthor());

    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    public void setItemOnClickListener(OnLibraryItemClick listener) {
        this.listener = listener;
    }

    public interface OnLibraryItemClick {
        void onClick(Library lib);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        final TextView title;
        final TextView author;
        final CardView card;

        ViewHolder(final View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.lib_item_title);
            author = itemView.findViewById(R.id.lib_item_author);
            card = itemView.findViewById(R.id.lib_card_background);
        }
    }

    class Library {
        final String name;
        final String author;
        final String url;

        Library(String name, String author, String url) {
            this.name = name;
            this.author = author;
            this.url = url;
        }

        String getName() {
            return name;
        }

        String getAuthor() {
            return author;
        }

        String getUrl() {
            return url;
        }


    }
}
