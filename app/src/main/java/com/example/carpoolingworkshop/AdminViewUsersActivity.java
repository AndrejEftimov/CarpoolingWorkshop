package com.example.carpoolingworkshop;

import static android.preference.PreferenceManager.getDefaultSharedPreferences;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.List;

public class AdminViewUsersActivity extends AppCompatActivity {
    private ListView lv_user_list;
    private DBHelper dbHelper;
    private SharedPreferences sharedPreferences;
    private ArrayAdapter<UserModel> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //EdgeToEdge.enable(this);
        setContentView(R.layout.activity_admin_view_users);

        Toolbar appbar = findViewById(R.id.appbar);
        appbar.setTitle(getResources().getString(R.string.app_name) + "  |  " + "Admin - View Users");
        setSupportActionBar(appbar);
        // Show Back Button
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // setup properties and event listeners
        initProperties();
        setupEventListeners();
        updateUserList();

        // redirect to LoginActivity if not logged in
        RedirectIfLoggedOut();
    }

    @Override
    protected void onResume() {
        super.onResume();

        updateUserList();
    }

    public void initProperties(){
        lv_user_list = findViewById(R.id.lv_user_list);
        dbHelper = new DBHelper(this);
        sharedPreferences = getDefaultSharedPreferences(this);

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
    }

    public void updateUserList(){
        List<UserModel> users = dbHelper.getAllUsers();
        adapter = new ArrayAdapter<UserModel>(this, android.R.layout.simple_list_item_1, users);
        lv_user_list.setAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.admin_menu, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int itemId = item.getItemId();
        if(itemId == R.id.enter_as_user_item){
            RedirectToActivity(HomeActivity.class);
        }else if(itemId == R.id.view_users_item){
            RedirectToActivity(AdminViewUsersActivity.class);
        }else if(itemId == R.id.view_vehicles_item){
            RedirectToActivity(AdminViewUsersActivity.class);
        }else if(itemId == R.id.logout_item){
            RedirectToActivity(LogoutActivity.class);
        }

        return super.onOptionsItemSelected(item);
    }

    public void RedirectToActivity(Class activityClass){
        Intent intent = new Intent(this, activityClass);
        startActivity(intent);
    }

    public void RedirectIfLoggedOut(){
        int userId = sharedPreferences.getInt("LoggedInUserId", -1);
        if(userId < 0 || dbHelper.getUser(userId) == null) {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putInt("LoggedInUserId", -1);
            editor.apply();
            RedirectToActivity(LoginActivity.class);
        }
    }
}