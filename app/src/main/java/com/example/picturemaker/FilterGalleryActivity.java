package com.example.picturemaker;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.picturemaker.storage.Storage;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Stack;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class FilterGalleryActivity extends AppCompatActivity {

    private Spinner spin_genre;
    private Spinner spin_author;
    private Spinner spin_level;
    private String genre;
    private String level;
    private String author;

    private void RefreshAuthors(List<String> authors) {
        List<String> new_list = new ArrayList<String>();
        new_list.add("Любой");
        new_list.addAll(authors);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, new_list);
        this.spin_author.setAdapter(adapter);
        if (this.author != null)
            this.spin_author.setSelection(new_list.indexOf(this.author));
    }

    private void RefreshGenres(List<String> genres) {
        List<String> new_list = new ArrayList<String>();
        new_list.add("Любой");
        new_list.addAll(genres);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, new_list);
        this.spin_genre.setAdapter(adapter);
        if (this.genre != null)
            this.spin_genre.setSelection(new_list.indexOf(this.genre));
    }

    private void RefreshLevels(List<String> levels) {
        List<String> new_list = new ArrayList<String>();
        new_list.add("Любой");
        new_list.addAll(levels);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, new_list);
        this.spin_level.setAdapter(adapter);
        if (this.level != null)
            this.spin_level.setSelection(new_list.indexOf(this.level));
    }


//    private void RefreshData() {
//        ArrayAdapter<String> adapter;
//        List<String> items = Arrays.asList("Любой", "Абстракционизм", "Анималистика", "Бытовой", "Импрессионизм",
//                "Исторический", "Натюрморт", "Ню", "Пейзаж", "Портрет");
//        adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, items);
//        this.spin_genre.setAdapter(adapter);
//        if (this.genre != null)
//            this.spin_genre.setSelection(items.indexOf(this.genre));
//
//
//        items = Arrays.asList("Любой", "Пабло Пикассо", "Винсент ван Гог", "Сальвадор Дали", "Рембрандт",
//                "Клод Моне", "Иван Константинович Айвазовский", "Леонардо да Винчи", "Энди Уорхол", "Поль Гоген");
//        adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, items);
//        this.spin_author.setAdapter(adapter);
//        if (this.act != null)
//            this.spin_author.setSelection(items.indexOf(this.act));
//
//        items = Arrays.asList("Любое", "Легко", "Средне", "Сложно");
//        adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, items);
//        this.spin_level.setAdapter(adapter);
//        if (this.level != null)
//            this.spin_level.setSelection(items.indexOf(this.level));
//    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter_gallery);

        Toolbar toolbar = (Toolbar) findViewById(R.id.filter_gallery_toolbar);
        setSupportActionBar(toolbar);
        Button accept_button = (Button) findViewById(R.id.accept_filter);
        accept_button.setOnClickListener(v -> {
            Intent answerIntent = new Intent();
            answerIntent.putExtra("genre", (String) spin_genre.getSelectedItem());
            answerIntent.putExtra("author", (String) spin_author.getSelectedItem());
            answerIntent.putExtra("level", (String) spin_level.getSelectedItem());
            setResult(RESULT_OK, answerIntent);
            finish();
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("");

        toolbar.setNavigationOnClickListener(v -> {
            setResult(RESULT_CANCELED);
            finish();
        });

        this.spin_genre = (Spinner) findViewById(R.id.spin_filter_genre);
        this.spin_author = (Spinner) findViewById(R.id.spin_filter_act);
        this.spin_level = (Spinner) findViewById(R.id.spin_filter_level);

        this.genre = this.getIntent().getStringExtra("genre");
        this.level = this.getIntent().getStringExtra("level");
        this.author = this.getIntent().getStringExtra("author");

        Storage storage = Storage.getInstance(this);
        storage.GetAuthors(this::RefreshAuthors);
        storage.GetGenres(this::RefreshGenres);
        storage.GetLevels(this::RefreshLevels);
    }
}