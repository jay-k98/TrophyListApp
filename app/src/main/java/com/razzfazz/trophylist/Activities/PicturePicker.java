package com.razzfazz.trophylist.Activities;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.razzfazz.trophylist.R;

public class PicturePicker extends AppCompatActivity {

    private ImageView picture0, picture1, picture2,
            picture3, picture4, picture5,
            picture6, picture7, picture8,
            picture9, picture10, picture11;

    // array of all profile picture resource ids
    private static final int[] res_ids = {R.drawable.picture0,
            R.drawable.picture1,
            R.drawable.picture2,
            R.drawable.picture3,
            R.drawable.picture4,
            R.drawable.picture5,
            R.drawable.picture6,
            R.drawable.picture7,
            R.drawable.picture8,
            R.drawable.picture9,
            R.drawable.picture10,
            R.drawable.picture11};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_picture_picker);

        picture0 = findViewById(R.id.pic0);
        picture1 = findViewById(R.id.pic1);
        picture2 = findViewById(R.id.pic2);
        picture3 = findViewById(R.id.pic3);
        picture4 = findViewById(R.id.pic4);
        picture5 = findViewById(R.id.pic5);
        picture6 = findViewById(R.id.pic6);
        picture7 = findViewById(R.id.pic7);
        picture8 = findViewById(R.id.pic8);
        picture9 = findViewById(R.id.pic9);
        picture10 = findViewById(R.id.pic10);
        picture11 = findViewById(R.id.pic11);

        picture0.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectPicture(res_ids[0]);
            }
        });
        picture1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectPicture(res_ids[1]);
            }
        });
        picture2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectPicture(res_ids[2]);
            }
        });
        picture3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectPicture(res_ids[3]);
            }
        });
        picture4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectPicture(res_ids[4]);
            }
        });
        picture5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectPicture(res_ids[5]);
            }
        });
        picture6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectPicture(res_ids[6]);
            }
        });
        picture7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectPicture(res_ids[7]);
            }
        });
        picture8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectPicture(res_ids[8]);
            }
        });
        picture9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectPicture(res_ids[9]);
            }
        });
        picture10.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectPicture(res_ids[10]);
            }
        });
        picture11.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectPicture(res_ids[11]);
            }
        });




    }

    // sets the users profile picture according to the selection
    private void selectPicture(int id) {
        Intent i = new Intent();
        i.putExtra("SELECTED_PIC", id);
        setResult(Activity.RESULT_OK, i);
        finish();
    }
}
