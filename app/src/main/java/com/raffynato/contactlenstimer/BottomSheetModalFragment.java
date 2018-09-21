package com.raffynato.contactlenstimer;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.design.widget.NavigationView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

public class BottomSheetModalFragment extends BottomSheetDialogFragment {

    public BottomSheetModalFragment() { }

    public static BottomSheetModalFragment newInstance() {
        BottomSheetModalFragment f = new BottomSheetModalFragment();

        return f;
    }

    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setStyle(BottomSheetDialogFragment.STYLE_NORMAL, R.style.BottomSheetDialogTheme);

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_bottom_sheet, container, false);

        NavigationView nv = view.findViewById(R.id.navigationView);
        nv.setVerticalScrollBarEnabled(false);
        nv.setNestedScrollingEnabled(false);
        nv.setScrollContainer(true);
        nv.getChildAt(0).setVerticalScrollBarEnabled(false);
        nv.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch(menuItem.getItemId()) {
                    case R.id.dev:
                        Toast.makeText(getActivity(), "Raffaelli Simone", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.version:
                        Toast.makeText(getActivity(), BuildConfig.VERSION_NAME, Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.lib_used:
                        Toast.makeText(getActivity(), "Library", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.rate_app:
                        launchMarket();
                        break;
                    case R.id.exit:
                        getActivity().finishAndRemoveTask();
                }
                return true;
            }
        });

        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    private void launchMarket() {
        Uri uri = Uri.parse("market://details?id=" + getActivity().getPackageName());
        Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
        try {
            startActivity(goToMarket);
        } catch (ActivityNotFoundException e) {
            Toast.makeText(getActivity(), "couldn't launch the market", Toast.LENGTH_LONG).show();
        }
    }

}