package com.example.picturemaker;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.example.picturemaker.adapters.AdapterColorsRV;
import com.example.picturemaker.support.Item;
import com.example.picturemaker.support.TestData;

public class PaintActivity extends AppCompatActivity {

    private Item item;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_paint);

        int picture_id = getIntent().getIntExtra("picture_id", 0);
        this.item = TestData.get_id(picture_id);

        RecyclerView rv_top = (RecyclerView) this.findViewById(R.id.rv_new);
        rv_top.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        AdapterColorsRV rvMain_adapter = new AdapterColorsRV(this, R.layout.color_brush_item, 0, 20);
        rv_top.setAdapter(rvMain_adapter);
    }
}