package com.maxrave.wallily.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.maxrave.wallily.databinding.ItemAddToCollectionBinding;
import com.maxrave.wallily.databinding.ItemCollectionBinding;

import java.util.ArrayList;
import com.maxrave.wallily.data.model.firebase.Collections;
public class AddToCollectionAdapter extends RecyclerView.Adapter<AddToCollectionAdapter.ViewHolder> {

    private ArrayList<Collections> collections = new ArrayList<>();

    public AddToCollectionAdapter(ArrayList<Collections> collections) {
        this.collections = collections;
    }

    public void updateList(ArrayList<Collections> collections) {
        this.collections = collections;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(ItemAddToCollectionBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false), listener);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(collections.get(position));
    }

    @Override
    public int getItemCount() {
        return collections.size();
    }

    public interface OnItemClickListener {
        void onItemClick(int position);
    }
    private OnItemClickListener listener;
    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }
    class ViewHolder extends RecyclerView.ViewHolder {
        ItemAddToCollectionBinding binding;
        OnItemClickListener listener;
        public ViewHolder(ItemAddToCollectionBinding binding, OnItemClickListener listener) {
            super(binding.getRoot());
            this.binding = binding;
            this.listener = listener;
            binding.getRoot().setOnClickListener(v -> {
                if (listener != null) {
                    int position = getBindingAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        listener.onItemClick(position);
                    }
                }
            });
        }
        void bind(Collections collection) {
            binding.tvCollectionName.setText(collection.getName());
        }
    }
}
