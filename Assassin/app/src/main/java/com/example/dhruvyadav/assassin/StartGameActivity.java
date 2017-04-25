package com.example.dhruvyadav.assassin;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

/**
 * Created by lukea on 4/25/2017.
 */

public class StartGameActivity extends AppCompatActivity {

    /**
     * EditText field for entering the desired URL to an image.
     */
    private EditText mGameID;
    private EditText mPlayerNames;

    // instantiate a variable for this activity's request
    private int myRequest;

    private static final int TARGET_REQUEST = 1;
    private static final int DEATH_REQUEST = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.continuegame);

        // Cache the EditText that holds the game ID
        mGameID = (EditText) findViewById(R.id.gameID);

        // Cache the EditText that holds the player's name
        mPlayerNames = (EditText) findViewById(R.id.playerNames);

        // Initialize the start button
        Button submitButton = (Button) findViewById(R.id.statusButton);

        // set OnClickListener to download image
        // from URL via ImageIntentService
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }
}
