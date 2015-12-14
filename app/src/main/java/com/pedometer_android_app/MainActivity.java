package com.pedometer_android_app;


import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

/**
 * Created by pol on 11/26/15.
 */

public class MainActivity extends AppCompatActivity {

    Button mButton;
    EditText mUsername;
    EditText mPassword;
    Db db = new Db(this);
    Button dbTestButton;
    TextView notRegged;
    User currentUser;

    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mButton = (Button) findViewById(R.id.login_button);
        mUsername = (EditText)findViewById(R.id.loginUsername);
        mPassword = (EditText)findViewById(R.id.loginPassword);
        notRegged = (TextView)findViewById(R.id.registerLink);

        /// temp
         //dbTestButton = (Button) findViewById(R.id.DB_button);

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

			// Not registered text "button"
			notRegged.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View r) {

                Intent intent=new Intent(r.getContext(),RegisterActivity.class);
                startActivity(intent);
            }
        });


            mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(checkLogin() == 0)
                {
                    TextView logInStatus=(TextView)findViewById(R.id.logInStatus);
                    logInStatus.setText("Wrong username or password,\ntry again!");
                }

            }
        });

		/*
        dbTestButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View l) {
                //db.getWritableDatabase();
                //db.addUser("19880315","POL","Hagge","RICK","hmmm");
                //db.addWalk("555","2015-04-30","19870315");
                //db.updateWalk("19880315","2015-04-30","9999");
                //db.deleteUser("19880315");
                //db.getUser("19880315");
                //db.getWalkData("19880315");
            }
        });
		*/
    }

        public int checkLogin(){

        String username = mUsername.getText().toString();
        String password = mPassword.getText().toString();
        currentUser = db.loginCheck(username,password);
        if(currentUser != null)
        {
            MainActivity.this.startActivity(new Intent(MainActivity.this, MainView.class));
            return 1;
        }

        return 0;

    }

}
