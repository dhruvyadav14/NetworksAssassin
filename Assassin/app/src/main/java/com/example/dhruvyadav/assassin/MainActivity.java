package com.example.dhruvyadav.assassin;

import android.content.Intent;
import android.os.Message;
import android.os.Messenger;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        // Initialize the start button
        Button startButton = (Button) findViewById(R.id.startButton);

        // set OnClickListener to download image
        // from URL via ImageIntentService
        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setContentView(R.layout.startgame);
            }
        });

        // Initialize the 'Download Thread' Button
        Button continueButton = (Button) findViewById(R.id.continueButton);

        // set OnClickListener to download image
        // from URL via a DownloadImageHandler
        continueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setContentView(R.layout.continuegame);
            }
        });
    }
}
