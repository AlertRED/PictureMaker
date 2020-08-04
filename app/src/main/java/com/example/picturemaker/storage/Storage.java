package com.example.picturemaker.storage;

import android.content.Context;
import android.graphics.Bitmap;

import androidx.core.util.Consumer;
import androidx.lifecycle.LiveData;
import androidx.room.Room;

import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.observers.DisposableMaybeObserver;
import io.reactivex.schedulers.Schedulers;


public class Storage {

    private static Storage instance;
    LocalStorage localStorage;
    InternalDB db;
    PictureDao pictureDao;
    Context context;

    private Storage(Context context) {
        this.localStorage = new LocalStorage();
        this.db = Room.databaseBuilder(context, InternalDB.class, "database-name").build();
        this.pictureDao = this.db.pictureDao();
        this.context = context;
    }

    public static Storage getInstance(Context context) {
        if (instance == null) {
            instance = new Storage(context);
        }
        return instance;
    }

    public void GetGenres(Consumer<List<String>> foo) {
        List<String> genres = this.localStorage.GetGenres();
        if (genres != null) {
            foo.accept(genres);
        } else {
            FirebaseDB.loadGenres(genres1 -> {
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
            FirebaseDB.loadAuthors(authors1 -> {
                this.localStorage.SaveStorageAuthors(authors1);
                GetAuthors(foo);
            });
        }
    }

    public void GetPicturesIds(Consumer<List<String>> foo, Map<String, Object> parameters) {
        List<String> picturesIds = this.localStorage.GetPicturesIds(parameters);
        if (picturesIds != null) {
            foo.accept(picturesIds);
        } else {
            FirebaseDB.LoadPictures(pictures1 -> {
                this.localStorage.SaveStorage(pictures1, parameters);
                GetPicturesIds(foo, parameters);
            }, parameters);
        }
    }

    public void GetPicture(Consumer<Picture> foo, String picture_id) {
        pictureDao.findById(picture_id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DisposableMaybeObserver<Picture>() {
                    @Override
                    public void onSuccess(Picture picture) {
                        foo.accept(picture);
                    }

                    @Override
                    public void onError(Throwable e) {
                    }

                    @Override
                    public void onComplete() {
                        Picture picture = localStorage.GetPicture(picture_id);
                        if (picture == null) {
                            FirebaseDB.loadItem(picture1 -> {
                                if (picture1.is_favorite) {
                                    SavePictureToDB(picture1, () -> {
                                    });
                                } else
                                    localStorage.SaveStorage(picture1);
                                foo.accept(picture1);
                            }, picture_id);
                        } else {
                            foo.accept(picture);
                        }
                    }
                });
    }

    public LiveData<List<Picture>> GetPicturesLiveData() {
        return pictureDao.getAll();
    }

    public void GetImage(Context context, String picture_name, Consumer<Bitmap> foo) {
        FirebaseDB.loadPicture(context, picture_name, foo, false);
    }

    public void SetFavoritePicture(String picture_id, boolean is_favorite, Runnable foo) {
        FirebaseDB.SetFavoritePicture(picture_id, is_favorite, () ->
                pictureDao.findById(picture_id)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new DisposableMaybeObserver<Picture>() {
                            // Если запись найдена в БД
                            @Override
                            public void onSuccess(Picture picture) {
                                picture.is_favorite = is_favorite;
                                localStorage.SaveStorage(picture);
                                DeletePictureToDB(picture, foo);
                            }

                            @Override
                            public void onError(Throwable e) {
                            }

                            @Override
                            public void onComplete() {
                                Picture picture = localStorage.GetPicture(picture_id);
                                if (picture != null) { // Если запись найдена в локальном хранилище
                                    picture.is_favorite = is_favorite;
                                    SavePictureToDB(picture, foo);
                                } else { // Если запись не найдена ни в БД ни в Хранилище, берем ее в Firebase
                                    FirebaseDB.loadItem(picture1 -> {
                                        picture1.is_favorite = is_favorite;
                                        SavePictureToDB(picture1, foo);
                                    }, picture_id);
                                }
                            }
                        }));
    }

    private void SavePictureToDB(Picture picture, Runnable foo) {
        Executor myExecutor = Executors.newSingleThreadExecutor();
        myExecutor.execute(() -> pictureDao.insertAll(picture));

//        Callable<Void> clb = () -> {
//            this.pictureDao.insertAll(picture);
//            return null;
//        };
//
//        Disposable f = Observable.just(clb)
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(voidCallable -> foo.run());
    }

    private void SavePictureToDB(Picture picture) {
        Executor executor = Executors.newSingleThreadExecutor();
        executor.execute(() -> pictureDao.update(picture));
    }

    private void UpdatePictureToDB(Picture picture, Runnable foo) {
        Callable<Void> clb = () -> {
            this.pictureDao.update(picture);
            return null;
        };

        Disposable f = Observable.just(clb)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(voidCallable -> foo.run());
    }

    private void UpdatePictureToDB(Picture picture) {
        Executor executor = Executors.newSingleThreadExecutor();
        executor.execute(() -> pictureDao.update(picture));
    }

    private void DeletePictureToDB(Picture picture, Runnable foo) {
        Executor myExecutor = Executors.newSingleThreadExecutor();
        myExecutor.execute(() -> {
            this.pictureDao.delete(picture);
        });
    }

    private void DeletePictureToDB(Picture picture) {
        Executor executor = Executors.newSingleThreadExecutor();
        executor.execute(() -> pictureDao.delete(picture));
    }
}
