package com.example.picturemaker.support;

import com.example.picturemaker.R;
import com.example.picturemaker.Storage.Picture;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class TestData {

    static private List<Picture> pictures;


    static public Picture get(int index){
        return pictures.get(index);
    }

    static public Picture get_id(int id) {
//        for (Item item : items)
//            if (item.id == id) return item;
        return get(0);
    }

    static public int size(){
        return 0;
    }


    static public void generate(){

        pictures = new ArrayList<>();

        int[] pictures = {
                R.drawable.image1, R.drawable.image2, R.drawable.image3, R.drawable.image4,
                R.drawable.image5, R.drawable.image6, R.drawable.image7, R.drawable.image8,
                R.drawable.image9, R.drawable.image10};

        String[] names = {"Звездная ночь", "Черный квадрат", "Мона Лиза",
                "Утро в сосновом бору","Девятый вал","Девочка с персиками",
                "Тайная вечеря","Постоянство памяти","Рождение Венеры","Боярыня Морозова"};

//        for (int i=0; i<pictures.length; i++){
//            TestData.items.add(new Item(pictures[i], names[i], i));
//        }
    }

    static public Iterator<Picture> getIter(){
        return pictures.iterator();
    }

}
