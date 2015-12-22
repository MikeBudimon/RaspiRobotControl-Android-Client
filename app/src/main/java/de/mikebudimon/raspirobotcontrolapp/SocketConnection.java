package de.mikebudimon.raspirobotcontrolapp;

import android.app.Activity;
import android.os.AsyncTask;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

/**
 * Connects to a server and sends commands.
 */
public class SocketConnection extends AsyncTask<String, Void, String> {

    private String mServerName;
    private int mPort;
    private Socket mClient;
    private String mCommand = "5";
    private DataInputStream mInputStream;
    private DataOutputStream mOutputStream;
    private boolean mFlag = false;
    private Activity mActivity;
    private TextView mTextDistance;
    private ImageView mDistanceWarning;
    private String mDistance = "0";
    private boolean mWarningState = false;

    public SocketConnection(String ServerName, int port, Activity activity) {
        this.mServerName = ServerName;
        this.mPort = port;
        this.mActivity = activity;
    }

    public void setCommand(String command) {
        this.mCommand = command;
    }

    public void stop() throws IOException {
        mOutputStream.writeUTF("stop");
        mOutputStream.flush();
        mFlag = false;
        mInputStream.close();
        mOutputStream.close();
        mClient.close();
    }

    public boolean getFlag() {
        return mFlag;
    }

    @Override
    protected String doInBackground(String... params) {
        try {
            mClient = new Socket(mServerName, mPort);
            mInputStream = new DataInputStream(mClient.getInputStream());
            mOutputStream = new DataOutputStream(mClient.getOutputStream());
            mFlag = true;

            mActivity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(mActivity, "Connected to " + mClient.getRemoteSocketAddress(), Toast.LENGTH_SHORT).show();
                }
            });

            mTextDistance = (TextView) mActivity.findViewById(R.id.textViewDistance);
            mDistanceWarning = (ImageView) mActivity.findViewById(R.id.distanceWarning);

            String oldCommand = "5";

            while (mFlag) {

                if (mInputStream.available() > 0) {
                    mDistance = mInputStream.readUTF();

                    // displays current distance in cm from the ultrasonicsensor
                    mActivity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mTextDistance.setText(mDistance + "cm");
                        }

                    });

                    //Log.d("TEST", Double.valueOf(mDistance) + "");
                }


                // activates the warning image
                if (Double.valueOf(mDistance) <= 10.0 && !mWarningState) {
                    mActivity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mDistanceWarning.setVisibility(View.VISIBLE);
                            mWarningState = true;
                        }
                    });
                }

                // deactivates the warning image
                if (Double.valueOf(mDistance) > 10.0 && mWarningState) {
                    mActivity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mDistanceWarning.setVisibility(View.INVISIBLE);
                            mWarningState = false;
                        }
                    });
                }


                // only sends command to server when it's different than last time
                if (!oldCommand.equals(mCommand)) {
                    mOutputStream.flush();
                    mOutputStream.writeUTF(mCommand);
                    oldCommand = mCommand;
                }

                Thread.sleep(150);
            }

        } catch (Exception e) {
            e.printStackTrace();
            mActivity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(mActivity, "Connection error.", Toast.LENGTH_LONG).show();
                }
            });

        }

        return null;
    }

}
