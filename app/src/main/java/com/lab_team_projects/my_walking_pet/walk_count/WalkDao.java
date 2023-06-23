package com.lab_team_projects.my_walking_pet.walk_count;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

/**
 * 기기 내부 저장소에 저장되는 걸음 객체를 room database로 호출할 수 있도록 하는 구현 객체입니다.
 */
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

    @Query("SELECT * FROM Walk ORDER BY id")
    List<Walk> getAll();

    @Query("SELECT * FROM Walk ORDER BY id DESC LIMIT 1")
    Walk getLast();

}

