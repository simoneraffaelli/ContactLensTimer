package com.raffinato.contactlensreminder;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class LibraryDialog extends DialogFragment {

    public LibraryDialog() {
    }

    public static LibraryDialog newInstance() {

        Bundle args = new Bundle();

        LibraryDialog fragment = new LibraryDialog();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //inflate layout with recycler view
        View v = inflater.inflate(R.layout.fragment_library_dialog, container, false);
        RecyclerView mRecyclerView = v.findViewById(R.id.recycler_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(layoutManager);
        LibraryDialogAdapter adapter = new LibraryDialogAdapter();
        mRecyclerView.setAdapter(adapter);
        adapter.setItemOnClickListener((MainActivity) getActivity());

        getDialog().getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        //get your recycler view and populate it.
        return v;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        return super.onCreateDialog(savedInstanceState);
    }
}
