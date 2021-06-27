package com.yh.simvest;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {Share.class}, views = {ShareDBView.class},version = 1)
public abstract class AppDatabase extends RoomDatabase {
    public abstract ShareDao shareDao();
}
