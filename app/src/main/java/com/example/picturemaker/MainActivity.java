package com.example.picturemaker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ImageView;

import com.example.picturemaker.support.TestData;
import com.example.picturemaker.ui.main.GalleryFragment;
import com.example.picturemaker.ui.main.HomeFragment;
import com.example.picturemaker.ui.main.ProfileFragment;
import com.example.picturemaker.ui.main.RecentlyFragment;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class MainActivity extends AppCompatActivity {

//    final Fragment fragment1 = new HomeFragment();
//    final Fragment fragment2 = new GalleryFragment();
//    final Fragment fragment3 = new RecentlyFragment();
//    final Fragment fragment4 = new ProfileFragment();
//    final FragmentManager fm = getSupportFragmentManager();
//    Fragment active = fragment1;

    public void openFragment(Fragment fragment) {
//        fm.beginTransaction().hide(active).show(fragment).commit();
//        active = fragment;

        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.rating_picture, fragment);
//        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();

//        FragmentTransaction ft = getFragmentManager().beginTransaction();
//        ft.add(R.id.fragment2, frag2);
//        ft.commit();
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        fm.beginTransaction().add(R.id.container, fragment4, "4").hide(fragment4).commit();
//        fm.beginTransaction().add(R.id.container, fragment3, "3").hide(fragment3).commit();
//        fm.beginTransaction().add(R.id.container, fragment2, "2").hide(fragment2).commit();
//        fm.beginTransaction().add(R.id.container,fragment1, "1").commit();

        TestData.generate();

        FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();
        StorageReference storageReference = firebaseStorage.getReference();

        StorageReference imageRefl = storageReference.child("pictures/m1.jpg");
        final ImageView image = findViewById(R.id.imageView);
        long MAX_BYTES = 1024*1024;
        imageRefl.getBytes(MAX_BYTES).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {
                Bitmap bitmap = BitmapFactory.decodeByteArray(bytes,0,bytes.length);
                image.setImageBitmap(bitmap);
                Log.i("TAG", "onSuccess: ");
            }
        });
//        .addOnFailureListener(new OnFailureListener() {
//            @Override
//            public void onFailure(@NonNull Exception e) {
//
//            }
//        });

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("message");

        myRef.setValue("111");



//        openFragment(new HomeFragment());

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.action_home:
                        openFragment(new HomeFragment());
                        break;
                    case R.id.action_gallery:
                        openFragment(new GalleryFragment());
                        break;
                    case R.id.action_recently:
                        openFragment(new RecentlyFragment());
                        break;
                    case R.id.action_profile:
                        openFragment(new ProfileFragment());
                        break;
                }
                return true;
            }
        });

    }
}
