package com.ryfsystems.a3dprinter.activities;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ryfsystems.a3dprinter.R;
import com.ryfsystems.a3dprinter.adapters.PrinterAdapter;
import com.ryfsystems.a3dprinter.db.ConexionSQLiteHelper;
import com.ryfsystems.a3dprinter.entities.Printer;

import java.util.ArrayList;

import static com.ryfsystems.a3dprinter.utilities.Utilities.TABLE_PRINTER;
import static com.ryfsystems.a3dprinter.utilities.Utilities.dbName;

public class PrintersActivity extends AppCompatActivity implements View.OnClickListener {

    Button btnAddPrinter;

    ArrayList<Printer> printerList;

    ConexionSQLiteHelper conn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_printers);

        btnAddPrinter = findViewById(R.id.btnAddPrinter);
        btnAddPrinter.setOnClickListener(this);

        conn = new ConexionSQLiteHelper(getApplicationContext(), dbName, null, 1);

        RecyclerView rvPrinters = findViewById(R.id.rvPrinters);

        rvPrinters.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        rvPrinters.setLayoutManager(layoutManager);

        queryPrinterList();

        RecyclerView.Adapter mAdapter = new PrinterAdapter(printerList, this);
        rvPrinters.setAdapter(mAdapter);

    }

    private void queryPrinterList() {

        SQLiteDatabase db = conn.getReadableDatabase();

        Printer printer;
        printerList = new ArrayList<>();

        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_PRINTER, null);

        while (cursor.moveToNext()) {
            printer = new Printer();
            printer.setPId(cursor.getInt(0));
            printer.setPName(cursor.getString(1));
            printer.setPBrand(cursor.getString(2));
            printer.setPModel(cursor.getString(3));
            printer.setPImage(cursor.getString(4));

            printerList.add(printer);
        }
    }

    @Override
    public void onClick(View v) {
        Intent i;
        switch (v.getId()) {
            case R.id.btnAddPrinter:
                i = new Intent(getApplicationContext(), PrinterManagementActivity.class);
                finish();
                startActivity(i);
                break;
        }
    }
}