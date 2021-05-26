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

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.ryfsystems.a3dprinter.R;
import com.ryfsystems.a3dprinter.models.User;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    Button btnLogin;
    /*ConexionSQLiteHelper conn;*/
    EditText txtLoginUserEmail, txtLoginPassword;
    SQLiteDatabase db;
    TextView txtNewUser;

    Integer rolId = null;
    String userName = null;
    String encryptedPassword;

    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    FirebaseAuth firebaseAuth;

    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        /*conn = new ConexionSQLiteHelper(getApplicationContext(), dbName, null, 1);*/

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
        firebaseDatabase = FirebaseDatabase.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        databaseReference = firebaseDatabase.getReference();
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

        /*db = conn.getReadableDatabase();

        try {
            encryptedPassword = PasswordUtils.encrypt(password, PasswordUtils.SALT);
        } catch (Exception e) {
            e.printStackTrace();
        }

        String query =
                "SELECT " + FIELD_U_ROLE + ", " + FIELD_U_NAME +
                        " FROM " + TABLE_USER +
                        " WHERE " + FIELD_U_PASSWORD + " = '" + encryptedPassword.trim() + "'" +
                        "AND " + FIELD_U_USER + " = '" + user + "'";

        Cursor cursor = db.rawQuery(query, null);

        while (cursor.moveToNext()) {
            if (cursor.getInt(0) != 0) {
                rolId = cursor.getInt(0);
                userName = cursor.getString(1);
                cursor.close();
                return true;
            }
        }
        cursor.close();
        return false;*/
        progressDialog.show();
        firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        progressDialog.dismiss();
                        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
                        User user = new User();
                        user.setUId(firebaseUser.getUid());

                        Intent intent;
                        intent = new Intent(getApplicationContext(), MainActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putInt("rolId", rolId);
                        bundle.putString("userName", userName);
                        intent.putExtras(bundle);
                        finish();
                        startActivity(intent);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        progressDialog.dismiss();
                        Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }
}