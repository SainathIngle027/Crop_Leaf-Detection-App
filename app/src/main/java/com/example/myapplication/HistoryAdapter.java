package com.example.myapplication;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.squareup.picasso.Picasso;
import java.util.ArrayList;
import java.util.List;

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.HistoryViewHolder> {

    private List<HistoryItem> historyList;

    public HistoryAdapter() {
        this.historyList = new ArrayList<>();
    }

    public void addHistoryItem(HistoryItem historyItem) {
        historyList.add(historyItem);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public HistoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.history_item, parent, false);
        return new HistoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HistoryViewHolder holder, int position) {
        HistoryItem historyItem = historyList.get(position);

        // Use Picasso to load image from URL
        Picasso.get().load(historyItem.getImageUrl()).into(holder.imageView);

        holder.resultTextView.setText(historyItem.getResult());
    }

    @Override
    public int getItemCount() {
        return historyList.size();
    }

    public static class HistoryViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView resultTextView;

        public HistoryViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageUrlImageView);
            resultTextView = itemView.findViewById(R.id.resultTextView);
        }
    }
}
