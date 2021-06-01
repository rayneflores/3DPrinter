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
        /*progressDialog.show();
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
                });*/

        firebaseAuth.signInWithEmailAndPassword(email, encryptedPassword)
                .addOnSuccessListener(authResult -> {

                    DocumentReference documentReference = firebaseFirestore.collection("User").document(firebaseAuth.getCurrentUser().getUid());
                    documentReference.get()
                            .addOnSuccessListener(documentSnapshot -> {
                                Intent intent;
                                intent = new Intent(getApplicationContext(), MainActivity.class);
                                Bundle bundle = new Bundle();
                                bundle.putLong("rolId", (Long) documentSnapshot.get("urole"));
                                bundle.putString("userName", documentSnapshot.getString("uname"));
                                intent.putExtras(bundle);
                                progressDialog.dismiss();
                                startActivity(intent);
                                finish();
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {

                                }
                            });
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });
    }

    /*@Override
    protected void onStart() {
        super.onStart();
        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            startActivity(new Intent(getApplicationContext(), MainActivity.class));
            finish();
        }
    }*/
}