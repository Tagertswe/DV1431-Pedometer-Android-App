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
    Db db = new Db(this);
    Button dbTestButton;

    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //setContentView(R.layout.register_view);
        db.getWritableDatabase();

        mButton = (Button) findViewById(R.id.login_button);
       mUsername = (EditText)findViewById(R.id.loginUsername);
        mPassword = (EditText)findViewById(R.id.loginPassword);


         dbTestButton = (Button) findViewById(R.id.DB_button);

        //listener for password field, so you can press enter on the virtual
        // keyboard and act as if you were pressing the login button
        mPassword.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN) {
                    switch (keyCode) {
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

        dbTestButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View l) {
                //db.addUser("19880315","POL","Hagge","RICK","hmmm");
                //db.addWalk("555","2015-04-30","19870315");
                //db.updateWalk("19880315","2015-04-30","9999");
                //db.deleteUser("19880315");
                //db.getUser("19880315");
                //db.getWalkData("19880315");
            }
        });

    }

    public void checkLogin(){
        if( mUsername.getText().toString().contentEquals("pol") && mPassword.getText().toString().contentEquals("pol") )
        {
            // inside here check in database
            //Intent mainView = new Intent(MainActivity.this, MainView.class );
            MainActivity.this.startActivity(new Intent(MainActivity.this, MainView.class));
        }

    }

}
