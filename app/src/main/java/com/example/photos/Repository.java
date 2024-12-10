package com.example.photos;
import android.app.Application;
import androidx.lifecycle.LiveData;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Repository {
    private DAO dao;
    private LiveData<List<Images>> imagelist;

    ExecutorService executorService= Executors.newSingleThreadExecutor();

    public Repository(Application application)
    {

        Database database = Database.getInstance(application);
        dao = database.dao();
        imagelist = dao.getAllImages();

    }
    public void insert(Images images)
    {
        executorService.execute(new Runnable() {
            @Override
            public void run()
            {
                dao.insert(images);
            }
        });


    }

    public void delete(Images images)
    {
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                dao.delete(images);
            }
        });

    }
    public void update(Images images)
    {
        executorService.execute(new Runnable() {
        @Override
        public void run() {
            dao.update(images);
        }
    });

    }
    public LiveData<List<Images>> getImagelist()
    {
        return imagelist;
    }

}
