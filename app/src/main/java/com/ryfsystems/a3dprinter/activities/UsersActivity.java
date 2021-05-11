package com.ryfsystems.a3dprinter.activities;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import com.ryfsystems.a3dprinter.R;
import com.ryfsystems.a3dprinter.db.ConexionSQLiteHelper;
import com.ryfsystems.a3dprinter.entities.User;

import java.util.ArrayList;

import static com.ryfsystems.a3dprinter.utilities.Utilities.TABLE_USER;
import static com.ryfsystems.a3dprinter.utilities.Utilities.dbName;

public class UsersActivity extends AppCompatActivity implements View.OnClickListener {

    Button btnAddUser;
    ListView lvUsers;

    ArrayList<String> listUsers;
    ArrayList<User> usersList;

    ConexionSQLiteHelper conn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_users);

        btnAddUser = findViewById(R.id.btnAddUser);
        btnAddUser.setOnClickListener(this);

        lvUsers = findViewById(R.id.lvUsers);

        conn = new ConexionSQLiteHelper(getApplicationContext(), dbName, null, 1);

        queryUsersList();

        ArrayAdapter<CharSequence> adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, listUsers);

        lvUsers.setAdapter(adapter);

        lvUsers.setOnItemClickListener((parent, view, position, id) -> {

            User user = usersList.get(position);

            Intent intent = new Intent(getApplicationContext(), UserManagementActivity.class);

            Bundle bundle = new Bundle();
            bundle.putSerializable("user", user);
            intent.putExtras(bundle);
            finish();
            startActivity(intent);
        });
    }

    private void queryUsersList() {
        SQLiteDatabase db = conn.getReadableDatabase();

        User user;
        usersList = new ArrayList<>();

        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_USER, null);

        while (cursor.moveToNext()) {
            user = new User();
            user.setId(cursor.getInt(0));
            user.setName(cursor.getString(1));
            user.setUserName(cursor.getString(2));
            user.setPassword(cursor.getString(3));
            user.setEmail(cursor.getString(4));
            user.setPhone(cursor.getString(5));
            user.setRole(cursor.getInt(6));

            usersList.add(user);
        }
        getList();
    }

    private void getList() {
        listUsers = new ArrayList<>();

        for (int i = 0; i < usersList.size(); i++) {
            listUsers.add(usersList.get(i).getId() + ".- " + usersList.get(i).getName());
        }
    }

    @Override
    public void onClick(View v) {
        Intent i;
        switch (v.getId()) {
            case R.id.btnAddUser:
                i = new Intent(getApplicationContext(), UserManagementActivity.class);
                finish();
                startActivity(i);
                break;
        }
    }
}