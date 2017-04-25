package com.example.dhruvyadav.assassin;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 * Created by lukea on 4/25/2017.
 */

public class ContinueGameActivity extends AppCompatActivity {

    private static final int port = 8888;
    private InetAddress host = null;

    /**
     * EditText field for entering the desired URL to an image.
     */
    private EditText mGameID;
    private EditText mPlayerName;

    // instantiate a variable for this activity's request
    private int myRequest;

    private static final int TARGET_REQUEST = 1;
    private static final int DEATH_REQUEST = 2;

    // instantiate a variable for this activity's server command and response
    private String myCommand;
    private String myResponse;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.continuegame);

        // Cache the EditText that holds the game ID
        mGameID = (EditText) findViewById(R.id.gameID);

        // Cache the EditText that holds the player's name
        mPlayerName = (EditText) findViewById(R.id.playerName);

        // Initialize the target button
        Button targetButton = (Button) findViewById(R.id.targetButton);

        targetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myRequest = TARGET_REQUEST;
            }
        });

        // Initialize the death button
        Button deathButton = (Button) findViewById(R.id.deathButton);

        deathButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myRequest = DEATH_REQUEST;
            }
        });

        // Initialize the start button
        Button submitButton = (Button) findViewById(R.id.statusButton);

        // set OnClickListener to download image
        // from URL via ImageIntentService
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new ContinueGameTask();
            }
        });
    }

    /**
     * Extends AsyncTask, downloads image in the background, creates an
     * Intent that contains the path to the image file, and then sets this as
     * the result of the Activity.
     */
    private class ContinueGameTask extends AsyncTask<String, Void, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            if (myRequest == TARGET_REQUEST) {
                myCommand = "1;" + mGameID + "; " + mPlayerName;
            } else if (myRequest == DEATH_REQUEST) {
                myCommand = "2;" + mGameID + "; " + mPlayerName;
            }
        }

        @Override
        protected String doInBackground(String... input) {
            Socket s = null;
            BufferedReader in = null;
            PrintWriter out = null;

            try {
                host = InetAddress.getByName("172.31.21.180");
                s = new Socket(host, 8888);

                out = new PrintWriter(s.getOutputStream(), true);
                in = new BufferedReader(new InputStreamReader(s.getInputStream()));

                out.write(myCommand);

                while (in.readLine() == null) {
                    myResponse = in.readLine();
                }

            } catch (UnknownHostException e) {
                System.out.println("Host not found");
            } catch (IOException e) {
                System.out.println("I/O error: " + e.getMessage());
            }

            // Make sure socket is closed
            if (s != null && !s.isClosed()) {
                try {
                    s.close();
                } catch (IOException e) {
                    System.out.println("I/O error: " + e.getMessage());
                }
            }

            return myResponse;
        }

        @Override
        protected void onPostExecute(String input) {
            super.onPostExecute(input);
            // create an Intent that is the result of the DownloadActivity
            Intent result = new Intent().putExtra("response", input);

            // Set Intent to be the result
            setResult(RESULT_OK, result);

            finish();
        }
    }
}
