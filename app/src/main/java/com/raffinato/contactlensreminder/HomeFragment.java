package com.raffinato.contactlensreminder;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.drawable.Animatable;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.google.android.material.chip.Chip;
import androidx.fragment.app.Fragment;
import androidx.transition.AutoTransition;
import androidx.transition.ChangeBounds;
import androidx.transition.Fade;
import androidx.transition.TransitionManager;
import androidx.transition.TransitionSet;
import androidx.vectordrawable.graphics.drawable.AnimatedVectorDrawableCompat;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.raffinato.contactlensreminder.listeners.OnCaseClick;
import com.raffinato.contactlensreminder.listeners.OnChipClick;

import org.joda.time.format.DateTimeFormat;

import java.util.List;

import static android.content.Context.MODE_PRIVATE;

public class HomeFragment extends Fragment {

    private static final String LXLENSID = "lxLensId";
    private static final String RXLENSID = "rxLensId";

    private static final int LEFTLENS = 0;
    private static final int RIGHTLENS = 1;
    private final Lens[] lensArray = {null, null};
    private int duration;

    private OnChipClick chipListener;
    private OnCaseClick onCaseCickListener;

    public HomeFragment() {
    }

    public static HomeFragment newInstance(List<Lens> lenses) {
        HomeFragment f = new HomeFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable(LXLENSID, lenses.isEmpty() ? null : lenses.get(0).isActive() ? lenses.get(0) : null);
        bundle.putParcelable(RXLENSID, lenses.isEmpty() ? null : lenses.get(1).isActive() ? lenses.get(1) : null);
        bundle.putInt("Duration", lenses.isEmpty() ? 1 : lenses.get(0).getDuration().getTime());
        f.setArguments(bundle);

        return f;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle bundle = getArguments();
        if (bundle != null) {
            lensArray[0] = bundle.getParcelable(LXLENSID);
            lensArray[1] = bundle.getParcelable(RXLENSID);
            duration = bundle.getInt("Duration", 1);
        }

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        LinearLayout leftLens = view.findViewById(R.id.left_lens);
        LinearLayout rightLens = view.findViewById(R.id.right_lens);
        setUpLensDesc(leftLens, LEFTLENS);
        setUpLensDesc(rightLens, RIGHTLENS);

        Chip refreshChip = view.findViewById(R.id.chip_refresh);
        Chip deleteChip = view.findViewById(R.id.chip_delete);
        refreshChip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chipListener.onRefreshClick();
            }
        });
        deleteChip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chipListener.onDeleteClick();
            }
        });

        setupLensCaseTracker(view);

        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnChipClick) {
            chipListener = (OnChipClick) context;
        } else {
            //Mettere un log
        }
        if (context instanceof OnCaseClick) {
            onCaseCickListener = (OnCaseClick) context;
        } else {
            //Mettere un log
        }

    }

    @Override
    public void onDetach() {
        super.onDetach();
        chipListener = null;
        onCaseCickListener = null;
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d("YYY", "ONRESUME");
    }

    private void setUpLensDesc(View lensLayout, int side) {
        Lens lens = lensArray[side];

        ((TextView) lensLayout.findViewById(R.id.lens_side)).setText(side == LEFTLENS ? getResources().getString(R.string.home_left_side) : getResources().getString(R.string.home_right_side));
        ((TextView) lensLayout.findViewById(R.id.lens_countdown)).setText(
                lens == null ? "--" : String.valueOf(lens.getRemainingTime().getDays() > 0 ? lens.getRemainingTime().getDays() : 0));
        ((TextView) lensLayout.findViewById(R.id.lens_deadline)).setText(
                lens == null ? "----" : lens.getExpDate().toString(DateTimeFormat.forPattern("dd/MM/yyyy")));
        ((BarChart) lensLayout.findViewById(R.id.progress_bar)).setMaxValue(
                lens == null ? 1 : lens.getDuration().getTime());
        ((BarChart) lensLayout.findViewById(R.id.progress_bar)).setValue(
                lens == null ? 1 : lens.getDuration().getTime() - lens.getRemainingTime().getDays());

        if (lens != null && lensArray[side].getRemainingTime().getDays() <= 0) {
            ((BarChart) lensLayout.findViewById(R.id.progress_bar)).setExpiredColor();
            ((ImageView) lensLayout.findViewById(R.id.lens_drwbl)).setImageDrawable(getActivity().getDrawable(R.drawable.ic_contact_lens_red));
        }
    }

    private void setupLensCaseTracker(View view) {
        setupLensCaseView(view);

    }

    private void setupLensCaseView(View view) {
        final ImageView img = view.findViewById(R.id.hf_expand);
        final LinearLayout caseContainer = view.findViewById(R.id.hf_case);
        final LinearLayout lensContainer = view.findViewById(R.id.hf_hcontainer);
        final LinearLayout fragContainer = view.findViewById(R.id.hf_vcontainer);
        final ImageView caseImg = view.findViewById(R.id.hf_case_img);
        final AnimatedVectorDrawableCompat avdc = AnimatedVectorDrawableCompat.create(getContext(), R.drawable.avd_exp_coll);
        final AnimatedVectorDrawableCompat avdcRev = AnimatedVectorDrawableCompat.create(getContext(), R.drawable.avd_coll_exp);
        final TransitionSet tSet = new TransitionSet()
                .addTransition(new ChangeBounds()
                        .addTarget(lensContainer)
                        .setInterpolator(new AccelerateDecelerateInterpolator())
                        .setDuration(300))
                .addTransition(new Fade()
                        .addTarget(caseContainer)
                        .setInterpolator(new AccelerateDecelerateInterpolator())
                        .setDuration(350))
                .addTransition(new AutoTransition()
                        .addTarget(R.id.chip_container)
                        .addTarget(R.id.hf_expand)
                        .addTarget(R.id.hf_ntf_container)
                        .setInterpolator(new AccelerateDecelerateInterpolator())
                        .setDuration(200));
        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (caseContainer.getVisibility() == View.GONE) {
                    img.setImageDrawable(avdc);
                    Animatable anim = ((Animatable) ((ImageView) v).getDrawable());
                    if (!anim.isRunning()) {
                        anim.start();

                        TransitionManager.beginDelayedTransition(fragContainer, tSet);
                        caseContainer.setVisibility(View.VISIBLE);
                    }
                } else {
                    img.setImageDrawable(avdcRev);
                    Animatable anim = ((Animatable) ((ImageView) v).getDrawable());
                    if (!anim.isRunning()) {
                        anim.start();

                        TransitionManager.beginDelayedTransition(fragContainer, tSet);
                        caseContainer.setVisibility(View.GONE);
                    }
                }

            }
        });
        SharedPreferences pref = getContext().getSharedPreferences(MainActivity.SP_LENSESINCASE, MODE_PRIVATE);
        final int lensesRemianing = pref.getInt(MainActivity.SP_LENSESINCASE_K1, 0);
        TextView tv1 = view.findViewById(R.id.hf_case_lenses_left);
        TextView tv2 = view.findViewById(R.id.hf_case_lenses_time_left);
        tv1.setText(getResources().getString(R.string.hf_case_lenses_left).replace("@1", String.valueOf(lensesRemianing / 2)).replace("@2", String.valueOf(lensesRemianing)));
        tv2.setText(getResources().getString(R.string.hf_case_lenses_time_left).replace("@1", String.valueOf((lensesRemianing / 2) * duration)) );
        caseImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onCaseCickListener.onClick(lensesRemianing);
            }
        });

    }

    public void updateLenses(List<Lens> list) {
        lensArray[0] = list.get(0);
        lensArray[1] = list.get(1);
    }
}
