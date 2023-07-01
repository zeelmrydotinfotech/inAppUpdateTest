package com.tridhyaintuit.mynotes.models.AppDatabase;

import android.app.Application;

import androidx.lifecycle.LiveData;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Repository {

    private DaoNote daoNote;
    private LiveData <List<Note>> notes;

    ExecutorService executors = Executors.newSingleThreadExecutor();

    public Repository(Application application){
        RoomAppDatabase roomAppDatabase = RoomAppDatabase.getRoomAppDatabase(application);
        daoNote = roomAppDatabase.daoNote();
        notes = daoNote.getAllNotes();

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
}
