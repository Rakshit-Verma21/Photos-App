package com.example.photos;

import android.app.Application;
import android.provider.ContactsContract;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

public class ModelClass extends AndroidViewModel
{

    private Repository repository;
    private LiveData<List<Images>>images;

    public ModelClass(@NonNull Application application)
    {
        super(application);

        repository=new Repository(application);
        images=repository.getImagelist();
    }
    public void insert(Images images)
    {

        repository.insert(images);


    }
    public  void delete(Images images)
    {
        repository.delete(images);

    }
    public  void update(Images images)
    {
        repository.update(images);


    }
    public LiveData<List<Images>>getImages()
    {
        return images;
    }


}
