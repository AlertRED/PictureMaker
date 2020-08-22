package com.example.picturemaker.ui.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.picturemaker.R;
import com.example.picturemaker.storage.Storage;
import com.google.firebase.auth.FirebaseUser;

public class ProfileFragment extends Fragment {
    private Storage storage;

//    private MainViewModel mViewModel;

    public static ProfileFragment newInstance() {
        return new ProfileFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        this.storage = Storage.getInstance(this.getContext());
        FirebaseUser user = this.storage.getUser();
        TextView tvUsername = this.getActivity().findViewById(R.id.textView10);
        tvUsername.setText("id: ".concat(user.getUid()));
    }

}