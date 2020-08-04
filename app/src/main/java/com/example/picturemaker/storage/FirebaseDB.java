package com.example.picturemaker.storage;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.util.Consumer;

import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
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
import java.util.Dictionary;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

public class FirebaseDB {

    static private FirebaseStorage fStorage;
    static private FirebaseDatabase fDatabase;
    static private FirebaseAuth fAuth;

    static private long MEGABYT = 1024 * 1024;

    static private FirebaseDatabase getDatabase() {
        if (fDatabase == null) {
            fDatabase = FirebaseDatabase.getInstance();
            fDatabase.setPersistenceEnabled(true);
        }
        return fDatabase;
    }

    static private FirebaseStorage getStorage() {
        if (fStorage == null) {
            fStorage = FirebaseStorage.getInstance();
        }
        return fStorage;
    }

    static private FirebaseAuth getAuth() {
        if (fAuth == null) {
            fAuth = FirebaseAuth.getInstance();
        }
        return fAuth;
    }

    static public FirebaseUser getUser() {
        return getAuth().getCurrentUser();
    }

    static public void login(Activity activity, Runnable foo) {
        FirebaseAuth auth = getAuth();
        FirebaseUser currentUser = auth.getCurrentUser();
        if (currentUser == null) {
            auth.signInAnonymously().addOnCompleteListener(activity, task -> {
                if (task.isSuccessful()) {
                    foo.run();
                } else {
                }
            });
        } else {
            foo.run();
        }
    }

    static public void loadAuthors(Consumer<List<String>> foo) {
        DatabaseReference ref = getDatabase().getReference("authors");
        ref.keepSynced(true);
        Query query = ref;
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<String> authors = new ArrayList<>();
                for (DataSnapshot singleSnapshot : dataSnapshot.getChildren()) {
                    String author = (String) singleSnapshot.getValue();
                    authors.add(author);
                }
                foo.accept(authors);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    static public void loadGenres(Consumer<List<String>> foo) {
        DatabaseReference ref = getDatabase().getReference("genres");
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


    static public void SetFavoritePicture(String picture_id, boolean is_favorite, Runnable foo) {
        String uid = getUser().getUid();
        DatabaseReference ref = getDatabase().getReference("likes").child("user-".concat(uid)).child(picture_id).child("is_favorite");

        if (is_favorite) {
            ref.setValue(true).addOnSuccessListener(aVoid -> foo.run());
        } else {
            ref.removeValue().addOnSuccessListener(aVoid -> foo.run());
        }
    }

    static public void LoadPictures(Consumer<List<Picture>> foo, Map<String, Object> parameters) {
        DatabaseReference ref = getDatabase().getReference("pictures");
        ref.keepSynced(true);
        Query picturesQuery = ref;

        if (parameters.containsKey("is_popular")) {
            String is_popular = (String) parameters.get("is_popular");
            picturesQuery = ref.orderByChild("is_popular").equalTo(is_popular);
        }
        if (parameters.containsKey("level")) {
            int level = (int) parameters.get("level");
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
//        if (parameters.containsKey("is_popular"))
//            picturesQuery = ref.orderByChild("is_popular").equalTo((boolean) parameters.get("is_popular"));

        picturesQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<Picture> pictures = new ArrayList<>();
                Dictionary<String, Integer> id_and_key = new Hashtable<>();
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

    static public void loadItem(Consumer<Picture> foo, String name) {
        DatabaseReference ref = getDatabase().getReference("pictures");
        ref.keepSynced(true);
        Query picturesQuery = ref.orderByChild("name").equalTo(name);

        picturesQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Picture picture = null;
                for (DataSnapshot singleSnapshot : dataSnapshot.getChildren()) {
                    picture = singleSnapshot.getValue(Picture.class);
                    picture.public_id = singleSnapshot.getKey();
                    break;
                }

                DatabaseReference ref = getDatabase().getReference("likes").child("user-".concat(getUser().getUid())).child(picture.public_id);
                ref.keepSynced(true);
                Query personalPicturesQuery = ref;
                Picture finalPicture = picture;
                personalPicturesQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.child("is_favorite").exists())
                            finalPicture.is_favorite = snapshot.child("is_favorite").getValue(Boolean.class);
                        if (snapshot.child("stars").exists())
                            finalPicture.score = snapshot.child("stars").getValue(Integer.class);
                        foo.accept(finalPicture);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    static public void loadPicture(Context context, String name, ImageView image, boolean is_disk_cache) {
        StorageReference imageRef = getStorage().getReference().child("pictures/".concat(name));
        GlideRequests glide = GlideApp.with(context);
        DiskCacheStrategy cache_type = is_disk_cache ? DiskCacheStrategy.ALL : DiskCacheStrategy.NONE;
        glide.asBitmap().diskCacheStrategy(cache_type).load(imageRef)
                .into(new CustomTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                        image.setImageBitmap(resource);
                    }

                    @Override
                    public void onLoadCleared(@Nullable Drawable placeholder) {
                    }

                    @Override
                    public void onLoadFailed(@Nullable Drawable errorDrawable) {
                        super.onLoadFailed(errorDrawable);
                    }
                });
    }

    static public void loadPicture(Context context, String name, Consumer<Bitmap> foo, boolean is_disk_cache) {
        StorageReference imageRef = getStorage().getReference().child("pictures/".concat(name));
        GlideRequests glide = GlideApp.with(context);
        DiskCacheStrategy cache_type = is_disk_cache ? DiskCacheStrategy.ALL : DiskCacheStrategy.NONE;
        glide.asBitmap().diskCacheStrategy(cache_type).load(imageRef)
                .into(new CustomTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                        foo.accept(resource);
                    }

                    @Override
                    public void onLoadCleared(@Nullable Drawable placeholder) {
                    }

                    @Override
                    public void onLoadFailed(@Nullable Drawable errorDrawable) {
                        super.onLoadFailed(errorDrawable);

                    }
                });
    }

}
