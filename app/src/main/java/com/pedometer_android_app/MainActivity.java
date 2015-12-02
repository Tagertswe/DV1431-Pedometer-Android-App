package com.pedometer_android_app;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

/**
 * Created by pol on 11/26/15.
 */
public class MainActivity extends AppCompatActivity {
    Button mButton;
    EditText mUsername;
    EditText mPassword;

    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        mButton = (Button) findViewById(R.id.login_button);
        mUsername = (EditText)findViewById(R.id.loginUsername);
        mPassword = (EditText)findViewById(R.id.loginPassword);

        //listener for password field, so you can press enter on the virtual
        // keyboard and act as if you were pressing the login button
        mPassword.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN)
                {
                    switch (keyCode)
                    {
                        case KeyEvent.KEYCODE_ENTER:
                            checkLogin();
                            return true;
                        default:
                            break;
                    }
                }
                return false;
            }
        });
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkLogin();

            }
        });

    }


    public void checkLogin(){
        if( mUsername.getText().toString().contentEquals("pol") && mPassword.getText().toString().contentEquals("pol") ){
            //Intent mainView = new Intent(MainActivity.this, MainView.class );
            MainActivity.this.startActivity(new Intent(MainActivity.this, MainView.class));
        }
    }
}
