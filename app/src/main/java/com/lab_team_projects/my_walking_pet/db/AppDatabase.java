package com.lab_team_projects.my_walking_pet.db;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.lab_team_projects.my_walking_pet.walk_count.Walk;
import com.lab_team_projects.my_walking_pet.walk_count.WalkDao;


@Database(entities = {Walk.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    private static AppDatabase database;
    private static final String DATABASE_NAME = "MY-WALKING-PET";


    /* 싱글톤 */
    public synchronized static AppDatabase getInstance(Context context)
    {
        if(database == null) {
            database = Room.databaseBuilder(context.getApplicationContext(), AppDatabase.class, DATABASE_NAME)
                    .fallbackToDestructiveMigration()   // 스키마(데이터베이스) 버전 변경 가능
                    .allowMainThreadQueries()   // 메인 쓰레드에서 IO를 가능 하게 함
                    .build();
        }
        return database;
    }

    public abstract WalkDao walkDao();

}
