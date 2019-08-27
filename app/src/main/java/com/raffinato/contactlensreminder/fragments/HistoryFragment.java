package com.raffinato.contactlensreminder.fragments;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.raffinato.contactlensreminder.utility.HistoryListAdapter;
import com.raffinato.contactlensreminder.utility.classes.Lens;
import com.raffinato.contactlensreminder.MainActivity;
import com.raffinato.contactlensreminder.utility.PullToDismiss;
import com.raffinato.contactlensreminder.R;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.Objects;

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
        recyclerView.addItemDecoration(new DividerItemDecoration(Objects.requireNonNull(getActivity()), LinearLayoutManager.VERTICAL));
        HistoryListAdapter adapter = new HistoryListAdapter(l);
        recyclerView.setAdapter(adapter);

        adapter.notifyDataSetChanged();

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        ((MainActivity) Objects.requireNonNull(getActivity())).toggleBottomAppBar(View.VISIBLE);
    }

    @Override
    public void onDismissed() {
        ((MainActivity) Objects.requireNonNull(getActivity())).dismissFragment();
    }

    @Override
    public boolean onShouldInterceptTouchEvent() {
        return ((LinearLayoutManager) Objects.requireNonNull(recyclerView.getLayoutManager())).findFirstCompletelyVisibleItemPosition() != 0 && !l.isEmpty();
    }

}
