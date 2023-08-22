package com.maxrave.wallily.ui;

import static autodispose2.AutoDispose.autoDisposable;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.maxrave.wallily.adapter.PictureAdapter;
import com.maxrave.wallily.databinding.ActivityMainBinding;
import com.maxrave.wallily.pagination.LoadStateAdapter;
import com.maxrave.wallily.pagination.PictureComparator;
import com.maxrave.wallily.viewModel.SharedViewModel;

import autodispose2.androidx.lifecycle.AndroidLifecycleScopeProvider;
import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private SharedViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        viewModel = new ViewModelProvider(this)
                .get(SharedViewModel.class);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        PictureAdapter adapter = new PictureAdapter(new PictureComparator());
        binding.rvListItem.setAdapter(adapter.withLoadStateFooter(
                new LoadStateAdapter(v -> {
                  adapter.retry();
                })));
        binding.rvListItem.setHasFixedSize(true);
        binding.rvListItem.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
        binding.rvListItem.setItemViewCacheSize(100);
        binding.rvListItem.setDrawingCacheEnabled(true);
        binding.rvListItem.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);

        viewModel.picturePagingDataFlowable.to(autoDisposable(AndroidLifecycleScopeProvider.from(this)))
                .subscribe(pagingData -> adapter.submitData(getLifecycle(), pagingData));
    }
}