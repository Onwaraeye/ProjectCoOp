package com.example.projectcoop;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.projectcoop.models.Partner;

public class LoginActivity extends AppCompatActivity {

    private EditText etUser;
    private EditText etPass;

    boolean isValid = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        etUser = findViewById(R.id.etUsername);
        etPass = findViewById(R.id.etPassword);
        Button btLogin = findViewById(R.id.btLogin);

        btLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String inputUser = etUser.getText().toString();
                String inputPass = etPass.getText().toString();

                if (inputUser.isEmpty() || inputPass.isEmpty()){
                    Toast.makeText(getApplicationContext(),"Enter details",Toast.LENGTH_SHORT).show();
                }else {
                    isValid = validate(inputUser,inputPass);
                    if (!isValid){
                        Toast.makeText(getApplicationContext(),"Incorrect",Toast.LENGTH_SHORT).show();
                    }else {
                        Toast.makeText(getApplicationContext(),"Login successful",Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                        startActivity(intent);
                    }
                }
            }
        });
    }
    private boolean validate(String user,String pass){
        String username = "Admin";
        String password = "123456";
        if (user.equals(username) && pass.equals(password)){
            return true;
        }
        return false;
    }
}