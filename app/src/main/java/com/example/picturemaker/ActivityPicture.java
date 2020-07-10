package com.example.picturemaker;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class ActivityPicture extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_picture);

        int picture_id = getIntent().getIntExtra("picture_id",0);
        final Item item = TestData.get_id(picture_id);

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



        ImageView picture = findViewById(R.id.picture);
        picture.setImageResource(item.picture);

        TextView name = findViewById(R.id.activity_picture_name);
        name.setText(item.name);

        int draw_favorite = item.is_favorite ? R.drawable.ic_favorite_36x36 : R.drawable.ic_unfavorite_36x36;
        final ImageView favorite = findViewById(R.id.activity_picture_favorite);
        favorite.setImageResource(draw_favorite);

        int draw_score = item.is_favorite ? R.drawable.ic_baseline_star_border_24 : R.drawable.ic_baseline_star_24;
        final ImageView score = findViewById(R.id.activity_picture_score);
        score.setImageResource(draw_score);

        favorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!item.is_favorite){
                    favorite.setImageResource(R.drawable.ic_favorite_36x36);
                    Toast.makeText(v.getContext(), "Добавлено в избранное", Toast.LENGTH_SHORT).show();
                } else {
                    favorite.setImageResource(R.drawable.ic_unfavorite_36x36);
                    Toast.makeText(v.getContext(), "Убрано из избранного", Toast.LENGTH_SHORT).show();
                }
                item.is_favorite = !item.is_favorite;
            }
        });

        score.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (item.score==0){
                    score.setImageResource(R.drawable.ic_baseline_star_border_24);
                } else {
                    score.setImageResource(R.drawable.ic_baseline_star_24);
                }
            }
        });


    }
}