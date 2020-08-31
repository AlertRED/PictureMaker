package com.example.picturemaker.storage;

import android.content.Context;
import android.graphics.Bitmap;
import android.widget.ImageView;

import androidx.core.util.Consumer;
import androidx.lifecycle.LiveData;

import com.google.firebase.auth.FirebaseUser;

import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;


public class Storage {

    private static Storage instance;
    public final FirebaseDB firebase;
    public ViewPictureDao viewPictureDao;
    LocalStorage localStorage;
    InternalDB db;
    PictureDao pictureDao;

    private Storage(Context context) {
        this.firebase = FirebaseDB.getInstance(context);
        this.localStorage = new LocalStorage();
        this.db = InternalDB.getDatabase(context);
        this.pictureDao = this.db.pictureDao();
        this.viewPictureDao = this.db.viewPictureDao();
    }

    public static Storage getInstance(Context context) {
        if (instance == null) {
            instance = new Storage(context);
        }
        return instance;
    }

    public FirebaseUser getUser() {
        return this.firebase.getUser();
    }

    public void GetGenres(Consumer<List<String>> foo) {
        List<String> genres = this.localStorage.GetGenres();
        if (genres != null) {
            foo.accept(genres);
        } else {
            this.firebase.loadGenres(genres1 -> {
                this.localStorage.SaveStorageGenres(genres1);
                GetGenres(foo);
            });
        }
    }

    public void GetLevels(Consumer<List<String>> foo) {
        List<String> levels = this.localStorage.GetLevels();
        foo.accept(levels);
    }

    public void GetAuthors(Consumer<List<String>> foo) {
        List<String> authors = this.localStorage.GetAuthors();
        if (authors != null) {
            foo.accept(authors);
        } else {
            this.firebase.loadAuthors(authors1 -> {
                this.localStorage.SaveStorageAuthors(authors1);
                GetAuthors(foo);
            });
        }
    }

    public LiveData<List<Picture>> GetLiveDataFromView(String viewName) {
        return this.viewPictureDao.getPicturesFromView(viewName);
    }

    public void LoadPicturesByGallery(Context context, Map<String, Object> parameters) {
        this.DeletePictures("Gallery");
        this.LoadPictures("Gallery", parameters);
    }

    public void LoadPicturesByCollection(Context context, String collectionType) {
        Map<String, Object> parameters = new Hashtable<>();
        if (collectionType.equals("Favorites"))
            this.firebase.getFavoriteIds(pictureIds -> {
                for (String pictureId : pictureIds) {
                    parameters.put("picture_id", pictureId);
                    this.LoadPictures(collectionType, parameters);
                }
            });
        else if (collectionType.equals("Process"))
            this.firebase.getProcessIds(pictureIds -> {
                for (String pictureId : pictureIds) {
                    parameters.put("picture_id", pictureId);
                    this.LoadPictures(collectionType, parameters);
                }
            });
        else if (collectionType.equals("Finish"))
            this.firebase.getFinishedIds(pictureIds -> {
                for (String pictureId : pictureIds) {
                    parameters.put("picture_id", pictureId);
                    this.LoadPictures(collectionType, parameters);
                }
            });

    }

    public void LoadPicturesByNews(Context context) {
        Map<String, Object> parameters = new Hashtable<>();
        parameters.put("is_last", true);
        parameters.put("count", 2);
        this.LoadPictures("News", parameters);
    }

    public void LoadPicturesByPopular(Context context) {
        Map<String, Object> parameters = new Hashtable<>();
        parameters.put("is_popular", true);
        this.LoadPictures("Popular", parameters);
    }

    public LiveData<Picture> GetLivePicture(long id) {
        return this.pictureDao.liveById(id);
    }

    private void DeletePictures(String viewName) {
        Executor myExecutor = Executors.newSingleThreadExecutor();
        myExecutor.execute(() -> this.viewPictureDao.deleteAllPicturesFromView(viewName));
    }

    private void LoadPictures(String viewName, Map<String, Object> parameters) {
        this.firebase.LoadPictures(pictures -> {
            Executor myExecutor = Executors.newSingleThreadExecutor();
            myExecutor.execute(() -> {
                for (Picture picture : pictures) {
                    long id = this.pictureDao.insertOrUpdate(picture);
                    this.viewPictureDao.insert(new ViewPicture(viewName, id));
                }
            });
        }, parameters);
    }

    public void GetImage(Context context, String picture_name, ImageView imageView) {
        this.firebase.loadImage(context, picture_name, imageView, false);
    }

    public void GetImage(Context context, String picture_name, Consumer<Bitmap> foo) {

        this.firebase.loadImage(context, picture_name, foo, false);
    }

    public void SetFavoritePicture(long pictureId, boolean isFavorite) {
        Executor myExecutor = Executors.newSingleThreadExecutor();
        myExecutor.execute(() -> {
            Picture picture = pictureDao.findById(pictureId);
            picture.is_favorite = isFavorite;
            pictureDao.update(picture);
            if (isFavorite)
                this.viewPictureDao.insert(new ViewPicture("Favorites", picture.id));
            else
                this.viewPictureDao.deleteByViewAndPicture("Favorites", picture.id);
            this.firebase.SetFavoritePicture(picture.public_id, isFavorite, () -> {
            });
        });
    }
}
