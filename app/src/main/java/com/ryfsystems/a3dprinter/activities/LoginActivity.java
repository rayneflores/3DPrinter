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

    Integer rolId = null;
    String userName = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        conn = new ConexionSQLiteHelper(getApplicationContext(), dbName, null, 1);

        btnLogin = findViewById(R.id.btnLogin);
        btnLogin.setOnClickListener(this);

        txtLoginUser = findViewById(R.id.txtLoginUser);
        txtLoginPassword = findViewById(R.id.txtLoginPassword);
    }

    @Override
    public void onClick(View v) {
        if (allFilled()) {
            if (chekUserPassword(txtLoginUser.getText().toString(), txtLoginPassword.getText().toString())) {
                Intent mainIntent;
                mainIntent = new Intent(getApplicationContext(), MainActivity.class);
                Bundle bundle = new Bundle();
                bundle.putInt("rolId", rolId);
                bundle.putString("userName", userName);
                mainIntent.putExtras(bundle);
                finish();
                startActivity(mainIntent);
            } else {
                Toast.makeText(getApplicationContext(), "Combinacion Usuario/Password Incorrecta!!!", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(getApplicationContext(), "Debe llenar todos los Campos", Toast.LENGTH_SHORT).show();
        }

    }

    private boolean allFilled() {
        return !txtLoginUser.getText().toString().trim().equalsIgnoreCase("") &&
                !txtLoginPassword.getText().toString().trim().equalsIgnoreCase("");
    }

    private boolean chekUserPassword(String user, String password) {
        db = conn.getReadableDatabase();

        String query =
                "SELECT " + FIELD_U_ROLE + ", " + FIELD_U_NAME +
                        " FROM " + TABLE_USER +
                        " WHERE " + FIELD_U_USER + " = '" + user + "' " +
                        " AND " + FIELD_U_PASSWORD + " = '" + password + "'";

        Cursor cursor = db.rawQuery(query, null);

        while (cursor.moveToNext()) {
            if (cursor.getInt(0) != 0) {
                rolId = cursor.getInt(0);
                userName = cursor.getString(1);
                return true;
            }
        }
        return false;
    }
}