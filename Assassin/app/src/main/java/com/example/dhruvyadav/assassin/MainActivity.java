package com.example.dhruvyadav.assassin;


import android.content.Intent;
import android.os.AsyncTask;
import android.os.Message;
import android.os.Messenger;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import java.net.*;
import java.io.*;
import java.util.*;
import android.app.ProgressDialog;


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


    public class SendMessagesTask extends AsyncTask<URL, Integer, Long> {
        private ProgressDialog progressDialog;

        @Override

        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(MainActivity.this);
            progressDialog.setCancelable(true);
            progressDialog.setMessage("Loading...");
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressDialog.setProgress(0);
            progressDialog.show();
        }

        protected Long doInBackground(URL... params) {
            int command = 0;
            String gameID = null;
            String name = null;
            Socket s = null;

            if(args.length!=2) {
                System.out.println("Please specify the hostname and the port number as command line args.");
                System.exit(1);
            } else {
                command = args[0];
                try {
                    gameID = Integer.parseInt(args[1]);
                } catch(NumberFormatException e) {
                    System.out.println("Invalid port number: "+args[1]);
                    System.exit(1);
                }
            }

            try {
                s = new Socket(host, port);
            } catch (UnknownHostException e) {
                System.out.println("Host not found");
                System.exit(-1);
            } catch (IOException e) {
                System.out.println("Cannot connect to server");
                System.exit(-1);
            }

            InputStream is = null;
            OutputStream os = null;
            try {
                is = s.getInputStream();
                os = s.getOutputStream();
            } catch (IOException e1) {
                System.out.println("Error with socket. Server not found");
            }
            PrintWriter out = new PrintWriter(os, true);
            InputStreamReader isr = new InputStreamReader(is);
            BufferedReader in =  new BufferedReader(isr);

            //ENTER A COMMAND, 1 IF YOU WANT TO START, 2 IF YOU KILLED, 3 IF YOU GOT KILLED
            //IF 1, GIVE A GAMEID YOU WANT TO JOIN AND GIVE YOUR NAME
            //IF 2 DO THE SAME THING
            //IF 3 DO THE SAME THING

            
        }



        protected void onProgressUpdate(Integer... progress) {
            setProgressPercent(progress[0]);
        }

        protected void onPostExecute(Long result) {
            showDialog("Downloaded " + result + " bytes")
        }
    }
}
