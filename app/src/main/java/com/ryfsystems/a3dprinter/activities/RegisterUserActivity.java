package com.ryfsystems.a3dprinter.activities;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.ryfsystems.a3dprinter.R;
import com.ryfsystems.a3dprinter.db.ConexionSQLiteHelper;
import com.ryfsystems.a3dprinter.models.User;
import com.ryfsystems.a3dprinter.utilities.PasswordUtils;
import com.ryfsystems.a3dprinter.utilities.Utilities;

import java.util.UUID;

import static com.ryfsystems.a3dprinter.utilities.Utilities.dbName;

public class RegisterUserActivity extends AppCompatActivity implements View.OnClickListener {

    EditText txtRegisterUserName, txtRegisterUserUsername, txtRegisterUserPassword, txtRegisterUserPasswordConfirm, txtRegisterUserEmail, txtRegisterUserPhone;
    SQLiteDatabase db;

    /*Rol creado de Usuario Basico para los Usuarios Creados por fuera*/
    Integer rolId = 2;
    String encryptedPassword = null;

    ConexionSQLiteHelper conn;

    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_user);

        conn = new ConexionSQLiteHelper(getApplicationContext(), dbName, null, 1);

        txtRegisterUserName = findViewById(R.id.txtRegisterUserName);
        txtRegisterUserUsername = findViewById(R.id.txtRegisterUserUsername);
        txtRegisterUserPassword = findViewById(R.id.txtRegisterUserPassword);
        txtRegisterUserPasswordConfirm = findViewById(R.id.txtRegisterUserPasswordConfirm);
        txtRegisterUserEmail = findViewById(R.id.txtRegisterUserEmail);
        txtRegisterUserPhone = findViewById(R.id.txtRegisterUserPhone);

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
        if (v.getId() == R.id.btnRegisterUserRegister) {
            if (allFilled()) {
                if (txtRegisterUserPassword.getText().length() >= 6) {
                    if (validaPass()) {
                        try {
                            encryptedPassword = PasswordUtils.encrypt(txtRegisterUserPassword.getText().toString(), PasswordUtils.SALT);

                            firebaseAuth.createUserWithEmailAndPassword(txtRegisterUserEmail.getText().toString(), encryptedPassword).addOnCompleteListener(task -> {
                                if (task.isSuccessful()) {
                                    User user = new User();
                                    user.setUId(firebaseAuth.getCurrentUser().getUid());
                                    user.setUName(txtRegisterUserName.getText().toString());
                                    user.setUUserName(txtRegisterUserUsername.getText().toString());
                                    user.setUPassword(encryptedPassword.trim());
                                    user.setUEmail(txtRegisterUserEmail.getText().toString());
                                    user.setUPhone(txtRegisterUserPhone.getText().toString());
                                    user.setURole(rolId);

                                    databaseReference.child("User").child(user.getUId()).setValue(user).addOnCompleteListener(taskResult -> {
                                        if (taskResult.isSuccessful()) {
                                            Toast.makeText(getApplicationContext(), "Usuario Creado Satisfactoriamente", Toast.LENGTH_SHORT).show();
                                            finish();
                                            startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                                        } else {
                                            Toast.makeText(getApplicationContext(), "No se pudieron registrar los datos del Usuario", Toast.LENGTH_SHORT).show();
                                        }
                                    });

                                } else {
                                    Toast.makeText(getApplicationContext(), "No se pudo registrar el Usuario", Toast.LENGTH_SHORT).show();
                                }
                            });
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else {
                        Toast.makeText(getApplicationContext(), "Los Passwords no Coinciden... Reintente", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "El password debe Contener al menos 6 Caracteres", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(getApplicationContext(), "Debe llenar todos los Campos", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void registerUser(String name, String userName, String password, String email, String phone) {

        /*Para Firebase*/
        User user = new User();
        user.setUId(UUID.randomUUID().toString());
        user.setUName(name);
        user.setUUserName(userName);
        user.setUPassword(password);
        user.setUEmail(email);
        user.setUPhone(phone);
        user.setURole(rolId);

        databaseReference.child("User").child(user.getUId()).setValue(user);
        /*End Firebase*/

        finish();
        startActivity(new Intent(getApplicationContext(), LoginActivity.class));
    }

    private int validUser(String username) {
        db = conn.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + Utilities.TABLE_USER + " WHERE " + Utilities.FIELD_U_USER + " = '" + username + "'", null);
        return cursor.getCount();
    }


    private boolean allFilled() {
        return
                !txtRegisterUserName.getText().toString().trim().equalsIgnoreCase("") &&
                        !txtRegisterUserUsername.getText().toString().trim().equalsIgnoreCase("") &&
                        !txtRegisterUserPassword.getText().toString().trim().equalsIgnoreCase("") &&
                        !txtRegisterUserPasswordConfirm.getText().toString().trim().equalsIgnoreCase("") &&
                        !txtRegisterUserEmail.getText().toString().trim().equalsIgnoreCase("") &&
                        !txtRegisterUserPhone.getText().toString().trim().equalsIgnoreCase("");
    }

    private boolean validaPass() {
        return txtRegisterUserPassword.getText().toString().equals(txtRegisterUserPasswordConfirm.getText().toString());
    }
}