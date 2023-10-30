package com.maxrave.wallily.ui;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.maxrave.wallily.R;
import com.maxrave.wallily.adapter.CollectionAdapter;
import com.maxrave.wallily.adapter.HitAdapter;
import com.maxrave.wallily.data.model.firebase.Collections;
import com.maxrave.wallily.data.model.pixabayResponse.Hit;
import com.maxrave.wallily.databinding.BottomSheetRemoveFromCollectionBinding;
import com.maxrave.wallily.databinding.FragmentDetailCollectionBinding;
import com.maxrave.wallily.viewModel.SharedViewModel;

import java.util.ArrayList;

import dev.chrisbanes.insetter.Insetter;


public class DetailCollectionFragment extends Fragment {

    SharedViewModel viewModel;
    FragmentDetailCollectionBinding binding;

    String collectionId;

    HitAdapter adapter;

    ArrayList<Hit> hits;
    public DetailCollectionFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentDetailCollectionBinding.inflate(inflater, container, false);
        Insetter.builder()
                .margin(WindowInsetsCompat.Type.statusBars())
                .applyToView(binding.appBarLayout);
        // Inflate the layout for this fragment
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewModel = new ViewModelProvider(requireActivity()).get(SharedViewModel.class);
        hits = new ArrayList<>();
        adapter =  new HitAdapter(hits, requireContext());
        binding.rvListItem.setAdapter(adapter);
        binding.rvListItem.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));

        if (getArguments() != null && getArguments().getString("collectionId") != null) {
            collectionId = getArguments().getString("collectionId");
            viewModel.collectionId.postValue(collectionId);
            Collections collection = viewModel.getCollectionData(collectionId);
            fetchData(collection);
        }
        else {
            collectionId = viewModel.collectionId.getValue();
            Collections collection = viewModel.getCollectionData(collectionId);
            binding.toolbar.setTitle(collection.getName());
            observeDataFromViewModel();
        }

        adapter.setOnClickListener(item -> {
            viewModel.setPicture(item);
            Navigation.findNavController(view).navigate(R.id.action_global_previewFragment);
        });
        adapter.setOnLongClickListener(item -> {
            BottomSheetDialog dialog = new BottomSheetDialog(requireContext());
            BottomSheetRemoveFromCollectionBinding binding = BottomSheetRemoveFromCollectionBinding.inflate(getLayoutInflater());
            binding.btRemove.setOnClickListener(v -> {
                viewModel.removeHitFromCollection(collectionId, item);
                viewModel.getListCollectionsLiveData().observe(getViewLifecycleOwner(), collections -> {
                    if (collections != null) {
                        if (viewModel.getCollectionData(collectionId) != null && viewModel.getCollectionData(collectionId).getListHitsId().size() > 0) {
                            fetchData(viewModel.getCollectionData(collectionId));
                        }
                        else {
                            Navigation.findNavController(view).popBackStack();
                        }
                    }
                });
                dialog.dismiss();
            });
            dialog.setContentView(binding.getRoot());
            dialog.setCancelable(true);
            dialog.show();
        });
        binding.toolbar.setNavigationOnClickListener(v -> {
            Navigation.findNavController(view).popBackStack();
        });
    }
    private void fetchData(Collections collection) {
        viewModel.hitsFromCollection.postValue(null);
        binding.toolbar.setTitle(collection.getName());
        viewModel.getHitsFromCollection(collection.getListHitsId());
        viewModel.hitsFromCollection.observe(getViewLifecycleOwner(), hit -> {
            if (hit != null) {
                hits.clear();
                hits.addAll(hit);
                adapter.updateList(hits);
                viewModel.updateImageUrlCollection(collectionId, hits);
            }
        });
    }

    private void observeDataFromViewModel() {
        if (viewModel.hitsFromCollection.getValue() != null) {
            hits.addAll(viewModel.hitsFromCollection.getValue());
            adapter.updateList(hits);
        }
        else {
            Collections collection = viewModel.getCollectionData(collectionId);
            fetchData(collection);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (getArguments() != null) {
            getArguments().clear();
        }
    }
}