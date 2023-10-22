package com.maxrave.wallily.ui;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.maxrave.wallily.R;
import com.maxrave.wallily.databinding.FragmentSettingsBinding;
import com.maxrave.wallily.viewModel.SharedViewModel;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;

import dev.chrisbanes.insetter.Insetter;

public class SettingsFragment extends Fragment {

    FragmentSettingsBinding binding;
    SharedViewModel viewModel;


    public SettingsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentSettingsBinding.inflate(inflater, container, false);
        Insetter.builder()
                .margin(WindowInsetsCompat.Type.statusBars())

                // This is a shortcut for view.setOnApplyWindowInsetsListener(builder.build())
                .applyToView(binding.appBarLayout);
        // Inflate the layout for this fragment
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewModel = new ViewModelProvider(requireActivity()).get(SharedViewModel.class);
        binding.toolbar.setNavigationOnClickListener(v -> {
            Navigation.findNavController(view).popBackStack();
        });
        if (Glide.getPhotoCacheDir(requireContext()) != null) {
            Log.w("TAG", "onViewCreated: " + Glide.getPhotoCacheDir(requireContext()).getAbsolutePath());
            File file = new File(Glide.getPhotoCacheDir(requireContext()).getPath());
            Log.w("TAG", "onViewCreated: " + file.length());
            binding.tvCacheSize.setText(file.length()/(1024 * 1024) + " MB");
        }
        binding.btClearCache.setOnClickListener(v -> {
            Glide.get(requireContext()).clearMemory();
            new Thread(() -> Glide.get(requireContext()).clearDiskCache()).start();
            binding.tvCacheSize.setText("0 MB");
        });
    }
}