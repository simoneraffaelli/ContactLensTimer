package com.raffinato.contactlensreminder.fragments;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.navigation.NavigationView;
import com.raffinato.contactlensreminder.R;
import com.raffinato.contactlensreminder.listeners.OnSettingsButtonClick;

import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.Objects;

public class BSMenuFragment extends BottomSheetDialogFragment {

    private OnSettingsButtonClick settingsButtonListener;

    public BSMenuFragment() {
    }

    public static BSMenuFragment newInstance() {
        BSMenuFragment f = new BSMenuFragment();

        return f;
    }

    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setStyle(BottomSheetDialogFragment.STYLE_NORMAL, R.style.BottomSheetDialogTheme);

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_bs_menu, container, false);

        NavigationView nv = view.findViewById(R.id.navigationView);
        nv.setVerticalScrollBarEnabled(false);
        nv.setNestedScrollingEnabled(false);
        nv.setScrollContainer(true);
        nv.getChildAt(0).setVerticalScrollBarEnabled(false);
        nv.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.translations:
                        translations();
                        break;
                    case R.id.open_settings:
                        settingsButtonListener.onSettingsClick();
                        break;
                    case R.id.rate_app:
                        launchMarket();
                        break;
                    case R.id.exit:
                        Objects.requireNonNull(getActivity()).finishAndRemoveTask();
                        break;
                }
                return true;
            }
        });

        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if (context instanceof OnSettingsButtonClick) {
            settingsButtonListener = (OnSettingsButtonClick) context;
        } else {
            //Mettere un log
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();

        settingsButtonListener = null;
    }

    private void launchMarket() {
        Uri uri = Uri.parse("market://details?id=" + Objects.requireNonNull(getActivity()).getPackageName());
        Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
        try {
            startActivity(goToMarket);
        } catch (ActivityNotFoundException e) {
            Toast.makeText(getActivity(), "couldn't launch the market", Toast.LENGTH_LONG).show();
        }
    }

    private void translations() {
        Uri uri = Uri.parse("http://lenses.simoneraffaelli.it/translations/");
        Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
        try {
            startActivity(goToMarket);
        } catch (ActivityNotFoundException e) {
            Toast.makeText(getActivity(), "couldn't launch browser", Toast.LENGTH_LONG).show();
        }
    }

}