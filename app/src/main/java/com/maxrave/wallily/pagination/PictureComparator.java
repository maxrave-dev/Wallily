package com.maxrave.wallily.pagination;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;

import com.maxrave.wallily.data.model.pixabayResponse.Hit;
import com.maxrave.wallily.data.model.pixabayResponse.PixabayResponse;

public class PictureComparator extends DiffUtil.ItemCallback<Hit> {
    @Override
    public boolean areItemsTheSame(@NonNull Hit oldItem, @NonNull Hit newItem) {
        return oldItem.getId() == newItem.getId();
    }

    @Override
    public boolean areContentsTheSame(@NonNull Hit oldItem, @NonNull Hit newItem) {
        return oldItem.getId() == (newItem.getId());
    }
}