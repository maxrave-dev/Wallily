package com.maxrave.wallily.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.maxrave.wallily.R;
import com.maxrave.wallily.databinding.ItemCollectionBinding;

import java.util.ArrayList;
import com.maxrave.wallily.data.model.firebase.Collections;
import com.stfalcon.multiimageview.MultiImageView;

public class CollectionAdapter extends RecyclerView.Adapter<CollectionAdapter.ViewHolder> {
    private ArrayList<Collections> list = new ArrayList<>();

    private Context context;

    public interface OnItemClickListener {
        void onItemClick(int position);
    }
    public interface OnLongClickListener {
        void onLongClick(int position);
    }

    private OnItemClickListener listener;

    private OnLongClickListener longClickListener;

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }
    public void setOnLongClickListener(OnLongClickListener longClickListener) {
        this.longClickListener = longClickListener;
    }

    public CollectionAdapter(Context context, ArrayList<Collections> list) {
        this.context = context;
        this.list = list;
    }
    public void updateList(ArrayList<Collections> list) {
        this.list = list;
        notifyDataSetChanged();
    }
    class ViewHolder extends RecyclerView.ViewHolder {
        ItemCollectionBinding binding;

        OnItemClickListener listener;
        OnLongClickListener longClickListener;
        public ViewHolder(ItemCollectionBinding binding, OnItemClickListener listener, OnLongClickListener longClickListener) {
            super(binding.getRoot());
            this.binding = binding;
            this.listener = listener;
            this.longClickListener = longClickListener;
            binding.getRoot().setOnClickListener(v -> {
                if (listener != null) {
                    int position = getBindingAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        listener.onItemClick(position);
                    }
                }
            });
            binding.getRoot().setOnLongClickListener(v -> {
                if (longClickListener != null) {
                    int position = getBindingAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        longClickListener.onLongClick(position);
                    }
                }
                return true;
            });
        }

        public void bind(com.maxrave.wallily.data.model.firebase.Collections collections) {
            binding.tvCollectionName.setText(collections.getName());
            if (collections.getListHitsId() != null && collections.getListHitsId().size() > 0) {
                binding.tvHitsCount.setText(context.getString(R.string.hits_count, String.valueOf(collections.getListHitsId().size())));
                Glide.with(context).asBitmap().load(collections.getListHitsId().get(0).getThumbnailUrl()).into(new CustomTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                        binding.ivCollection.setImageBitmap(resource);
                    }

                    @Override
                    public void onLoadCleared(@Nullable Drawable placeholder) {

                    }
                });
            }
            else {
                binding.tvHitsCount.setText(context.getString(R.string.hits_count, "0"));
            }
        }
    }

    @NonNull
    @Override
    public CollectionAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(ItemCollectionBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false), listener, longClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull CollectionAdapter.ViewHolder holder, int position) {
        holder.bind(list.get(position));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}
