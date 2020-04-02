package com.moraspirit.moraspiritapp.sports;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.OnConflictStrategy;

import java.util.ArrayList;
import java.util.List;

@Dao
public interface MatchDao {

    @Query("SELECT * FROM `Match`")
    List<Match> getAll();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(ArrayList<Match> match);

    @Insert
    void insert(Match match);

    @Delete
    void delete(Match match);

    @Query("DELETE FROM `Match`")
    void deleteAll();

}