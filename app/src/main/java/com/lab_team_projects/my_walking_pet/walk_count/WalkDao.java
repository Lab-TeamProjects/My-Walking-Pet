package com.lab_team_projects.my_walking_pet.walk_count;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface WalkDao {
    @Insert
    void insert(Walk walk);

    @Update
    void update(Walk walk);

    @Query("SELECT * FROM Walk ORDER BY id DESC LIMIT 1")
    LiveData<Walk> getWalkLast();

    @Delete
    void delete(Walk walk);

    @Query("SELECT * FROM Walk ORDER BY id DESC LIMIT 1")
    List<Walk> getAll();

    @Query("SELECT * FROM Walk ORDER BY id DESC LIMIT 1")
    Walk getLast();

}

