package com.raffynato.contactlenstimer;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.chip.Chip;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.raffynato.contactlenstimer.listeners.OnChipClick;

import org.joda.time.format.DateTimeFormat;

import java.util.List;

public class HomeFragment extends Fragment {

    private static final String LXLENSID = "lxLensId";
    private static final String RXLENSID = "rxLensId";

    private static final int LEFTLENS = 0;
    private static final int RIGHTLENS = 1;

    private OnChipClick chipListener;

    public HomeFragment() {
    }

    public static HomeFragment newInstance(List<Lens> lenses) {
        HomeFragment f = new HomeFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable(LXLENSID, lenses.isEmpty() ? null : lenses.get(0));
        bundle.putParcelable(RXLENSID, lenses.isEmpty() ? null : lenses.get(1));
        f.setArguments(bundle);

        return f;
    }

    private Lens[] lensArray = {null, null};

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle bundle = getArguments();
        if (bundle != null) {
            lensArray[0] = bundle.getParcelable(LXLENSID);
            lensArray[1] = bundle.getParcelable(RXLENSID);
        }

    }

    private void setUpLensDesc(View lensLayout, int side) {
        ((TextView)lensLayout.findViewById(R.id.lens_side)).setText(side == LEFTLENS ? "Left" : "Right");
        ((TextView)lensLayout.findViewById(R.id.lens_countdown)).setText(
                lensArray[side] == null ? "--" : String.valueOf(lensArray[side].getRemainingTime().getDays() > 0 ? lensArray[side].getRemainingTime().getDays() : 0 ));
        ((TextView)lensLayout.findViewById(R.id.lens_deadline)).setText(
                lensArray[side] == null ? "----" : lensArray[side].getExpDate().toString(DateTimeFormat.forPattern("dd/MM/yyyy")));
        ((BarChart)lensLayout.findViewById(R.id.progress_bar)).setMaxValue(
                lensArray[side] == null ? 1 : lensArray[side].getDuration().getTime());
        ((BarChart)lensLayout.findViewById(R.id.progress_bar)).setValue(
                lensArray[side] == null ? 1 : lensArray[side].getDuration().getTime()-lensArray[side].getRemainingTime().getDays());
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        LinearLayout leftLens = view.findViewById(R.id.left_lens);
        LinearLayout rightLens  = view.findViewById(R.id.right_lens);
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

    }

    @Override
    public void onDetach() {
        super.onDetach();
        chipListener = null;
    }

    public void updateLenses(List<Lens> list) {
        lensArray[0] = list.get(0);
        lensArray[1] = list.get(1);
    }
}
