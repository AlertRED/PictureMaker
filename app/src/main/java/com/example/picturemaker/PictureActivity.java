package com.example.picturemaker;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.LiveData;

import com.example.picturemaker.storage.Picture;
import com.example.picturemaker.storage.Storage;
import com.example.picturemaker.support.FastBlur;

import eightbitlab.com.blurview.BlurView;
import eightbitlab.com.blurview.RenderScriptBlur;

public class PictureActivity extends AppCompatActivity {

    public ImageView image;
    private Picture picture;
    private TextView name;
    private TextView my_score;
    private TextView total_score;
    private ImageView favorite;
    private ImageView score;
    //    private List<ImageView> puzzles;
//    private ProgressBar progress;
    private LinearLayout layout_progress;
    private Button button_start;

    private Storage storage;
    private ImageView imageBackground;
    private BlurView blurLayout;
    private Toolbar toolbar;
    private ViewGroup root;

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

    private void LoadPictureBackground(Bitmap bitmap){
        BitmapDrawable drawable = new BitmapDrawable(getResources(), bitmap);
        this.imageBackground.setImageDrawable(drawable);
        this.image.setImageDrawable(drawable);
    }

    private void RefreshData() {

        this.storage.GetImage(this, this.picture.public_picture, imageBackground);
        this.storage.GetImage(this, this.picture.public_picture, image);
//        this.storage.GetImage(this, this.picture.public_picture, this::LoadPictureBackground);




        this.name.setText(this.picture.name);
        this.total_score.setText("Рейтинг: ".concat(String.valueOf(this.picture.total_score)));
        this.favorite.setImageResource(this.picture.is_favorite ? R.drawable.ic_favorite_36 : R.drawable.ic_unfavorite_36);
//        this.score.setImageResource(this.picture.score > 0 ? R.drawable.ic_baseline_star_24 : R.drawable.ic_baseline_star_border_24);
//        for (int i = 0; i < this.puzzles.size(); i++) {
//            int state = i > picture.level - 1 ? View.GONE : View.VISIBLE;
//            this.puzzles.get(i).setVisibility(state);
//        }

        this.my_score.setText("Ваша оценка: ".concat(String.valueOf(this.picture.score)));
//        if (this.picture.progress == 0) {
//            this.layout_progress.setVisibility(View.GONE);
//            this.my_score.setVisibility(View.GONE);
//        } else if (this.picture.progress == 100) {
//            this.layout_progress.setVisibility(View.GONE);
//            this.my_score.setText("Ваша оценка: ".concat(String.valueOf(this.picture.score)));
//        } else {
////            this.progress.setMax(100);
////            this.progress.setProgress(this.picture.progress);
//        }

        final Activity activity = this;

        this.button_start.setOnClickListener(v -> {
            Intent intent = new Intent(activity, PaintActivity.class);
            intent.putExtra("picture_id", picture.public_id);
            startActivity(intent);
        });

        this.favorite.setOnClickListener(v -> storage.SetFavoritePicture(picture.id, !picture.is_favorite));

//        this.score.setOnClickListener(v -> {
//            if (picture.progress == 100)
//                ShowRating();
//            else Toast.makeText(v.getContext(), "Картина не завершина", Toast.LENGTH_SHORT).show();
//        });
    }

    private void LoadItem(Picture picture) {
        this.picture = picture;
        RefreshData();

    }

    private void backArrow(){
        ContextCompat.getDrawable(this, R.drawable.abc_ic_ab_back_material);
        setSupportActionBar(this.toolbar);
        final Drawable upArrow = ContextCompat.getDrawable(this, R.drawable.ic_baseline_arrow_back_ios_24);
        upArrow.setColorFilter(ContextCompat.getColor(this, R.color.white), PorterDuff.Mode.SRC_ATOP);
        getSupportActionBar().setHomeAsUpIndicator(upArrow);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("");
        toolbar.setNavigationOnClickListener(v -> finish());
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
//        this.score = findViewById(R.id.activity_picture_score);
//        this.progress = findViewById(R.id.activity_picture_progress);
        this.layout_progress = findViewById(R.id.activity_picture_ll_progress);
        this.button_start = findViewById(R.id.button_start);

//        this.puzzles = new ArrayList<ImageView>();
//        this.puzzles.add((ImageView) findViewById(R.id.puzzle1));
//        this.puzzles.add((ImageView) findViewById(R.id.puzzle2));
//        this.puzzles.add((ImageView) findViewById(R.id.puzzle3));

        this.storage = Storage.getInstance(this);
//        storage.GetPicture(this::LoadItem, picture_id);
        LiveData<Picture> live = this.storage.GetLivePicture(picture_id);
        live.observe(this, this::LoadItem);

        toolbar = (Toolbar) findViewById(R.id.filter_gallery_toolbar2);
        this.backArrow();
        imageBackground = findViewById(R.id.backgroundPicture);
        blurLayout = (BlurView) findViewById(R.id.blurLayout);

        this.root = findViewById(R.id.content_profile_picture);
        blurBackground();
    }

    private void blur(Bitmap bkg, ImageView imageBackground) {
//        long startMs = System.currentTimeMillis();
        float scaleFactor = 4f;
        float radius = 20f;

        Bitmap overlay = Bitmap.createBitmap((int) (imageBackground.getMeasuredWidth()/scaleFactor),
                (int) (imageBackground.getMeasuredHeight()/scaleFactor), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(overlay);
        canvas.translate(-imageBackground.getLeft()/scaleFactor, -imageBackground.getTop()/scaleFactor);
        canvas.scale(1 / scaleFactor, 1 / scaleFactor);
        Paint paint = new Paint();
        paint.setFlags(Paint.FILTER_BITMAP_FLAG);
        canvas.drawBitmap(bkg, 0, 0, paint);

        overlay = FastBlur.doBlur(overlay, (int)radius, true);
        imageBackground.setImageDrawable(new BitmapDrawable(getResources(), overlay));
//        statusText.setText(System.currentTimeMillis() - startMs + "ms");
    }

    private void blurBackground() {
        float radius = 8f;

        View decorView = getWindow().getDecorView();
        Drawable windowBackground = decorView.getBackground();

        blurLayout.setupWith(this.root)
                .setFrameClearDrawable(windowBackground)
                .setBlurAlgorithm(new RenderScriptBlur(this))
                .setBlurRadius(radius)
                .setHasFixedTransformationMatrix(true);
    }

}