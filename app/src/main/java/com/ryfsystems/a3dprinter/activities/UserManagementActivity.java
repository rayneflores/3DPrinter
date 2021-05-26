package com.ryfsystems.a3dprinter.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.ryfsystems.a3dprinter.R;
import com.ryfsystems.a3dprinter.models.Role;
import com.ryfsystems.a3dprinter.models.User;
import com.ryfsystems.a3dprinter.utilities.PasswordUtils;

import java.util.ArrayList;
import java.util.UUID;

public class UserManagementActivity extends AppCompatActivity implements View.OnClickListener {

    Bundle received;
    Button btnRegistrar;
    EditText txtUserName, txtUserUsername, txtUserPassword, txtUserPasswordConfirm, txtUserEmail, txtUserPhone;
    Spinner spRoles;
    TextView lblTitle;

    Integer rolId = null;
    String userId = null;

    String encryptedPassword;
    String decryptedPassword;

    ArrayList<Role> rolesFbList = new ArrayList<>();
    ArrayAdapter<Role> arrayAdapterRole;

    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_management);

        txtUserName = findViewById(R.id.txtUserName);
        txtUserUsername = findViewById(R.id.txtUserUsername);
        txtUserPassword = findViewById(R.id.txtUserPassword);
        txtUserPasswordConfirm = findViewById(R.id.txtUserPasswordConfirm);
        txtUserEmail = findViewById(R.id.txtUserEmail);
        txtUserPhone = findViewById(R.id.txtUserPhone);
        spRoles = findViewById(R.id.spRoles);
        lblTitle = findViewById(R.id.lblUserManagementTitle);
        btnRegistrar = findViewById(R.id.btnUserRegister);

        initializeFirebase();

        listFbRoles();

        spRoles.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                rolId = rolesFbList.get(position).getRType();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        received = getIntent().getExtras();

        User userReceived;

        if (received != null) {

            lblTitle.setText("Actualizacion de Usuario");
            btnRegistrar.setText("Actualizar");

            userReceived = (User) received.getSerializable("user");

            try {
                decryptedPassword = PasswordUtils.decrypt(userReceived.getUPassword(), PasswordUtils.SALT);
            } catch (Exception e) {
                e.printStackTrace();
            }

            userId = userReceived.getUId();
            txtUserName.setText(userReceived.getUName());
            txtUserUsername.setText(userReceived.getUUserName());
            txtUserPassword.setText(decryptedPassword.trim());
            txtUserPasswordConfirm.setText(decryptedPassword.trim());
            txtUserEmail.setText(userReceived.getUEmail());
            txtUserPhone.setText(userReceived.getUPhone());
            spRoles.setSelection(userReceived.getURole());
            rolId = userReceived.getURole();
        }
    }

    private void initializeFirebase() {
        FirebaseApp.initializeApp(this);
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();
    }

    private void listFbRoles() {
        databaseReference.child("Role").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                rolesFbList.clear();
                for (DataSnapshot objSnapshot : snapshot.getChildren()) {
                    Role role = objSnapshot.getValue(Role.class);
                    rolesFbList.add(role);

                    arrayAdapterRole = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_spinner_item, rolesFbList);
                    spRoles.setAdapter(arrayAdapterRole);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public void onClick(View v) {
        if (allFilled()) {
            if (validaPass()) {
                if (rolId != null) {
                    if (received != null) {
                        try {
                            encryptedPassword = PasswordUtils.encrypt(txtUserPassword.getText().toString(), PasswordUtils.SALT);
                            updateUser(
                                    userId,
                                    txtUserName.getText().toString(),
                                    txtUserUsername.getText().toString(),
                                    encryptedPassword.trim(),
                                    txtUserEmail.getText().toString(),
                                    txtUserPhone.getText().toString(),
                                    rolId
                            );
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else {
                        try {
                            encryptedPassword = PasswordUtils.encrypt(txtUserPassword.getText().toString(), PasswordUtils.SALT);
                            saveUser(
                                    txtUserName.getText().toString(),
                                    txtUserUsername.getText().toString(),
                                    encryptedPassword.trim(),
                                    txtUserEmail.getText().toString(),
                                    txtUserPhone.getText().toString(),
                                    rolId
                            );
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "Debe Registrar un Rol", Toast.LENGTH_SHORT).show();
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
                        !txtUserUsername.getText().toString().trim().equalsIgnoreCase("") &&
                        !txtUserPassword.getText().toString().trim().equalsIgnoreCase("") &&
                        !txtUserPasswordConfirm.getText().toString().trim().equalsIgnoreCase("") &&
                        !txtUserEmail.getText().toString().trim().equalsIgnoreCase("") &&
                        !txtUserPhone.getText().toString().trim().equalsIgnoreCase("");
    }

    private boolean validaPass() {
        return txtUserPassword.getText().toString().equals(txtUserPasswordConfirm.getText().toString());
    }

    private void saveUser(String name, String userName, String password, String email, String phone, int rolId) {

        User user = new User();
        user.setUId(UUID.randomUUID().toString());
        user.setUName(name);
        user.setUUserName(userName);
        user.setUPassword(password);
        user.setUEmail(email);
        user.setUPhone(phone);
        user.setURole(rolId);

        databaseReference.child("User").child(user.getUId()).setValue(user);

        Toast.makeText(getApplicationContext(), "Usuario Registrado Satisfactoriamente", Toast.LENGTH_SHORT).show();

        finish();
        startActivity(new Intent(getApplicationContext(), UsersActivity.class));
    }

    private void updateUser(String userId, String name, String userName, String password, String email, String phone, int rolId) {

        User user = new User();
        user.setUId(userId);
        user.setUName(name);
        user.setUUserName(userName);
        user.setUPassword(password);
        user.setUEmail(email);
        user.setUPhone(phone);
        user.setURole(rolId);

        databaseReference.child("User").child(user.getUId()).setValue(user);

        Toast.makeText(getApplicationContext(), "Usuario Actualizado Satisfactoriamente", Toast.LENGTH_SHORT).show();
        finish();
        startActivity(new Intent(getApplicationContext(), UsersActivity.class));
    }
}