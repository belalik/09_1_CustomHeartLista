package com.example.a09_1_customheartlista;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;

public class DetailActivity extends AppCompatActivity {

    TextView dtlTitle;
    TextView dtlSubtitle;
    ImageView dtlImgIcon;

    ImageView dtlImgHeart;

    private static final String TAG = "DetailActivity - thomas";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        initializeViews();

        Intent i = getIntent();
        Gson gson = new Gson();
        String animalFromIntent = i.getStringExtra(MainActivity.ANIMAL_KEY);

        Log.i(TAG, "onCreate: animal in string format: "+animalFromIntent);
        Animal animal = gson.fromJson(animalFromIntent, Animal.class);

        dtlTitle.setText(animal.getTitle());
        dtlSubtitle.setText(animal.getSubtitle());
        dtlImgIcon.setImageResource(animal.getImage());

        if (animal.isShortlisted()) {
            dtlImgHeart.setImageResource(R.drawable.ic_red_heart);
        }
        else {
            dtlImgHeart.setImageResource(R.drawable.ic_blank_heart);
        }
    }

    private void initializeViews() {
        dtlTitle = findViewById(R.id.dtl_txt_title);
        dtlSubtitle = findViewById(R.id.dtl_txt_subtitle);
        dtlImgIcon = findViewById(R.id.dtl_img_icon);
        dtlImgHeart = findViewById(R.id.dtl_img_heart);
    }
}