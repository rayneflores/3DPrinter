package com.ryfsystems.a3dprinter.activities;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.ryfsystems.a3dprinter.R;
import com.ryfsystems.a3dprinter.db.ConexionSQLiteHelper;

import static com.ryfsystems.a3dprinter.utilities.Utilities.FIELD_P_BRAND;
import static com.ryfsystems.a3dprinter.utilities.Utilities.FIELD_P_ID;
import static com.ryfsystems.a3dprinter.utilities.Utilities.FIELD_P_IMAGE;
import static com.ryfsystems.a3dprinter.utilities.Utilities.FIELD_P_MODEL;
import static com.ryfsystems.a3dprinter.utilities.Utilities.FIELD_P_NAME;
import static com.ryfsystems.a3dprinter.utilities.Utilities.TABLE_PRINTER;
import static com.ryfsystems.a3dprinter.utilities.Utilities.dbName;
import static com.ryfsystems.a3dprinter.utilities.Utilities.dbVersion;

public class PrinterManagementActivity extends AppCompatActivity implements View.OnClickListener {

    Bundle received;
    Button btnPrinterRegister;
    ConexionSQLiteHelper conn;
    EditText txtPrinterName, txtPrinterBrand, txtPrinterModel, txtPrinterUrlPhoto;

    Integer printerId = null;

    SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_printer_management);

        btnPrinterRegister = findViewById(R.id.btnPrinterRegister);
        txtPrinterName = findViewById(R.id.txtPrinterName);
        txtPrinterBrand = findViewById(R.id.txtPrinterBrand);
        txtPrinterModel = findViewById(R.id.txtPrinterModel);
        txtPrinterUrlPhoto = findViewById(R.id.txtPrinterUrlPhoto);

        conn = new ConexionSQLiteHelper(getApplicationContext(), dbName, null, dbVersion);
    }

    @Override
    public void onClick(View v) {
        if (allFilled()) {
            if (received != null) {
                updatePrinter();
            } else {
                createPrinter();
            }
        } else {
            Toast.makeText(getApplicationContext(), "Debe llenar todos los Campos", Toast.LENGTH_SHORT).show();
        }

    }

    private void createPrinter() {
        db = conn.getWritableDatabase();

        String insert = "INSERT INTO " +
                TABLE_PRINTER + " (" +
                FIELD_P_NAME + ", " +
                FIELD_P_BRAND + ", " +
                FIELD_P_MODEL + ", " +
                FIELD_P_IMAGE + ") VALUES ('" +
                txtPrinterName.getText().toString() + "', '" +
                txtPrinterBrand.getText().toString() + "', '" +
                txtPrinterModel.getText().toString() + "', '" +
                txtPrinterUrlPhoto.getText().toString() + "')";
        db.execSQL(insert);
        Toast.makeText(getApplicationContext(), "Impresora Registrada Satisfactoriamente", Toast.LENGTH_SHORT).show();
        db.close();
        finish();
        startActivity(new Intent(getApplicationContext(), PrintersActivity.class));
    }

    private boolean allFilled() {
        return
                !txtPrinterName.getText().toString().trim().equalsIgnoreCase("") &&
                        !txtPrinterBrand.getText().toString().trim().equalsIgnoreCase("") &&
                        !txtPrinterModel.getText().toString().trim().equalsIgnoreCase("") &&
                        !txtPrinterUrlPhoto.getText().toString().trim().equalsIgnoreCase("");
    }

    private void updatePrinter() {
        db = conn.getWritableDatabase();

        String update = "UPDATE " +
                TABLE_PRINTER + " SET " +
                FIELD_P_NAME + "='" + txtPrinterName.getText().toString() + "='" +
                FIELD_P_BRAND + "='" + txtPrinterBrand.getText().toString() + "='" +
                FIELD_P_MODEL + "='" + txtPrinterModel.getText().toString() + "='" +
                FIELD_P_IMAGE + "='" + txtPrinterUrlPhoto.getText().toString() + "' WHERE " +
                FIELD_P_ID + " = " + printerId;
        db.execSQL(update);
        Toast.makeText(getApplicationContext(), "Impresora Actualizada Satisfactoriamente", Toast.LENGTH_SHORT).show();
        db.close();
        finish();
        startActivity(new Intent(getApplicationContext(), PrintersActivity.class));
    }
}