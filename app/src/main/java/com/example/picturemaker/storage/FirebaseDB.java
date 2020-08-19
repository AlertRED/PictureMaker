package com.example.picturemaker.storage;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.arch.core.util.Function;
import androidx.core.util.Consumer;

import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.target.Target;
import com.bumptech.glide.request.transition.Transition;
import com.example.picturemaker.R;
import com.example.picturemaker.support.GlideApp;
import com.example.picturemaker.support.GlideRequests;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;


public class FirebaseDB {

    public FirebaseStorage fStorage;
    private FirebaseDatabase fDatabase;
    private FirebaseAuth fAuth;
    private static FirebaseDB INSTANCE;

    private FirebaseDB(Context context) {
        this.fDatabase = FirebaseDatabase.getInstance();
        this.fDatabase.setPersistenceEnabled(true);
        this.fStorage = FirebaseStorage.getInstance();
        this.fAuth = FirebaseAuth.getInstance();
    }

    static FirebaseDB getInstance(Context context){
        if (INSTANCE == null){
            INSTANCE = new FirebaseDB(context);
        }
        return INSTANCE;
    }

    public FirebaseUser getUser(){
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

    public void loadAuthors(Consumer<List<String>> foo) {
        loadFilter(foo, "authors");
    }

    public void loadGenres(Consumer<List<String>> foo) {
        loadFilter(foo, "genres");
    }

    private void loadFilter(Consumer<List<String>> foo, String filter_name){
        DatabaseReference ref = fDatabase.getReference("genres");
        ref.keepSynced(true);
        Query query = ref;
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<String> genres = new ArrayList<>();
                for (DataSnapshot singleSnapshot : dataSnapshot.getChildren()) {
                    String genre = (String) singleSnapshot.getValue();
                    genres.add(genre);
                }
                foo.accept(genres);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void SetFavoritePicture(String picture_id, boolean is_favorite, Runnable foo) {
        String uid = fAuth.getUid();
        DatabaseReference ref = fDatabase.getReference("likes").child("user-".concat(uid)).child(picture_id).child("is_favorite");
        ref.keepSynced(false);
        if (is_favorite) {
            ref.setValue(true).addOnSuccessListener(aVoid -> foo.run());
        } else {
            ref.removeValue().addOnSuccessListener(aVoid -> foo.run());
        }
    }

    public void LoadPictures(Consumer<List<Picture>> foo, Map<String, Object> parameters) {
        DatabaseReference ref = fDatabase.getReference("pictures");
        ref.keepSynced(true);
        Query picturesQuery = ref;

        if (parameters.containsKey("picture_id")) {
            String is_popular = (String) parameters.get("picture_id");
            picturesQuery = ref.orderByKey().equalTo(is_popular);
        }
        if (parameters.containsKey("is_popular")) {
            Boolean is_popular = (Boolean) parameters.get("is_popular");
            picturesQuery = ref.orderByChild("is_popular").equalTo(is_popular);
        }
        if (parameters.containsKey("is_last")) {
            Integer count = (Integer) parameters.get("count");
            picturesQuery = ref.limitToLast(count);
        }
        if (parameters.containsKey("level")) {
            Integer level = (Integer) parameters.get("level");
            picturesQuery = ref.orderByChild("level").startAt(level).endAt(level);
        }
        if (parameters.containsKey("author")) {
            String author = (String) parameters.get("author");
            picturesQuery = ref.orderByChild("author").equalTo(author);
        }
        if (parameters.containsKey("genre")) {
            String genre = (String) parameters.get("genre");
            picturesQuery = ref.orderByChild("genre").equalTo(genre);
        }

        picturesQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<Picture> pictures = new ArrayList<>();
                for (DataSnapshot singleSnapshot : dataSnapshot.getChildren()) {
                    Picture picture = singleSnapshot.getValue(Picture.class);
                    picture.public_id = singleSnapshot.getKey();
                    pictures.add(picture);
                }
                foo.accept(pictures);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    public void getFavoriteIds(Consumer<List<String>> foo){
        String uid = this.getUser().getUid();
        DatabaseReference ref = fDatabase.getReference("likes").child("user-".concat(uid));
        Query picturesQuery = ref.orderByChild("is_favorite").equalTo(true);
        ref.keepSynced(true);

        picturesQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<String> pictureIds = new ArrayList<>();
                for (DataSnapshot singleSnapshot : dataSnapshot.getChildren()) {
                    pictureIds.add(singleSnapshot.getKey());
                }
                foo.accept(pictureIds);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }


    private void returnData(DataSnapshot dataSnapshot, Consumer<List<Picture>> foo){

    }

    public void loadImage(Context context, String name, ImageView imageView, boolean is_disk_cache) {
        StorageReference imageRef = fStorage.getReference().child("pictures/".concat(name));
        DiskCacheStrategy cache_type = is_disk_cache ? DiskCacheStrategy.ALL : DiskCacheStrategy.ALL;
        RequestOptions requestOptions = new RequestOptions().placeholder(R.drawable.bg_load_image);
        GlideRequests glide = GlideApp.with(context);
        glide.asBitmap().diskCacheStrategy(cache_type).apply(requestOptions).load(imageRef).into(imageView);
    }

//    public void loadImage(Context context, String name, Function2<Integer,Integer> foo, boolean is_disk_cache) {
//        StorageReference imageRef = fStorage.getReference().child("pictures/".concat(name));
//        DiskCacheStrategy cache_type = is_disk_cache ? DiskCacheStrategy.ALL : DiskCacheStrategy.ALL;
//        GlideRequests glide = GlideApp.with(context);
//
//        glide.asBitmap().diskCacheStrategy(cache_type).load(imageRef).listener(new RequestListener<Bitmap>() {
//            @Override
//            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Bitmap> target, boolean isFirstResource) {
//                return false;
//            }
//
//            @Override
//            public boolean onResourceReady(Bitmap resource, Object model, Target<Bitmap> target, DataSource dataSource, boolean isFirstResource) {
//                foo.apply(resource.getHeight(), resource.getWidth());
//                return false;
//            }
//        }).submit();
//    }
}
