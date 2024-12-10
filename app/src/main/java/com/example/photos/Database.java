package com.example.photos;

import android.content.Context;

import androidx.room.Room;
import androidx.room.RoomDatabase;

@androidx.room.Database(entities = Images.class,version = 1)
public abstract class Database extends RoomDatabase
{
    private static Database instance;
    public abstract DAO dao();

    public static synchronized Database getInstance(Context context)
    {
        if(instance==null)
        {
            instance= Room.databaseBuilder(context.getApplicationContext()
            ,Database.class,"photos_database")
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return instance;
    }


}
