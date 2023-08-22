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

public class PictureAdapter extends PagingDataAdapter<Hit, PictureAdapter.PictureViewHolder> {
    public PictureAdapter(@NonNull DiffUtil.ItemCallback<Hit> diffCallback) {
        super(diffCallback);
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
        String url = hit.getPageURL();
        String keyword = "photos/";

        int index = url.lastIndexOf(keyword);
        if (index != -1) {
            String extractedString = url.substring(index + keyword.length());
            int lastIndex = extractedString.lastIndexOf("-");
            if (lastIndex != -1) {
                String result = extractedString.substring(0, lastIndex);
                binding.tvPictureTitle.setText(result.replace("-", " "));
                System.out.println("Result: " + result);
            } else {
                System.out.println("No hyphen found.");
            }
            System.out.println("Extracted: " + extractedString);
        } else {
            System.out.println("Keyword not found.");
        }
    }

    class PictureViewHolder extends RecyclerView.ViewHolder {
        public PictureViewHolder(ItemPictureBinding binding) {
            super(binding.getRoot());
        }
    }
}
