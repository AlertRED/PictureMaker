package com.example.picturemaker.ui.main;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SimpleItemAnimator;

import com.example.picturemaker.PictureActivity;
import com.example.picturemaker.R;
import com.example.picturemaker.adapters.AdapterHomeTopRV;
import com.example.picturemaker.storage.Picture;
import com.example.picturemaker.storage.Storage;
import com.example.picturemaker.support.PictureDiffUtilCallback;

import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Set;

class HomeHolder {
    private ImageView image;
    private TextView title;
    private ImageView favorite;
    private View layer;

    public HomeHolder(View view) {
        this.image = view.findViewById(R.id.imageview);
        this.title = view.findViewById(R.id.picture_name);
        this.favorite = view.findViewById(R.id.favorite_image_item_home);
        this.layer = view;
//        this.layer.setAlpha(0);
    }

    public ImageView getImage() {
        return image;
    }

    private void setImage(Bitmap bitmap) {
        this.image.setImageBitmap(bitmap);
//        this.layer.animate().alpha(1f).setDuration(250);
    }

    public void loadImage(Context context, String name) {
        Storage.getInstance(context).GetImage(context, name, this::setImage);
    }

    public View getLayer() {
        return layer;
    }

    public TextView getTitle() {
        return title;
    }

    public void setTitle(TextView title) {
        this.title = title;
    }

    public ImageView getFavorite() {
        return favorite;
    }

    public void setFavorite(ImageView favorite) {
        this.favorite = favorite;
    }

}

public class HomeFragment extends Fragment {

    Storage storage;
    private RecyclerView rv_top;
    private AdapterHomeTopRV rv_top_adapter;
    private LinearLayout home_ll;
    private Map<Picture, HomeHolder> picturesAndLayers;

    public static HomeFragment newInstance() {
        return new HomeFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }


    private void RefreshInfo(HomeHolder holder, Picture picture) {
        holder.loadImage(this.getContext(), picture.public_picture);
        holder.getTitle().setText(picture.name);
        holder.getFavorite().setImageResource(picture.is_favorite ? R.drawable.ic_favorite_36 : R.drawable.ic_unfavorite_36);

        holder.getFavorite().setOnClickListener(v -> {
            storage.SetFavoritePicture(picture.id, !picture.is_favorite);
        });

        holder.getLayer().setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), PictureActivity.class);
            intent.putExtra("pictureId", picture.id);
            startActivity(intent);
        });
    }

    private View addView(Picture picture) {
        LayoutInflater inflater = getLayoutInflater();
        View layer = inflater.inflate(R.layout.item_pictute_popular, null);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT);
        params.setMargins(20, 10, 20, 10);
        layer.setLayoutParams(params);
        home_ll.addView(layer);
        return layer;
    }

    private boolean ComparePicture(Picture new_picture, Picture old_picture) {
        return (new_picture.is_favorite == old_picture.is_favorite
                && new_picture.public_picture.equals(old_picture.public_picture));
    }

    public void ShowPictures(List<Picture> pictures) {
        Map<Picture, HomeHolder> newPicturesAndHolders = new Hashtable<>();
        for (Picture new_picture : pictures) {
            Picture old_picture = null;
            HomeHolder holder = null;
            for (Map.Entry<Picture, HomeHolder> entry : picturesAndLayers.entrySet()) {
                old_picture = entry.getKey();
                holder = entry.getValue();
                if (old_picture.id == new_picture.id)
                    break;
            }
            if (old_picture != null) {
                if (!ComparePicture(new_picture, old_picture)) {
                    RefreshInfo(holder, new_picture);
                }
            } else {
                View layer = addView(new_picture);
                holder = new HomeHolder(layer);
                RefreshInfo(holder, new_picture);
            }
            newPicturesAndHolders.put(new_picture, holder);
        }
        this.picturesAndLayers = newPicturesAndHolders;
    }

    public void RefreshAdapter(List<Picture> pictures) {
        PictureDiffUtilCallback pictureDiffUtilCallback = new PictureDiffUtilCallback(rv_top_adapter.getData(), pictures);
        DiffUtil.DiffResult productDiffResult = DiffUtil.calculateDiff(pictureDiffUtilCallback);
        rv_top_adapter.setData(pictures);
        productDiffResult.dispatchUpdatesTo(rv_top_adapter);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        this.home_ll = (LinearLayout) this.getActivity().findViewById(R.id.home_ll);
        this.storage = Storage.getInstance(this.getContext());
        this.picturesAndLayers = new Hashtable<Picture, HomeHolder>();

        LiveData<List<Picture>> liveData1 = this.storage.GetLiveDataFromView("Popular");
        liveData1.observe(getViewLifecycleOwner(), this::ShowPictures);
        this.storage.LoadPicturesByPopular();


        this.rv_top = (RecyclerView) this.getActivity().findViewById(R.id.rv_new);
        this.rv_top.setLayoutManager(new LinearLayoutManager(this.getContext(), LinearLayoutManager.HORIZONTAL, false));
        ((SimpleItemAnimator) rv_top.getItemAnimator()).setSupportsChangeAnimations(false);
        rv_top_adapter = new AdapterHomeTopRV(this.getContext(), R.layout.item_pictute_top, 0, 30);
        rv_top.setAdapter(rv_top_adapter);

        LiveData<List<Picture>> liveData2 = this.storage.GetLiveDataFromView("Top");
        liveData2.observe(getViewLifecycleOwner(), this::RefreshAdapter);
        this.storage.LoadPicturesByTop();
    }
}