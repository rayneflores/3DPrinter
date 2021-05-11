package com.ryfsystems.a3dprinter.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.ryfsystems.a3dprinter.R;
import com.ryfsystems.a3dprinter.db.ConexionSQLiteHelper;

import static com.ryfsystems.a3dprinter.utilities.Utilities.dbName;
import static com.ryfsystems.a3dprinter.utilities.Utilities.dbVersion;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    CardView cvUsers, cvPrinters, cvServiceOrder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        new ConexionSQLiteHelper(getApplicationContext(), dbName, null, dbVersion);

        cvUsers = findViewById(R.id.cvUsers);
        cvPrinters = findViewById(R.id.cvPrinters);
        cvServiceOrder = findViewById(R.id.cvServiceOrders);

        cvUsers.setOnClickListener(this);
        cvPrinters.setOnClickListener(this);
        cvServiceOrder.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Intent i;
        switch (v.getId()) {
            case R.id.cvUsers:
                i = new Intent(getApplicationContext(), UsersActivity.class);
                startActivity(i);
                break;
        }
    }
}