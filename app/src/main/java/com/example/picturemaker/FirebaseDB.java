package com.example.picturemaker;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
import com.example.picturemaker.support.Item;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
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

    static public void likePicture(String picture_id, boolean is_like) {
        String uid = getUser().getUid();
        DatabaseReference ref = getDatabase().getReference("likes").child("user-".concat(uid)).child(picture_id).child("is_favorite");

        if (is_like){
            ref.setValue(true);
        } else {
            ref.removeValue();
        }
    }

    static public void loadItems(Consumer<List<Item>> foo) {
        DatabaseReference ref = getDatabase().getReference("pictures");
        ref.keepSynced(true);
        Query picturesQuery = ref;

        picturesQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<Item> items = new ArrayList<>();
                Dictionary<String, Integer> id_and_key = new Hashtable<>();
                for (DataSnapshot singleSnapshot : dataSnapshot.getChildren()) {
                    Item item = singleSnapshot.getValue(Item.class);
                    item.public_id = singleSnapshot.getKey();
                    items.add(item);
                    id_and_key.put(item.public_id, items.size()-1);
                }
                DatabaseReference ref = getDatabase().getReference("likes").child("user-".concat(getUser().getUid()));
                ref.keepSynced(true);
                Query personalPicturesQuery = ref;
                personalPicturesQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot singleSnapshot : snapshot.getChildren()) {
                            Integer index = id_and_key.remove(singleSnapshot.getKey());
                            if (singleSnapshot.child("is_favorite").exists())
                                items.get(index).is_favorite = singleSnapshot.child("is_favorite").getValue(Boolean.class);
                            if (singleSnapshot.child("stars").exists())
                                items.get(index).score = singleSnapshot.child("stars").getValue(Integer.class);
                        }
                        foo.accept(items);
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

    static public void loadItem(Consumer<Item> foo, String name) {
        DatabaseReference ref = getDatabase().getReference("pictures");
        ref.keepSynced(true);
        Query picturesQuery = ref.orderByChild("name").equalTo(name);

        picturesQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Item item = null;
                for (DataSnapshot singleSnapshot : dataSnapshot.getChildren()) {
                    item = singleSnapshot.getValue(Item.class);
                    item.public_id = singleSnapshot.getKey();
                    break;
                }

                DatabaseReference ref = getDatabase().getReference("likes").child("user-".concat(getUser().getUid())).child(item.public_id);
                ref.keepSynced(true);
                Query personalPicturesQuery = ref;
                Item finalItem = item;
                personalPicturesQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.child("is_favorite").exists())
                            finalItem.is_favorite = snapshot.child("is_favorite").getValue(Boolean.class);
                        if (snapshot.child("stars").exists())
                            finalItem.score = snapshot.child("stars").getValue(Integer.class);
                        foo.accept(finalItem);
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


//        imageRefl.getBytes(MEGABYT).addOnSuccessListener(new OnSuccessListener<byte[]>() {
//            @Override
//            public void onSuccess(byte[] bytes) {
//                Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
//                image.setImageBitmap(bitmap);
//            }
//        }).addOnFailureListener(new OnFailureListener() {
//            @Override
//            public void onFailure(@NonNull Exception e) {
//
//            }
//        });
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

//        Glide.with(context).asBitmap().load(imageRef.toString())
//                .into(new CustomTarget<Bitmap>() {
//                    @Override
//                    public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
//                        foo.accept(resource);
//                    }
//
//                    @Override
//                    public void onLoadCleared(@Nullable Drawable placeholder) {
//                    }
//
//                    @Override
//                    public void onLoadFailed(@Nullable Drawable errorDrawable) {
//                        super.onLoadFailed(errorDrawable);
//                    }
//                });


//        imageRef.getBytes(MEGABYT).addOnSuccessListener(new OnSuccessListener<byte[]>() {
//            @Override
//            public void onSuccess(byte[] bytes) {
//                Bitmap bitmap = BitmapFactory.decodeByteArray(bytes,0,bytes.length);
//                foo.accept(bitmap);
//            }
//        }).addOnFailureListener(new OnFailureListener() {
//            @Override
//            public void onFailure(@NonNull Exception e) {
//
//            }
//        });
    }

}
