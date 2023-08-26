package com.maxrave.wallily.ui;

import static autodispose2.AutoDispose.autoDisposable;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.os.Parcelable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.maxrave.wallily.R;
import com.maxrave.wallily.adapter.PictureAdapter;
import com.maxrave.wallily.databinding.FragmentHomeBinding;
import com.maxrave.wallily.pagination.LoadStateAdapter;
import com.maxrave.wallily.pagination.PictureComparator;
import com.maxrave.wallily.viewModel.SharedViewModel;

import autodispose2.androidx.lifecycle.AndroidLifecycleScopeProvider;
import dagger.hilt.android.AndroidEntryPoint;
import dev.chrisbanes.insetter.Insetter;

@AndroidEntryPoint
public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;
    SharedViewModel viewModel;

    private PictureAdapter adapter;

    public HomeFragment() {
        // Required empty public constructor
    }
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        Insetter.builder()
                .margin(WindowInsetsCompat.Type.statusBars())

                // This is a shortcut for view.setOnApplyWindowInsetsListener(builder.build())
                .applyToView(binding.topAppBarLayout);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewModel = new ViewModelProvider(requireActivity())
                .get(SharedViewModel.class);
        adapter = new PictureAdapter(new PictureComparator(), item -> {
            viewModel.setPicture(item);
            Navigation.findNavController(view).navigate(R.id.action_global_previewFragment);
        });
        adapter.setStateRestorationPolicy(RecyclerView.Adapter.StateRestorationPolicy.PREVENT_WHEN_EMPTY);
        binding.rvListItem.setAdapter(adapter.withLoadStateFooter(
                new LoadStateAdapter(v -> {
                    adapter.retry();
                })));
        binding.rvListItem.setHasFixedSize(true);
        binding.rvListItem.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
        binding.rvListItem.setItemViewCacheSize(500);
        binding.rvListItem.setDrawingCacheEnabled(true);
        binding.rvListItem.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
        Log.d("HomeFragment", "onViewCreated: " + viewModel.picturePagingDataFlowable);

        viewModel.getDataList().observe(getViewLifecycleOwner(), paging -> {
            adapter.submitData(getLifecycle(), paging);
        });
    }
}