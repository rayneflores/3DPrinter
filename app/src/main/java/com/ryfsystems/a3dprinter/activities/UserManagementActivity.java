package com.ryfsystems.a3dprinter.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
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

public class UserManagementActivity extends AppCompatActivity implements View.OnClickListener {

    Bundle received;
    Button btnRegistrar;
    EditText txtUserName, txtUserPassword, txtUserPasswordConfirm, txtUserEmail, txtUserPhone;
    TextView lblTitle;

    Boolean isAdmin;
    String userId = null;

    String encryptedPassword;
    String decryptedPassword;

    CheckBox chkAdmin;

    FirebaseFirestore firebaseFirestore;
    FirebaseAuth firebaseAuth;

    User userReceived;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_management);

        txtUserName = findViewById(R.id.txtUserName);
        txtUserPassword = findViewById(R.id.txtUserPassword);
        txtUserPasswordConfirm = findViewById(R.id.txtUserPasswordConfirm);
        txtUserEmail = findViewById(R.id.txtUserEmail);
        txtUserPhone = findViewById(R.id.txtUserPhone);
        lblTitle = findViewById(R.id.lblUserManagementTitle);
        btnRegistrar = findViewById(R.id.btnUserRegister);
        chkAdmin = findViewById(R.id.chkAdmin);

        chkAdmin.setOnCheckedChangeListener((buttonView, isChecked) -> chkAdmin.setChecked(isChecked));

        initializeFirebase();

        received = getIntent().getExtras();

        if (received != null) {

            lblTitle.setText(R.string.actualizar_usuario);
            btnRegistrar.setText(R.string.actualizar);

            userReceived = (User) received.getSerializable("user");

            isAdmin = userReceived.getUAdmin() == 1;


            try {
                decryptedPassword = PasswordUtils.decrypt(userReceived.getUPassword(), PasswordUtils.SALT);
            } catch (Exception e) {
                e.printStackTrace();
            }

            userId = userReceived.getUId();
            txtUserName.setText(userReceived.getUName());
            txtUserPassword.setText(decryptedPassword.trim());
            txtUserPasswordConfirm.setText(decryptedPassword.trim());
            txtUserEmail.setText(userReceived.getUEmail());
            txtUserPhone.setText(userReceived.getUPhone());
            chkAdmin.setChecked(isAdmin);
        }
    }

    private void initializeFirebase() {
        FirebaseApp.initializeApp(this);
        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
    }

    @Override
    public void onClick(View v) {
        if (allFilled()) {
            if (validaPass()) {
                if (received != null) {
                    try {
                        encryptedPassword = PasswordUtils.encrypt(txtUserPassword.getText().toString(), PasswordUtils.SALT);
                        updateUser(
                                userId,
                                txtUserName.getText().toString(),
                                encryptedPassword.trim(),
                                txtUserEmail.getText().toString(),
                                txtUserPhone.getText().toString(),
                                chkAdmin.isChecked() ? 1L : 0L
                        );
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    try {
                        encryptedPassword = PasswordUtils.encrypt(txtUserPassword.getText().toString(), PasswordUtils.SALT);
                        firebaseAuth.createUserWithEmailAndPassword(txtUserEmail.getText().toString(), encryptedPassword)
                                .addOnSuccessListener(authResult -> {
                                    FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
                                    User user = new User();
                                    user.setUId(firebaseUser.getUid());
                                    user.setUName(txtUserName.getText().toString());
                                    user.setUPassword(encryptedPassword.trim());
                                    user.setUEmail(txtUserEmail.getText().toString());
                                    user.setUPhone(txtUserPhone.getText().toString());
                                    if (chkAdmin.isChecked()) {
                                        user.setUAdmin(1L);
                                    } else {
                                        user.setUAdmin(0L);
                                    }
                                    DocumentReference documentReference = firebaseFirestore.collection("User").document(firebaseUser.getUid());
                                    documentReference.set(user);

                                    Toast.makeText(getApplicationContext(), "Usuario Registrado Satisfactoriamente", Toast.LENGTH_SHORT).show();
                                    Intent i = new Intent(getApplicationContext(), UsersActivity.class);
                                    startActivity(i);
                                    finish();
                                })
                                .addOnFailureListener(e -> Toast.makeText(getApplicationContext(), "No se pudieron registrar los datos del Usuario. Additional data: " + e.getMessage(), Toast.LENGTH_SHORT).show());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            } else {
                Toast.makeText(getApplicationContext(), "Los Passwords no Coinciden... Reintente", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(getApplicationContext(), "Debe llenar todos los Campos", Toast.LENGTH_SHORT).show();
        }
    }

    private Boolean allFilled() {
        return
                !txtUserName.getText().toString().trim().equalsIgnoreCase("") &&
                        !txtUserPassword.getText().toString().trim().equalsIgnoreCase("") &&
                        !txtUserPasswordConfirm.getText().toString().trim().equalsIgnoreCase("") &&
                        !txtUserEmail.getText().toString().trim().equalsIgnoreCase("") &&
                        !txtUserPhone.getText().toString().trim().equalsIgnoreCase("");
    }

    private boolean validaPass() {
        return txtUserPassword.getText().toString().equals(txtUserPasswordConfirm.getText().toString());
    }

    private void updateUser(String id, String name, String password, String email, String phone, Long admin) {

        User user = new User(id, name, password, email, phone, admin);

        DocumentReference documentReference = firebaseFirestore.collection("User").document(user.getUId());
        documentReference.set(user);

        Toast.makeText(getApplicationContext(), "Usuario Actualizado Satisfactoriamente", Toast.LENGTH_SHORT).show();
        Intent i = new Intent(getApplicationContext(), UsersActivity.class);
        startActivity(i);
        finish();
    }
}