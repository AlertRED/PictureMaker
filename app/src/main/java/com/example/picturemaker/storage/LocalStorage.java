package com.example.picturemaker.storage;

import java.util.List;

public class LocalStorage {
    private List<String> genres;
    private List<String> levels;
    private List<String> authors;

    public LocalStorage() {
    }

    public void SaveStorageLevels(List<String> levels) {
        this.levels = levels;
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
