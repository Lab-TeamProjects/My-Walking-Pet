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
    /**
     * Insert.
     *
     * @param walk the walk
     */
    @Insert
    void insert(Walk walk);

    /**
     * Update.
     *
     * @param walk the walk
     */
    @Update
    void update(Walk walk);

    /**
     * Gets walk last.
     *
     * @return the walk last
     */
    @Query("SELECT * FROM Walk ORDER BY id DESC LIMIT 1")
    LiveData<Walk> getWalkLast();

    /**
     * Delete.
     *
     * @param walk the walk
     */
    @Delete
    void delete(Walk walk);

    /**
     * Gets all.
     *
     * @return the all
     */
    @Query("SELECT * FROM Walk ORDER BY id")
    List<Walk> getAll();

    /**
     * Gets last.
     *
     * @return the last
     */
    @Query("SELECT * FROM Walk ORDER BY id DESC LIMIT 1")
    Walk getLast();

}

