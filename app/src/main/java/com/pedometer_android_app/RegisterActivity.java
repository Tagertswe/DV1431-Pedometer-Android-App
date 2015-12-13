package com.pedometer_android_app;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;


/**
 * Created by Pol on 2015-12-12.
 */
public class RegisterActivity extends AppCompatActivity {
    Button registerButton;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_view);

        registerButton = (Button) findViewById(R.id.Register_button);
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(registerUser() != -1)
                {
                    // Takes user back to login page.
                    Intent intent=new Intent(v.getContext(),MainActivity.class);
                    startActivity(intent);
                }

                else
                {
                    // Display message on fail.
                    TextView regStatus=(TextView)findViewById(R.id.regStatus);
                    regStatus.setText("Try again!");
                }


            }
        });

    }


    public long registerUser()
    {
        EditText SSN = (EditText)findViewById(R.id.registerSSN);
        String ssn = SSN.getText().toString();
        EditText Name = (EditText)findViewById(R.id.registerName);
        String name = Name.getText().toString();
        EditText Surname = (EditText)findViewById(R.id.registerSurname);
        String surname = Surname.getText().toString();
        EditText Username = (EditText)findViewById(R.id.registerUsername);
        String userName = Username.getText().toString();
        EditText Password = (EditText)findViewById(R.id.registerPassword);
        String password = Password.getText().toString();

        Db db = new Db(this);
        long returnV = db.addUser(ssn,name,surname,userName,password);
        return returnV;
    }


}