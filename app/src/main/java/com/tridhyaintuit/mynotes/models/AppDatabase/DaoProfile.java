package com.tridhyaintuit.mynotes.models.AppDatabase;


import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface DaoProfile {

    @Insert
    void insert(ProfileData profile);

    @Update
    void update(ProfileData profile);

    @Delete
    void delete(ProfileData profile);

    @Query("SELECT * FROM profile ORDER BY id ASC")
        //add LiveData if not working.
    LiveData <List<ProfileData>> getProfile();
}
