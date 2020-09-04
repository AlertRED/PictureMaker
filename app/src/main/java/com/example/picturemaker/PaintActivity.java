package com.example.picturemaker;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.picturemaker.adapters.AdapterColorsRV;
import com.example.picturemaker.storage.Picture;
import com.example.picturemaker.storage.Storage;
import com.otaliastudios.zoom.ZoomLayout;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class PaintActivity extends AppCompatActivity {

    private Storage storage;
    private ImageView image;

    private void LoadItem(Picture picture) {
        if (picture != null) {
            this.storage.GetImage(this, picture.public_picture, image);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_paint);
        this.storage = Storage.getInstance(this);
        this.image = (ImageView) findViewById(R.id.imageView2);

        int picture_id = getIntent().getIntExtra("picture_id", 0);
        LiveData<Picture> live = this.storage.GetLivePicture(picture_id);
        live.observe(this, this::LoadItem);

        Random rnd = new Random();
        List<Integer> colors = new ArrayList<>();
        for (int i = 0; i < 100; i++)
            colors.add(Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256)));
        RecyclerView rv_top = (RecyclerView) this.findViewById(R.id.rv_colors);
        rv_top.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        AdapterColorsRV rvMain_adapter = new AdapterColorsRV(this, colors, R.layout.item_color_brush, 0, 20);
        rv_top.setAdapter(rvMain_adapter);

        ZoomLayout zoomLayout = findViewById(R.id.zoom_layout);
        zoomLayout.zoomTo(0.7f, false);
    }
}