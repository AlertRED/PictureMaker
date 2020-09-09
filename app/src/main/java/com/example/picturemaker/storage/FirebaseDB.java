package com.example.picturemaker.storage;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.core.util.Consumer;

import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.example.picturemaker.R;
import com.example.picturemaker.storage.room_tables.Picture;
import com.example.picturemaker.support.GlideApp;
import com.example.picturemaker.support.GlideRequests;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FieldPath;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreSettings;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.TreeMap;

import static android.content.ContentValues.TAG;


public class FirebaseDB {

    private static FirebaseDB INSTANCE;
    private final FirebaseFirestore fFirestore;
    public FirebaseStorage fStorage;
    private FirebaseAuth fAuth;

    private FirebaseDB() {
        this.fStorage = FirebaseStorage.getInstance();
        this.fAuth = FirebaseAuth.getInstance();
        this.fFirestore = FirebaseFirestore.getInstance();
        FirebaseFirestoreSettings settings = new FirebaseFirestoreSettings.Builder()
                .setPersistenceEnabled(false)
                .build();
        this.fFirestore.setFirestoreSettings(settings);
    }

    static FirebaseDB getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new FirebaseDB();
        }
        return INSTANCE;
    }

    public FirebaseUser getUser() {
        return this.fAuth.getCurrentUser();
    }

    public void login(Activity activity, Runnable foo) {
        FirebaseUser currentUser = fAuth.getCurrentUser();
        if (currentUser == null) {
            fAuth.signInAnonymously().addOnCompleteListener(activity, task -> {
                if (task.isSuccessful())
                    foo.run();
            });
        } else {
            foo.run();
        }
    }

    public void loadAuthors(Consumer<Map<String, Integer>> foo) {
        loadFilter(foo, "authors");
    }

    public void loadLevels(Consumer<Map<String, Integer>> foo) {
        loadFilter(foo, "levels");
    }

    public void loadGenres(Consumer<Map<String, Integer>> foo) {
        loadFilter(foo, "genres");
    }

    private void loadFilter(Consumer<Map<String, Integer>> foo, String filter_name) {
        com.google.firebase.firestore.Query query = this.fFirestore.collection(filter_name);
        query.get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Map<String, Integer> filters = new TreeMap<>();
                        for (QueryDocumentSnapshot document : Objects.requireNonNull(task.getResult())) {
                            String filter = (String) document.getData().get("en");
                            int id = Integer.parseInt(document.getId());
                            filters.put(filter, id);
                        }
                        foo.accept(filters);
                    } else {
                        Log.w(TAG, "Error getting documents.", task.getException());
                    }
                });
    }

    public void SetFavoritePicture(String picture_id, boolean is_favorite, Runnable foo) {
        String uid = fAuth.getUid();
        Map<String, Object> picture = new HashMap<>();
        picture.put("is_favorite", is_favorite);
        picture.put("picture_id", picture_id);
        picture.put("user_id", uid);
        assert uid != null;
        this.fFirestore.collection("users-pictures")
                .document(uid + picture_id)
                .set(picture)
                .addOnSuccessListener(aVoid -> foo.run());
    }

    public void LoadPictures(Consumer<List<Picture>> foo, Map<String, Object> parameters) {

        com.google.firebase.firestore.Query query = this.fFirestore.collection("pictures");

        if (parameters.containsKey("picture_id")) {
            String picture_id = (String) parameters.get("picture_id");
            query = query.whereEqualTo(FieldPath.documentId(), picture_id);
        }
        if (parameters.containsKey("is_popular")) {
            Boolean is_popular = (Boolean) parameters.get("is_popular");
            query = query.whereEqualTo("is_popular", is_popular);
        }
        if (parameters.containsKey("is_last")) {
            Integer count = (Integer) parameters.get("count");
            assert count != null;
            query = query.orderBy("_id").limit(count);
        }
        if (parameters.containsKey("level_id")) {
            Integer level = (Integer) parameters.get("level_id");
            query = query.whereEqualTo("level_id", level);
        }
        if (parameters.containsKey("author_id")) {
            Integer author = (Integer) parameters.get("author_id");
            query = query.whereEqualTo("author_id", author);
        }
        if (parameters.containsKey("genre_id")) {
            Integer genre = (Integer) parameters.get("genre_id");
            query = query.whereEqualTo("genre_id", genre);
        }
        if (parameters.containsKey("order_field")) {
            String order_field = (String) parameters.get("order_field");
            Boolean is_asc = (Boolean) parameters.get("is_asc");
            assert is_asc != null;
            assert order_field != null;
            Query.Direction type_sort = is_asc ? Query.Direction.ASCENDING : Query.Direction.DESCENDING;
            System.err.println(is_asc);
            query = query.orderBy(order_field, type_sort);
        }

        query.get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        List<Picture> pictures = new ArrayList<>();
                        for (QueryDocumentSnapshot document : Objects.requireNonNull(task.getResult())) {
                            Picture picture = document.toObject(Picture.class);
                            picture.public_id = document.getId();
                            picture.name = (String) document.getData().get("name_en");
                            pictures.add(picture);
                        }
                        foo.accept(pictures);
                    } else {
                        Log.w(TAG, "Error getting documents.", task.getException());
                    }
                });
    }

    public void getFavoriteIds(Consumer<List<String>> foo) {
        String uid = this.getUser().getUid();
        com.google.firebase.firestore.Query query = this.fFirestore.collection("users-pictures");
        query = query.whereEqualTo("user_id", uid).whereEqualTo("is_favorite", true);
        getFromUserPicture(foo, query);
    }

    public void getProcessIds(Consumer<List<String>> foo) {
        String uid = this.getUser().getUid();
        com.google.firebase.firestore.Query query = this.fFirestore.collection("users-pictures");
        query = query.whereEqualTo("user_id", uid).whereEqualTo("is_process", true);
        getFromUserPicture(foo, query);
    }

    public void getFinishedIds(Consumer<List<String>> foo) {
        String uid = this.getUser().getUid();
        com.google.firebase.firestore.Query query = this.fFirestore.collection("users-pictures");
        query = query.whereEqualTo("user_id", uid).whereEqualTo("is_finish", true);
        getFromUserPicture(foo, query);
    }

    public void getFromUserPicture(Consumer<List<String>> foo, com.google.firebase.firestore.Query query) {
        query.get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        List<String> pictures = new ArrayList<>();
                        for (QueryDocumentSnapshot document : Objects.requireNonNull(task.getResult())) {
                            String picture_id = (String) document.getData().get("picture_id");
                            pictures.add(picture_id);
                        }
                        foo.accept(pictures);
                    } else {
                        Log.w(TAG, "Error getting documents.", task.getException());
                    }
                });
    }

    public void loadImage(Context context, String name, ImageView imageView, boolean is_disk_cache) {
        StorageReference imageRef = fStorage.getReference().child("pictures/".concat(name));
        DiskCacheStrategy cache_type = is_disk_cache ? DiskCacheStrategy.ALL : DiskCacheStrategy.ALL;
        RequestOptions requestOptions = new RequestOptions().placeholder(R.drawable.bg_load_image);
        GlideRequests glide = GlideApp.with(context);
        glide.asBitmap().diskCacheStrategy(cache_type).apply(requestOptions).load(imageRef).into(imageView);
    }

    public void loadImage(Context context, String name, Consumer<Bitmap> foo, boolean is_disk_cache) {
        StorageReference imageRef = fStorage.getReference().child("pictures/".concat(name));
        DiskCacheStrategy cache_type = is_disk_cache ? DiskCacheStrategy.ALL : DiskCacheStrategy.ALL;
        GlideRequests glide = GlideApp.with(context);
        glide.asBitmap().diskCacheStrategy(cache_type).load(imageRef).listener(new RequestListener<Bitmap>() {
            @Override
            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Bitmap> target, boolean isFirstResource) {
                return false;
            }

            @Override
            public boolean onResourceReady(Bitmap resource, Object model, Target<Bitmap> target, DataSource dataSource, boolean isFirstResource) {
                foo.accept(resource);
                return false;
            }
        }).submit();
    }
}
