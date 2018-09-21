package com.raffynato.contactlenstimer;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.joda.time.format.DateTimeFormat;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {

    private List<LensesInUse> mDataset;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView titleLx;
        public TextView titleRx;

        ViewHolder(View itemView) {
            super(itemView);
            titleLx = itemView.findViewById(R.id.item_row_text1);
            titleRx = itemView.findViewById(R.id.item_row_text2);
        }
    }

    public MyAdapter(ArrayList<Lens> ll) {
        if(ll != null) {
            List<LensesInUse> liul = new ArrayList<>();
            ListIterator<Lens> li = ll.listIterator();
            while (li.hasNext()) {
                liul.add(new LensesInUse(li.next(), li.next()));
            }
            this.mDataset = liul;
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.history_item_row, parent, false);

        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        LensesInUse liu = mDataset.get(position);
        holder.titleLx.setText(liu.getLxLens().getExpDate().toString(DateTimeFormat.forPattern("dd/MM/yyyy")));
        holder.titleRx.setText(liu.getRxLens().getExpDate().toString(DateTimeFormat.forPattern("dd/MM/yyyy")));
    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }
}
