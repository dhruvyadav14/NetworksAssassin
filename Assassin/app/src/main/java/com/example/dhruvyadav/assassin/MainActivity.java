package com.example.dhruvyadav.assassin;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    // Values that identify the requests to start or continue a game
    private static final int START_REQUEST = 1;
    private static final int CONTINUE_REQUEST = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        // initialize the start button
        Button startButton = (Button) findViewById(R.id.startButton);

        // set OnClickListener to start game if clicked
        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Create Intent to start StartGameActivity
                Intent startGameIntent = new Intent(MainActivity.this, StartGameActivity.class);

                // start startGameActivity using startGameIntent
                startActivityForResult(startGameIntent, START_REQUEST);
            }
        });

        // Initialize the continue button
        Button continueButton = (Button) findViewById(R.id.continueButton);

        // set OnClickListener to continue game if clicked
        continueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Create Intent to start ContinueGameActivity
                Intent continueGameIntent = new Intent(MainActivity.this, ContinueGameActivity.class);

                // start continueGameActivity using continueGameIntent
                startActivityForResult(continueGameIntent, CONTINUE_REQUEST);
            }
        });
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == START_REQUEST || requestCode == CONTINUE_REQUEST) {
            if (resultCode == RESULT_OK) {
                setContentView(R.layout.activity_main);

                TextView response = (TextView) findViewById(R.id.response);

                // set status message to the server's response string
                response.setText(data.getStringExtra("response"));
            }
        }
    }
}