package com.example.dhruvyadav.assassin;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;

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

    // port and host IP address
    private static final int port = 8888;
    private InetAddress host = null;

    // Private data fields
    private EditText mGameID;
    private EditText mPlayerName;
    private int mRequestCode;

    // RadioGroup for the two continue options
    private RadioGroup continueButtons;

    // Request types
    private static final int TARGET_REQUEST = 1;
    private static final int DEATH_REQUEST = 2;

    // instantiate variables for this activity's server command and response
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

        // Cache the RadioGroup that holds the player's mission
        continueButtons = (RadioGroup) findViewById(R.id.continueButtons);

        // Initialize the start button
        Button submitButton = (Button) findViewById(R.id.statusButton);

        // set OnClickListener to update request code before sending command
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int selectedId = continueButtons.getCheckedRadioButtonId();

                // set request code
                if(selectedId == R.id.targetButton)
                    mRequestCode = TARGET_REQUEST;
                else if(selectedId == R.id.deathButton)
                    mRequestCode = DEATH_REQUEST;

                new ContinueGameTask();
            }
        });
    }

    /**
     * Extends AsyncTask, sends command to server, awaits and receives a
     * response, and then sets this as the result of the Activity.
     */
    private class ContinueGameTask extends AsyncTask<Void, Void, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // set command string
            myCommand = mRequestCode + "; " + mGameID + "; " + mPlayerName;
        }

        @Override
        protected String doInBackground(Void... input) {
            // instantiate socket, reader, and writer
            Socket s = null;
            BufferedReader in = null;
            PrintWriter out = null;

            try {
                // connect to server with host name and IP
                host = InetAddress.getByName("172.31.21.180");
                s = new Socket(host, 8888);

                out = new PrintWriter(s.getOutputStream(), true);
                in = new BufferedReader(new InputStreamReader(s.getInputStream()));

                // send the command to the server
                out.write(myCommand);

                // wait until the server sends a response
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

            // return server's response as result
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