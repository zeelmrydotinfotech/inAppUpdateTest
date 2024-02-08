package com.tridhyaintuit.mynotes.models.AppDatabase;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "profile")
public class ProfileData {
    @PrimaryKey(autoGenerate = true)
    public int id;
    public byte[] imageUrl;

    public ProfileData(int id, byte[] imageUrl) {
        this.id = id;
        this.imageUrl = imageUrl;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
    public byte[] getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(byte[] imageUrl) {
        this.imageUrl = imageUrl;
    }

}
