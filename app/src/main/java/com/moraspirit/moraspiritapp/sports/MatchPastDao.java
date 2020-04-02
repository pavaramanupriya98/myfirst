package com.moraspirit.moraspiritapp.sports;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.ArrayList;
import java.util.List;

@Dao
public interface MatchPastDao {

    @Query("SELECT * FROM `PastMatch`")
    List<PastMatch> getAll();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(ArrayList<PastMatch> match);

    @Insert
    void insert(PastMatch match);

    @Delete
    void delete(PastMatch match);

    @Query("DELETE FROM `PastMatch`")
    void deleteAll();

}