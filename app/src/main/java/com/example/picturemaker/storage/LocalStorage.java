package com.example.picturemaker.storage;

import java.util.Comparator;
import java.util.Map;
import java.util.TreeMap;

class ValueComparator implements Comparator<String> {
    Map<String, Integer> base;

    public ValueComparator(Map<String, Integer> base) {
        this.base = base;
    }

    public int compare(String a, String b) {
        Integer _a = base.get(a);
        Integer _b = base.get(b);
        assert _a != null;
        assert _b != null;
        if (_a < _b) {
            return -1;
        } else if (_a > _b) {
            return 1;
        }
        return 0;
    }
}

public class LocalStorage {
    private Map<String, Integer> genres;
    private Map<String, Integer> levels;
    private Map<String, Integer> authors;

    public LocalStorage() {
    }

    public void SaveStorageLevels(Map<String, Integer> levels) {
        ValueComparator bvc = new ValueComparator(levels);
        this.levels = new TreeMap<>(bvc);
        this.levels.putAll(levels);
    }

    public void SaveStorageGenres(Map<String, Integer> genres) {
        this.genres = genres;
    }

    public void SaveStorageAuthors(Map<String, Integer> authors) {
        this.authors = authors;
    }

    public Map<String, Integer> GetAuthors() {
        return this.authors;
    }

    public Map<String, Integer> GetGenres() {
        return this.genres;
    }

    public Map<String, Integer> GetLevels() {
        return this.levels;
    }

}
