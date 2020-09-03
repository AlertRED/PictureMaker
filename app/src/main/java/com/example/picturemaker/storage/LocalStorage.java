package com.example.picturemaker.storage;

import java.util.Arrays;
import java.util.List;

public class LocalStorage {
    private List<String> genres;
    private List<String> levels;
    private List<String> authors;

    public LocalStorage() {
        this.levels = Arrays.asList("Легкий", "Средний", "Сложный");
    }

    public void SaveStorageGenres(List<String> genres) {
        this.genres = genres;
    }

    public void SaveStorageAuthors(List<String> authors) {
        this.authors = authors;
    }

    public List<String> GetAuthors() {
        return this.authors;
    }

    public List<String> GetGenres() {
        return this.genres;
    }

    public List<String> GetLevels() {
        return this.levels;
    }

}
