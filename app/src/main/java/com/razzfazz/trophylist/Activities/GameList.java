package com.razzfazz.trophylist.Activities;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.support.annotation.NonNull;
import android.support.constraint.Group;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.razzfazz.trophylist.GameDatabase.DBHelper;
import com.razzfazz.trophylist.Adapter.GameListAdapter;
import com.razzfazz.trophylist.R;
import com.razzfazz.trophylist.Util.ExperienceLogic;

import static com.razzfazz.trophylist.Util.ExperienceLogic.calculateLevelprogress;

public class GameList extends AppCompatActivity implements GameListAdapter.OnItemListener {

    private static final int GAME_EDITOR_ACTIVITY_REQUEST = 1;
    private static final int GAME_PROGRESS_ACTIVITY_REQUEST = 2;

    private TextView gamerTag;
    private TextView bronzeCount;
    private TextView silverCount;
    private TextView goldCount;
    private TextView platCount;
    private TextView userLevel;
    private FloatingActionButton buttonAdd;
    private RecyclerView gameListRecycler;
    private ProgressBar progressBarUserLevel;
    private ImageView profilePic;
    private Spinner spinnerFilter;
    private Spinner spinnerOrderBy;
    private CheckBox checkBox100Percent;
    private SQLiteDatabase mDatabase;
    private GameListAdapter mAdapter;
    private ImageButton buttonFilter;
    private ImageButton buttonToggleOrder;

    private String orderBy;
    private String order;
    private String filter;
    private static String ASC = "ASC";
    private static String DESC = "DESC";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_list);

        // connect views
        profilePic = findViewById(R.id.userPicture);
        gameListRecycler = findViewById(R.id.recyclerGameList);
        userLevel = findViewById(R.id.textViewUserLevel);
        gamerTag = findViewById(R.id.gamerTag);
        bronzeCount = findViewById(R.id.bronzeCount);
        silverCount = findViewById(R.id.silverCount);
        goldCount = findViewById(R.id.goldCount);
        platCount = findViewById(R.id.platCount);
        progressBarUserLevel = findViewById(R.id.progressBarUserLevel);
        checkBox100Percent = findViewById(R.id.checkBox100Percent);
        buttonAdd = findViewById(R.id.buttonAddGameItem);
        buttonFilter = findViewById(R.id.imageButtonFilter);
        buttonToggleOrder = findViewById(R.id.imageButtonToggleOrder);
        spinnerFilter = findViewById(R.id.spinnerFilterGenre);
        spinnerOrderBy = findViewById(R.id.spinnerOrderBy);

        // getting SharedPreferences for
        SharedPreferences loginPreferences = getSharedPreferences("loginPrefs", MODE_PRIVATE);
        int pic_id = loginPreferences.getInt("profilePic", R.drawable.picture0);
        String username = loginPreferences.getString("username", "Gamer1337");

        // initializing values and opening the database
        order = ASC;
        filter = "All";
        profilePic.setImageResource(pic_id);
        gamerTag.setText(username);
        DBHelper dbHelper = new DBHelper(this);
        mDatabase = dbHelper.getWritableDatabase();

        // setting up the RecyclerViews Layout Manager
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        gameListRecycler.setLayoutManager(linearLayoutManager);

        // adding the divider for the items in the list
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(gameListRecycler.getContext(),
                linearLayoutManager.getOrientation());
        gameListRecycler.addItemDecoration(dividerItemDecoration);

        // setting the Adapter of the RecyclerView
        mAdapter = new GameListAdapter(this, getAllItems(), this);
        gameListRecycler.setAdapter(mAdapter);

        // creating the ItemTouchHelper for "swipe to delete"-gesture
        // and attaching it to the RecyclerView
        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder viewHolder1) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
                removeItem((long) viewHolder.itemView.getTag());
            }
        }).attachToRecyclerView(gameListRecycler);

        // filter-spinner gets filled with the preset values from string-array "filter_array"
        ArrayAdapter<CharSequence> fAdapter = ArrayAdapter.createFromResource(this,
                R.array.filter_array, android.R.layout.simple_spinner_item);
        fAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerFilter.setAdapter(fAdapter);
        spinnerFilter.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                filter = spinnerFilter.getSelectedItem().toString();
                mAdapter.swapCursor(getFilterCursor());
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                mAdapter.swapCursor(getFilterCursor());
            }
        });

        // orderBy-spinner gets filled with the preset values from string-array "order_by_array"
        ArrayAdapter<CharSequence> oAdapter = ArrayAdapter.createFromResource(this,
                R.array.order_by_array, android.R.layout.simple_spinner_item);
        oAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerOrderBy.setAdapter(oAdapter);
        spinnerOrderBy.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                orderBy = spinnerOrderBy.getSelectedItem().toString();
                if (orderBy.equals("Percent"))
                    order = DESC;
                if (orderBy.equals("Title") || orderBy.equals("Genre"))
                    order = ASC;
                mAdapter.swapCursor(getFilterCursor());
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                mAdapter.swapCursor(getFilterCursor());
            }
        });
        // setting OnClickListeners
        buttonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGameEditor();
            }
        });
        buttonFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                filter();
            }
        });
        buttonToggleOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleOrder();
            }
        });
        // setting OnClickListeners for all items of the group to get to the Statistics Activity
        Group group = findViewById(R.id.groupStatistics);
        int[] refIds = group.getReferencedIds();
        for (int id : refIds) {
            findViewById(id).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    openStatistics();
                }
            });
        }
        checkBox100Percent.setOnCheckedChangeListener(new CheckBox.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                mAdapter.swapCursor(getFilterCursor());
            }
        });
    }

    // by swiping left or right on an item in the list it gets removed
    // we need to delete the entry in the game_info_table and the corresponding
    // user_progress_table entry
    // to update the RecyclerView we swap the Cursor of the RecyclerViews Adapter
    private void removeItem(long id) {
        mDatabase.delete(DBHelper.GAME_INFO_TABLE, DBHelper.COLUMN_ID + " = "  + id, null);
        mDatabase.delete(DBHelper.USER_PROGRESS_TABLE, DBHelper.COLUMN_ID + " = " + id, null);
        mAdapter.swapCursor(getFilterCursor());
        updateUserStatistics();
    }

    // call swapCursor() with the getFilterCursor() method, to pass
    // the filtered List to the Adapter and update the RecyclerView
    private void filter() {
        mAdapter.swapCursor(getFilterCursor());
    }

    // queries the gamecollection.db with the filters and order-by-statement, that the user selected
    private Cursor getFilterCursor() {
        String query;
        String percentStr = (checkBox100Percent.isChecked()) ? " AND " +
                DBHelper.USER_PROGRESS_PERCENT_WITH_PREFIX + " = 100 " : "";
        String genreStr = (!filter.equals("All")) ? " AND " +
                DBHelper.GAME_INFO_GENRE_WITH_PREFIX + " = '" +
                spinnerFilter.getSelectedItem().toString() + "' " : "";

        query = "SELECT " + DBHelper.GAME_INFO_ID_WITH_PREFIX + "," +
                DBHelper.GAME_INFO_TITLE_WITH_PREFIX + "," +
                DBHelper.GAME_INFO_PICTURE_WITH_PREFIX + "," +
                DBHelper.USER_PROGRESS_PROGRESS_BRONZE_WITH_PREFIX + "," +
                DBHelper.USER_PROGRESS_PROGRESS_SILVER_WITH_PREFIX + "," +
                DBHelper.USER_PROGRESS_PROGRESS_GOLD_WITH_PREFIX + "," +
                DBHelper.USER_PROGRESS_PROGRESS_PLAT_WITH_PREFIX + "," +
                DBHelper.USER_PROGRESS_PERCENT_WITH_PREFIX +
                " FROM " + DBHelper.GAME_INFO_TABLE +
                " gi INNER JOIN " + DBHelper.USER_PROGRESS_TABLE +
                " up WHERE " + DBHelper.GAME_INFO_ID_WITH_PREFIX + " = " +
                DBHelper.USER_PROGRESS_ID_WITH_PREFIX +
                genreStr +
                percentStr +
                " ORDER BY " + orderBy + " COLLATE NOCASE " + order;
        Log.d("QUERY", query);
        return mDatabase.rawQuery(query, null);
    }

    // toggles the order of the list, depending on its previous state ASC/DESC and updates the
    // Cursor
    private void toggleOrder() {
        if(order == ASC) {
            order = DESC;
        } else {
            order = ASC;
        }
        mAdapter.swapCursor(getFilterCursor());
    }

    // opens the Statistics Activity
    private void openStatistics() {
        Intent intent = new Intent(this, Statistics.class);
        startActivity(intent);
    }

    // opens the GameEditor Activity
    private void openGameEditor() {
        Intent intent = new Intent(this, GameEditor.class);
        startActivityForResult(intent, GAME_EDITOR_ACTIVITY_REQUEST);
    }

    // updates the statistics to always be correct
    @Override
    protected void onResume() {
        super.onResume();
        SharedPreferences loginPreferences = getSharedPreferences("loginPrefs", MODE_PRIVATE);
        int pic_id = loginPreferences.getInt("profilePic", R.drawable.picture0);
        profilePic.setImageResource(pic_id);
        updateUserStatistics();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch(requestCode) {
            case GAME_EDITOR_ACTIVITY_REQUEST : {
                if (resultCode == Activity.RESULT_OK) {
                    // all columns of the user_progress_table get filled with zero, because you don't
                    // have any progress when first creating a game
                    ContentValues upValues = new ContentValues();
                    upValues.put(DBHelper.COLUMN_PROGRESS_BRONZE, 0);
                    upValues.put(DBHelper.COLUMN_PROGRESS_SILVER, 0);
                    upValues.put(DBHelper.COLUMN_PROGRESS_GOLD, 0);
                    upValues.put(DBHelper.COLUMN_PROGRESS_PLAT, 0);
                    upValues.put(DBHelper.COLUMN_TIME_SPENT, 0);
                    upValues.put(DBHelper.COLUMN_PERCENT, 0);

                    insertUserProgress(upValues);

                    // inserts the data of GameEditor into the game_info-table
                    ContentValues giValues = new ContentValues();
                    giValues.put(DBHelper.COLUMN_TITLE, data.getStringExtra("TITLE"));
                    giValues.put(DBHelper.COLUMN_GENRE, data.getStringExtra("GENRE"));
                    giValues.put(DBHelper.COLUMN_MAX_BRONZE, Integer.parseInt(data.getStringExtra("MAX_BRONZE")));
                    giValues.put(DBHelper.COLUMN_MAX_SILVER, Integer.parseInt(data.getStringExtra("MAX_SILVER")));
                    giValues.put(DBHelper.COLUMN_MAX_GOLD, Integer.parseInt(data.getStringExtra("MAX_GOLD")));
                    giValues.put(DBHelper.COLUMN_HAS_PLAT, Integer.parseInt(data.getStringExtra("HAS_PLAT")));
                    giValues.put(DBHelper.COLUMN_PICTURE, Integer.parseInt(data.getStringExtra("PICTURE")));

                    insertGameInfo(giValues);

                    Toast.makeText(this, "Save Successful", Toast.LENGTH_SHORT).show();

                    // updates RecyclerView
                    mAdapter.swapCursor(getAllItems());
                }
                break;
            }
            case GAME_PROGRESS_ACTIVITY_REQUEST : {
                if(resultCode == Activity.RESULT_OK) {
                    // game_progress_table entry gets updated with users data from GameProgressActivity
                    ContentValues updateValues = new ContentValues();
                    updateValues.put(DBHelper.COLUMN_PROGRESS_BRONZE, Integer.parseInt(data.getStringExtra("PROGRESS_BRONZE")));
                    updateValues.put(DBHelper.COLUMN_PROGRESS_SILVER, Integer.parseInt(data.getStringExtra("PROGRESS_SILVER")));
                    updateValues.put(DBHelper.COLUMN_PROGRESS_GOLD, Integer.parseInt(data.getStringExtra("PROGRESS_GOLD")));
                    updateValues.put(DBHelper.COLUMN_PROGRESS_PLAT, Integer.parseInt(data.getStringExtra("PROGRESS_PLAT")));
                    updateValues.put(DBHelper.COLUMN_TIME_SPENT, Integer.parseInt(data.getStringExtra("TIME_SPENT")));
                    updateValues.put(DBHelper.COLUMN_PERCENT, Integer.parseInt(data.getStringExtra("PERCENT")));

                    updateUserProgress(updateValues, Long.parseLong(data.getStringExtra("PROGRESS_ID")));

                    // updates RecyclerView
                    mAdapter.swapCursor(getAllItems());
                }
            }
        }
    }

    // inserts data into user_progress_table
    private long insertUserProgress(ContentValues cv) {
        return mDatabase.insert(DBHelper.USER_PROGRESS_TABLE, null, cv);
    }

    // inserts data into game_info_table
    private long insertGameInfo(ContentValues cv) {
        return mDatabase.insert(DBHelper.GAME_INFO_TABLE, null, cv);
    }

    // updates entry in user_progress_table
    private long updateUserProgress(ContentValues cv, long id) {
        return mDatabase.update(DBHelper.USER_PROGRESS_TABLE, cv, "id = " + id, null);
    }

    // returns a Cursor with all items in the game_info and user_progress table
    // the items get joined on their id which is always identical, because it is set them same time
    // they are created and whenever an item gets deleted the entry gets deleted in both tables
    private Cursor getAllItems() {
        try {
            String query = "SELECT " + DBHelper.GAME_INFO_ID_WITH_PREFIX + "," +
                    DBHelper.GAME_INFO_TITLE_WITH_PREFIX + "," +
                    DBHelper.GAME_INFO_PICTURE_WITH_PREFIX + "," +
                    DBHelper.USER_PROGRESS_PROGRESS_BRONZE_WITH_PREFIX + "," +
                    DBHelper.USER_PROGRESS_PROGRESS_SILVER_WITH_PREFIX + "," +
                    DBHelper.USER_PROGRESS_PROGRESS_GOLD_WITH_PREFIX + "," +
                    DBHelper.USER_PROGRESS_PROGRESS_PLAT_WITH_PREFIX + "," +
                    DBHelper.USER_PROGRESS_PERCENT_WITH_PREFIX +
                    " FROM " + DBHelper.GAME_INFO_TABLE +
                    " gi INNER JOIN " + DBHelper.USER_PROGRESS_TABLE +
                    " up WHERE " +  DBHelper.GAME_INFO_ID_WITH_PREFIX + " = " +
                    DBHelper.USER_PROGRESS_ID_WITH_PREFIX;
            Log.d("QUERY", query);
            return mDatabase.rawQuery(query, null);
        } catch(SQLiteException e) {
            Log.d("RAZZ_FAZZ", "something went wrong");
        }

        return null;
    }

    // updates the users statistics:
    // - all bronze, silver, gold and platinum trophies
    // - the user level progress
    private void updateUserStatistics() {
        String query = "SELECT SUM(" + DBHelper.COLUMN_PROGRESS_BRONZE +
                "), SUM(" + DBHelper.COLUMN_PROGRESS_SILVER +
                "), SUM(" + DBHelper.COLUMN_PROGRESS_GOLD +
                "), SUM(" + DBHelper.COLUMN_PROGRESS_PLAT +
                ") FROM " + DBHelper.USER_PROGRESS_TABLE;
        Cursor cursor = mDatabase.rawQuery(query, null);
        int mAmountBr = 0;
        int mAmountSv = 0;
        int mAmountGd = 0;
        int mAmountPl = 0;
        if(cursor.moveToFirst()) {
            mAmountBr = cursor.getInt(0);
            mAmountSv = cursor.getInt(1);
            mAmountGd = cursor.getInt(2);
            mAmountPl = cursor.getInt(3);
            int[] trophies = {mAmountBr, mAmountSv, mAmountGd, mAmountPl};
            calculateLevelprogress(trophies);
            progressBarUserLevel.setProgress(ExperienceLogic.getLevelProgress());
            userLevel.setText(String.valueOf(ExperienceLogic.getLevel()));
        }
        cursor.close();
        bronzeCount.setText(String.valueOf(mAmountBr));
        silverCount.setText(String.valueOf(mAmountSv));
        goldCount.setText(String.valueOf(mAmountGd));
        platCount.setText(String.valueOf(mAmountPl));
    }


    // opens the GameProgress Activity
    @Override
    public void onItemClick(int position) {
        Cursor cursor = mAdapter.getCursor();
        cursor.moveToPosition(position);
        Intent intent = new Intent(this, GameProgress.class);
        intent.putExtra("ID", String.valueOf(cursor.getString(cursor.getColumnIndex("id"))));
        startActivityForResult(intent, GAME_PROGRESS_ACTIVITY_REQUEST);
    }
}
