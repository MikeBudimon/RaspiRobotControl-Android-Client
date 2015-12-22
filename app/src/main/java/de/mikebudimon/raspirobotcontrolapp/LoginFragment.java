package de.mikebudimon.raspirobotcontrolapp;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

/**
 * Pass data(IP and Port) for Control-Fragment
 */
public class LoginFragment extends Fragment {

    // needs to be declared global, else it has to be final
    private EditText editTextIP;
    private EditText editTextPort;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.login_fragment, container, false);

        editTextIP = (EditText) view.findViewById(R.id.editTextIP);
        editTextPort = (EditText) view.findViewById(R.id.editTextPort);
        Button buttonLogin = (Button) view.findViewById(R.id.buttonLogin);

        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), Control.class);
                intent.putExtra("IP", editTextIP.getText().toString());
                intent.putExtra("Port", Integer.valueOf(editTextPort.getText().toString()));
                startActivity(intent);
            }
        });


        return view;
    }


}
