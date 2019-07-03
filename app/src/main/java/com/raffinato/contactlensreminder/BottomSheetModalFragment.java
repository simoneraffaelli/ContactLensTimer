package com.raffinato.contactlensreminder;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.navigation.NavigationView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

public class BottomSheetModalFragment extends BottomSheetDialogFragment {

    public BottomSheetModalFragment() {
    }

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
                switch (menuItem.getItemId()) {
                    case R.id.translations:
                        translations();
                        break;
                    case R.id.dev:
                        mySite();
                        break;
                    case R.id.version:
                        Toast.makeText(getActivity(), BuildConfig.VERSION_NAME, Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.lib_used:
                        LibraryDialog d = LibraryDialog.newInstance();
                        d.show(getActivity().getSupportFragmentManager(), null);
                        break;
                    case R.id.rate_app:
                        launchMarket();
                        break;
                    case R.id.exit:
                        getActivity().finishAndRemoveTask();
                        break;
                    /*case R.id.test_notification:
                        DateTime date = new DateTime();
                        date = date.plusMinutes(1).withSecondOfMinute(0);
                        Toast.makeText(getActivity(), "Alarm Scheduled At: " + date.toString(DateTimeFormat.forPattern("dd/MM - hh:mm:ss")), Toast.LENGTH_SHORT).show();
                        NotificationScheduler.testNotifications(getActivity(), AlarmReceiver.class, date);
                        break;
                        */
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

    private void mySite() {
        Uri uri = Uri.parse("https://www.simoneraffaelli.it");
        Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
        try {
            startActivity(goToMarket);
        } catch (ActivityNotFoundException e) {
            Toast.makeText(getActivity(), "couldn't launch browser", Toast.LENGTH_LONG).show();
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