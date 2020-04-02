package com.moraspirit.moraspiritapp.article;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;
@Dao
public interface ArticleDao {
    @Query("SELECT * FROM Article")
    List<Article> getAll();

    @Query("SELECT * FROM Article WHERE id=0")
    Article getFirst();

    @Insert
    void insertAll(Article... users);

    @Insert
    void insert(Article user);

    @Delete
    void delete(Article user);

    @Query("DELETE FROM Article")
    void deleteAll();
}