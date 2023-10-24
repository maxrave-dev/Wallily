package com.maxrave.wallily.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.paging.PagingDataAdapter;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.target.Target;
import com.bumptech.glide.request.transition.Transition;
import com.maxrave.wallily.R;
import com.maxrave.wallily.data.model.pixabayResponse.Hit;
import com.maxrave.wallily.databinding.ItemPictureBinding;
import com.squareup.picasso.Picasso;

import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.inject.Inject;

import coil.Coil;
import coil.ImageLoader;
import coil.request.ImageRequest;

public class PictureAdapter extends PagingDataAdapter<Hit, PictureAdapter.PictureViewHolder> {
    private final OnItemClickListener listener;
    private final Context context;
    public PictureAdapter(@NonNull DiffUtil.ItemCallback<Hit> diffCallback, OnItemClickListener listener, Context context) {
        super(diffCallback);
        this.listener = listener;
        this.context = context;
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
