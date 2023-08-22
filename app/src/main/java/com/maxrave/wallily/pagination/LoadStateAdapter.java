package com.maxrave.wallily.pagination;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.paging.LoadState;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.progressindicator.CircularProgressIndicator;
import com.maxrave.wallily.R;
import com.maxrave.wallily.databinding.LoadStateBinding;

import org.jetbrains.annotations.NotNull;

public class LoadStateAdapter extends androidx.paging.LoadStateAdapter<LoadStateAdapter.LoadStateViewHolder> {
    // Define Retry Callback

    public LoadStateAdapter(@NonNull View.OnClickListener retry) {
        super();
    }
    @NotNull
    @Override
    public LoadStateViewHolder onCreateViewHolder(@NotNull ViewGroup parent,
                                                  @NotNull LoadState loadState) {
        // Return new LoadStateViewHolder object
        return new LoadStateViewHolder(parent);
    }

    @Override
    public void onBindViewHolder(@NotNull LoadStateViewHolder holder,
                                 @NotNull LoadState loadState) {
        // Call Bind Method to bind visibility  of views
        holder.bind(loadState);
    }

    public static class LoadStateViewHolder extends RecyclerView.ViewHolder {
        // Define Progress bar
        private CircularProgressIndicator mProgressBar;

        LoadStateViewHolder(
                @NonNull ViewGroup parent) {
            super(LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.load_state, parent, false));
            LoadStateBinding binding = LoadStateBinding.bind(itemView);
            mProgressBar = binding.loading;
        }

        public void bind(LoadState loadState) {
            // Check load state
            if (loadState instanceof LoadState.Error) {
                // Get the error
                LoadState.Error loadStateError = (LoadState.Error) loadState;
                // Set text of Error message
                Toast.makeText(itemView.getContext(), loadStateError.getError().getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
            // set visibility of widgets based on LoadState
            mProgressBar.setVisibility(loadState instanceof LoadState.Loading
                    ? View.VISIBLE : View.GONE);
        }
    }
}
