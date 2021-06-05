package com.ryfsystems.a3dprinter.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.ryfsystems.a3dprinter.R;
import com.ryfsystems.a3dprinter.utilities.PasswordUtils;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    Button btnLogin;
    EditText txtLoginUserEmail, txtLoginPassword;
    SQLiteDatabase db;
    TextView txtNewUser;

    String encryptedPassword;

    FirebaseAuth firebaseAuth;
    FirebaseFirestore firebaseFirestore;

    ProgressDialog progressDialog;

    Intent i;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Espere...");
        progressDialog.setMessage("Comprobando Usuario y Password");
        progressDialog.setCanceledOnTouchOutside(false);

        btnLogin = findViewById(R.id.btnLogin);
        btnLogin.setOnClickListener(this);

        txtLoginUserEmail = findViewById(R.id.txtLoginUserEmail);
        txtLoginPassword = findViewById(R.id.txtLoginPassword);

        txtNewUser = findViewById(R.id.txtNewUser);
        txtNewUser.setOnClickListener(this);

        initializeFirebase();
    }

    private void initializeFirebase() {
        FirebaseApp.initializeApp(this);
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();
    }


    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()) {
            case R.id.txtNewUser:
                intent = new Intent(getApplicationContext(), RegisterUserActivity.class);
                finish();
                startActivity(intent);
                break;
            case R.id.btnLogin:
                if (allFilled()) {
                    progressDialog.show();
                    chekUserPassword(txtLoginUserEmail.getText().toString(), txtLoginPassword.getText().toString());
                } else {
                    Toast.makeText(getApplicationContext(), "Debe llenar todos los Campos", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    private boolean allFilled() {
        return !txtLoginUserEmail.getText().toString().trim().equalsIgnoreCase("") &&
                !txtLoginPassword.getText().toString().trim().equalsIgnoreCase("");
    }

    private void chekUserPassword(String email, String password) {

        try {
            encryptedPassword = PasswordUtils.encrypt(password, PasswordUtils.SALT);
        } catch (Exception e) {
            e.printStackTrace();
        }

        firebaseAuth.signInWithEmailAndPassword(email, encryptedPassword)
                .addOnSuccessListener(authResult -> {

                    DocumentReference documentReference = firebaseFirestore.collection("User").document(firebaseAuth.getCurrentUser().getUid());
                    documentReference.get()
                            .addOnSuccessListener(documentSnapshot -> {
                                i = new Intent(getApplicationContext(), MainActivity.class);
                                Bundle bundle = new Bundle();
                                bundle.putLong("isAdmin", (Long) documentSnapshot.get("uisadmin"));
                                bundle.putString("userName", documentSnapshot.getString("uname"));
                                i.putExtras(bundle);
                                progressDialog.dismiss();
                                startActivity(i);
                                finish();
                            })
                            .addOnFailureListener(e -> {
                                progressDialog.dismiss();
                                Toast.makeText(LoginActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                            });
                })
                .addOnFailureListener(e -> {
                    progressDialog.dismiss();
                    Toast.makeText(LoginActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                });
    }

    /*@Override
    protected void onStart() {
        super.onStart();
        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            DocumentReference documentReference = firebaseFirestore.collection("User").document(firebaseAuth.getCurrentUser().getUid());
            documentReference.get()
                    .addOnSuccessListener(documentSnapshot -> {
                        Intent intent;
                        intent = new Intent(getApplicationContext(), MainActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putLong("isAdmin", (Long) documentSnapshot.get("uisadmin"));
                        bundle.putString("userName", documentSnapshot.getString("uname"));
                        intent.putExtras(bundle);
                        progressDialog.dismiss();
                        startActivity(intent);
                        finish();
                    });
        }
    }*/

    @Override
    public void onBackPressed() {
        FirebaseAuth.getInstance().signOut();
        finish();
    }
}