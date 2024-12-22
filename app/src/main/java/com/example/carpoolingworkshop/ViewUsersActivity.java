package com.example.carpoolingworkshop;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.util.List;

public class ViewUsersActivity extends AppCompatActivity {
    ListView lv_user_list;
    Button add_user_btn;
    DBHelper dbHelper;
    ArrayAdapter<UserModel> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //EdgeToEdge.enable(this);
        setContentView(R.layout.activity_view_users);

        Toolbar appbar = findViewById(R.id.appbar);
        appbar.setTitle(getResources().getString(R.string.app_name) + "  |  " + "All Users");
        setSupportActionBar(appbar);
        // Show Back Button
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Initialize Properties and setup Event Listeners
        initProperties();
        setupEventListeners();
    }

    @Override
    protected void onResume() {
        super.onResume();

        updateUserList();
    }

    public void initProperties(){
        lv_user_list = findViewById(R.id.lv_user_list);
        add_user_btn = findViewById(R.id.add_user_btn);
        dbHelper = new DBHelper(this);

        updateUserList();
    }

    public void setupEventListeners(){
        lv_user_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                UserModel user = (UserModel) parent.getItemAtPosition(position);
                dbHelper.deleteUser(user.getId());
                updateUserList();
            }
        });

        add_user_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RedirectToActivity(RegisterActivity.class);
            }
        });
    }

    public void updateUserList(){
        List<UserModel> users = dbHelper.getAllUsers();
        adapter = new ArrayAdapter<UserModel>(this, android.R.layout.simple_list_item_1, users);
        lv_user_list.setAdapter(adapter);
    }

    public void RedirectToActivity(Class activityClass){
        Intent intent = new Intent(this, activityClass);
        startActivity(intent);
    }
}