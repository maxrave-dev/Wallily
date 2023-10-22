package com.maxrave.wallily.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.maxrave.wallily.data.db.entities.SearchHistory;
import com.maxrave.wallily.databinding.ItemHistoryBinding;

import java.util.ArrayList;

public class SearchHistoryAdapter extends RecyclerView.Adapter<SearchHistoryAdapter.ViewHolder> {

    public ArrayList<SearchHistory> searchHistoryArrayList;
    private Context context;

    public interface OnItemClickListener {
        void onItemClick(SearchHistory searchHistory);
    }
    public interface OnRemoveItemClickListener {
        void onRemoveItemClick(SearchHistory searchHistory);
    }

    private OnItemClickListener mListener;
    private OnRemoveItemClickListener mRemoveListener;
    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }
    public void setOnRemoveItemClickListener(OnRemoveItemClickListener listener) {
        mRemoveListener = listener;
    }

    public SearchHistoryAdapter(Context context, ArrayList<SearchHistory> searchHistoryArrayList) {
        this.context = context;
        this.searchHistoryArrayList = searchHistoryArrayList;
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        ItemHistoryBinding binding;
        public ViewHolder(@NonNull ItemHistoryBinding binding, OnItemClickListener listener, OnRemoveItemClickListener rListener) {
            super(binding.getRoot());
            this.binding = binding;
            mListener = listener;
            mRemoveListener = rListener;
        }
        public void bind(SearchHistory searchHistory) {
            binding.tvHistory.setText(searchHistory.getSearchHistory());
        }
    }
    @NonNull
    @Override
    public SearchHistoryAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(ItemHistoryBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false), mListener, mRemoveListener);
    }

    @Override
    public void onBindViewHolder(@NonNull SearchHistoryAdapter.ViewHolder holder, int position) {
        holder.bind(searchHistoryArrayList.get(position));
    }

    @Override
    public int getItemCount() {
        return searchHistoryArrayList.size();
    }
    public void updateList(ArrayList<SearchHistory> searchHistoryArrayList) {
        this.searchHistoryArrayList = searchHistoryArrayList;
        notifyDataSetChanged();
    }
}
