package com.example.picturemaker;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

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

    static private StorageReference storageRef = FirebaseStorage.getInstance().getReference();;
    static private DatabaseReference databaseRef = FirebaseDatabase.getInstance().getReference("pictures");

    static private long MEGABYT = 1024*1024;

    static public void loadItem(String name, Consumer<List<Item>> foo){
        Query picturesQuery = Firebase.databaseRef.orderByChild(name);

        picturesQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<Item> items = new ArrayList<>();
                for(DataSnapshot singleSnapshot : dataSnapshot.getChildren()){
                    Item item = singleSnapshot.getValue(Item.class);
                    item.public_id = singleSnapshot.getKey();
                    item.pictureBits = Firebase.GetPicture(item.public_picture);
                    items.add(item);
                }
                foo.accept(items);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    static public Bitmap GetPicture(String name){
        StorageReference imageRefl = storageRef.child("pictures/".concat(name));
        final Bitmap[] bitmap = new Bitmap[1];
        imageRefl.getBytes(MEGABYT).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {
                bitmap[0] = BitmapFactory.decodeByteArray(bytes,0,bytes.length);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });
        return bitmap[0];
    }

}
