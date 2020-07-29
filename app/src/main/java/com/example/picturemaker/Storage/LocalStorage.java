package com.example.picturemaker.Storage;

import java.util.ArrayList;
import java.util.Dictionary;
import java.util.Hashtable;
import java.util.List;

public class LocalStorage {
    private Dictionary<String, Picture> items;
    private Dictionary<String, List<String>> history;

    public LocalStorage() {
        this.items = new Hashtable<>();
        this.history = new Hashtable<>();
    }


    private String GenerateKey(boolean is_favorite, int level){
        return String.valueOf(is_favorite).concat(String.valueOf(level));
    };

    public Picture GetPicture(String public_id){
        return items.get(public_id);
    }

    public List<Picture> Query(boolean is_favorite, int level){
        String key = GenerateKey(is_favorite, level);
        List<String> ids = history.get(key);
        List<Picture> pictures = null;
        if (ids != null) {
            pictures = new ArrayList<>();
            for (String index : ids) {
                pictures.add(this.items.get(index));
            }
        }
        return pictures;
    }

    public void SaveStorage(List<Picture> pictures, boolean is_favorite, int level){
        String key = GenerateKey(is_favorite, level);
        List<String> ids = new ArrayList<>();
        for (Picture picture : pictures) {
            ids.add(picture.public_id);
            this.items.put(picture.public_id, picture);
        }
        history.put(key, ids);
    }

    public void SaveStorage(Picture picture){
        this.items.put(picture.public_id, picture);
    }

}
