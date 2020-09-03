package com.example.picturemaker;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.example.picturemaker.ui.fragments.CollectionFragment;
import com.example.picturemaker.ui.fragments.GalleryFragment;
import com.example.picturemaker.ui.fragments.HomeFragment;
import com.example.picturemaker.ui.fragments.ProfileFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {


    final Fragment fragment1 = new HomeFragment();
    final Fragment fragment2 = new GalleryFragment();
    final Fragment fragment3 = new CollectionFragment();
    final Fragment fragment4 = new ProfileFragment();
    final FragmentManager fm = getSupportFragmentManager();
    Fragment active = fragment1;

    public void openFragment(Fragment fragment) {
        fm.beginTransaction().hide(active).show(fragment).commit();
        active = fragment;
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
        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.action_home:
                    openFragment(fragment1);
                    break;
                case R.id.action_gallery:
                    openFragment(fragment2);
                    break;
                case R.id.action_recently:
                    openFragment(fragment3);
                    break;
            }
            return true;
        });

    }
}
