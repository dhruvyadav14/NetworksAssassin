package com.example.dhruvyadav.assassin;

import android.content.Intent;
import android.os.Message;
import android.os.Messenger;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import java.net.*;
import java.io.*;

public class MainActivity extends AppCompatActivity {

    /* Values that identify the requests to start or continue a game.
     */
    private static final int START_GAME_REQUEST = 1;
    private static final int CONTINUE_GAME_REQUEST = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button startButton = (Button) findViewById(R.id.startButton);

        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startGame(view);
            }
        });

        // Initialize the 'Download Thread' Button
        Button continueButton = (Button) findViewById(R.id.continueButton);

        // set OnClickListener to download image
        // from URL via a DownloadImageHandler
        continueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                continueGame(view);
            }
        });
    }

    public void startGame(View view) {
        // Create Intent to start StartGameActivity
        Intent startGameIntent = new Intent(MainActivity.this, StartGameActivity.class);

        // start DownloadActivity using downloadImageIntent
        startActivityForResult(startGameIntent, START_GAME_REQUEST);
    }

    public void continueGame(View view) {
        // Create Intent to start ContinueGameActivity
        Intent continueGameIntent = new Intent(MainActivity.this, ContinueGameActivity.class);

        // start DownloadActivity using downloadImageIntent
        startActivityForResult(continueGameIntent, CONTINUE_GAME_REQUEST);
    }

    /**
     * Hook method called back by the Android Activity framework when
     * an Activity that's been launched exits, giving the requestCode
     * it was started with, the resultCode it returned, and any
     * additional data from it.
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == START_GAME_REQUEST) {

        }

        if (requestCode == CONTINUE_GAME_REQUEST) {

        }

    }
}