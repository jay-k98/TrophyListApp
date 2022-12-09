package com.razzfazz.trophylist.Activities;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.razzfazz.trophylist.R;

public class GameEditor extends AppCompatActivity {

    private EditText mEditTextBronzeCount;
    private EditText mEditTextSilverCount;
    private EditText mEditTextGoldCount;
    private EditText mEditTextTitle;
    private Spinner mSpinnerGenre;
    private CheckBox mCheckBoxPlat;
    private Button mButtonSave;
    private Button mButtonCancel;
    private Button mButtonReset;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_editor);

        // connect views
        mEditTextBronzeCount = findViewById(R.id.editTextBronzeCount);
        mEditTextSilverCount = findViewById(R.id.editTextSilverCount);
        mEditTextGoldCount = findViewById(R.id.editTextGoldCount);
        mCheckBoxPlat = findViewById(R.id.checkBoxPlat);
        mButtonSave = findViewById(R.id.buttonSaveProgress);
        mButtonCancel = findViewById(R.id.buttonCancelProgress);
        mButtonReset = findViewById(R.id.buttonReset);
        mEditTextTitle = findViewById(R.id.editTextEnterTitle);
        mSpinnerGenre = findViewById(R.id.spinnerGenre);

        // genre-spinner gets filled with the preset values from string-array "genre_array"
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.genre_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSpinnerGenre.setAdapter(adapter);

        // setting OnClickListeners
        mButtonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cancel();
            }
        });
        mButtonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveEntry();
            }
        });
        mButtonReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetAllFields();
            }
        });
    }

    // A user can create a game with or without a platinum
    // he can set his Selection with a CheckBox, since a
    // Playstation game can have max. one platinum trophy
    private void saveEntry() {
        if(checkAllEntries()) {
            String platChecked;
            if (mCheckBoxPlat.isChecked()) {
                platChecked = "1";
            } else {
                platChecked = "0";
            }
            Intent i = new Intent();
            // fill the Intent with the users data
            i.putExtra("TITLE", mEditTextTitle.getText().toString());
            i.putExtra("GENRE", mSpinnerGenre.getSelectedItem().toString());
            i.putExtra("MAX_BRONZE", mEditTextBronzeCount.getText().toString());
            i.putExtra("MAX_SILVER", mEditTextSilverCount.getText().toString());
            i.putExtra("MAX_GOLD", mEditTextGoldCount.getText().toString());
            i.putExtra("HAS_PLAT", platChecked);
            i.putExtra("PICTURE", String.valueOf(mSpinnerGenre.getSelectedItemPosition()));

            // setting the result for Activity GameList
            setResult(Activity.RESULT_OK, i);
            finish();
        }
    }

    // setting RESULT_CANCELED for Activity GameList
    private void cancel() {
        setResult(Activity.RESULT_CANCELED);
        finish();
    }

    // resets all fields with empty spaces
    protected void resetAllFields() {
        mEditTextTitle.setText("");
        mEditTextBronzeCount.setText("");
        mEditTextSilverCount.setText("");
        mEditTextGoldCount.setText("");
        mCheckBoxPlat.setChecked(true);
    }

    // checks all entries if they are empty
    // checks if the title is too long (max. 26 characters)
    private boolean checkAllEntries() {
        final String EMPTY = "";
        String title = mEditTextTitle.getText().toString();
        if(title.equals(EMPTY)) {
            Toast.makeText(this, "Title is empty", Toast.LENGTH_SHORT).show();
            return false;
        }
        if(title.length() > 26) {
            Toast.makeText(this, "Title is too long", Toast.LENGTH_SHORT).show();
            return false;
        }
        if(mEditTextBronzeCount.getText().toString().equals(EMPTY)) {
            Toast.makeText(this, "Quantity of bronze trophies is empty", Toast.LENGTH_SHORT).show();
            return false;
        }
        if(mEditTextSilverCount.getText().toString().equals(EMPTY)) {
            Toast.makeText(this, "Quantity of silver trophies is empty", Toast.LENGTH_SHORT).show();
            return false;
        }
        if(mEditTextGoldCount.getText().toString().equals(EMPTY)) {
            Toast.makeText(this, "Quantity of gold trophies is empty", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }
}
