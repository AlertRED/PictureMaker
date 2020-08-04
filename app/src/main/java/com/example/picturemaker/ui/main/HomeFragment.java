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
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.picturemaker.PictureActivity;
import com.example.picturemaker.R;
import com.example.picturemaker.storage.Picture;
import com.example.picturemaker.storage.Storage;
import com.example.picturemaker.adapters.AdapterHomeTopRV;

import java.util.Hashtable;
import java.util.List;
import java.util.Map;

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
        this.layer.setAlpha(0);
    }

    public ImageView getImage() {
        return image;
    }

    private void setImage(Bitmap bitmap) {
        this.image.setImageBitmap(bitmap);
        this.layer.animate().alpha(1f).setDuration(250);
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

    private Map<String, HomeHolder> pictures_holdes = new Hashtable<>();
    private RecyclerView rv_top;
    Storage storage;
    private LinearLayout home_ll;

    public static HomeFragment newInstance() {
        return new HomeFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }


    private void RefreshData() {
        for (Map.Entry<String, HomeHolder> entry : pictures_holdes.entrySet()) {
            storage.GetPicture(this::RefreshPicture, entry.getKey());
        }
    }

    private void RefreshPicture(Picture picture) {
        HomeHolder holder = this.pictures_holdes.get(picture.public_id);

        assert holder != null;
        holder.loadImage(this.getContext(), picture.public_picture);
        holder.getTitle().setText(picture.name);
        holder.getFavorite().setImageResource(picture.is_favorite ? R.drawable.ic_favorite_36 : R.drawable.ic_unfavorite_36);

        holder.getFavorite().setOnClickListener(v -> {
            storage.SetFavoritePicture(picture.public_id, !picture.is_favorite, () -> {updatePictureInfo(picture.public_id);});
            if (!picture.is_favorite) {
                holder.getFavorite().setImageResource(R.drawable.ic_favorite_36);
                Toast.makeText(v.getContext(), "Добавлено в избранное", Toast.LENGTH_SHORT).show();
            } else {
                holder.getFavorite().setImageResource(R.drawable.ic_unfavorite_36);
                Toast.makeText(v.getContext(), "Убрано из избранного", Toast.LENGTH_SHORT).show();
            }
        });

        holder.getLayer().setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), PictureActivity.class);
            intent.putExtra("picture_id", picture.public_id);
            startActivity(intent);
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        this.RefreshData();
    }

    private void addView(String pictureId) {
        LayoutInflater inflater = getLayoutInflater();
        View layer = inflater.inflate(R.layout.item_pictute_popular, null);
        this.pictures_holdes.put(pictureId, new HomeHolder(layer));
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT);
        params.setMargins(20, 10, 20, 10);
        layer.setLayoutParams(params);
        home_ll.addView(layer);
    }

    private void updatePictureInfo(String pictureId){
        this.storage.GetPicture(this::RefreshPicture, pictureId);
    }

    public void showPictures(List<String> picturesIds) {
        for (String pictureId : picturesIds) {
            addView(pictureId);
            updatePictureInfo(pictureId);
        }
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden)
            RefreshData();
    }

    public void RefreshAdapter(List<String> picturesIds) {
        AdapterHomeTopRV rvMain_adapter = new AdapterHomeTopRV(getContext(), R.layout.item_pictute_top, picturesIds, 0, 20);
        rv_top.setAdapter(rvMain_adapter);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        home_ll = (LinearLayout) this.getActivity().findViewById(R.id.home_ll);
        storage = Storage.getInstance(this.getContext());

        Map<String, Object> parameters = new Hashtable<>();
        parameters.put("is_popular", true);
        storage.GetPicturesIds(this::showPictures, parameters);

        rv_top = (RecyclerView) this.getActivity().findViewById(R.id.rv_new);
        rv_top.setLayoutManager(new LinearLayoutManager(this.getContext(), LinearLayoutManager.HORIZONTAL, false));
        Map<String, Object> parameters1 = new Hashtable<>();
        storage.GetPicturesIds(this::RefreshAdapter, parameters1);
    }
}