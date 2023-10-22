package com.maxrave.wallily.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.maxrave.wallily.data.model.pixabayResponse.Hit;
import com.maxrave.wallily.databinding.ItemPictureBinding;

import java.util.ArrayList;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class HitAdapter extends RecyclerView.Adapter<HitAdapter.ViewHolder> {
    private ArrayList<Hit> hits = new ArrayList<>();

    private Context context;

    private OnItemClickListener listener;
    private OnLongClickListener longClickListener;

    public HitAdapter(ArrayList<Hit> hits, Context context) {
        this.hits = hits;
        this.context = context;
    }
    public void updateList(ArrayList<Hit> hits) {
        this.hits = hits;
        notifyDataSetChanged();
    }
    public interface OnItemClickListener {
        void onItemClick(Hit item);
    }

    public interface OnLongClickListener {
        void onLongClick(Hit item);
    }

    public void setOnClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }
    public void setOnLongClickListener(OnLongClickListener longClickListener) {
        this.longClickListener = longClickListener;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(ItemPictureBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false), listener, longClickListener);
    }

    @Override
    public int getItemCount() {
        return hits.size();
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(hits.get(position));
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        ItemPictureBinding binding;

        OnItemClickListener listener;

        OnLongClickListener longClickListener;

        public ViewHolder(ItemPictureBinding binding, OnItemClickListener listener, OnLongClickListener longClickListener) {
            super(binding.getRoot());
            this.binding = binding;
            this.listener = listener;
            this.longClickListener = longClickListener;
            binding.getRoot().setOnClickListener(v -> {
                if (listener != null) {
                    int position = getBindingAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        listener.onItemClick(hits.get(position));
                    }
                }
            });
            binding.getRoot().setOnLongClickListener(v -> {
                if (longClickListener != null) {
                    int position = getBindingAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        longClickListener.onLongClick(hits.get(position));
                    }
                }
                return true;
            });
        }

        public void bind(Hit hit) {
            Glide.with(context).asBitmap().
                    load(hit.getWebformatURL())
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(new CustomTarget<Bitmap>() {
                        @Override
                        public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                            binding.ivPicture.setImageBitmap(resource);
                        }

                        @Override
                        public void onLoadCleared(@Nullable Drawable placeholder) {

                        }
                    });
            Pattern pattern = Pattern.compile("/([^/]+)/$");
            Matcher matcher = pattern.matcher(Objects.requireNonNull(hit.getPageURL()));

            String name = matcher.find() ? matcher.group(1) : "";
            if (name != null && name.contains("-")) {
                name = name.replace("-", " ").replaceAll("\\d", "");

            }
            binding.tvPictureTitle.setText(name);
        }

    }
}
