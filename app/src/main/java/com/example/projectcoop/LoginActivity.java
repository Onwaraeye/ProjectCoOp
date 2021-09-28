package com.example.projectcoop;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;

import com.example.projectcoop.models.Partner;

public class LoginActivity extends AppCompatActivity {

    private EditText etUser;
    private EditText etPass;
    private CheckBox remember;
    boolean isValid = false;

    String inputUser;
    String inputPass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        etUser = findViewById(R.id.etUsername);
        etPass = findViewById(R.id.etPassword);
        remember = findViewById(R.id.remember);
        Button btLogin = findViewById(R.id.btLogin);

        SharedPreferences keep = PreferenceManager.getDefaultSharedPreferences(this);
        String user = keep.getString("user",inputUser);
        etUser.setText(user);
        String pass = keep.getString("pass",inputPass);
        etPass.setText(pass);

        SharedPreferences preferences = getSharedPreferences("checkbox",MODE_PRIVATE);
        String checkbox = preferences.getString("remember","");
        if (checkbox.equals("true")){
            Intent intent = new Intent(getApplicationContext(),MainActivity.class);
            startActivity(intent);
            finish();
        }else if (checkbox.equals("false")){
            etPass.setText("");
        }

        btLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                inputUser = etUser.getText().toString();
                inputPass = etPass.getText().toString();
                SharedPreferences keep = PreferenceManager.getDefaultSharedPreferences(getApplication());
                SharedPreferences.Editor editorkp = keep.edit();
                editorkp.putString("user",inputUser);
                editorkp.putString("pass",inputPass);
                editorkp.apply();
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
                        finish();
                    }
                }
            }
        });

        remember.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (compoundButton.isChecked()){
                    SharedPreferences preferences = getSharedPreferences("checkbox",MODE_PRIVATE);
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putString("remember","true");
                    editor.apply();
                }else if (!compoundButton.isChecked()){
                    SharedPreferences preferences = getSharedPreferences("checkbox",MODE_PRIVATE);
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putString("remember","false");
                    editor.apply();
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