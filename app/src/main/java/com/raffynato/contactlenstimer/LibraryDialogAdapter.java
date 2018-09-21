package com.raffynato.contactlenstimer;

import android.support.annotation.NonNull;
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
    private final static String PULL_TO_DISMISS_URL= "https://github.com/fatihsokmen/pull-to-dismiss";

    private final static String DATEPICKER_TIMELINE = "Datepicker Timeline";
    private final static String DATEPICKER_TIMELINE_AUTHOR = "badoualy";
    private final static String DATEPICKER_TIMELINE_URL = "https://github.com/badoualy/datepicker-timeline";

    private final static String SEGMENTED_CONTROL = "Android Segmented Control";
    private final static String SEGMENTED_CONTROL_AUTHOR = "Kaopiz";
    private final static String SEGMENTED_CONTROL_URL = "https://github.com/Kaopiz/android-segmented-control";

    private List<Library> mDataset;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView title;
        public TextView author;

        ViewHolder(View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.lib_item_title);
            author = itemView.findViewById(R.id.lib_item_author);
        }
    }

    public LibraryDialogAdapter(){
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
                .inflate(R.layout.history_item_row, viewGroup, false);

        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        Library l = mDataset.get(i);
        viewHolder.title.setText(l.getName());
        viewHolder.author.setText(l.getAuthor());

    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    private class Library {
        String getName() {
            return name;
        }

        String getAuthor() {
            return author;
        }

        String getUrl() {
            return url;
        }

        final String name;
        final String author;
        final String url;

        Library(String name, String author, String url) {
            this.name = name;
            this.author = author;
            this.url = url;
        }


    }
}
