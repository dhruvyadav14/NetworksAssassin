package com.example.dhruvyadav.assassin;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import java.io.*;
import java.net.*;

public class StartGameActivity extends Activity {

    // port and host IP address
    private static final int port = 8888;
    private static final String host = "34.208.54.66";

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

        System.out.println("Activity started");

        // Initialize the start button
        Button submitButton = (Button) findViewById(R.id.submit1);

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.out.println("Clicked");
                // Cache the EditText that holds the game ID
                mGameID = (EditText) findViewById(R.id.gameID1);

                // Cache the EditText that holds the player's name
                mPlayerNames = (EditText) findViewById(R.id.playerNames);

                System.out.println("Creating AsyncTask");
                // Create AsyncTask to start game
                new StartGameTask().execute();
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
            // set command string
            myCommand = "0; " + mGameID.getText() + "; " + mPlayerNames.getText();
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

                /*
                String line = "";

                while(!line.equals("test")) {
                    line = in.readLine();
                    System.out.println(line);
                }
                out.write("test");
                */

                // send the command to the server
                out.write(myCommand);
                out.flush();
                out.close();

                System.out.println("Command sent: " + myCommand);

                System.out.println("Awaiting response...");

                myResponse = "Nada";

                if(in.ready()) {
                    myResponse = in.readLine();
                }

                in.close();

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