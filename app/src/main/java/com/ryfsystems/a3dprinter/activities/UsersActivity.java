package com.ryfsystems.a3dprinter.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.ryfsystems.a3dprinter.R;
import com.ryfsystems.a3dprinter.models.User;

import java.util.ArrayList;

public class UsersActivity extends AppCompatActivity implements View.OnClickListener {

    Button btnAddUser;
    ListView lvUsers;

    ArrayList<User> usersFbList = new ArrayList<>();
    ArrayAdapter<User> arrayAdapterUser;

    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

    User userSelected;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_users);

        btnAddUser = findViewById(R.id.btnAddUser);
        btnAddUser.setOnClickListener(this);

        lvUsers = findViewById(R.id.lvUsers);

        initializeFirebase();

        listFbUsers();

        lvUsers.setOnItemClickListener((parent, view, position, id) -> {
            userSelected = (User) parent.getItemAtPosition(position);
            Intent intent = new Intent(UsersActivity.this.getApplicationContext(), UserManagementActivity.class);
            Bundle bundle = new Bundle();
            bundle.putSerializable("user", userSelected);
            intent.putExtras(bundle);
            UsersActivity.this.finish();
            UsersActivity.this.startActivity(intent);
        });
    }

    private void initializeFirebase() {
        FirebaseApp.initializeApp(this);
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();
    }

    private void listFbUsers() {

        databaseReference.child("User").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                usersFbList.clear();
                for (DataSnapshot objSnapshot : snapshot.getChildren()) {
                    User user = objSnapshot.getValue(User.class);
                    usersFbList.add(user);

                    arrayAdapterUser = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_list_item_1, usersFbList);
                    lvUsers.setAdapter(arrayAdapterUser);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

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