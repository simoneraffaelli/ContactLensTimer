package com.raffinato.contactlensreminder;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.raffinato.contactlensreminder.listeners.OnCaseButtonsClick;

public class AddLensesInCaseFragment extends BottomSheetDialogFragment {

    private final static String LENSESREMAINING = "lensesremaining";
    private int lensesRemaining;

    private OnCaseButtonsClick buttonsListener;

    public AddLensesInCaseFragment() {
    }

    public static AddLensesInCaseFragment newInstance(int lensesRemianing) {
        AddLensesInCaseFragment f = new AddLensesInCaseFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(LENSESREMAINING, lensesRemianing);
        f.setArguments(bundle);

        return f;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(BottomSheetDialogFragment.STYLE_NORMAL, R.style.BottomSheetDialogTheme);

        Bundle bundle = getArguments();
        if (bundle != null) {
            this.lensesRemaining = bundle.getInt(LENSESREMAINING);
        }

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_add_lenses_in_case, container, false);

        configureButtons(view);
        configureTxt(view);

        return view;
    }

    private void configureTxt(View view) {
        EditText txt = view.findViewById(R.id.case_lenses_remaining);
        txt.setText(String.valueOf(this.lensesRemaining));
    }

    private void configureButtons(final View view) {
        Button bs = view.findViewById(R.id.case_save);
        Button br = view.findViewById(R.id.case_reset);
        bs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buttonsListener.onSaveClick(((EditText) view.findViewById(R.id.case_lenses_remaining)).getText().toString());
            }
        });
        br.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((EditText) view.findViewById(R.id.case_lenses_remaining)).setText("0");
            }
        });
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnCaseButtonsClick) {
            buttonsListener = (OnCaseButtonsClick) context;
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        buttonsListener = null;
    }
}
