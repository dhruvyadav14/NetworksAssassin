package com.example.dhruvyadav.assassin;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import java.io.*;
import java.net.*;

/**
 * Created by lukea on 4/25/2017.
 */

public class StartGameActivity extends AppCompatActivity {

    // port and host IP address
    private static final int port = 8888;
    private InetAddress host = null;

    // private data fields
    private EditText mGameID;
    private EditText mPlayerNames;

    // instantiate variables for this activity's server command and response
    private String myCommand;
    private String myResponse;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.startgame);

        // Cache the EditText that holds the game ID
        mGameID = (EditText) findViewById(R.id.gameID);

        // Cache the EditText that holds the player's name
        mPlayerNames = (EditText) findViewById(R.id.playerNames);

        // Initialize the start button
        Button submitButton = (Button) findViewById(R.id.statusButton);

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new StartGameTask();
            }
        });
    }

    /**
     * Extends AsyncTask, downloads image in the background, creates an
     * Intent that contains the path to the image file, and then sets this as
     * the result of the Activity.
     */
    private class StartGameTask extends AsyncTask<Void, Void, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            myCommand = "0; " + mGameID + "; " + mPlayerNames;
        }

        @Override
        protected String doInBackground(Void... input) {
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
        protected void onPostExecute(String input)
        {
            // create an Intent that is the result of the DownloadActivity
            Intent result = new Intent().putExtra("response", input);

            // Set Intent to be the result
            setResult(RESULT_OK, result);

            finish();
        }
    }
}
