package com.example.picturemaker.storage;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Dictionary;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

public class LocalStorage {
    private Dictionary<String, Picture> items;
    private Dictionary<String, List<String>> history;
    private List<String> genres;
    private List<String> levels;
    private List<String> authors;

    public LocalStorage() {
        this.items = new Hashtable<>();
        this.history = new Hashtable<>();
        this.levels = Arrays.asList("Легкий", "Средний", "Сложный");
    }

    public void SaveStorageGenres(List<String> genres){
        this.genres = genres;
    }

    public void SaveStorageAuthors(List<String> authors){
        this.authors = authors;
    }

    public List<String> GetAuthors(){
        return this.authors;
    };

    public List<String> GetGenres(){
        return this.genres;
    };

    public List<String> GetLevels(){
        return this.levels;
    };

    private String GenerateKey(Map<String, Object> parameters){
        return parameters.toString();
    };

    public Picture GetPicture(String public_id){
        return items.get(public_id);
    }

    public List<String> GetPicturesIds(Map<String, Object> parameters){
        String key = GenerateKey(parameters);
        return history.get(key);
    }

    public void SaveStorage(List<Picture> pictures, Map<String, Object> parameters){
        String key = GenerateKey(parameters);
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
