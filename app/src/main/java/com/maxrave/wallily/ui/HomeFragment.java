package com.maxrave.wallily.ui;

import static com.maxrave.wallily.data.datastore.DataStoreManager.TRUE;
import static autodispose2.AutoDispose.autoDisposable;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.paging.PagingData;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.auth.api.identity.BeginSignInRequest;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.firestore.FirebaseFirestore;
import com.maxrave.wallily.R;
import com.maxrave.wallily.adapter.PictureAdapter;
import com.maxrave.wallily.common.Config;
import com.maxrave.wallily.data.datastore.DataStoreManager;
import com.maxrave.wallily.data.model.firebase.Account;
import com.maxrave.wallily.databinding.FragmentHomeBinding;
import com.maxrave.wallily.pagination.LoadStateAdapter;
import com.maxrave.wallily.pagination.PictureComparator;
import com.maxrave.wallily.viewModel.SharedViewModel;

import java.util.Objects;

import dagger.hilt.android.AndroidEntryPoint;
import dev.chrisbanes.insetter.Insetter;

@AndroidEntryPoint
public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;
    SharedViewModel viewModel;

    private PictureAdapter adapter;

    private String keyword;

    private FirebaseAuth mAuth;

    private FirebaseFirestore db;
    // [END declare_auth]

    private GoogleSignInClient mGoogleSignInClient;

    ActivityResultLauncher<Intent> mGetContent = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
            result -> {
        Log.w("Log In", "onActivityResult: " + result.toString());
                if (result.getResultCode() == Activity.RESULT_OK) {
                    Intent intent = result.getData();
                    // Handle the Intent
                    Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(intent);
                    try {
                        // Google Sign In was successful, authenticate with Firebase
                        GoogleSignInAccount account = task.getResult(ApiException.class);
                        Log.d("Log In", "firebaseAuthWithGoogle:" + account.getId());
                        firebaseAuthWithGoogle(account.getIdToken());
                    } catch (ApiException e) {
                        // Google Sign In failed, update UI appropriately
                        Log.w("Log In", "Google sign in failed", e);
                        Toast.makeText(requireContext(), "Sign in failed", Toast.LENGTH_SHORT).show();
                    }
                }
                else {
                    Toast.makeText(requireContext(), "Sign in failed", Toast.LENGTH_SHORT).show();
                }
            });
    public HomeFragment() {
        // Required empty public constructor
    }

    public void firebaseAuthWithGoogle(String idToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(requireActivity(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("Log in", "signInWithCredential:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            if (Objects.requireNonNull(task.getResult().getAdditionalUserInfo()).isNewUser()) {
                                FirebaseUser userNonNull = FirebaseAuth.getInstance().getCurrentUser();
                                viewModel.createNewAccount(new Account(
                                      userNonNull.getEmail() , userNonNull.getUid(), userNonNull.getDisplayName(), userNonNull.getDisplayName()
                                ));
                            }
                            viewModel.setLoggedIn(true);
                            viewModel.getAccount();
                            updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("Log in", "signInWithCredential:failure", task.getException());
                            viewModel.setLoggedIn(false);
                            updateUI(null);
                        }
                    }
                });
    }
    private void updateUI(FirebaseUser user) {
        if (user != null) {
            binding.navView.getHeaderView(0).findViewById(R.id.info_layout).setVisibility(View.VISIBLE);
            binding.navView.getHeaderView(0).findViewById(R.id.btLogIn).setVisibility(View.GONE);
            ((TextView) binding.navView.getHeaderView(0).findViewById(R.id.tvAccountName)).setText(user.getDisplayName());
            ((TextView) binding.navView.getHeaderView(0).findViewById(R.id.tvAccountEmail)).setText(user.getEmail());
            viewModel.getAccount();
            viewModel.getAccountLiveData().observe(getViewLifecycleOwner(), account -> {
                if (account != null) {
                    Glide.with(requireContext()).load(account.getAvatar_url()).into(((ImageView) binding.navView.getHeaderView(0).findViewById(R.id.ivAvatar)));
                }
            });
        } else {
            binding.navView.getHeaderView(0).findViewById(R.id.info_layout).setVisibility(View.GONE);
            binding.navView.getHeaderView(0).findViewById(R.id.btLogIn).setVisibility(View.VISIBLE);
            ((ImageView) binding.navView.getHeaderView(0).findViewById(R.id.ivAvatar)).setImageBitmap(null);
        }
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
    public void onResume() {
        super.onResume();
        binding.searchBar.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if (query != null) {
                    viewModel.query = query;
                    binding.rvListItem.smoothScrollToPosition(0);
                    adapter.submitData(getLifecycle(), PagingData.empty());
                    viewModel.getData(keyword, viewModel.query);
                    viewModel.getDataList().observe(getViewLifecycleOwner(), paging -> {
                        adapter.submitData(getLifecycle(), paging);
                    });
                    return true;
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewModel = new ViewModelProvider(requireActivity())
                .get(SharedViewModel.class);

        // [END config_signin]

        // [START initialize_auth]
        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        viewModel.getLoggedIn();
        viewModel.getLoggedInLiveData().observe(getViewLifecycleOwner(), loggedIn -> {
            if (Objects.equals(loggedIn, TRUE)) {
                binding.navView.getHeaderView(0).findViewById(R.id.info_layout).setVisibility(View.VISIBLE);
                binding.navView.getHeaderView(0).findViewById(R.id.btLogIn).setVisibility(View.GONE);
                updateUI(mAuth.getCurrentUser());
            } else {
                binding.navView.getHeaderView(0).findViewById(R.id.info_layout).setVisibility(View.GONE);
                binding.navView.getHeaderView(0).findViewById(R.id.btLogIn).setVisibility(View.VISIBLE);
            }
        });
        binding.navView.getHeaderView(0).findViewById(R.id.btLogIn).setOnClickListener(v -> {
            GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                    .requestIdToken("408664017500-g320431ogbnlqhb9jihbspd0f4fnea3e.apps.googleusercontent.com")
                    .requestEmail()
                    .build();
            mGoogleSignInClient = GoogleSignIn.getClient(requireContext(), gso);
            mGetContent.launch(mGoogleSignInClient.getSignInIntent());

        });
        binding.navView.setNavigationItemSelectedListener(item -> {
                    if (item.getItemId() == R.id.logout_item) {
                        if (Objects.equals(viewModel.getLoggedInLiveData().getValue(), TRUE)) {
                            viewModel.setLoggedIn(false);
                            mAuth.signOut();
                            viewModel.removeAccount();
                            updateUI(null);
                            Toast.makeText(requireContext(), "Log out success", Toast.LENGTH_SHORT).show();
                            return false;
                        }
                        else {
                            Toast.makeText(requireContext(), "You are not logged in", Toast.LENGTH_SHORT).show();
                            return false;
                        }
                    }
                    return false;
                }
            );
        binding.toolBarLayout.setNavigationOnClickListener(v -> {
            binding.rootLayout.open();
        });
        binding.searchBar.setVisibility(View.GONE);
        binding.toolBarLayout.setOnMenuItemClickListener(menuItem ->{
            if (menuItem.getItemId() == R.id.search) {
                if (binding.searchBar.getVisibility() == View.GONE) {
                    binding.searchBar.setVisibility(View.VISIBLE);
                    binding.searchBar.requestFocus();
                }
                else {
                    binding.searchBar.setVisibility(View.GONE);
                }
            }
            return true;
        });
        adapter = new PictureAdapter(new PictureComparator(), item -> {
            viewModel.setPicture(item);
            Navigation.findNavController(view).navigate(R.id.action_global_previewFragment);
        }, requireContext());
        adapter.setStateRestorationPolicy(RecyclerView.Adapter.StateRestorationPolicy.PREVENT_WHEN_EMPTY);
        binding.rvListItem.setAdapter(adapter.withLoadStateFooter(
                new LoadStateAdapter(v -> {
                    adapter.retry();
                })));
        binding.rvListItem.setHasFixedSize(true);
        binding.rvListItem.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
        Log.d("HomeFragment", "onViewCreated: " + viewModel.picturePagingDataFlowable);

        if (viewModel.keyword == null) {
            Log.w("Reload", "onViewCreated: " + viewModel.keyword);
            keyword = "";
            viewModel.keyword = keyword;
            viewModel.getData("", "");
            viewModel.getDataList().observe(getViewLifecycleOwner(), paging -> {
                adapter.submitData(getLifecycle(), paging);
            });
        }
        else {
            Log.w("Load from ViewModel", "onViewCreated: " + viewModel.keyword);
            keyword = viewModel.keyword;
            binding.chipGroup.check(Config.listChipId.get(Config.listKeyword.indexOf(keyword)));
            viewModel.getDataList().observe(getViewLifecycleOwner(), paging -> {
                adapter.submitData(getLifecycle(), paging);
            });
        }
        binding.chipGroup.setOnCheckedStateChangeListener((group, checkedIds) -> {
            if (Config.listChipId.contains(checkedIds.get(0)) && !Objects.equals(viewModel.keyword, Config.listKeyword.get(Config.listChipId.indexOf(checkedIds.get(0))))) {
                keyword = Config.listKeyword.get(Config.listChipId.indexOf(checkedIds.get(0)));
                binding.rvListItem.smoothScrollToPosition(0);
                viewModel.keyword = keyword;
                viewModel.getData(keyword, viewModel.query);
                viewModel.getDataList().observe(getViewLifecycleOwner(), paging -> {
                    adapter.submitData(getLifecycle(), paging);
                });
            }
        });
    }
}