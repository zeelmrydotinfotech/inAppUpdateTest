package com.tridhyaintuit.mynotes.models.AppDatabase;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(entities = {Note.class},version = 1)
public abstract class RoomAppDatabase extends RoomDatabase {

    private static RoomAppDatabase roomAppDatabase;

    public abstract DaoNote daoNote();

    public static synchronized RoomAppDatabase getRoomAppDatabase(Context context){
        if (roomAppDatabase == null){

            roomAppDatabase = Room.databaseBuilder(context.getApplicationContext()
                    ,RoomAppDatabase.class,"note_database")
                    .fallbackToDestructiveMigration()
                    .addCallback(roomCallback)
                    .build();
        }
        return roomAppDatabase;
    }

    private static RoomDatabase.Callback roomCallback = new RoomDatabase.Callback(){
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);

            DaoNote daoNote = roomAppDatabase.daoNote();

            ExecutorService executors = Executors.newSingleThreadExecutor();

            executors.execute(new Runnable() {
                @Override
                public void run() {
                    daoNote.insert(new Note("Title 1", "Description 1"));
                    daoNote.insert(new Note("Title 2", "Description 2"));
                    daoNote.insert(new Note("Title 3", "Description 3"));
                    daoNote.insert(new Note("Title 4", "Description 4"));
                }
            });
        }
    };
}
