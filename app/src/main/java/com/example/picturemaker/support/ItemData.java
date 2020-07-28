package com.example.picturemaker.support;

import androidx.core.util.Consumer;

import com.example.picturemaker.FirebaseDB;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Dictionary;
import java.util.Hashtable;
import java.util.List;

public class ItemData {
    static private Dictionary<String, Item> items = new Hashtable<>();
    static private Dictionary<String, List<String>> history = new Hashtable<>();
    static private Dictionary<String , Consumer<Item>> consumers = new Hashtable<>();
    static private Dictionary<String , Consumer<List<Item>>> many_consumers = new Hashtable<>();

    static public void addItem(String public_id, Item item){
        items.put(public_id, item);
    }

    static public Item getItem(String public_id){
        return items.get(public_id);
    }

    static public void loadItem(Consumer<Item> foo, String public_id){
        Item item = items.get(public_id);
        if (item==null){
            consumers.put(public_id, foo);
            FirebaseDB.loadItem(ItemData::runConsumer, public_id);
        } else {
            foo.accept(item);
        }
    }

    static public void loadItems(Consumer<List<Item>> foo){
        FirebaseDB.loadItems(foo);
    }

    static private void runManyConsumer(List<Item> items){
    }

    static public void likePicture(String picture_id, boolean is_like){
        FirebaseDB.likePicture(picture_id, is_like);
    }


    static private void runConsumer(Item item){
        ItemData.items.put(item.public_id, item);
        Consumer<Item> consumer = consumers.remove(item.public_id);
        consumer.accept(item);
    }
}
