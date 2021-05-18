package com.ryfsystems.a3dprinter.activities;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.ryfsystems.a3dprinter.R;
import com.ryfsystems.a3dprinter.db.ConexionSQLiteHelper;
import com.ryfsystems.a3dprinter.utilities.PasswordUtils;

import static com.ryfsystems.a3dprinter.utilities.Utilities.FIELD_U_NAME;
import static com.ryfsystems.a3dprinter.utilities.Utilities.FIELD_U_PASSWORD;
import static com.ryfsystems.a3dprinter.utilities.Utilities.FIELD_U_ROLE;
import static com.ryfsystems.a3dprinter.utilities.Utilities.FIELD_U_USER;
import static com.ryfsystems.a3dprinter.utilities.Utilities.TABLE_USER;
import static com.ryfsystems.a3dprinter.utilities.Utilities.dbName;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    Button btnLogin;
    ConexionSQLiteHelper conn;
    EditText txtLoginUser, txtLoginPassword;
    SQLiteDatabase db;
    TextView txtNewUser;

    Integer rolId = null;
    String userName = null;
    String encryptedPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        conn = new ConexionSQLiteHelper(getApplicationContext(), dbName, null, 1);

        btnLogin = findViewById(R.id.btnLogin);
        btnLogin.setOnClickListener(this);

        txtLoginUser = findViewById(R.id.txtLoginUser);
        txtLoginPassword = findViewById(R.id.txtLoginPassword);

        txtNewUser = findViewById(R.id.txtNewUser);
        txtNewUser.setOnClickListener(this);
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
                    if (chekUserPassword(txtLoginUser.getText().toString(), txtLoginPassword.getText().toString())) {
                        intent = new Intent(getApplicationContext(), MainActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putInt("rolId", rolId);
                        bundle.putString("userName", userName);
                        intent.putExtras(bundle);
                        finish();
                        startActivity(intent);
                    } else {
                        Toast.makeText(getApplicationContext(), "Combinacion Usuario/Password Incorrecta!!!", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "Debe llenar todos los Campos", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    private boolean allFilled() {
        return !txtLoginUser.getText().toString().trim().equalsIgnoreCase("") &&
                !txtLoginPassword.getText().toString().trim().equalsIgnoreCase("");
    }

    private boolean chekUserPassword(String user, String password) {
        db = conn.getReadableDatabase();

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

        System.out.println("Aqui: " + query);

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
        return false;
    }
}