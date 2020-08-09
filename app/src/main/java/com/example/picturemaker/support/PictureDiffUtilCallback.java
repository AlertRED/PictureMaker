package com.example.picturemaker.support;

import androidx.recyclerview.widget.DiffUtil;

import com.example.picturemaker.storage.Picture;

import java.util.List;

public class PictureDiffUtilCallback extends DiffUtil.Callback {

    private final List<Picture> oldList;
    private final List<Picture> newList;

    public PictureDiffUtilCallback(List<Picture> oldList, List<Picture> newList) {
        this.oldList = oldList;
        this.newList = newList;
    }

    @Override
    public int getOldListSize() {
        return oldList.size();
    }

    @Override
    public int getNewListSize() {
        return newList.size();
    }

    @Override
    public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
        Picture oldProduct = oldList.get(oldItemPosition);
        Picture newProduct = newList.get(newItemPosition);
        return oldProduct.id == newProduct.id;
    }

    @Override
    public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
        Picture oldProduct = oldList.get(oldItemPosition);
        Picture newProduct = newList.get(newItemPosition);
        return oldProduct.is_favorite == newProduct.is_favorite
                && oldProduct.public_id.equals(newProduct.public_id)
                && oldProduct.public_picture.equals(newProduct.public_picture);
    }
}