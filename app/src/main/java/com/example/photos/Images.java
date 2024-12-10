package com.example.photos;


import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName="img_table")
public class Images
{
    @PrimaryKey(autoGenerate = true)
    public int id;

    public String image_title;
    public String image_description;

    public byte[] image;

    public Images(String image_title, String image_description, byte[] image)
    {
        this.image_title = image_title;
        this.image_description = image_description;
        this.image = image;
    }

    public int getId() {
        return id;
    }

    public String getImage_title() {
        return image_title;
    }

    public String getImage_description() {
        return image_description;
    }

    public byte[] getImage() {
        return image;
    }

    public void setId(int id) {
        this.id = id;
    }
}
