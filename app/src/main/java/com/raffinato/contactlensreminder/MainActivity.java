package com.raffinato.contactlensreminder;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;

import com.github.badoualy.datepicker.DatePickerTimeline;
import com.google.android.material.bottomappbar.BottomAppBar;

import androidx.appcompat.widget.AppCompatSpinner;
import androidx.transition.Slide;
import androidx.transition.Transition;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.appcompat.app.AppCompatActivity;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import com.raffinato.contactlensreminder.database.DatabaseManager;
import com.raffinato.contactlensreminder.listeners.OnAppBarButtonClick;
import com.raffinato.contactlensreminder.listeners.OnCaseButtonsClick;
import com.raffinato.contactlensreminder.listeners.OnCaseClick;
import com.raffinato.contactlensreminder.listeners.OnChipClick;
import com.raffinato.contactlensreminder.listeners.OnSaveButtonClick;
import com.raffinato.contactlensreminder.listeners.PullToFragmentDismiss;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements OnAppBarButtonClick, PullToFragmentDismiss, OnChipClick, LibraryDialogAdapter.OnLibraryItemClick, OnSaveButtonClick, OnCaseClick, OnCaseButtonsClick {

    private List<Lens> lenses;
    private DatabaseManager dbManager;

    public static final String SP_LENSESINCASE = "splensesincase";
    public static final String SP_LENSESINCASE_K1 = "lensesremaining";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        dbManager = new DatabaseManager(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.getSupportActionBar().setDisplayShowCustomEnabled(true);
        this.getSupportActionBar().setCustomView(LayoutInflater.from(this).inflate(R.layout.actionbar_custom, null));
        this.getSupportActionBar().setElevation(0);

        if (savedInstanceState == null) {
            lenses = dbManager.getLenses();
            addFragment(HomeFragment.newInstance(lenses), false);

            setupBottomAppBar();
            findViewById(R.id.fab).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onButtonClick();
                }
            });

        }
    }


    private void animateTransition(Fragment fragment, FragmentManager manager) {
        Transition t1 = new Slide();
        fragment.setEnterTransition(t1);
//        Fragment exitFrag = manager.findFragmentById(R.id.fragment_container);
//        if(exitFrag != null) {
//            Transition t2 = new Fade();
//            exitFrag.setExitTransition(t2);
//        }
    }

    private void addFragment(final Fragment fragment, boolean back) {
        FragmentManager manager = getSupportFragmentManager();
        animateTransition(fragment, manager);
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.add(R.id.fragment_container, fragment);
        if (back) {
            transaction.addToBackStack(null);
        }
        transaction.commit();
    }

    private void replaceFragment(Fragment fragment, boolean back) {
        FragmentManager manager = getSupportFragmentManager();
        Fragment f = manager.findFragmentById(R.id.fragment_container);
        FragmentTransaction transaction = manager.beginTransaction();
        //animateTransition(fragment, manager);
        transaction.replace(R.id.fragment_container, fragment, fragment.getClass().getSimpleName());
        if (back) {
            transaction.addToBackStack(fragment.getClass().getSimpleName());
        }
        transaction.commit();
    }

    private void onMenuClick() {
        BottomSheetModalFragment f = BottomSheetModalFragment.newInstance();
        f.show(getSupportFragmentManager(), f.getTag());
    }

    private void onHistoryClick() {
        ArrayList<Lens> lenses = dbManager.getHistory();

        HistoryFragment f = HistoryFragment.newInstance(lenses);
        this.addFragment(f, true);
    }

    /*
    @Override
    public void onSettingClick() {
        SettingsFragment f = SettingsFragment.newInstance();
        this.addFragment(f, true);
    }
    */

    private void onButtonClick() {
        FragmentManager manager = getSupportFragmentManager();
        Fragment fragment = manager.findFragmentById(R.id.fragment_container);
        if (fragment instanceof HomeFragment) {
            AddNewLensFragment f = AddNewLensFragment.newInstance();
            f.show(manager, "BS_ANL");
        }
    }

    @Override
    public void onRefreshClick() {
        HomeFragment f = (HomeFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_container);
        lenses.clear();
        dbManager.deactivateLenses();
        List<Lens> tmp = dbManager.getLenses();
        if (!tmp.isEmpty()) {
            lenses.add(new Lens(tmp.get(0).getDuration(), new DateTime()));
            lenses.add(new Lens(tmp.get(1).getDuration(), new DateTime()));
            dbManager.addLenses(new LensesInUse(lenses.get(0), lenses.get(1)));
            f.updateLenses(lenses);
            getSupportFragmentManager().beginTransaction().detach(f).attach(f).commitNowAllowingStateLoss();
            setupNotifications(lenses);
        }
    }

    @Override
    public void onDeleteClick() {
        HomeFragment f = (HomeFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_container);
        lenses.clear();
        lenses.add(null);
        lenses.add(null);
        dbManager.deactivateLenses();
        f.updateLenses(lenses);
        getSupportFragmentManager().beginTransaction().detach(f).attach(f).commitNowAllowingStateLoss();
        NotificationScheduler.cancelReminders(this, AlarmReceiver.class);
    }

    private void setupNotifications(List<Lens> lenses) {
        NotificationScheduler.cancelReminders(this,AlarmReceiver.class);

        if (lenses.get(0).getExpDate().isEqual(lenses.get(1).getExpDate())) {
            NotificationScheduler.setReminder(this, AlarmReceiver.class, lenses.get(0).getExpDate().getDayOfYear(), new DateTime().withMinuteOfHour(0).getMinuteOfHour(), new DateTime().withHourOfDay(20).getHourOfDay(), 0);
            //NotificationScheduler.setReminder(this, AlarmReceiver.class, new DateTime().getDayOfYear(), new DateTime().plusMinutes(1).getMinuteOfHour(), new DateTime().getHourOfDay(), 0);
        } else {
            NotificationScheduler.setReminder(this, AlarmReceiver.class, lenses.get(0).getExpDate().getDayOfYear(), new DateTime().withMinuteOfHour(0).getMinuteOfHour(), new DateTime().withHourOfDay(20).getHourOfDay(), NotificationHelper.LX_LENS_NOT_ID);
            NotificationScheduler.setReminder(this, AlarmReceiver.class, lenses.get(1).getExpDate().getDayOfYear(), new DateTime().withMinuteOfHour(0).getMinuteOfHour(), new DateTime().withHourOfDay(20).getHourOfDay(), NotificationHelper.RX_LENS_NOT_ID);
            //NotificationScheduler.setReminder(this, AlarmReceiver.class, new DateTime().getDayOfYear(), new DateTime().plusMinutes(1).getMinuteOfHour(), new DateTime().getHourOfDay(), NotificationHelper.RX_LENS_NOT_ID);
            //NotificationScheduler.setReminder(this, AlarmReceiver.class, new DateTime().getDayOfYear(), new DateTime().plusMinutes(2).getMinuteOfHour(), new DateTime().getHourOfDay(), NotificationHelper.LX_LENS_NOT_ID);

        }
    }

    public void dismissFragment() {
        FragmentManager manager = getSupportFragmentManager();
        manager.popBackStackImmediate();
    }

    public void setupBottomAppBar() {
        BottomAppBar bottom_appbar = findViewById(R.id.bottomappbar);
        bottom_appbar.replaceMenu(R.menu.bottom_appbar_menu);

        bottom_appbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onMenuClick();
            }
        });
        /*
        bottom_appbar.findViewById(R.id.app_bar_setting).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onSettingClick();
            }
        });
        */
        bottom_appbar.findViewById(R.id.app_bar_history).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onHistoryClick();
            }
        });
    }

    @Override
    public void onClick(LibraryDialogAdapter.Library lib) {
        Uri uri = Uri.parse(lib.getUrl());
        Intent goToUrl = new Intent(Intent.ACTION_VIEW, uri);
        try {
            startActivity(goToUrl);
        } catch (ActivityNotFoundException e) {
            Toast.makeText(this, "couldn't launch the browser", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void OnSaveButtonClick(boolean switchClicked, View view) {
        lenses.clear();

        View body = view.findViewById(R.id.anl_body);
        View leftLensBody = body.findViewById(R.id.anl_body_left_lens);
        View rightLensBody = body.findViewById(R.id.anl_body_right_lens);

        DatePickerTimeline datePickerLx = leftLensBody.findViewById(R.id.datepicker);
        AppCompatSpinner mySpinnerLx = leftLensBody.findViewById(R.id.spinner);

        DatePickerTimeline datePickerRx = rightLensBody.findViewById(R.id.datepicker);
        AppCompatSpinner mySpinnerRx = rightLensBody.findViewById(R.id.spinner);

        DateTime lxDate = new DateTime(DateTime.parse(datePickerLx.getSelectedDay() + "/" + (datePickerLx.getSelectedMonth() + 1) + "/" + datePickerLx.getSelectedYear(), DateTimeFormat.forPattern("dd/MM/yyyy")));
        Lens.Duration lxDuration = Lens.Duration.fromSpinnerSelection(mySpinnerLx.getSelectedItemPosition());
        lenses.add(0, new Lens(lxDuration, lxDate));
        if(switchClicked) {
            lenses.add(new Lens(lxDuration, lxDate));
        } else {
            DateTime rxDate = new DateTime(DateTime.parse(datePickerRx.getSelectedDay() + "/" + (datePickerRx.getSelectedMonth() + 1) + "/" + datePickerRx.getSelectedYear(), DateTimeFormat.forPattern("dd/MM/yyyy")));
            Lens.Duration rxDuration = Lens.Duration.fromSpinnerSelection(mySpinnerRx.getSelectedItemPosition());
            lenses.add(new Lens(rxDuration, rxDate));
        }
        dbManager.deactivateLenses();
        dbManager.addLenses(new LensesInUse(lenses.get(0), lenses.get(1)));

        setupNotifications(lenses);

        AddNewLensFragment f = ((AddNewLensFragment)getSupportFragmentManager().findFragmentByTag("BS_ANL"));
        f.dismiss();
        replaceFragment(HomeFragment.newInstance(lenses), false);
    }

    @Override
    public void onClick(int lensesRemianing) {
        FragmentManager manager = getSupportFragmentManager();
        AddLensesInCaseFragment f = AddLensesInCaseFragment.newInstance(lensesRemianing);
        f.show(manager, "BS_HF_CASE");
    }

    @Override
    public void onSaveClick(String s) {
        SharedPreferences.Editor editor = getSharedPreferences(SP_LENSESINCASE, MODE_PRIVATE).edit();
        editor.putInt(SP_LENSESINCASE_K1, Integer.parseInt(s));
        editor.apply();

        AddLensesInCaseFragment f = ((AddLensesInCaseFragment)getSupportFragmentManager().findFragmentByTag("BS_HF_CASE"));
        f.dismiss();
        replaceFragment(HomeFragment.newInstance(lenses), false);
    }
}
