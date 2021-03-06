package com.example.android.todo_list.jadwalin.Database;

import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;
import android.os.AsyncTask;
import android.support.annotation.NonNull;

import com.example.android.todo_list.jadwalin.Dao.NoteDao;
import com.example.android.todo_list.jadwalin.Entity.Note;

@Database(entities = Note.class,version = 1)
public abstract class NoteDatabase extends RoomDatabase {

    public abstract NoteDao noteDao();

    private static NoteDatabase instance;

    public static synchronized NoteDatabase getInstance(Context context){
        if (instance == null){
            instance = Room.databaseBuilder(context,NoteDatabase.class,"note_database")
                    .fallbackToDestructiveMigration()
                    .addCallback(roomCallback)
                    .build();
        }
        return instance;
    }

    private static RoomDatabase.Callback roomCallback = new RoomDatabase.Callback(){
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
            new PopulateDbAsyncTask(instance).execute();
        }
    };

    private static class PopulateDbAsyncTask extends AsyncTask<Void,Void,Void>{
        private NoteDao noteDao;
        private PopulateDbAsyncTask(NoteDatabase noteDatabase){
            this.noteDao = noteDatabase.noteDao();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            noteDao.insert(new Note("Title","Description",1));
            noteDao.insert(new Note("Title","Description",2));
            noteDao.insert(new Note("Title","Description",3));
            return null;
        }
    }
}
