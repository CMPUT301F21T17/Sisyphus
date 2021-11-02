package com.example.sisyphus.View;


import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;


import com.example.sisyphus.R;


public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent intent = new Intent(MainActivity.this, Entry.class);
        startActivity(intent);
    }


    //Leah's
    //        Intent intent = new Intent(getApplicationContext(), Entry.class);
    //        startActivity(intent);

    //New thing that I added for testing push now!

}