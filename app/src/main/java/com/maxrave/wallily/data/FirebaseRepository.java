package com.maxrave.wallily.data;

import android.util.Log;

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FieldPath;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.maxrave.wallily.data.model.firebase.Account;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicReference;

public class FirebaseRepository {
    String TAG = "FIREBASE_REPOSITORY";
    FirebaseFirestore firestoreDB = FirebaseFirestore.getInstance();

    Gson gson = new GsonBuilder().create();

    public FirebaseRepository() {
    }

    private static final FirebaseRepository ourInstance = new FirebaseRepository();
    public static FirebaseRepository getInstance() {
        return ourInstance;
    }

    public static HashMap<String, Object> accountToHashMap(Account account) {
        Gson gson = new Gson();
        String json = gson.toJson(account);// obj is your object
        Type type = new TypeToken<HashMap<String, Object>>(){}.getType();
        HashMap<String, Object> result = new HashMap<>();
        result.put("account", new Gson().fromJson(json, type));
        return result;
    }

    public static Account getAccountFromHashMap(HashMap<String, Object> hashMap) {
        Gson gson = new Gson();
        JsonElement json = gson.toJsonTree(hashMap);
        return gson.fromJson(json, Account.class);
    }

    public void updateAccount(Account account) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        firestoreDB.collection("user").whereEqualTo(FieldPath.of("account", "uid"), user.getUid())
                .get().addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        if (task.getResult().getDocuments().size() > 0) {
                            firestoreDB.collection("user").document(Objects.requireNonNull(task.getResult().getDocuments().get(0).getId()))
                                    .update(accountToHashMap(account));
                        }
                    }
                });
    }

    public Task<QuerySnapshot> getAccount() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        return firestoreDB.collection("user").whereEqualTo(FieldPath.of("account", "uid"), user.getUid())
                .get();
    }

    public void createNewAccount(Account account) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        firestoreDB.collection("user").add(accountToHashMap(account)).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Log.d(TAG, "createNewAccount: " + task.getResult().getId());
            }
            else {
                Log.d(TAG, "createNewAccount: " + task.getException().getMessage());
            }
        });
    }
}
