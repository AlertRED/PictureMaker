package com.example.picturemaker;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.core.util.Consumer;

import com.example.picturemaker.support.Item;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
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

public class Firebase {

    static private StorageReference fStorage = FirebaseStorage.getInstance().getReference();
    static private DatabaseReference fDatabase = FirebaseDatabase.getInstance().getReference("pictures");

    static private long MEGABYT = 1024*1024;

//    static private FirebaseDatabase getDatabase(){
//        if (fDatabase == null) {
//            fDatabase = FirebaseDatabase.getInstance();
//            fDatabase.setPersistenceEnabled(true);
//        }
//        return fDatabase;
//    }
//
//    static private FirebaseStorage getStorage(){
//        if (fStorage == null) {
//            fStorage = FirebaseStorage.getInstance();
//        }
//        return fStorage;
//    }


    static public void loadItem(Consumer<List<Item>> foo){
        Query picturesQuery = fDatabase;

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
        StorageReference imageRefl = fStorage.child("pictures/".concat(name));
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

    static public void loadPicture(String name, Consumer<Bitmap> foo){
        StorageReference imageRefl = fStorage.child("pictures/".concat(name));
        imageRefl.getBytes(MEGABYT).addOnSuccessListener(new OnSuccessListener<byte[]>() {
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
