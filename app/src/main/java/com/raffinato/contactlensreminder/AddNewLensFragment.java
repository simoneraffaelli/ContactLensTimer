package com.raffinato.contactlensreminder;

import android.content.Context;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatSpinner;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.Toast;

import com.github.badoualy.datepicker.DatePickerTimeline;

import org.joda.time.DateTime;

import java.util.Calendar;

import co.ceryle.segmentedbutton.SegmentedButtonGroup;


public class AddNewLensFragment extends Fragment implements PullToDismiss.Listener {

    public AddNewLensFragment() {
    }

    public static AddNewLensFragment newInstance() {
        AddNewLensFragment fragment = new AddNewLensFragment();

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_add_new_lens, container, false);

        PullToDismiss pdl = view.findViewById(R.id.sliding_layout);
        pdl.setListener(this);

        setupRadioGroupListener(view);
        setupAddNewLensView(view);

        return view;
    }


    private void setupRadioGroupListener(final View view) {
        final SegmentedButtonGroup segmentedButtonGroup = view.findViewById(R.id.segmentedcontrols);
        segmentedButtonGroup.setOnClickedButtonListener(new SegmentedButtonGroup.OnClickedButtonListener() {
            @Override
            public void onClickedButton(int position) {
                switch(position) {
                    case 0: view.findViewById(R.id.add_new_left_lens).setVisibility(View.VISIBLE);
                            view.findViewById(R.id.add_new_right_lens).setVisibility(View.GONE);
                            break;
                    case 1: view.findViewById(R.id.add_new_left_lens).setVisibility(View.GONE);
                            view.findViewById(R.id.add_new_right_lens).setVisibility(View.VISIBLE);
                            break;
                    default:
                        Toast.makeText(getContext(), "An error occurred :(", Toast.LENGTH_SHORT).show();
                }
            }
        });

        Switch s = view.findViewById(R.id.anl_switch);
        s.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if(isChecked) {
                    view.findViewById(R.id.add_new_left_lens).setVisibility(View.VISIBLE);
                    view.findViewById(R.id.add_new_right_lens).setVisibility(View.GONE);
                    segmentedButtonGroup.setEnabled(false);
                } else {
                    segmentedButtonGroup.setEnabled(true);
                }
            }
        });
    }

    private void setupAddNewLensView(View view) {
        DateTime now = DateTime.now();
        DatePickerTimeline datePickerLx = view.findViewById(R.id.datepicker_lx);
        AppCompatSpinner mySpinnerLx = view.findViewById(R.id.spinner_lx);
        DatePickerTimeline datePickerRx = view.findViewById(R.id.datepicker_rx);
        AppCompatSpinner mySpinnerRx = view.findViewById(R.id.spinner_rx);
        datePickerLx.getMonthView().setDefaultColor(getResources().getColor(R.color.almostBlue));
        datePickerLx.setFirstVisibleDate(now.getYear() - 1, Calendar.JANUARY, 1);
        datePickerLx.setLastVisibleDate(now.getYear() + 2, Calendar.DECEMBER, 1);
        datePickerLx.setSelectedDate(now.getYear(), now.getMonthOfYear() - 1, now.getDayOfMonth());
        datePickerLx.setFollowScroll(false);

        mySpinnerLx.setAdapter(new ArrayAdapter<>(getActivity(), R.layout.support_simple_spinner_dropdown_item, Lens.Duration.values()));
        datePickerRx.getMonthView().setDefaultColor(getResources().getColor(R.color.almostBlue));
        datePickerRx.setFirstVisibleDate(now.getYear() - 1, Calendar.JANUARY, 1);
        datePickerRx.setLastVisibleDate(now.getYear() + 2, Calendar.DECEMBER, 1);
        datePickerRx.setSelectedDate(now.getYear(), now.getMonthOfYear() - 1, now.getDayOfMonth());
        datePickerRx.setFollowScroll(false);
        mySpinnerRx.setAdapter(new ArrayAdapter<>(getActivity(), R.layout.support_simple_spinner_dropdown_item, Lens.Duration.values()));
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        FloatingActionButton fab = getActivity().findViewById(R.id.fab);
        fab.setImageResource(R.drawable.ic_baseline_done_24px);
    }

    public void onDestroy() {
        super.onDestroy();

        ((MainActivity) getActivity()).setupBottomAppBar();
        FloatingActionButton fab = getActivity().findViewById(R.id.fab);
        fab.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorAccent)));
        fab.setImageResource(R.drawable.ic_baseline_add_24px);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onDismissed() {
        ((MainActivity) getActivity()).dismissFragment();
    }

    @Override
    public boolean onShouldInterceptTouchEvent() {
        return false;
    }
}
