package com.maxrave.wallily.ui;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.firebase.auth.FirebaseAuth;
import com.maxrave.wallily.R;
import com.maxrave.wallily.data.model.firebase.Account;
import com.maxrave.wallily.databinding.FragmentAccountBinding;
import com.maxrave.wallily.viewModel.SharedViewModel;

import dev.chrisbanes.insetter.Insetter;

public class AccountFragment extends Fragment {

    FragmentAccountBinding binding;

    SharedViewModel viewModel;
    public AccountFragment() {
        // Required empty public constructor
    }
    ActivityResultLauncher<Intent> mGetImage = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result ->
    {
        if (result.getResultCode() == Activity.RESULT_OK) {
            if (result.getData() != null) {
                Uri uri = result.getData().getData();
                if (uri != null) {
                    ContentResolver contentResolver = requireContext().getContentResolver();
                    Integer takeFlags = Intent.FLAG_GRANT_READ_URI_PERMISSION;
                    requireActivity().grantUriPermission(requireActivity().getPackageName(), uri, takeFlags);
                    contentResolver.takePersistableUriPermission(uri, takeFlags);
                    viewModel.uploadImage(uri);
                }
            }

        }
    });


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentAccountBinding.inflate(inflater, container, false);
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
        viewModel = new ViewModelProvider(requireActivity())
                .get(SharedViewModel.class);
        viewModel.getAccount();
        viewModel.getAccountLiveData().observe(getViewLifecycleOwner(), account -> {
            if (account != null) {
                binding.tvDisplayName.setText(account.getDisplay_name());
                binding.tvEmail.setText(account.getEmail());
                Glide.with(requireContext()).load(account.getAvatar_url()).into(binding.ivAvatar);
            }
        });
        binding.toolbar.setNavigationOnClickListener(v -> {
            Navigation.findNavController(view).popBackStack();
        });

        binding.btChangeAvatar.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
            intent.setType("image/*");
            mGetImage.launch(intent);
            viewModel.getAccountLiveData().observe(getViewLifecycleOwner(), account -> {
                if (account != null) {
                    Glide.with(requireContext()).load(account.getAvatar_url()).into(binding.ivAvatar);
                }
            });
        });
        binding.btChangeDisplayName.setOnClickListener(v -> {
            EditText editText = new EditText(requireContext());
            editText.setPadding(16, 16, 16, 16);
            editText.setInputType(InputType.TYPE_CLASS_TEXT);
            MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(requireContext());
            builder.setTitle("Change display name");
            builder.setView(editText);
            builder.setPositiveButton("Change", (dialog, which) -> {
                String displayName = editText.getText().toString();
                if (!displayName.isEmpty()) {
                    Account account = viewModel.getAccountLiveData().getValue();
                    viewModel.updateAccount(new Account(account.getEmail(), account.getUid(), displayName, account.getAvatar_url()));

                }
                else {
                    Toast.makeText(requireContext(), "Name can't be empty", Toast.LENGTH_SHORT).show();
                }
            });
            builder.setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss());
            builder.create();
            builder.show();
        });
        binding.btLogOut.setOnClickListener(v -> {
            FirebaseAuth mAuth = FirebaseAuth.getInstance();
            viewModel.setLoggedIn(false);
            mAuth.signOut();
            viewModel.logOut();
            Toast.makeText(requireContext(), "Log out success", Toast.LENGTH_SHORT).show();
            Navigation.findNavController(view).popBackStack();
        });
        binding.btRemoveAccount.setOnClickListener(v -> {
            MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(requireContext());
            builder.setTitle("Remove account");
            builder.setMessage("Are you sure you want to remove your account?");
            builder.setPositiveButton("Remove", (dialog, which) -> {
                viewModel.setLoggedIn(false);
                viewModel.removeAccount();
                Toast.makeText(requireContext(), "Account removed", Toast.LENGTH_SHORT).show();
                Navigation.findNavController(view).popBackStack();
            });
        });
    }
}