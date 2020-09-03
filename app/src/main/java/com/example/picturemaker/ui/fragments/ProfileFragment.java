package com.example.picturemaker.ui.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.picturemaker.R;
import com.example.picturemaker.storage.Storage;
import com.google.firebase.auth.FirebaseUser;

import java.util.Objects;

public class ProfileFragment extends Fragment {


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
        Storage storage = Storage.getInstance(this.getContext());
        FirebaseUser user = storage.getUser();
        TextView tvUsername = Objects.requireNonNull(this.getActivity()).findViewById(R.id.textView10);
        tvUsername.setText("id: ".concat(user.getUid()));

    }

}