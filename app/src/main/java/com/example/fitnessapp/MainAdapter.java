package com.example.fitnessapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class MainAdapter extends RecyclerView.Adapter<MainAdapter.ViewHolder> {
    private ArrayList<String> fitnessHistory;

    public MainAdapter(ArrayList<String> fitnessHistory) {
        this.fitnessHistory = fitnessHistory;
    }

    @NonNull
    @Override
    public MainAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MainAdapter.ViewHolder holder, int position) {
        holder.fitnessEvent.setText(fitnessHistory.get(position));
    }

    @Override
    public int getItemCount() {
        return fitnessHistory.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView fitnessEvent;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            fitnessEvent = (TextView) itemView.findViewById(R.id.mSessionEvent);
        }
    }
}
