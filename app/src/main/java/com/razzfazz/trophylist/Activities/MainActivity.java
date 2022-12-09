package com.razzfazz.trophylist.Activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.razzfazz.trophylist.Activities.GameList;
import com.razzfazz.trophylist.R;

import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private ImageView mBackground;
    private EditText mEditTextEnterUser;
    private Button mButtonLogin;
    private CheckBox mCheckBoxRemember;
    private String mUsername;
    private SharedPreferences loginPreferences;

    private static final int[] BACKGROUNDS = {R.drawable.background0, R.drawable.background1, R.drawable.background2, R.drawable.background3};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // connecting views
        mBackground = findViewById(R.id.background);
        mButtonLogin = findViewById(R.id.buttonLogin);
        mEditTextEnterUser = findViewById(R.id.editTextUsername);
        mCheckBoxRemember = findViewById(R.id.checkBoxRemember);

        // setting a random background
        mBackground.setImageResource(BACKGROUNDS[(int)(Math.random()*BACKGROUNDS.length)]);

        loginPreferences = getSharedPreferences("loginPrefs", MODE_PRIVATE);
        boolean rememberMe = loginPreferences.getBoolean("rememberMe", false);
        // saving username if user wants to
        if (rememberMe) {
            mEditTextEnterUser.setText(loginPreferences.getString("username", ""));
            mCheckBoxRemember.setChecked(true);
        }

        // setting OnClickListeners
        mButtonLogin.setOnClickListener(this);
    }


    // opens the GameList Activity if the user enters a username in correct format
    // the correct format: minimum 3 letters, maximum 16 and only - _ as special characters  allowed
    @Override
    public void onClick(View v) {
        if(v == mButtonLogin) {
            String un = mEditTextEnterUser.getText().toString();
            if(checkUsername(un) && regexUsername(un)) {
                mUsername = un;
                SharedPreferences.Editor loginPrefsEditor = loginPreferences.edit();
                if (mCheckBoxRemember.isChecked()) {
                    // saving the username in SharedPreferences
                    loginPrefsEditor.putBoolean("rememberMe", true);
                    loginPrefsEditor.putString("username", mUsername);
                    loginPrefsEditor.apply();
                } else {
                    loginPrefsEditor.clear();
                    loginPrefsEditor.commit();
                }
                login();
            } else {
                Toast.makeText(getApplicationContext(), getString(R.string.invalidUsername), Toast.LENGTH_SHORT).show();
            }
        }
    }

    // checks the username for length or empty spaces
    public boolean checkUsername(String username) {
        return !(username.trim().length() == 0 || username.length() > 16 || username.length() < 3);
    }

    // checks for legal characters
    public boolean regexUsername(String username) {
        return Pattern.matches("^[a-zA-Z0-9_-]+$", username);
    }

    // opens GameList Activity
    private void login() {
        Intent userLoginIntent = new Intent(this, GameList.class);
        startActivity(userLoginIntent);
    }



}
