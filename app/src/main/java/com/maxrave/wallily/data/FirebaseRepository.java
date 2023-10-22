package com.maxrave.wallily.data;

import android.net.Uri;
import android.util.Log;
import android.util.Pair;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldPath;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.maxrave.wallily.data.model.firebase.Account;
import com.maxrave.wallily.data.model.firebase.Collections;
import com.maxrave.wallily.data.model.firebase.HitId;

import java.lang.reflect.Type;
import java.util.ArrayList;
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

    public static ArrayList<Collections> getCollectionsListFromHashMap(ArrayList<Object> listHashMap) {
        Gson gson = new Gson();
        ArrayList<Collections> collections = new ArrayList<>();
        listHashMap.forEach(hashMap -> {
            Log.w("FIREBASE_REPOSITORY", "hashmap: " + hashMap.toString());
            JsonElement json = gson.toJsonTree(hashMap);
            Collections collection = gson.fromJson(json, Collections.class);
            Log.w("FIREBASE_REPOSITORY", "collection: " + collection.getName());
            collections.add(collection);
        });
        Log.w("FIREBASE_REPOSITORY", "collections: " + collections.size());
        return collections;
    }

    public Task<QuerySnapshot> findCollection(String document, String id) {
        return firestoreDB.collection("user").document(document).collection("collections").whereEqualTo("id", id).get();
    }

    public Task<QuerySnapshot> findDocument() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        return firestoreDB.collection("user").whereEqualTo(FieldPath.of("account", "uid"), user.getUid())
                .get();
    }

    public Task<Void> updateAccount(Account account, @NonNull String id) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        return firestoreDB.collection("user").document(id)
                .update(accountToHashMap(account));
//                .get().addOnCompleteListener(task -> {
//                    if (task.isSuccessful()) {
//                        if (task.getResult().getDocuments().size() > 0) {
//                            firestoreDB.collection("user").document(Objects.requireNonNull(task.getResult().getDocuments().get(0).getId()))
//                                    .update(accountToHashMap(account));
//                        }
//                    }
//                });
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

    public Pair<UploadTask, StorageReference> uploadImage(Uri uri) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            FirebaseStorage storage = FirebaseStorage.getInstance();
            StorageReference storageRef = storage.getReference();
            StorageReference newUploadRef = storageRef.child("image/" + user.getUid() + ".jpg");
            UploadTask uploadTask = newUploadRef.putFile(uri);

// Register observers to listen for when the download is done or if it fails
            return new Pair<>(uploadTask, newUploadRef);
        }
        return null;
    }

    public void removeAccount() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if (user != null) {
            user.delete()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "User account deleted.");
                        }
                    });
        }
    }

    public Task<DocumentSnapshot> getDataOfDocument(String id) {
        return firestoreDB.collection("user").document(id).get();
    }

    public Task<Void> createCollection(String id, String name, String collectionsId) {
        return firestoreDB.collection("user").document(id).update("collections", FieldValue.arrayUnion(new Collections(collectionsId, new ArrayList<>(), name)));
    }

    public Task<QuerySnapshot> getCollectionData(String id, String collectionId) {
        return firestoreDB.collection("user").document(id).collection("collections").whereEqualTo("id", collectionId).get();
    }

    public Task<Void> updateCollection(ArrayList<Collections> collections, String document) {
        return firestoreDB.collection("user").document(document).update("collections", collections);
    }
}
