package com.ryfsystems.a3dprinter.activities;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.ryfsystems.a3dprinter.R;
import com.ryfsystems.a3dprinter.db.ConexionSQLiteHelper;
import com.ryfsystems.a3dprinter.utilities.PasswordUtils;
import com.ryfsystems.a3dprinter.utilities.Utilities;

import static com.ryfsystems.a3dprinter.utilities.Utilities.FIELD_U_EMAIL;
import static com.ryfsystems.a3dprinter.utilities.Utilities.FIELD_U_NAME;
import static com.ryfsystems.a3dprinter.utilities.Utilities.FIELD_U_PASSWORD;
import static com.ryfsystems.a3dprinter.utilities.Utilities.FIELD_U_PHONE;
import static com.ryfsystems.a3dprinter.utilities.Utilities.FIELD_U_ROLE;
import static com.ryfsystems.a3dprinter.utilities.Utilities.FIELD_U_USER;
import static com.ryfsystems.a3dprinter.utilities.Utilities.TABLE_USER;
import static com.ryfsystems.a3dprinter.utilities.Utilities.dbName;

public class RegisterUserActivity extends AppCompatActivity implements View.OnClickListener {

    Button btnRegisterUserRegister;
    EditText txtRegisterUserName, txtRegisterUserUsername, txtRegisterUserPassword, txtRegisterUserPasswordConfirm, txtRegisterUserEmail, txtRegisterUserPhone;
    SQLiteDatabase db;

    Integer rolId = 2;
    String encryptedPassword = null;

    ConexionSQLiteHelper conn;

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
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btnRegisterUserRegister) {

            if (allFilled()) {
                if (validaPass()) {
                    if (validUser(txtRegisterUserUsername.getText().toString()) == 0) {
                        try {
                            encryptedPassword = PasswordUtils.encrypt(txtRegisterUserPassword.getText().toString(), PasswordUtils.SALT);
                            registerUser(
                                    txtRegisterUserName.getText().toString(),
                                    txtRegisterUserUsername.getText().toString(),
                                    encryptedPassword.trim(),
                                    txtRegisterUserEmail.getText().toString(),
                                    txtRegisterUserPhone.getText().toString());
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else {
                        Toast.makeText(getApplicationContext(), "Usuario Existe!!!... Reintente", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "Los Passwords no Coinciden... Reintente", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(getApplicationContext(), "Debe llenar todos los Campos", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void registerUser(String name, String userName, String password, String email, String phone) {
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