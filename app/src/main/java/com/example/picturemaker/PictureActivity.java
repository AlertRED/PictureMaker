package com.example.picturemaker;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.LiveData;

import com.example.picturemaker.storage.Picture;
import com.example.picturemaker.storage.Storage;

import java.util.ArrayList;
import java.util.List;

public class PictureActivity extends AppCompatActivity {

    private Picture picture;
    private ImageView image;
    private TextView name;
    private TextView my_score;
    private TextView total_score;
    private ImageView favorite;
    private ImageView score;
    private List<ImageView> puzzles;
    private ProgressBar progress;
    private LinearLayout layout_progress;
    private Button button_start;

    private Storage storage;

    public void ShowRating() {
        final AlertDialog.Builder popDialog = new AlertDialog.Builder(this);
        View view = View.inflate(this, R.layout.activity_rating_picture, null);
        popDialog.setView(view);

        final RatingBar progress = view.findViewById(R.id.ratingBar);
        progress.setRating((float) picture.score);

        popDialog.setPositiveButton("Готово",
                (dialog, which) -> {
                    picture.score = (int) progress.getRating();
                    RefreshData();
                    dialog.dismiss();
                }).setNegativeButton("Отмена",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        RefreshData();
                        dialog.cancel();
                    }
                });

        popDialog.create();
        popDialog.show();
    }

    private void LoadPicture(Bitmap bitmap) {
        this.image.setImageBitmap(bitmap);
    }

    private void RefreshData() {

        this.storage.GetImage(this, this.picture.public_picture, this::LoadPicture);

        this.name.setText(this.picture.name);
        this.total_score.setText("Рейтинг: ".concat(String.valueOf(this.picture.total_score)));
        this.favorite.setImageResource(this.picture.is_favorite ? R.drawable.ic_favorite_36 : R.drawable.ic_unfavorite_36);
        this.score.setImageResource(this.picture.score > 0 ? R.drawable.ic_baseline_star_24 : R.drawable.ic_baseline_star_border_24);
        for (int i = 0; i < this.puzzles.size(); i++) {
            int state = i > picture.level - 1 ? View.GONE : View.VISIBLE;
            this.puzzles.get(i).setVisibility(state);
        }

        if (this.picture.progress == 0) {
            this.layout_progress.setVisibility(View.GONE);
            this.my_score.setVisibility(View.GONE);
        } else if (this.picture.progress == 100) {
            this.layout_progress.setVisibility(View.GONE);
            this.my_score.setText("Ваша оценка: ".concat(String.valueOf(this.picture.score)));
        } else {
            this.progress.setMax(100);
            this.progress.setProgress(this.picture.progress);
        }

        final Activity activity = this;

        this.button_start.setOnClickListener(v -> {
            Intent intent = new Intent(activity, PaintActivity.class);
            intent.putExtra("picture_id", picture.public_id);
            startActivity(intent);
        });

        this.favorite.setOnClickListener(v -> storage.SetFavoritePicture(picture.id, !picture.is_favorite));

        this.score.setOnClickListener(v -> {
            if (picture.progress == 100)
                ShowRating();
            else Toast.makeText(v.getContext(), "Картина не завершина", Toast.LENGTH_SHORT).show();
        });
    }

    private void LoadItem(Picture picture) {
        this.picture = picture;
        RefreshData();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_picture);

        long picture_id = getIntent().getIntExtra("pictureId", 0);

        this.image = findViewById(R.id.picture);
        this.name = findViewById(R.id.activity_picture_name);
        this.my_score = findViewById(R.id.picture_my_score);
        this.total_score = findViewById(R.id.picture_total_score);
        this.favorite = findViewById(R.id.activity_picture_favorite);
        this.score = findViewById(R.id.activity_picture_score);
        this.progress = findViewById(R.id.activity_picture_progress);
        this.layout_progress = findViewById(R.id.activity_picture_ll_progress);
        this.button_start = findViewById(R.id.button_start);

        this.puzzles = new ArrayList<ImageView>();
        this.puzzles.add((ImageView) findViewById(R.id.puzzle1));
        this.puzzles.add((ImageView) findViewById(R.id.puzzle2));
        this.puzzles.add((ImageView) findViewById(R.id.puzzle3));

        this.storage = Storage.getInstance(this);
//        storage.GetPicture(this::LoadItem, picture_id);
        LiveData<Picture> live = this.storage.GetLivePicture(picture_id);
        live.observe(this, this::LoadItem);

        Toolbar toolbar = (Toolbar) findViewById(R.id.filter_gallery_toolbar2);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Картина");
        toolbar.setNavigationOnClickListener(v -> finish());

    }

}