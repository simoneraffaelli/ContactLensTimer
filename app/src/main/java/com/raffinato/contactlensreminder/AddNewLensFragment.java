package com.raffinato.contactlensreminder;

import android.content.Context;
import android.graphics.drawable.Animatable;
import android.os.Bundle;
import androidx.annotation.NonNull;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import androidx.appcompat.widget.AppCompatSpinner;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.core.view.ViewCompat;
import androidx.core.widget.ImageViewCompat;
import androidx.vectordrawable.graphics.drawable.AnimatedVectorDrawableCompat;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.github.badoualy.datepicker.DatePickerTimeline;
import com.raffinato.contactlensreminder.listeners.OnSaveButtonClick;

import org.joda.time.DateTime;

import java.util.Calendar;



public class AddNewLensFragment extends BottomSheetDialogFragment {

    private boolean switchClicked = false;

    private OnSaveButtonClick saveButtonListener;

    public AddNewLensFragment() {
    }

    public static AddNewLensFragment newInstance() {
        AddNewLensFragment f = new AddNewLensFragment();

        return f;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(BottomSheetDialogFragment.STYLE_NORMAL, R.style.BottomSheetDialogTheme);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_add_new_lens, container, false);

        setupAddNewLensView(view);
        configureSwitch(view);
        configureSaveButton(view);

        return view;
    }

    private void configureSaveButton(final View view) {
        Button b = view.findViewById(R.id.anl_save_btn);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveButtonListener.OnSaveButtonClick(switchClicked,view);
            }
        });
    }


    private void configureSwitch(final View view) {
        final LinearLayoutCompat leftLens = view.findViewById(R.id.anl_swc_left_lens);
        final ViewGroup rightLens = view.findViewById(R.id.anl_swc_right_lens);
        final ImageView leftImg = leftLens.findViewById(R.id.anl_swc_lens_img);
        final TextView leftTxt = leftLens.findViewById(R.id.anl_swc_lens_txt);
        final ImageView rightImg = rightLens.findViewById(R.id.anl_swc_lens_img);
        final TextView rightTxt = rightLens.findViewById(R.id.anl_swc_lens_txt);
        final ViewGroup rightBody = view.findViewById(R.id.anl_body_right_lens);
        final ViewGroup leftBody = view.findViewById(R.id.anl_body_left_lens);

        leftLens.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rightBody.setVisibility(View.GONE);
                leftBody.setVisibility(View.VISIBLE);

                leftImg.setImageDrawable(getResources().getDrawable(R.drawable.ic_left_cl_blue, null));
                leftTxt.setTextColor(getResources().getColor(R.color.colorAccent, null));
                rightImg.setImageDrawable(getResources().getDrawable(R.drawable.ic_right_cl, null));
                rightTxt.setTextColor(getResources().getColor(R.color.justBlack, null));
            }
        });
        rightLens.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rightBody.setVisibility(View.VISIBLE);
                leftBody.setVisibility(View.GONE);

                rightImg.setImageDrawable(getResources().getDrawable(R.drawable.ic_right_cl_blue, null));
                rightTxt.setTextColor(getResources().getColor(R.color.colorAccent, null));
                leftImg.setImageDrawable(getResources().getDrawable(R.drawable.ic_left_cl, null));
                leftTxt.setTextColor(getResources().getColor(R.color.justBlack, null));
            }
        });

        ImageView swc = view.findViewById(R.id.anl_swc_link);
        swc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AnimatedVectorDrawableCompat avdcRev = AnimatedVectorDrawableCompat.create(getContext(), R.drawable.avd_anim_rev);
                AnimatedVectorDrawableCompat avdc = AnimatedVectorDrawableCompat.create(getContext(), R.drawable.avd_anim);
                if(switchClicked) {
                    ((ImageView) v).setImageDrawable(avdc);
                    Animatable anim = ((Animatable)((ImageView)v).getDrawable());
                    if(anim.isRunning()){
                        //anim.stop();
                        return;
                    } else {
                      anim.start();
                    }

                    leftLens.setClickable(true);
                    rightLens.setClickable(true);
                    leftBody.setVisibility(View.GONE);
                    rightBody.setVisibility(View.VISIBLE);
                    leftImg.setImageDrawable(getResources().getDrawable(R.drawable.ic_left_cl_blue, null));
                    rightImg.setImageDrawable(getResources().getDrawable(R.drawable.ic_right_cl, null));
                    leftTxt.setTextColor(getResources().getColor(R.color.colorAccent, null));
                    rightTxt.setTextColor(getResources().getColor(R.color.justBlack, null));
                } else {
                    ((ImageView) v).setImageDrawable(avdcRev);
                    Animatable anim = ((Animatable)((ImageView)v).getDrawable());
                    if(anim.isRunning()){
                        //anim.stop();
                        return;
                    } else {
                        anim.start();
                    }

                    leftLens.setClickable(false);
                    rightLens.setClickable(false);
                    leftBody.setVisibility(View.VISIBLE);
                    rightBody.setVisibility(View.GONE);
                    leftImg.setImageDrawable(getResources().getDrawable(R.drawable.ic_left_cl_grey, null));
                    rightImg.setImageDrawable(getResources().getDrawable(R.drawable.ic_right_cl_grey, null));
                    leftTxt.setTextColor(getResources().getColor(R.color.boringGrey, null));
                    rightTxt.setTextColor(getResources().getColor(R.color.boringGrey, null));
                }
                switchClicked = !switchClicked;
            }
        });
        swc.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Toast.makeText(getActivity(), getResources().getString(R.string.anl_switch_txt), Toast.LENGTH_LONG).show();
                return true;
            }
        });
    }

    private void setupAddNewLensView(View view) {
        View leftLensSwitch = view.findViewById(R.id.anl_swc_left_lens);
        ViewGroup rightLensSwitch = view.findViewById(R.id.anl_swc_right_lens);
        ViewGroup leftLensBody = view.findViewById(R.id.anl_body_left_lens);
        ViewGroup rightLensBody = view.findViewById(R.id.anl_body_right_lens);

        ImageView lensSwcImgLx = leftLensSwitch.findViewById(R.id.anl_swc_lens_img);
        TextView lensSwcTxtLx = leftLensSwitch.findViewById(R.id.anl_swc_lens_txt);
        DatePickerTimeline datePickerLx = leftLensBody.findViewById(R.id.datepicker);
        AppCompatSpinner mySpinnerLx = leftLensBody.findViewById(R.id.spinner);

        ImageView lensSwcImgRx = rightLensSwitch.findViewById(R.id.anl_swc_lens_img);
        TextView lensSwcTxtRx = rightLensSwitch.findViewById(R.id.anl_swc_lens_txt);
        DatePickerTimeline datePickerRx = rightLensBody.findViewById(R.id.datepicker);
        AppCompatSpinner mySpinnerRx = rightLensBody.findViewById(R.id.spinner);

        setupView(true, lensSwcImgLx, lensSwcTxtLx, datePickerLx, mySpinnerLx);
        setupView(false, lensSwcImgRx, lensSwcTxtRx, datePickerRx, mySpinnerRx);
    }

    private void setupView(boolean isLeft, ImageView img, TextView txt, DatePickerTimeline date, AppCompatSpinner spin) {
        DateTime now = DateTime.now();

        img.setImageDrawable(getResources().getDrawable(isLeft ? R.drawable.ic_left_cl_blue : R.drawable.ic_right_cl, null));
        txt.setText(getResources().getString(isLeft ? R.string.anl_switch_left : R.string.anl_switch_right));
        if (isLeft) {
            txt.setTextColor(getResources().getColor(R.color.colorAccent, null));
        }
        date.getMonthView().setDefaultColor(getResources().getColor(R.color.almostBlue, null));
        date.setFirstVisibleDate(now.getYear() - 1, Calendar.JANUARY, 1);
        date.setLastVisibleDate(now.getYear() + 2, Calendar.DECEMBER, 1);
        date.setSelectedDate(now.getYear(), now.getMonthOfYear() - 1, now.getDayOfMonth());
        date.setFollowScroll(false);
        spin.setAdapter(ArrayAdapter.createFromResource(getActivity(), R.array.str_arr_spinner, R.layout.support_simple_spinner_dropdown_item));
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnSaveButtonClick) {
            saveButtonListener = (OnSaveButtonClick) context;
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        saveButtonListener = null;
    }

}
