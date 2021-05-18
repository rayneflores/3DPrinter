package com.ryfsystems.a3dprinter.activities;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.ryfsystems.a3dprinter.R;
import com.ryfsystems.a3dprinter.db.ConexionSQLiteHelper;
import com.ryfsystems.a3dprinter.entities.Roles;
import com.ryfsystems.a3dprinter.entities.User;
import com.ryfsystems.a3dprinter.utilities.PasswordUtils;

import java.util.ArrayList;

import static com.ryfsystems.a3dprinter.utilities.Utilities.FIELD_U_EMAIL;
import static com.ryfsystems.a3dprinter.utilities.Utilities.FIELD_U_ID;
import static com.ryfsystems.a3dprinter.utilities.Utilities.FIELD_U_NAME;
import static com.ryfsystems.a3dprinter.utilities.Utilities.FIELD_U_PASSWORD;
import static com.ryfsystems.a3dprinter.utilities.Utilities.FIELD_U_PHONE;
import static com.ryfsystems.a3dprinter.utilities.Utilities.FIELD_U_ROLE;
import static com.ryfsystems.a3dprinter.utilities.Utilities.FIELD_U_USER;
import static com.ryfsystems.a3dprinter.utilities.Utilities.TABLE_ROLE;
import static com.ryfsystems.a3dprinter.utilities.Utilities.TABLE_USER;
import static com.ryfsystems.a3dprinter.utilities.Utilities.dbName;
import static com.ryfsystems.a3dprinter.utilities.Utilities.dbVersion;

public class UserManagementActivity extends AppCompatActivity implements View.OnClickListener {

    Bundle received;
    Button btnRegistrar;
    EditText txtUserName, txtUserUsername, txtUserPassword, txtUserPasswordConfirm, txtUserEmail, txtUserPhone;
    Spinner spRoles;
    SQLiteDatabase db;
    TextView lblTitle;

    Integer rolId = null;
    Integer userId = null;

    String encryptedPassword;
    String decryptedPassword;

    ArrayList<String> listRoles;
    ArrayList<Roles> rolesList;

    ConexionSQLiteHelper conn;

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

        conn = new ConexionSQLiteHelper(getApplicationContext(), dbName, null, dbVersion);

        queryRolesList();

        ArrayAdapter<CharSequence> adapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, listRoles);

        spRoles.setAdapter(adapter);

        spRoles.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position != 0) {
                    rolId = rolesList.get(position - 1).getId();
                } else {
                    rolId = null;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        /**/
        received = getIntent().getExtras();

        User user;

        if (received != null) {

            lblTitle.setText("Actualizacion de Usuario");
            btnRegistrar.setText("Actualizar");

            user = (User) received.getSerializable("user");

            try {
                decryptedPassword = PasswordUtils.decrypt(user.getUPassword(), PasswordUtils.SALT);
            } catch (Exception e) {
                e.printStackTrace();
            }


            userId = user.getUId();
            txtUserName.setText(user.getUName());
            txtUserUsername.setText(user.getUUserName());
            txtUserPassword.setText(decryptedPassword.trim());
            txtUserPasswordConfirm.setText(decryptedPassword.trim());
            txtUserEmail.setText(user.getUEmail());
            txtUserPhone.setText(user.getUPhone());
            spRoles.setSelection(user.getURole());
        }
        /**/
    }

    private void queryRolesList() {
        SQLiteDatabase db = conn.getReadableDatabase();

        Roles role;
        rolesList = new ArrayList<>();

        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_ROLE, null);

        while (cursor.moveToNext()) {
            role = new Roles();
            role.setId(cursor.getInt(0));
            role.setName(cursor.getString(1));

            rolesList.add(role);
        }

        fillList();
    }

    private void fillList() {
        listRoles = new ArrayList<>();
        listRoles.add("Seleccione un Rol");

        for (int i = 0; i < rolesList.size(); i++) {
            listRoles.add(rolesList.get(i).getId() + " - " + rolesList.get(i).getName());
        }
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

        db = conn.getWritableDatabase();

        String insert = "INSERT INTO " +
                TABLE_USER + " (" +
                FIELD_U_NAME + ", " +
                FIELD_U_USER + ", " +
                FIELD_U_PASSWORD + ", " +
                FIELD_U_EMAIL + ", " +
                FIELD_U_PHONE + ", " +
                FIELD_U_ROLE + ") VALUES ('" +
                name + "', '" +
                userName + "', '" +
                password + "', '" +
                email + "', '" +
                phone + "', " +
                rolId + ")";

        db.execSQL(insert);
        Toast.makeText(getApplicationContext(), "Usuario Registrado Satisfactoriamente", Toast.LENGTH_SHORT).show();
        db.close();
        finish();
        startActivity(new Intent(getApplicationContext(), UsersActivity.class));
    }

    private void updateUser(String name, String userName, String password, String email, String phone, int rolId) {
        db = conn.getWritableDatabase();

        String update = "UPDATE " +
                TABLE_USER + " SET " +
                FIELD_U_NAME + "='" + name + "', " +
                FIELD_U_USER + "='" + userName + "', " +
                FIELD_U_PASSWORD + "='" + password + "', " +
                FIELD_U_EMAIL + "='" + email + "', " +
                FIELD_U_PHONE + "='" + phone + "', " +
                FIELD_U_ROLE + "=" + rolId + " WHERE " +
                FIELD_U_ID + " = " + userId;

        db.execSQL(update);
        Toast.makeText(getApplicationContext(), "Usuario Actualizado Satisfactoriamente", Toast.LENGTH_SHORT).show();
        db.close();
        finish();
        startActivity(new Intent(getApplicationContext(), UsersActivity.class));
    }
}