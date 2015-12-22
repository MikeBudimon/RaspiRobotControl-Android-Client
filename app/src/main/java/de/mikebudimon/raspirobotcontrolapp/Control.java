package de.mikebudimon.raspirobotcontrolapp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.webkit.WebView;
import android.widget.ImageButton;

import java.io.IOException;

/**
 * Sends input through a socket to the server(Rpi) and shows the CameraOutput
 */
public class Control extends AppCompatActivity {

    SocketConnection socketConnection;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.control);

        ImageButton buttonLeft = (ImageButton) findViewById(R.id.buttonLeft);
        ImageButton buttonRight = (ImageButton) findViewById(R.id.buttonRight);
        ImageButton buttonUp = (ImageButton) findViewById(R.id.buttonUp);
        ImageButton buttonDown = (ImageButton) findViewById(R.id.buttonDown);
        ImageButton buttonStop = (ImageButton) findViewById(R.id.buttonStop);
        ImageButton buttonBuzzer = (ImageButton) findViewById(R.id.buttonBuzzer);

        // connect to server
        socketConnection = new SocketConnection(getIntent().getStringExtra("IP"), getIntent().getIntExtra("Port", 9000), this);
        socketConnection.execute();

        // show webcam stream
        WebView camStream = (WebView) findViewById(R.id.myStream);
        camStream.getSettings().setLoadWithOverviewMode(true);
        camStream.getSettings().setUseWideViewPort(true);
        camStream.loadUrl("http://" + getIntent().getStringExtra("IP") + ":8080/stream_simple.html");

        buttonLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                socketConnection.setCommand("4");
                //Log.d("COMMAND", "4");
            }
        });

        buttonRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                socketConnection.setCommand("6");
                //Log.d("COMMAND", "6");
            }
        });

        buttonUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                socketConnection.setCommand("8");
                //Log.d("COMMAND", "8");
            }
        });

        buttonDown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                socketConnection.setCommand("2");
                //Log.d("COMMAND", "2");
            }
        });

        buttonStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                socketConnection.setCommand("5");
                //Log.d("COMMAND", "5");
            }
        });

        buttonBuzzer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                socketConnection.setCommand("0");
                //Log.d("COMMAND", "0");
            }
        });

    }

    /**
     * Called just before the activity is destroyed.
     */
    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d("DEBUG: ", "The onDestroy() event");
        try {
            if (socketConnection.getFlag()) socketConnection.stop();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**
     * Called when another activity is taking focus.
     */
    @Override
    protected void onPause() {
        super.onPause();
        Log.d("DEBUG: ", "The onPause() event");
        try {
            if (socketConnection.getFlag()) socketConnection.stop();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**
     * Called when the activity is no longer visible.
     */
    @Override
    protected void onStop() {
        super.onStop();
        Log.d("DEBUG: ", "The onStop() event");
        try {
            if (socketConnection.getFlag()) socketConnection.stop();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
