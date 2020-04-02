package com.moraspirit.moraspiritapp.database;

import android.content.Context;

import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.moraspirit.moraspiritapp.article.Article;
import com.moraspirit.moraspiritapp.article.ArticleDao;
import com.moraspirit.moraspiritapp.sports.Match;
import com.moraspirit.moraspiritapp.sports.MatchDao;
import com.moraspirit.moraspiritapp.sports.MatchPastDao;
import com.moraspirit.moraspiritapp.sports.PastMatch;

@androidx.room.Database(entities = {Article.class, Match.class, PastMatch.class},version = 5)
public abstract class Database extends RoomDatabase {
    private static String DB_NAME = "MoraSpiritDB";
    private static Database instance;

    public static synchronized Database getInstatnce(Context context){
        if(instance == null){
            instance = Room.databaseBuilder(context.getApplicationContext(), Database.class,DB_NAME).fallbackToDestructiveMigration().build();
        }
        return instance;
    }
    public abstract ArticleDao articleDao();
    public abstract MatchDao matchDao();
    public abstract MatchPastDao matchPastDao();

}
