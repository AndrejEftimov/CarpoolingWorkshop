package com.example.carpoolingworkshop;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.List;

public class RegisterActivity extends AppCompatActivity {
    private TextView tv_errors;
    private EditText et_firstname;
    private EditText et_lastname;
    private EditText et_email;
    private EditText et_password;
    private EditText et_phone_number;
    private Button register_btn;
    private DBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        //EdgeToEdge.enable(this);

        Toolbar appbar = findViewById(R.id.appbar);
        appbar.setTitle(getResources().getString(R.string.app_name) + "  |  " + "Register");
        setSupportActionBar(appbar);
        // Show Back Button
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        initProperties();
        setupEventListeners();
    }

    public void initProperties(){
        register_btn = findViewById(R.id.register_btn);
        tv_errors = findViewById(R.id.tv_errors);
        et_firstname = findViewById(R.id.et_firstname);
        et_lastname = findViewById(R.id.et_lastname);
        et_email = findViewById(R.id.et_email);
        et_password = findViewById(R.id.et_password);
        et_phone_number = findViewById(R.id.et_phone_number);
        dbHelper = new DBHelper(this);
    }

    public void setupEventListeners(){
        register_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                create_user();
            }
        });
    }

    public void create_user() {
        UserModel user = null;
        try {
            user = new UserModel(
                    et_firstname.getText().toString(),
                    et_lastname.getText().toString(),
                    et_email.getText().toString(),
                    et_password.getText().toString(),
                    et_phone_number.getText().toString(),
                    0,
                    0,
                    "41.998274798664305",
                    "21.42582893371582",
                    "41.998274798664305",
                    "21.42582893371582",
                    -1);
        } catch (Exception e) {
            Toast.makeText(this, "Exception: " + e.toString(), Toast.LENGTH_SHORT).show();
        }

        List<String> errors = user.checkErrors();
        if (!errors.isEmpty()) {
            tv_errors.setText(errors.toString());
            Toast.makeText(this, "Invalid input!", Toast.LENGTH_LONG).show();
            return;
        }

        long userId = dbHelper.createUser(user);

        Log.d("In RegisterActivity", "user origin lat = " + dbHelper.getUser((int) userId).getOrigin_latitude());

        if (userId == -1) {
            Toast.makeText(this, "Couldn't create User.", Toast.LENGTH_SHORT).show();
            return;
        }

        boolean result = dbHelper.createDrive((int) userId);
        if(!result){
            Toast.makeText(this, "Couldn't create Drive for User.", Toast.LENGTH_SHORT).show();
            return;
        }

        RedirectToActivity(LoginActivity.class);
    }

    public void RedirectToActivity(Class activityClass){
        Intent intent = new Intent(this, activityClass);
        startActivity(intent);
    }
}
