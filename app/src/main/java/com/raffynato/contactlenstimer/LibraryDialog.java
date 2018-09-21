package com.raffynato.contactlenstimer;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class LibraryDialog extends DialogFragment {
    private RecyclerView mRecyclerView;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //inflate layout with recycler view
        View v = inflater.inflate(R.layout.fragment_library_dialog, container, false);
        mRecyclerView = v.findViewById(R.id.recycler_view);
        //setadapter
        //get your recycler view and populate it.
        return v;
    }
}
