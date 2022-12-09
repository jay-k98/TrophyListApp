package com.razzfazz.trophylist.GameDatabase;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

public class DBHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "gamecollection.db";
    private static final int DATABASE_VERSION = 1;

    public static final String GAME_INFO_TABLE = "game_info";
    public static final String USER_PROGRESS_TABLE = "user_progress";

    public static final String COLUMN_ID = "id";

    // Game Info Columns
    public static final String COLUMN_TITLE = "title";
    public static final String COLUMN_GENRE = "genre";
    public static final String COLUMN_MAX_BRONZE = "maxBronze";
    public static final String COLUMN_MAX_SILVER = "maxSilver";
    public static final String COLUMN_MAX_GOLD = "maxGold";
    public static final String COLUMN_HAS_PLAT = "hasPlat";
    public static final String COLUMN_PICTURE = "picture";

    // User Progress Columns
    public static final String COLUMN_PROGRESS_BRONZE = "progressBronze";
    public static final String COLUMN_PROGRESS_SILVER = "progressSilver";
    public static final String COLUMN_PROGRESS_GOLD = "progressGold";
    public static final String COLUMN_PROGRESS_PLAT = "progressPlat";
    public static final String COLUMN_TIME_SPENT = "timeSpent";
    public static final String COLUMN_PERCENT = "percent";

    // columns with prefixes
    public static final String GAME_INFO_ID_WITH_PREFIX = "gi.id";
    public static final String GAME_INFO_TITLE_WITH_PREFIX = "gi.title";
    public static final String GAME_INFO_GENRE_WITH_PREFIX = "gi.genre";
    public static final String GAME_INFO_MAX_BRONZE_WITH_PREFIX = "gi.maxBronze";
    public static final String GAME_INFO_MAX_SILVER_WITH_PREFIX = "gi.maxSilver";
    public static final String GAME_INFO_MAX_GOLD_WITH_PREFIX = "gi.maxGold";
    public static final String GAME_INFO_HAS_PLAT_WITH_PREFIX = "gi.hasPlat";
    public static final String GAME_INFO_PICTURE_WITH_PREFIX = "gi.picture";

    public static final String USER_PROGRESS_ID_WITH_PREFIX = "up.id";
    public static final String USER_PROGRESS_PROGRESS_BRONZE_WITH_PREFIX = "up.progressBronze";
    public static final String USER_PROGRESS_PROGRESS_SILVER_WITH_PREFIX = "up.progressSilver";
    public static final String USER_PROGRESS_PROGRESS_GOLD_WITH_PREFIX = "up.progressGold";
    public static final String USER_PROGRESS_PROGRESS_PLAT_WITH_PREFIX = "up.progressPlat";
    public static final String USER_PROGRESS_TIME_SPENT_WITH_PREFIX = "up.timeSpent";
    public static final String USER_PROGRESS_PERCENT_WITH_PREFIX = "up.percent";

    // Create Table Statements
    public static final String CREATE_GAME_INFO_TABLE = "CREATE TABLE " +
            GAME_INFO_TABLE + " (" +
            COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            COLUMN_TITLE + " TEXT NOT NULL, " +
            COLUMN_GENRE + " TEXT NOT NULL, " +
            COLUMN_MAX_BRONZE + " INTEGER NOT NULL, " +
            COLUMN_MAX_SILVER + " INTEGER NOT NULL, " +
            COLUMN_MAX_GOLD + " INTEGER NOT NULL, " +
            COLUMN_HAS_PLAT + " INTEGER NOT NULL, " +
            COLUMN_PICTURE + " INTEGER NOT NULL" + //stores Image Uri
            ");";

    public static final String CREATE_USER_PROGRESS_TABLE = "CREATE TABLE " +
            USER_PROGRESS_TABLE + " (" +
            COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            COLUMN_PROGRESS_BRONZE + " INTEGER NOT NULL, " +
            COLUMN_PROGRESS_SILVER + " INTEGER NOT NULL, " +
            COLUMN_PROGRESS_GOLD + " INTEGER NOT NULL, " +
            COLUMN_PROGRESS_PLAT + " INTEGER NOT NULL, " +
            COLUMN_TIME_SPENT + " INTEGER NOT NULL, " +
            COLUMN_PERCENT + " INTEGER NOT NULL" +
            ");";


    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_GAME_INFO_TABLE);
        db.execSQL(CREATE_USER_PROGRESS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+ USER_PROGRESS_TABLE);
        db.execSQL("DROP TABLE IF EXISTS "+ GAME_INFO_TABLE);
        onCreate(db);
    }

    }
