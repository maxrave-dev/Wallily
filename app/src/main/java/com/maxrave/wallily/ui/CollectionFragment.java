package com.maxrave.wallily.ui;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.maxrave.wallily.R;
import com.maxrave.wallily.adapter.CollectionAdapter;
import com.maxrave.wallily.data.model.firebase.Collections;
import com.maxrave.wallily.databinding.BottomSheetCollectionBinding;
import com.maxrave.wallily.databinding.FragmentCollectionBinding;
import com.maxrave.wallily.viewModel.SharedViewModel;

import java.util.ArrayList;

import dev.chrisbanes.insetter.Insetter;

public class CollectionFragment extends Fragment {
    public CollectionFragment() {
        // Required empty public constructor
    }
    FragmentCollectionBinding binding;
    SharedViewModel viewModel;
    CollectionAdapter adapter;

    ArrayList<Collections> list;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentCollectionBinding.inflate(inflater, container, false);
        // Inflate the layout for this fragment
        Insetter.builder()
                .margin(WindowInsetsCompat.Type.statusBars())

                // This is a shortcut for view.setOnApplyWindowInsetsListener(builder.build())
                .applyToView(binding.appBarLayout);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewModel = new ViewModelProvider(requireActivity()).get(SharedViewModel.class);

        list = new ArrayList<>();
        adapter = new CollectionAdapter(requireContext(), list);

        binding.rvListCollection.setAdapter(adapter);
        binding.rvListCollection.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));

        viewModel.getListCollections();
        viewModel.getListCollectionsLiveData().observe(getViewLifecycleOwner(), collections -> {
            list.clear();
            list.addAll(collections);
            Log.w("COLLECTION_FRAGMENT", "onViewCreated: " + list.toString());
            adapter.updateList(list);
        });
        binding.toolbar.setNavigationOnClickListener(v -> Navigation.findNavController(view).popBackStack());
        binding.toolbar.setOnMenuItemClickListener(item -> {
            if (item.getItemId() == R.id.add_collection_item) {
                Log.w("COLLECTION_FRAGMENT", "click new: " + list.size());
                BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(requireContext());
                BottomSheetCollectionBinding bottomSheetCollectionBinding = BottomSheetCollectionBinding.inflate(getLayoutInflater());
                bottomSheetCollectionBinding.btCreate.setOnClickListener(v -> {
                    if (bottomSheetCollectionBinding.etCollectionName.getText().toString().isEmpty()) {
                        bottomSheetCollectionBinding.etCollectionName.setError("Please enter collection name");
                    } else {
                        viewModel.createCollection(bottomSheetCollectionBinding.etCollectionName.getText().toString());
                        viewModel.getListCollectionsLiveData().observe(getViewLifecycleOwner(), collections -> {
                            list.clear();
                            list.addAll(collections);
                            Log.w("COLLECTION_FRAGMENT", "onViewCreated: " + list.toString());
                            adapter.updateList(list);
                        });
                        bottomSheetDialog.dismiss();
                    }
                });
                bottomSheetDialog.setContentView(bottomSheetCollectionBinding.getRoot());
                bottomSheetDialog.setCancelable(true);
                bottomSheetDialog.show();
                return true;
            }
            return false;
        });
        adapter.setOnItemClickListener(position -> {
            if (list.get(position).getListHitsId() != null && list.get(position).getListHitsId().size() > 0) {
                Bundle bundle = new Bundle();
                bundle.putString("collectionId", list.get(position).getId());
                Navigation.findNavController(view).navigate(R.id.action_global_detailCollectionFragment, bundle);
            }
            else {
                Toast.makeText(requireContext(), "This collection is empty", Toast.LENGTH_SHORT).show();
            }
        });
    }
}