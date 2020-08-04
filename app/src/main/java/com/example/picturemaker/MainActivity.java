package com.example.picturemaker;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.example.picturemaker.ui.main.GalleryFragment;
import com.example.picturemaker.ui.main.HomeFragment;
import com.example.picturemaker.ui.main.ProfileFragment;
import com.example.picturemaker.ui.main.RecentlyFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {


    final Fragment fragment1 = new HomeFragment();
    final Fragment fragment2 = new GalleryFragment();
    final Fragment fragment3 = new RecentlyFragment();
    final Fragment fragment4 = new ProfileFragment();
    final FragmentManager fm = getSupportFragmentManager();
    Fragment active = fragment1;
    private FirebaseAuth mAuth;

    public void openFragment(Fragment fragment) {
        fm.beginTransaction().hide(active).show(fragment).commit();
        active = fragment;

//        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
//        fragmentTransaction.replace(R.id.rating_picture, fragment);
////        fragmentTransaction.addToBackStack(null);
//        fragmentTransaction.commit();

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fm.beginTransaction().add(R.id.container, fragment4, "4").hide(fragment4).commit();
        fm.beginTransaction().add(R.id.container, fragment3, "3").hide(fragment3).commit();
        fm.beginTransaction().add(R.id.container, fragment2, "2").hide(fragment2).commit();
        fm.beginTransaction().add(R.id.container, fragment1, "1").commit();

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.action_home:
//                        openFragment(new HomeFragment());
                        openFragment(fragment1);
                        break;
                    case R.id.action_gallery:
//                        openFragment(new GalleryFragment());
                        openFragment(fragment2);
                        break;
                    case R.id.action_recently:
//                        openFragment(new RecentlyFragment());
                        openFragment(fragment3);
                        break;
                    case R.id.action_profile:
//                        openFragment(new ProfileFragment());
                        openFragment(fragment4);
                        break;
                }
                return true;
            }
        });

    }
}
