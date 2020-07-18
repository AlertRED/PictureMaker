package com.example.picturemaker;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.util.Consumer;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
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
import java.util.List;

public class FirebaseDB {

    static private FirebaseStorage fStorage;
    static private FirebaseDatabase fDatabase;
    static private FirebaseAuth fAuth;

    static private long MEGABYT = 1024*1024;

    static private FirebaseDatabase getDatabase(){
        if (fDatabase == null) {
            fDatabase = FirebaseDatabase.getInstance();
            fDatabase.setPersistenceEnabled(true);
        }
        return fDatabase;
    }

    static private FirebaseStorage getStorage(){
        if (fStorage == null) {
            fStorage = FirebaseStorage.getInstance();
        }
        return fStorage;
    }

    static private FirebaseAuth getAuth(){
        if (fAuth == null) {
            fAuth = FirebaseAuth.getInstance();
        }
        return fAuth;
    }

    static public void login(Activity activity){
        FirebaseAuth auth = getAuth();
        FirebaseUser currentUser = auth.getCurrentUser();
        if (currentUser==null){
            auth.signInAnonymously().addOnCompleteListener(activity, task -> {
                if (task.isSuccessful()) {
                    Log.d("TAG", "signInAnonymously:success");
                } else {
                    Log.w("TAG", "signInAnonymously:failure", task.getException());
                }
            });
        }
    }

    static public FirebaseUser getUser(){
        return getAuth().getCurrentUser();
    }

    static public void loadItem(Consumer<List<Item>> foo){
        DatabaseReference ref = getDatabase().getReference("pictures");
        ref.keepSynced(true);
        Query picturesQuery = ref;

        picturesQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<Item> items = new ArrayList<>();
                for(DataSnapshot singleSnapshot : dataSnapshot.getChildren()){
                    Item item = singleSnapshot.getValue(Item.class);
                    item.public_id = singleSnapshot.getKey();
                    items.add(item);
                }
                foo.accept(items);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    static public void loadPicture(String name, ImageView image){
        StorageReference imageRefl = getStorage().getReference().child("pictures/".concat(name));
        imageRefl.getBytes(MEGABYT).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {
                Bitmap bitmap = BitmapFactory.decodeByteArray(bytes,0,bytes.length);
                image.setImageBitmap(bitmap);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });
    }

    static public void loadPicture(Context context,String name, Consumer<Bitmap> foo){
        StorageReference imageRef = getStorage().getReference().child("pictures/".concat(name));




        imageRef.getBytes(MEGABYT).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {
                Bitmap bitmap = BitmapFactory.decodeByteArray(bytes,0,bytes.length);
                foo.accept(bitmap);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });
    }

}
