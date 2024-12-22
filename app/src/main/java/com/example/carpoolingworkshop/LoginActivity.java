package com.example.carpoolingworkshop;

import static android.preference.PreferenceManager.getDefaultSharedPreferences;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.lang.reflect.Array;

public class LoginActivity extends AppCompatActivity {
    private EditText et_email;
    private EditText et_password;
    private Button login_btn;
    private Button create_account_btn;
    private DBHelper dbHelper;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);

        Toolbar appbar = findViewById(R.id.appbar);
        appbar.setTitle(getResources().getString(R.string.app_name) + "  |  " + "Login");
        setSupportActionBar(appbar);
        // Show Back Button
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // setup properties and event listeners
        initProperties();
        setupEventListeners();

        if(sharedPreferences.getInt("LoggedInUserId", -1) >= 0){
            RedirectToActivity(HomeActivity.class);
        }
    }

    public void initProperties(){
        et_email = findViewById(R.id.et_email);
        et_password = findViewById(R.id.et_password);
        login_btn = findViewById(R.id.login_btn);
        create_account_btn = findViewById(R.id.create_account_btn);
        dbHelper = new DBHelper(this);
        sharedPreferences = getDefaultSharedPreferences(this);
    }

    public void setupEventListeners(){
        login_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();
            }
        });

        create_account_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RedirectToActivity(RegisterActivity.class);
            }
        });
    }

    public void login(){
        String email = et_email.getText().toString();
        String password = et_password.getText().toString();

        int userId = dbHelper.checkCredentials(email, password);
        if(userId >= 0){
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putInt("LoggedInUserId", userId);
            editor.apply();
            RedirectToActivity(HomeActivity.class);
        }
    }

    public void RedirectToActivity(Class activityClass){
        Intent intent = new Intent(this, activityClass);
        startActivity(intent);
    }
}