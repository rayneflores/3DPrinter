package com.ryfsystems.a3dprinter.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.ryfsystems.a3dprinter.R;
import com.ryfsystems.a3dprinter.db.ConexionSQLiteHelper;

import static com.ryfsystems.a3dprinter.utilities.Utilities.dbName;
import static com.ryfsystems.a3dprinter.utilities.Utilities.dbVersion;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    Bundle received;
    CardView cvUsers, cvPrinters, cvServiceOrder;
    ImageView ivUsers, ivPrinters;
    TextView tvUsers, tvPrinters, tvLoggedAs;

    Long rolId;
    String userName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        new ConexionSQLiteHelper(getApplicationContext(), dbName, null, dbVersion);

        received = getIntent().getExtras();

        if (received != null) {
            rolId = received.getLong("rolId");
            userName = received.getString("userName");
        }


        cvUsers = findViewById(R.id.cvUsers);
        cvPrinters = findViewById(R.id.cvPrinters);
        cvServiceOrder = findViewById(R.id.cvServiceOrders);

        ivUsers = findViewById(R.id.ivUsers);
        ivPrinters = findViewById(R.id.ivPrinters);

        tvUsers = findViewById(R.id.tvUsers);
        tvPrinters = findViewById(R.id.tvPrinters);
        tvLoggedAs = findViewById(R.id.tvLoggedAs);

        cvUsers.setOnClickListener(this);
        cvPrinters.setOnClickListener(this);
        cvServiceOrder.setOnClickListener(this);

        tvLoggedAs.setText("Usuario: " + userName);

        enableCardsRoles(Math.toIntExact(rolId));
    }

    private void enableCardsRoles(Integer rolId) {
        switch (rolId) {
            case 2:
                cvUsers.setEnabled(false);
                cvUsers.setElevation(0);
                ivUsers.setImageAlpha(20);
                tvUsers.setAlpha(0.2f);
                cvPrinters.setEnabled(false);
                cvPrinters.setElevation(0);
                ivPrinters.setImageAlpha(20);
                tvPrinters.setAlpha(0.2f);
                break;
        }
    }

    @Override
    public void onClick(View v) {
        Intent i;
        switch (v.getId()) {
            case R.id.cvUsers:
                i = new Intent(getApplicationContext(), UsersActivity.class);
                startActivity(i);
                break;
            case R.id.cvPrinters:
                i = new Intent(getApplicationContext(), PrintersActivity.class);
                startActivity(i);
                break;
        }
    }
}