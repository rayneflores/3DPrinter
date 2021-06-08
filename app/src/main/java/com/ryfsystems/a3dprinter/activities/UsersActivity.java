package com.ryfsystems.a3dprinter.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.FirebaseApp;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.ryfsystems.a3dprinter.R;
import com.ryfsystems.a3dprinter.adapters.UserAdapter;
import com.ryfsystems.a3dprinter.models.User;

import java.util.ArrayList;
import java.util.List;

public class UsersActivity extends AppCompatActivity implements View.OnClickListener {

    Button btnAddUser;
    RecyclerView rvUsers;

    List<User> userFbList = new ArrayList<>();
    RecyclerView.LayoutManager layoutManager;
    FirebaseFirestore firebaseFirestore;
    UserAdapter userAdapter;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_users);

        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Espere...");
        progressDialog.setMessage("Cargando Usuarios...");
        progressDialog.setCanceledOnTouchOutside(false);

        btnAddUser = findViewById(R.id.btnAddUser);
        btnAddUser.setOnClickListener(this);

        rvUsers = findViewById(R.id.rvUsers);

        rvUsers.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        rvUsers.setLayoutManager(layoutManager);

        initializeFirebase();

        listFbUsers();
    }

    private void initializeFirebase() {
        FirebaseApp.initializeApp(this);
        firebaseFirestore = FirebaseFirestore.getInstance();
    }

    private void listFbUsers() {
        progressDialog.show();

        firebaseFirestore.collection("User")
                .orderBy("uname")
                .get()
                .addOnCompleteListener(task -> {
                    progressDialog.dismiss();
                    for (DocumentSnapshot documentSnapshot : task.getResult()) {
                        User user = new User(
                                documentSnapshot.getString("uid"),
                                documentSnapshot.getString("uname"),
                                documentSnapshot.getString("upassword"),
                                documentSnapshot.getString("uemail"),
                                documentSnapshot.getString("uphone"),
                                documentSnapshot.getLong("uadmin")
                        );
                        userFbList.add(user);
                    }
                    userAdapter = new UserAdapter(UsersActivity.this, userFbList);
                    rvUsers.setAdapter(userAdapter);
                })
                .addOnFailureListener(e -> {
                    progressDialog.dismiss();
                    Toast.makeText(UsersActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }

    @Override
    public void onClick(View v) {
        Intent i;
        if (v.getId() == R.id.btnAddUser) {
            i = new Intent(getApplicationContext(), UserManagementActivity.class);
            startActivity(i);
            finish();
        }
    }
}