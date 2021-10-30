package com.example.sisyphus;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class Register extends AppCompatActivity {

    EditText registerEmail;
    EditText registerPassword;
    EditText registerPasswordConfirm;
    Button registerConfirm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register);

        EditText registerEmail = findViewById(R.id.registerEmail);
        EditText registerPassword = findViewById(R.id.registerPassword);
        EditText registerPasswordConfirm = findViewById(R.id.registerPasswordConfirm);
        Button registerConfirm = findViewById(R.id.registerConfirm);

        registerConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (registerPassword == registerPasswordConfirm) {
                    // Here is where you can tie the registering new user
                    // Data threads: registerEmail as new users email
                    // registerPassword as new users password
                    // registerPasswordConfirm is to strictly reinforce users password choice
                }
            }
        });
    }
}