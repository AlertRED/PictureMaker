package com.example.picturemaker;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
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

import com.example.picturemaker.support.Item;

import java.util.ArrayList;
import java.util.List;

public class PictureActivity extends AppCompatActivity {

    private Item item;
    private ImageView picture;
    private TextView name;
    private TextView my_score;
    private TextView total_score;
    private ImageView favorite;
    private ImageView score;
    private List<ImageView> puzzles;
    private ProgressBar progress;
    private LinearLayout layout_progress;
    private Button button_start;

    public void ShowRating() {
        final AlertDialog.Builder popDialog = new AlertDialog.Builder(this);
        View view = View.inflate(this, R.layout.activity_rating_picture, null);
        popDialog.setView(view);

        final RatingBar progress = view.findViewById(R.id.ratingBar);
        progress.setRating((float) item.score);

        popDialog.setPositiveButton("Готово",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        item.score = (int) progress.getRating();
                        RefreshData();
                        dialog.dismiss();
                    }
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

    private void RefreshData() {

        FirebaseDB.loadPicture(this,this.item.public_picture, this.picture, false);

        this.name.setText(this.item.name);
        this.total_score.setText("Рейтинг: ".concat(String.valueOf(this.item.total_score)));
        this.favorite.setImageResource(this.item.is_favorite ? R.drawable.ic_favorite_36 : R.drawable.ic_unfavorite_36);
        this.score.setImageResource(this.item.score > 0 ? R.drawable.ic_baseline_star_24 : R.drawable.ic_baseline_star_border_24);
        for (int i = 0; i < this.puzzles.size(); i++) {
            int state = i > item.level - 1 ? View.GONE : View.VISIBLE;
            this.puzzles.get(i).setVisibility(state);
        }

        if (this.item.progress == 0) {
            this.layout_progress.setVisibility(View.GONE);
            this.my_score.setVisibility(View.GONE);
        } else if (this.item.progress == 100){
            this.layout_progress.setVisibility(View.GONE);
            this.my_score.setText("Ваша оценка: ".concat(String.valueOf(this.item.score)));
        } else {
            this.progress.setMax(100);
            this.progress.setProgress(this.item.progress);
        }

        final Activity activity = this;

        this.button_start.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(activity, PaintActivity.class);
//                intent.putExtra("picture_id", item.id);
                startActivity(intent);
            }
        });

        this.favorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!item.is_favorite) {
                    favorite.setImageResource(R.drawable.ic_favorite_36);
                    Toast.makeText(v.getContext(), "Добавлено в избранное", Toast.LENGTH_SHORT).show();
                } else {
                    favorite.setImageResource(R.drawable.ic_unfavorite_36);
                    Toast.makeText(v.getContext(), "Убрано из избранного", Toast.LENGTH_SHORT).show();
                }
                item.is_favorite = !item.is_favorite;
            }
        });

        this.score.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (item.progress == 100)
                    ShowRating();
                else Toast.makeText(v.getContext(), "Картина не завершина", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void LoadItem(Item item){
        this.item = item;
        RefreshData();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_picture);

        String picture_id = getIntent().getStringExtra("picture_id");

        this.picture = findViewById(R.id.picture);
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

        FirebaseDB.loadItem(this::LoadItem, picture_id);

        Toolbar toolbar = (Toolbar) findViewById(R.id.filter_gallery_toolbar2);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Картина");
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });




    }
}