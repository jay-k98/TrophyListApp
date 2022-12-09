package com.razzfazz.trophylist.Activities;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.razzfazz.trophylist.GameDatabase.DBHelper;
import com.razzfazz.trophylist.Adapter.GameListAdapter;
import com.razzfazz.trophylist.R;
import com.razzfazz.trophylist.Util.ExperienceLogic;
import com.razzfazz.trophylist.Util.GlobalUtil;

public class GameProgress extends AppCompatActivity {

    private ImageView gamePicture;
    private TextView gameTitle;
    private TextView gameGenre;
    private ProgressBar progressBarGame;
    private TextView textViewAmountBronze;
    private TextView textViewAmountSilver;
    private TextView textViewAmountGold;
    private TextView textViewAmountPlatinum;
    private TextView textViewMaxBronze;
    private TextView textViewMaxSilver;
    private TextView textViewMaxGold;
    private TextView textViewMaxPlatinum;
    private Button decreaseBronze;
    private Button increaseBronze;
    private Button decreaseSilver;
    private Button increaseSilver;
    private Button decreaseGold;
    private Button increaseGold;
    private TextView valueTimeWasted;
    private TextView valueLastSession;
    private Button add15min;
    private Button add30min;
    private Button add60min;
    private Button minus15min;
    private Button buttonSaveProgress;
    private Button buttonCancelProgress;
    private SQLiteDatabase mDatabase;
    private String mId;
    private int mAmountBr;
    private int mAmountSv;
    private int mAmountGd;
    private int mAmountPl;
    private int mOrigAmountBr;
    private int mOrigAmountSv;
    private int mOrigAmountGd;
    private int mMaxBr;
    private int mMaxSv;
    private int mMaxGd;
    private int mMaxPl;
    private int mTimeSpent;
    private int mTimeLastSession;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_progress);

        // connect views
        gameTitle = findViewById(R.id.gameProgressTitle);
        gameGenre = findViewById(R.id.gameProgressGenre);
        gamePicture = findViewById(R.id.gameProgressPicture);
        progressBarGame = findViewById(R.id.progressBarGameProgress);
        textViewAmountBronze = findViewById(R.id.userProgressBronze);
        textViewAmountSilver = findViewById(R.id.userProgressSilver);
        textViewAmountGold = findViewById(R.id.userProgressGold);
        textViewAmountPlatinum = findViewById(R.id.userProgressPlatinum);
        textViewMaxBronze = findViewById(R.id.gameMaxBronze);
        textViewMaxSilver = findViewById(R.id.gameMaxSilver);
        textViewMaxGold = findViewById(R.id.gameMaxGold);
        textViewMaxPlatinum = findViewById(R.id.gameMaxPlatinum);
        valueTimeWasted = findViewById(R.id.valueTimeWasted);
        valueLastSession = findViewById(R.id.valueTimeLastSession);
        decreaseBronze = findViewById(R.id.buttonDecreaseBronze);
        decreaseSilver = findViewById(R.id.buttonDecreaseSilver);
        decreaseGold = findViewById(R.id.buttonDecreaseGold);
        increaseBronze = findViewById(R.id.buttonIncreaseBronze);
        increaseSilver = findViewById(R.id.buttonIncreaseSilver);
        increaseGold = findViewById(R.id.buttonIncreaseGold);
        add15min = findViewById(R.id.buttonAdd15min);
        add30min = findViewById(R.id.buttonAdd30min);
        add60min = findViewById(R.id.buttonAdd60min);
        minus15min = findViewById(R.id.buttonMinus15Min);
        buttonSaveProgress = findViewById(R.id.buttonSaveProgress);
        buttonCancelProgress = findViewById(R.id.buttonCancelProgress);

        // initialize values
        Intent i = getIntent();
        mId = i.getStringExtra("ID");
        DBHelper dbHelper = new DBHelper(this);
        mDatabase = dbHelper.getWritableDatabase();

        // setting OnClickListeners
        increaseBronze.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                increaseBr();
            }
        });
        increaseSilver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                increaseSv();
            }
        });
        increaseGold.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                increaseGd();
            }
        });
        decreaseBronze.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                decreaseBr();
            }
        });
        decreaseSilver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                decreaseSv();
            }
        });
        decreaseGold.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                decreaseGd();
            }
        });
        add15min.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addTime(15);
            }
        });
        add30min.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addTime(30);
            }
        });
        add60min.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addTime(60);
            }
        });
        minus15min.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                decreaseTime(15);
            }
        });
        buttonSaveProgress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveProgress();
            }
        });
        buttonCancelProgress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cancelProgress();
            }
        });

        this.initInformation();
    }

    // cancel to GameList Activity
    private void cancelProgress() {
        setResult(Activity.RESULT_CANCELED);
        finish();
    }

    // sets the result for GameList Activity, where the progress will be saved in the database
    private void saveProgress() {
        Intent i = new Intent();
        String plat = checkForPlat();

        i.putExtra("PROGRESS_BRONZE", String.valueOf(mAmountBr));
        i.putExtra("PROGRESS_SILVER", String.valueOf(mAmountSv));
        i.putExtra("PROGRESS_GOLD", String.valueOf(mAmountGd));
        i.putExtra("PROGRESS_PLAT", plat);
        i.putExtra( "TIME_SPENT", String.valueOf(mTimeSpent));
        i.putExtra("PROGRESS_ID", String.valueOf(mId));
        i.putExtra("PERCENT", String.valueOf(ExperienceLogic.calculateGameProgress(getAmounts(), getMaximums())));

        setResult(Activity.RESULT_OK, i);
        finish();
    }

    // checks if the user achieved the platinum trophy by collecting every other trophy
    private String checkForPlat() {
        if(mAmountGd == mMaxGd && mAmountBr == mMaxBr && mAmountSv == mMaxSv) {
            mAmountPl = 1;
            return "1";
        }
        return "0";
    }

    // increase amount of achieved bronze trophies by one
    private void increaseBr() {
        if(mAmountBr + 1 <= mMaxBr) {
            mAmountBr++;
            textViewAmountBronze.setText(String.valueOf(mAmountBr));
            progressBarGame.setProgress(ExperienceLogic.calculateGameProgress(getAmounts(), getMaximums()));
        }
        else {
            Toast.makeText(this, "Max. reached", Toast.LENGTH_SHORT).show();
        }
    }

    // decreases the amount of achieved bronze trophies by one, but you can't go under the original amount
    private void decreaseBr() {
        if(mAmountBr - 1 >= mOrigAmountBr && mAmountPl != 1) {
            mAmountBr--;
            textViewAmountBronze.setText(String.valueOf(mAmountBr));
            progressBarGame.setProgress(ExperienceLogic.calculateGameProgress(getAmounts(), getMaximums()));
        }
        else {
            Toast.makeText(this, "Min. reached", Toast.LENGTH_SHORT).show();
        }
    }

    // increase amount of achieved silver trophies by one
    private void increaseSv() {
        if(mAmountSv + 1 <= mMaxSv) {
            mAmountSv++;
            textViewAmountSilver.setText(String.valueOf(mAmountSv));
            progressBarGame.setProgress(ExperienceLogic.calculateGameProgress(getAmounts(), getMaximums()));
        }
        else {
            Toast.makeText(this, "Max. reached", Toast.LENGTH_SHORT).show();
        }
    }

    // decreases the amount of achieved silver trophies by one, but you can't go under the original amount
    private void decreaseSv() {
        if(mAmountSv - 1 >= mOrigAmountSv && mAmountPl != 1) {
            mAmountSv--;
            textViewAmountSilver.setText(String.valueOf(mAmountSv));
            progressBarGame.setProgress(ExperienceLogic.calculateGameProgress(getAmounts(), getMaximums()));
        }
        else {
            Toast.makeText(this, "Min. reached", Toast.LENGTH_SHORT).show();
        }
    }

    // increase amount of achieved gold trophies by one
    private void increaseGd() {
        if(mAmountGd + 1 <= mMaxGd) {
            mAmountGd++;
            textViewAmountGold.setText(String.valueOf(mAmountGd));
            progressBarGame.setProgress(ExperienceLogic.calculateGameProgress(getAmounts(), getMaximums()));
        }
        else {
            Toast.makeText(this, "Max. reached", Toast.LENGTH_SHORT).show();
        }
    }

    // decreases the amount of achieved gold trophies by one, but you can't go under the original amount
    private void decreaseGd() {
        if(mAmountGd - 1 >= mOrigAmountGd && mAmountPl != 1) {
            mAmountGd--;
            textViewAmountGold.setText(String.valueOf(mAmountGd));
            progressBarGame.setProgress(ExperienceLogic.calculateGameProgress(getAmounts(), getMaximums()));
        }
        else {
            Toast.makeText(this, "Min. reached", Toast.LENGTH_SHORT).show();
        }
    }

    // adds the selected amount of time to the total time spent
    private void addTime(int minutes) {
        mTimeSpent += minutes;
        String time = GlobalUtil.convertMinToH(mTimeSpent) + "h";
        valueTimeWasted.setText(time);
    }

    // decreases the total time spent, but never under the value of last session
    private void decreaseTime(int minutes) {
        if(mTimeSpent - minutes >= mTimeLastSession) {
            mTimeSpent -= minutes;
            String time = GlobalUtil.convertMinToH(mTimeSpent) + "h";
            valueTimeWasted.setText(time);
        }
        else {
            Toast.makeText(this, "Can't go under your last sessions time", Toast.LENGTH_SHORT).show();
        }
    }

    // initializes the game progress information
    private void initInformation() {
        String query = "SELECT " + DBHelper.GAME_INFO_TITLE_WITH_PREFIX + "," +
                        DBHelper.GAME_INFO_GENRE_WITH_PREFIX + "," +
                        DBHelper.GAME_INFO_MAX_BRONZE_WITH_PREFIX + "," +
                        DBHelper.GAME_INFO_MAX_SILVER_WITH_PREFIX + "," +
                        DBHelper.GAME_INFO_MAX_GOLD_WITH_PREFIX + "," +
                        DBHelper.GAME_INFO_HAS_PLAT_WITH_PREFIX + "," +
                        DBHelper.GAME_INFO_PICTURE_WITH_PREFIX + "," +
                        DBHelper.USER_PROGRESS_PROGRESS_BRONZE_WITH_PREFIX + "," +
                        DBHelper.USER_PROGRESS_PROGRESS_SILVER_WITH_PREFIX + "," +
                        DBHelper.USER_PROGRESS_PROGRESS_GOLD_WITH_PREFIX + "," +
                        DBHelper.USER_PROGRESS_PROGRESS_PLAT_WITH_PREFIX + "," +
                        DBHelper.USER_PROGRESS_TIME_SPENT_WITH_PREFIX + "," +
                        DBHelper.USER_PROGRESS_PERCENT_WITH_PREFIX +
                        " FROM " + DBHelper.GAME_INFO_TABLE +
                        " gi INNER JOIN " + DBHelper.USER_PROGRESS_TABLE +
                        " up WHERE " + DBHelper.GAME_INFO_ID_WITH_PREFIX +
                        " = " + DBHelper.USER_PROGRESS_ID_WITH_PREFIX +
                        " AND " + DBHelper.GAME_INFO_ID_WITH_PREFIX + " = " +
                        mId;

        Cursor cursor = mDatabase.rawQuery(query, null);

        if(cursor.moveToFirst()) {
            this.mMaxBr = cursor.getInt(2);
            this.mMaxSv = cursor.getInt(3);
            this.mMaxGd = cursor.getInt(4);
            this.mMaxPl = cursor.getInt(5);
            this.mOrigAmountBr = mAmountBr;
            this.mOrigAmountSv = mAmountSv;
            this.mOrigAmountGd = mAmountGd;
            this.mAmountBr = cursor.getInt(7);
            this.mAmountSv = cursor.getInt(8);
            this.mAmountGd = cursor.getInt(9);
            this.mAmountPl = cursor.getInt(10);
            this.mTimeSpent = cursor.getInt(11);
            this.mTimeLastSession = mTimeSpent;

            String slash = "/";
            String maxB = slash + mMaxBr;
            String maxS = slash + mMaxSv;
            String maxG = slash + mMaxGd;
            String maxP = slash + mMaxPl;

            gameTitle.setText(cursor.getString(0));
            gameGenre.setText(cursor.getString(1));
            textViewMaxBronze.setText(maxB);
            textViewMaxSilver.setText(maxS);
            textViewMaxGold.setText(maxG);
            textViewMaxPlatinum.setText(maxP);
            gamePicture.setImageResource(GameListAdapter.getGenreIcons()[cursor.getInt(6)]);
            textViewAmountBronze.setText(String.valueOf(mAmountBr));
            textViewAmountSilver.setText(String.valueOf(mAmountSv));
            textViewAmountGold.setText(String.valueOf(mAmountGd));
            textViewAmountPlatinum.setText(String.valueOf(mAmountPl));

            String time = GlobalUtil.convertMinToH(mTimeSpent) + "h";
            valueTimeWasted.setText(time);
            valueLastSession.setText(time);
            progressBarGame.setProgress(cursor.getInt(12));
        }
        cursor.close();
    }
    // returns an array with all trophy amounts
    private int[] getAmounts() {
        return new int[] {mAmountBr, mAmountSv, mAmountGd, mAmountPl};
    }

    // returns an array with all trophy maximums
    private int[] getMaximums() {
        return new int[] {mMaxBr, mMaxSv, mMaxGd, mMaxPl};
    }
}
