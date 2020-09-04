package com.example.picturemaker.ui.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.example.picturemaker.R;
import com.example.picturemaker.ui.fragments.collection_tabs.TabFragment;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.util.Objects;

class ViewPagerFragmentAdapter extends FragmentStateAdapter {

    public ViewPagerFragmentAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 1:
                return new TabFragment("Process");
            case 2:
                return new TabFragment("Finish");
            default:
                return new TabFragment("Favorites");
        }
    }

    @Override
    public int getItemCount() {
        return 3;
    }
}

public class CollectionFragment extends Fragment {



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_collection, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        String[] titles = new String[]{this.getResources().getString(R.string.collection_tab_favorite),
                this.getResources().getString(R.string.collection_tab_in_process),
                this.getResources().getString(R.string.collection_tab_finished)};

        TabLayout tabLayout = Objects.requireNonNull(this.getActivity()).findViewById(R.id.tab_layout);
        ViewPager2 viewPager = this.getActivity().findViewById(R.id.view_pager);
        viewPager.setAdapter(new ViewPagerFragmentAdapter(this.getActivity()));

        new TabLayoutMediator(tabLayout, viewPager,
                (tab, position) -> tab.setText(titles[position])).attach();
    }


}