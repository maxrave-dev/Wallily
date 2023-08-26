package com.maxrave.wallily.ui;

import android.app.WallpaperManager;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.maxrave.wallily.databinding.BottomSheetMoreBinding;
import com.maxrave.wallily.databinding.BottomSheetWallpaperChooseBinding;
import com.maxrave.wallily.databinding.FragmentPreviewBinding;
import com.maxrave.wallily.service.DownloadManager;
import com.maxrave.wallily.viewModel.SharedViewModel;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.io.IOException;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import dagger.hilt.android.AndroidEntryPoint;
import dev.chrisbanes.insetter.Insetter;

@AndroidEntryPoint
public class PreviewFragment extends Fragment {

    private FragmentPreviewBinding binding;

    private SharedViewModel viewModel;
    public PreviewFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentPreviewBinding.inflate(inflater, container, false);
        Insetter.builder()
                .marginTop(WindowInsetsCompat.Type.statusBars(), false)
                .applyToView(binding.toolBarLayout);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewModel = new ViewModelProvider(requireActivity())
                .get(SharedViewModel.class);

        viewModel.picture.observe(getViewLifecycleOwner(), hit -> {
            binding.loading.setVisibility(View.VISIBLE);
            setEnabledAll(binding.btSave, false);
            setEnabledAll(binding.btSet, false);
            setEnabledAll(binding.btMore, false);
            setEnabledAll(binding.btShare, false);
            Target target = new Target() {
                @Override
                public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                    binding.ivPicture.setImageBitmap(bitmap);
                    if (bitmap != null) {
                        viewModel.setBitmap(bitmap);
                    }
                    binding.loading.setVisibility(View.GONE);
                    setEnabledAll(binding.btSave, true);
                    setEnabledAll(binding.btSet, true);
                    setEnabledAll(binding.btMore, true);
                    setEnabledAll(binding.btShare, true);
                }

                @Override
                public void onBitmapFailed(Exception e, Drawable errorDrawable) {
                    binding.loading.setVisibility(View.GONE);
                    binding.ivPicture.setImageDrawable(errorDrawable);
                    viewModel.setBitmap(null);
                    setEnabledAll(binding.btSave, false);
                    setEnabledAll(binding.btSet, false);
                    setEnabledAll(binding.btMore, false);
                    setEnabledAll(binding.btShare, false);
                }

                @Override
                public void onPrepareLoad(Drawable placeHolderDrawable) {
                    setEnabledAll(binding.btSave, false);
                    setEnabledAll(binding.btSet, false);
                    setEnabledAll(binding.btMore, false);
                    setEnabledAll(binding.btShare, false);
                }
            };
            Picasso.get().load(hit.getLargeImageURL()).into(target);
        });

        binding.toolBarLayout.setNavigationOnClickListener(v -> Navigation.findNavController(view).popBackStack());
        binding.btMore.setOnClickListener(v -> {
            BottomSheetDialog dialog = new BottomSheetDialog(requireContext());
            BottomSheetMoreBinding dialogView = BottomSheetMoreBinding.inflate(getLayoutInflater());

            Pattern pattern = Pattern.compile("/([^/]+)/$");
            Matcher matcher = pattern.matcher(Objects.requireNonNull(viewModel.picture.getValue()).getPageURL());

            String name = matcher.find() ? matcher.group(1) : "";
            if (name != null && name.contains("-")) {
                name = name.replace("-", " ").replaceAll("\\d", "");

            }
            dialogView.tvTitle.setText(name);
            dialogView.tvAuthor.setText(viewModel.picture.getValue().getUser());
            dialogView.btShare.setOnClickListener(v1 -> {
                Intent shareIntent = new Intent(Intent.ACTION_SEND);
                shareIntent.setType("text/plain");
                shareIntent.putExtra(Intent.EXTRA_TEXT, Objects.requireNonNull(viewModel.picture.getValue()).getPageURL());
                Intent chooserIntent = Intent.createChooser(shareIntent, "Share URL");
                startActivity(chooserIntent);
            });
            dialogView.btSaveToDevice.setOnClickListener(v1 -> {
                DownloadManager downloadManager = new DownloadManager(requireContext());
                downloadManager.download(Objects.requireNonNull(viewModel.picture.getValue()).getLargeImageURL());
            });
            dialogView.btSet.setOnClickListener(v1 -> {
                BottomSheetWallpaperChooseBinding dialogView1 = BottomSheetWallpaperChooseBinding.inflate(getLayoutInflater());
                BottomSheetDialog dialog1 = new BottomSheetDialog(requireContext());
                dialogView1.btWallpaper.setOnClickListener(v2 -> {
                    WallpaperManager wallpaperManager = WallpaperManager.getInstance(requireContext());
                    try {
                        if (viewModel.bitmapLiveData.getValue() != null) {
                            wallpaperManager.setBitmap(viewModel.bitmapLiveData.getValue(), null, false, WallpaperManager.FLAG_SYSTEM);
                            Toast.makeText(requireContext(), "Set wallpaper success", Toast.LENGTH_SHORT).show();
                        }
                        else
                            Toast.makeText(requireContext(), "Set wallpaper failed", Toast.LENGTH_SHORT).show();
                    } catch (IOException e) {
                        e.printStackTrace();
                        Toast.makeText(requireContext(), "Set wallpaper failed with " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
                dialogView1.btLockscreen.setOnClickListener(v2 -> {
                    WallpaperManager wallpaperManager = WallpaperManager.getInstance(requireContext());
                    try {
                        if (viewModel.bitmapLiveData.getValue() != null) {
                            wallpaperManager.setBitmap(viewModel.bitmapLiveData.getValue(), null, false, WallpaperManager.FLAG_LOCK);
                            Toast.makeText(requireContext(), "Set wallpaper success", Toast.LENGTH_SHORT).show();
                        }
                        else
                            Toast.makeText(requireContext(), "Set wallpaper failed", Toast.LENGTH_SHORT).show();
                    } catch (IOException e) {
                        e.printStackTrace();
                        Toast.makeText(requireContext(), "Set wallpaper failed with " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
                dialogView1.btBoth.setOnClickListener(v2 -> {
                    WallpaperManager wallpaperManager = WallpaperManager.getInstance(requireContext());
                    try {
                        if (viewModel.bitmapLiveData.getValue() != null) {
                            wallpaperManager.setBitmap(viewModel.bitmapLiveData.getValue(), null, false, WallpaperManager.FLAG_LOCK | WallpaperManager.FLAG_SYSTEM);
                            Toast.makeText(requireContext(), "Set wallpaper success", Toast.LENGTH_SHORT).show();
                        }
                        else
                            Toast.makeText(requireContext(), "Set wallpaper failed", Toast.LENGTH_SHORT).show();
                    } catch (IOException e) {
                        e.printStackTrace();
                        Toast.makeText(requireContext(), "Set wallpaper failed with " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
                dialog1.setContentView(dialogView1.getRoot());
                dialog1.setCancelable(true);
                dialog1.show();
            });

            dialog.setCancelable(true);
            dialog.setContentView(dialogView.getRoot());
            dialog.show();
        });
        binding.btShare.setOnClickListener(v1 -> {
            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.setType("text/plain");
            shareIntent.putExtra(Intent.EXTRA_TEXT, Objects.requireNonNull(viewModel.picture.getValue()).getPageURL());
            Intent chooserIntent = Intent.createChooser(shareIntent, "Share URL");
            startActivity(chooserIntent);
        });
        binding.btSet.setOnClickListener(v1 -> {
            BottomSheetWallpaperChooseBinding dialogView = BottomSheetWallpaperChooseBinding.inflate(getLayoutInflater());
            BottomSheetDialog dialog = new BottomSheetDialog(requireContext());
            dialogView.btWallpaper.setOnClickListener(v -> {
                WallpaperManager wallpaperManager = WallpaperManager.getInstance(requireContext());
                try {
                    if (viewModel.bitmapLiveData.getValue() != null) {
                        wallpaperManager.setBitmap(viewModel.bitmapLiveData.getValue(), null, false, WallpaperManager.FLAG_SYSTEM);
                        Toast.makeText(requireContext(), "Set wallpaper success", Toast.LENGTH_SHORT).show();
                    }
                    else
                        Toast.makeText(requireContext(), "Set wallpaper failed", Toast.LENGTH_SHORT).show();
                } catch (IOException e) {
                    e.printStackTrace();
                    Toast.makeText(requireContext(), "Set wallpaper failed with " + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
            dialogView.btLockscreen.setOnClickListener(v -> {
                WallpaperManager wallpaperManager = WallpaperManager.getInstance(requireContext());
                try {
                    if (viewModel.bitmapLiveData.getValue() != null) {
                        wallpaperManager.setBitmap(viewModel.bitmapLiveData.getValue(), null, false, WallpaperManager.FLAG_LOCK);
                        Toast.makeText(requireContext(), "Set wallpaper success", Toast.LENGTH_SHORT).show();
                    }
                    else
                        Toast.makeText(requireContext(), "Set wallpaper failed", Toast.LENGTH_SHORT).show();
                } catch (IOException e) {
                    e.printStackTrace();
                    Toast.makeText(requireContext(), "Set wallpaper failed with " + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
            dialogView.btBoth.setOnClickListener(v -> {
                WallpaperManager wallpaperManager = WallpaperManager.getInstance(requireContext());
                try {
                    if (viewModel.bitmapLiveData.getValue() != null) {
                        wallpaperManager.setBitmap(viewModel.bitmapLiveData.getValue(), null, false, WallpaperManager.FLAG_LOCK | WallpaperManager.FLAG_SYSTEM);
                        Toast.makeText(requireContext(), "Set wallpaper success", Toast.LENGTH_SHORT).show();
                    }
                    else
                        Toast.makeText(requireContext(), "Set wallpaper failed", Toast.LENGTH_SHORT).show();
                } catch (IOException e) {
                    e.printStackTrace();
                    Toast.makeText(requireContext(), "Set wallpaper failed with " + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
            dialog.setContentView(dialogView.getRoot());
            dialog.setCancelable(true);
            dialog.show();

        });
        binding.btSave.setOnClickListener(v1 -> {
            DownloadManager downloadManager = new DownloadManager(requireContext());
            downloadManager.download(Objects.requireNonNull(viewModel.picture.getValue()).getLargeImageURL());
        });
    }
    public void setEnabledAll(View v, Boolean enabled) {
        v.setEnabled(enabled);
        v.setFocusable(enabled);
        if (v instanceof ViewGroup) {
            ViewGroup vg =(ViewGroup) v;
            for (int i = 0; i < vg.getChildCount(); i++) {
                setEnabledAll(vg.getChildAt(i), enabled);
            }
        }
    }
}