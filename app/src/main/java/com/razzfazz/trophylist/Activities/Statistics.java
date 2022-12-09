package com.razzfazz.trophylist.Activities;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.razzfazz.trophylist.GameDatabase.DBHelper;
import com.razzfazz.trophylist.R;
import com.razzfazz.trophylist.Util.ExperienceLogic;
import com.razzfazz.trophylist.Util.GlobalUtil;

public class Statistics extends AppCompatActivity {

    private static final int PICTURE_PICKER_REQUEST_CODE = 1;
    private ImageView editPicture;
    private TextView valueGamesOwned;
    private TextView valueGamesCompleted;
    private TextView valueTotalTimeSpent;
    private TextView valueAvgTimePerGame;
    private TextView valueAllTrophies;

    private SharedPreferences loginPreferences;
    private SQLiteDatabase mDatabase;

    private int mGamesOwned;
    private int mGamesCompleted;
    private int mTotalTimeSpent;
    private double mAvgTimePerGame;
    private int mAllTrophies;

    private int mAmountBr;
    private int mAmountSv;
    private int mAmountGd;
    private int mAmountPl;
    private TextView bronzeCount;
    private TextView silverCount;
    private TextView goldCount;
    private TextView platCount;
    private TextView gamerTag;
    private TextView userLevel;

    private ProgressBar progressBarUserLevel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistics);

        // connect views
        valueGamesOwned = findViewById(R.id.gamesOwnedValue);
        valueGamesCompleted = findViewById(R.id.gamesCompletedValue);
        valueTotalTimeSpent = findViewById(R.id.totalTimeSpentValue);
        valueAvgTimePerGame = findViewById(R.id.avgTimePerGameValue);
        valueAllTrophies = findViewById(R.id.allTrophiesValue);
        gamerTag = findViewById(R.id.gamerTag);
        bronzeCount = findViewById(R.id.bronzeCount);
        silverCount = findViewById(R.id.silverCount);
        goldCount = findViewById(R.id.goldCount);
        platCount = findViewById(R.id.platCount);
        progressBarUserLevel = findViewById(R.id.progressBarUserLevel);
        userLevel = findViewById(R.id.textViewUserLevelStat);
        editPicture = findViewById(R.id.userPicture);
        loginPreferences = getSharedPreferences("loginPrefs", MODE_PRIVATE);


        DBHelper dbHelper = new DBHelper(this);
        mDatabase = dbHelper.getReadableDatabase();
        int pic_id = loginPreferences.getInt("profilePic", R.drawable.picture0);
        String username = loginPreferences.getString("username", "Gamer1337");
        gamerTag.setText(username);
        editPicture.setImageResource(pic_id);

        // setting OnClickListeners
        editPicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openPicturePicker();
            }
        });

        this.initInformation();
    }

    // opens the PicturePicker Activity
    private void openPicturePicker() {
        Intent intent = new Intent(this, PicturePicker.class);
        startActivityForResult(intent, PICTURE_PICKER_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == PICTURE_PICKER_REQUEST_CODE) {
            if(resultCode == Activity.RESULT_OK) {
                SharedPreferences.Editor loginPrefsEditor = loginPreferences.edit();
                loginPrefsEditor.putInt("profilePic", data.getIntExtra("SELECTED_PIC", R.drawable.picture0));
                loginPrefsEditor.commit();
                
                int pic_id = loginPreferences.getInt("profilePic", R.drawable.picture0);
                editPicture.setImageResource(pic_id);
            }
        }
    }

    // initializes the statistics with different queries
    private void initInformation() {
        // needed to not display "NaNh" on some devices, when the database is empty
        mTotalTimeSpent = 0;
        // query that counts all games
        String query = "SELECT COUNT(" + DBHelper.COLUMN_ID +") FROM " + DBHelper.GAME_INFO_TABLE;
        Cursor cursor = mDatabase.rawQuery(query, null);
        if(cursor.moveToFirst()) {
            mGamesOwned = cursor.getInt(0);
        }
        cursor.close();

        // query that counts all games that are comlpeted
        query = "SELECT COUNT(" + DBHelper.COLUMN_PERCENT + 
                ") FROM " + DBHelper.USER_PROGRESS_TABLE + 
                " WHERE " + DBHelper.COLUMN_PERCENT + " = 100";
        cursor = mDatabase.rawQuery(query, null);
        if(cursor.moveToFirst()) {
            mGamesCompleted = cursor.getInt(0);
        }
        cursor.close();

        // query that gives the total time spent
        query = "SELECT SUM(" + DBHelper.COLUMN_TIME_SPENT + ") FROM " + DBHelper.USER_PROGRESS_TABLE;
        cursor = mDatabase.rawQuery(query, null);
        if(cursor.moveToFirst()) {
            mTotalTimeSpent = cursor.getInt(0);
        }
        cursor.close();

        // query that gets the counts of all trophies
        query = "SELECT SUM(" + DBHelper.COLUMN_PROGRESS_BRONZE +
                "), SUM(" + DBHelper.COLUMN_PROGRESS_SILVER +
                "), SUM(" + DBHelper.COLUMN_PROGRESS_GOLD +
                "), SUM(" + DBHelper.COLUMN_PROGRESS_PLAT +
                ") FROM " + DBHelper.USER_PROGRESS_TABLE;
        cursor = mDatabase.rawQuery(query, null);
        if(cursor.moveToFirst()) {
            mAmountBr = cursor.getInt(0);
            mAmountSv = cursor.getInt(1);
            mAmountGd = cursor.getInt(2);
            mAmountPl = cursor.getInt(3);
            int[] trophies = {mAmountBr, mAmountSv, mAmountGd, mAmountPl};
            ExperienceLogic.calculateLevelprogress(trophies);
            progressBarUserLevel.setProgress(ExperienceLogic.getLevelProgress());
            userLevel.setText(String.valueOf(ExperienceLogic.getLevel()));
            mAllTrophies = mAmountBr + mAmountSv + mAmountGd + mAmountPl;
        }
        cursor.close();

        // filling views with data
        bronzeCount.setText(String.valueOf(mAmountBr));
        silverCount.setText(String.valueOf(mAmountSv));
        goldCount.setText(String.valueOf(mAmountGd));
        platCount.setText(String.valueOf(mAmountPl));

        if(mGamesCompleted > 0) {
            mAvgTimePerGame = mTotalTimeSpent / (double) mGamesCompleted;
        } else {
            mAvgTimePerGame = 0.0;
        }

        String avgTPG = GlobalUtil.convertMinToH(mAvgTimePerGame)+ "h";
        String totalTS = GlobalUtil.convertMinToH(mTotalTimeSpent) + "h";

        valueGamesOwned.setText(String.valueOf(mGamesOwned));
        valueGamesCompleted.setText(String.valueOf(mGamesCompleted));
        valueTotalTimeSpent.setText(totalTS);
        valueAvgTimePerGame.setText(avgTPG);
        valueAllTrophies.setText(String.valueOf(mAllTrophies));
    }
}
