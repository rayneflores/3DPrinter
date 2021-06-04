package com.ryfsystems.a3dprinter.activities;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.ryfsystems.a3dprinter.R;
import com.ryfsystems.a3dprinter.models.User;
import com.ryfsystems.a3dprinter.utilities.PasswordUtils;

public class RegisterUserActivity extends AppCompatActivity implements View.OnClickListener {

    EditText txtRegisterUserName, txtRegisterUserPassword, txtRegisterUserPasswordConfirm, txtRegisterUserEmail, txtRegisterUserPhone;
    SQLiteDatabase db;

    /*Rol creado de Usuario Basico para los Usuarios Creados por la misma aplicacion*/
    Long rolId = 2l;
    String encryptedPassword = null;

    FirebaseAuth firebaseAuth;
    FirebaseFirestore firebaseFirestore;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_user);

        txtRegisterUserName = findViewById(R.id.txtRegisterUserName);
        txtRegisterUserPassword = findViewById(R.id.txtRegisterUserPassword);
        txtRegisterUserPasswordConfirm = findViewById(R.id.txtRegisterUserPasswordConfirm);
        txtRegisterUserEmail = findViewById(R.id.txtRegisterUserEmail);
        txtRegisterUserPhone = findViewById(R.id.txtRegisterUserPhone);

        initializeFirebase();
    }

    private void initializeFirebase() {
        FirebaseApp.initializeApp(this);
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btnRegisterUserRegister) {
            if (allFilled()) {
                if (txtRegisterUserPassword.getText().length() >= 6) {
                    if (validaPass()) {
                        try {
                            encryptedPassword = PasswordUtils.encrypt(txtRegisterUserPassword.getText().toString(), PasswordUtils.SALT);
                            firebaseAuth.createUserWithEmailAndPassword(txtRegisterUserEmail.getText().toString(), encryptedPassword)
                                    .addOnSuccessListener(authResult -> {
                                        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
                                        User user = new User();
                                        user.setUId(firebaseUser.getUid());
                                        user.setUName(txtRegisterUserName.getText().toString());
                                        user.setUPassword(encryptedPassword.trim());
                                        user.setUEmail(txtRegisterUserEmail.getText().toString());
                                        user.setUPhone(txtRegisterUserPhone.getText().toString());
                                        user.setURole(rolId);
                                        Toast.makeText(getApplicationContext(), "Cuenta de Usuario Creada", Toast.LENGTH_SHORT).show();
                                        DocumentReference documentReference = firebaseFirestore.collection("User").document(firebaseUser.getUid());
                                        documentReference.set(user);
                                        startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                                        finish();
                                    })
                                    .addOnFailureListener(e -> Toast.makeText(getApplicationContext(), "No se pudieron registrar los datos del Usuario", Toast.LENGTH_SHORT).show());
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

    private boolean allFilled() {
        return
                !txtRegisterUserName.getText().toString().trim().equalsIgnoreCase("") &&
                        !txtRegisterUserPassword.getText().toString().trim().equalsIgnoreCase("") &&
                        !txtRegisterUserPasswordConfirm.getText().toString().trim().equalsIgnoreCase("") &&
                        !txtRegisterUserEmail.getText().toString().trim().equalsIgnoreCase("") &&
                        !txtRegisterUserPhone.getText().toString().trim().equalsIgnoreCase("");
    }

    private boolean validaPass() {
        return txtRegisterUserPassword.getText().toString().equals(txtRegisterUserPasswordConfirm.getText().toString());
    }
}