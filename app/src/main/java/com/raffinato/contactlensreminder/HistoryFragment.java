package com.raffinato.contactlensreminder;

import android.content.Context;
import android.content.res.ColorStateList;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.google.android.material.bottomappbar.BottomAppBar;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

public class HistoryFragment extends Fragment implements PullToDismiss.Listener {

    private ArrayList<Lens> l;
    private RecyclerView recyclerView;

    public HistoryFragment() {
    }

    public static HistoryFragment newInstance(ArrayList<Lens> l) {
        HistoryFragment f = new HistoryFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList("list", l);
        f.setArguments(bundle);

        return f;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle bundle = getArguments();
        if (bundle != null) {
            l = bundle.getParcelableArrayList("list");
        }
    }

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_history, container, false);

        PullToDismiss pdl = view.findViewById(R.id.pull_dismiss_layout);
        pdl.setListener(this);

        recyclerView = view.findViewById(R.id.recyclerview);

        recyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), LinearLayoutManager.VERTICAL));
        HistoryListAdapter adapter = new HistoryListAdapter(l);
        recyclerView.setAdapter(adapter);

        adapter.notifyDataSetChanged();

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        FloatingActionButton fab = getActivity().findViewById(R.id.fab);
        fab.setImageResource(R.drawable.ic_baseline_done_24px);
        BottomAppBar bottom_appbar = getActivity().findViewById(R.id.bottomappbar);
        bottom_appbar.replaceMenu(R.menu.empty_menu);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        ((MainActivity) getActivity()).setupBottomAppBar();
        FloatingActionButton fab = getActivity().findViewById(R.id.fab);
        fab.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorAccent, null)));
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
        return ((LinearLayoutManager) recyclerView.getLayoutManager()).findFirstCompletelyVisibleItemPosition() != 0 && !l.isEmpty();
    }

}
