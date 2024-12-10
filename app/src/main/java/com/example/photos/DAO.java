package com.example.photos;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface DAO
{
    @Insert
    void insert(Images images);
    @Delete
    void delete(Images images);

    @Update
    void update(Images images);

    @Query("SELECT * FROM img_table ORDER BY id ASC")
    LiveData<List<Images>> getAllImages();


}
