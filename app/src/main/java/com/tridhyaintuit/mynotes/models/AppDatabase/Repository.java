package com.tridhyaintuit.mynotes.models.AppDatabase;

import android.app.Application;

import androidx.lifecycle.LiveData;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Repository {

    private DaoNote daoNote;
    private DaoProfile daoProfile;
    private LiveData <List<Note>> notes;
    private LiveData <List<ProfileData>> profileData;

    ExecutorService executors = Executors.newSingleThreadExecutor();

    public Repository(Application application){
        RoomAppDatabase roomAppDatabase = RoomAppDatabase.getRoomAppDatabase(application);
        daoNote = roomAppDatabase.daoNote();
        daoProfile = roomAppDatabase.daoProfile();
        notes = daoNote.getAllNotes();
        profileData = daoProfile.getProfile();

    }

    public void insert(Note note){
        executors.execute(new Runnable() {
            @Override
            public void run() {
                daoNote.insert(note);
            }
        });

    }

    public void update(Note note){
        executors.execute(new Runnable() {
            @Override
            public void run() {
                daoNote.update(note);
            }
        });
    }

    public void delete(Note note){
        executors.execute(new Runnable() {
            @Override
            public void run() {
                daoNote.delete(note);
            }
        });
    }

    public LiveData <List<Note>> getAllNotes(){
        return notes;
    }

    public void insertProfile(ProfileData profileData){
        executors.execute(new Runnable() {
            @Override
            public void run() {
                daoProfile.insert(profileData);
            }
        });

    }

    public void updateProfile(ProfileData profileData){
        executors.execute(new Runnable() {
            @Override
            public void run() {
                daoProfile.update(profileData);
            }
        });
    }

    public void deleteProfile(ProfileData profileData){
        executors.execute(new Runnable() {
            @Override
            public void run() {
                daoProfile.delete(profileData);
            }
        });
    }

    public LiveData <List<ProfileData>> getAllProfiles(){
        return profileData;
    }
}
