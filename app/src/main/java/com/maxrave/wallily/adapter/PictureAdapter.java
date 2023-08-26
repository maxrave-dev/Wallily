package com.maxrave.wallily.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.paging.PagingDataAdapter;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.maxrave.wallily.data.model.pixabayResponse.Hit;
import com.maxrave.wallily.databinding.ItemPictureBinding;
import com.squareup.picasso.Picasso;

import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import coil.Coil;
import coil.ImageLoader;

public class PictureAdapter extends PagingDataAdapter<Hit, PictureAdapter.PictureViewHolder> {
    private final OnItemClickListener listener;
    public PictureAdapter(@NonNull DiffUtil.ItemCallback<Hit> diffCallback, OnItemClickListener listener) {
        super(diffCallback);
        this.listener = listener;
    }
    public interface OnItemClickListener {
        void onItemClick(Hit item);
    }

    @NonNull
    @Override
    public PictureViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new PictureViewHolder(ItemPictureBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull PictureViewHolder holder, int position) {
        ItemPictureBinding binding = ItemPictureBinding.bind(holder.itemView);
        Hit hit = getItem(position);
        assert hit != null;
        Picasso.get().load(hit.getLargeImageURL()).into(binding.ivPicture);
        Pattern pattern = Pattern.compile("/([^/]+)/$");
        Matcher matcher = pattern.matcher(Objects.requireNonNull(hit.getPageURL()));

        String name = matcher.find() ? matcher.group(1) : "";
        if (name != null && name.contains("-")) {
            name = name.replace("-", " ").replaceAll("\\d", "");

        }
        binding.tvPictureTitle.setText(name);

        binding.getRoot().setOnClickListener(v -> {
            listener.onItemClick(hit);
        });
    }

    class PictureViewHolder extends RecyclerView.ViewHolder {
        public PictureViewHolder(ItemPictureBinding binding) {
            super(binding.getRoot());
        }
    }
}
