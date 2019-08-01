package com.raffinato.contactlensreminder;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;

public class SettingsFragment extends PreferenceFragmentCompat {

    private final static String JODA_TIME_AUTHOR = "jodastephen";
    private final static String JODA_TIME_URL = "http://www.joda.org/joda-time/";

    private final static String PULL_TO_DISMISS_AUTHOR = "fatihsokmen";
    private final static String PULL_TO_DISMISS_URL = "https://github.com/fatihsokmen/pull-to-dismiss";

    private final static String DATEPICKER_TIMELINE_AUTHOR = "badoualy";
    private final static String DATEPICKER_TIMELINE_URL = "https://github.com/badoualy/datepicker-timeline";

    private final static String CL_LOGO_AUTHOR = "Freepik";
    private final static String CL_LOGO_URL= "https://www.flaticon.com/free-icon/contact-lens_507109#term=contact%20lens&page=1&position=2";

    private final static String CL_BOX_AUTHOR= "Eucalyp";
    private final static String CL_BOX_URL = "https://www.flaticon.com/free-icon/contact-lens_588535#term=contact%20lens&page=1&position=28";

    private final static String CL_NOTIF_AUTHOR = "Olena Panasovska";
    private final static String CL_NOTIF_URL = "https://thenounproject.com/term/contact-lens/2717921/";

    private final static String SINGLE_CL_AUTHOR = "Linseed Studio";
    private final static String SINGLE_CL_URL = "https://thenounproject.com/term/contact-lens/2469770/";

    private final static String COUNTRICONS_AUTHOR = "countricons";
    private final static String COUNTRICONS_URL = "https://www.flaticon.com/packs/countricons";

    public SettingsFragment() {
    }

    public static SettingsFragment newInstance() {
        SettingsFragment f = new SettingsFragment();

        return f;
    }

    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ((MainActivity)getActivity()).toggleBottomAppBar(View.GONE);
    }

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        addPreferencesFromResource(R.xml.app_pref);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = super.onCreateView(inflater, container, savedInstanceState);

        setCreditItemListener(JODA_TIME_AUTHOR, JODA_TIME_URL);
        setCreditItemListener(DATEPICKER_TIMELINE_AUTHOR, DATEPICKER_TIMELINE_URL);
        setCreditItemListener(PULL_TO_DISMISS_AUTHOR, PULL_TO_DISMISS_URL);

        setCreditItemListener(CL_LOGO_AUTHOR, CL_LOGO_URL);
        setCreditItemListener(SINGLE_CL_AUTHOR, SINGLE_CL_URL);
        setCreditItemListener(CL_NOTIF_AUTHOR, CL_NOTIF_URL);
        setCreditItemListener(CL_BOX_AUTHOR, CL_BOX_URL);
        setCreditItemListener(COUNTRICONS_AUTHOR, COUNTRICONS_URL);

        return v;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        ((MainActivity)getActivity()).toggleBottomAppBar(View.VISIBLE);
    }

    private void setCreditItemListener(final String author, final String url) {
        findPreference(author).setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                Uri uri = Uri.parse(url);
                Intent u = new Intent(Intent.ACTION_VIEW, uri);
                try {
                    startActivity(u);
                } catch (ActivityNotFoundException e) {
                    Toast.makeText(getContext(), "couldn't launch browser", Toast.LENGTH_LONG).show();
                }
                return true;
            }
        });

    }
}
