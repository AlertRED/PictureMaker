package com.example.picturemaker.support;

import com.example.picturemaker.R;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class TestData {

    static private List<Item> items;


    static public Item get(int index){
        return items.get(index);
    }

    static public Item get_id(int id) {
//        for (Item item : items)
//            if (item.id == id) return item;
        return get(0);
    }

    static public int size(){
        return 0;
    }


    static public void generate(){

        items = new ArrayList<>();

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

    static public Iterator<Item> getIter(){
        return items.iterator();
    }

}
