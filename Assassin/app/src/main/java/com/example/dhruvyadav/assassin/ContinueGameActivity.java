package com.example.dhruvyadav.assassin;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

public class ContinueGameActivity extends Activity {

    // port and host IP address
    private static final int port = 8888;
    private static final String host = "34.208.54.66";

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

        continueButtons = (RadioGroup) findViewById(R.id.continueButtons);

        // Initialize the start button
        Button submitButton = (Button) findViewById(R.id.submit2);

        // set OnClickListener to update request code before sending command
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int selectedId = continueButtons.getCheckedRadioButtonId();

                // set request code
                if (selectedId == R.id.targetButton)
                    mRequestCode = TARGET_REQUEST;
                else if (selectedId == R.id.deathButton)
                    mRequestCode = DEATH_REQUEST;

                // Cache the EditText that holds the game ID
                mGameID = (EditText) findViewById(R.id.gameID2);

                // Cache the EditText that holds the player's name
                mPlayerName = (EditText) findViewById(R.id.playerName);

                System.out.println("Creating AsyncTask");
                // Create AsyncTask to continue game
                new ContinueGameTask().execute();
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
            myCommand = mRequestCode + "; " + mGameID.getText() + "; " + mPlayerName.getText() + "\n";
        }

        @Override
        protected String doInBackground(Void... input) {
            // instantiate socket, reader, and writer
            Socket s = null;

            try {
                // connect to server with host name and IP
                s = new Socket(host, port);

                System.out.println("Socket created");

                PrintWriter out = new PrintWriter(s.getOutputStream(), true);
                BufferedReader in = new BufferedReader(new InputStreamReader(s.getInputStream()));

                // send the command to the server
                out.write(myCommand);
                out.flush();

                System.out.println("Command sent: " + myCommand);

                System.out.println("Awaiting response...");

                myResponse = in.readLine();

                System.out.println("Response received: " + myResponse);

            } catch (UnknownHostException e) {
                myResponse = "Host not found";
            } catch (IOException e) {
                myResponse = "I/O error";
            }

            // Make sure socket is closed
            if (s != null && !s.isClosed()) {
                try {
                    s.close();
                    System.out.println("Socket closed");
                } catch (IOException e) {
                    System.out.println("I/O error: " + e.getMessage());
                    myResponse = "I/O error";
                }
            }

            // return server's response as result
            return myResponse;
        }

        @Override
        protected void onPostExecute(String input) {
            // create an Intent that is the result of the DownloadActivity
            Intent result = new Intent().putExtra("response", myResponse);

            // Set Intent to be the result
            setResult(RESULT_OK, result);

            finish();
        }
    }
}