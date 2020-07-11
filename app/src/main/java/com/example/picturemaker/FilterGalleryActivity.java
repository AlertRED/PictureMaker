package com.example.picturemaker;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.picturemaker.R;

public class FilterGalleryActivity extends AppCompatActivity {

    private Spinner spin_genre;
    private Spinner spin_act;
    private Spinner spin_level;

    private void RefreshData(){
        ArrayAdapter<String> adapter;
        String[] items;
        items = new String[]{"Любой", "Абстракционизм", "Анималистика", "Бытовой", "Импрессионизм",
                "Исторический", "Натюрморт", "Ню", "Пейзаж", "Портрет"};
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, items);
        this.spin_genre.setAdapter(adapter);

        items = new String[]{"Любой", "Пабло Пикассо", "Винсент ван Гог", "Сальвадор Дали", "Рембрандт",
                "Клод Моне", "Иван Константинович Айвазовский", "Леонардо да Винчи", "Энди Уорхол", "Поль Гоген"};
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, items);
        this.spin_act.setAdapter(adapter);

        items = new String[]{"Любое", "Легко", "Средне", "Сложно"};
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, items);
        this.spin_level.setAdapter(adapter);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.filter_gallery);

        Toolbar toolbar = (Toolbar) findViewById(R.id.filter_gallery_toolbar);
        setSupportActionBar(toolbar);
        Button accept_button = (Button) findViewById(R.id.accept_filter);
        accept_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("");

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        this.spin_genre = (Spinner) findViewById(R.id.spin_filter_genre);
        this.spin_act = (Spinner) findViewById(R.id.spin_filter_act);
        this.spin_level = (Spinner) findViewById(R.id.spin_filter_level);

        this.RefreshData();
    }
}