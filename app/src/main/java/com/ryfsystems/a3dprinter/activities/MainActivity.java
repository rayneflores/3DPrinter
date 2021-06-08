package com.ryfsystems.a3dprinter.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.google.firebase.auth.FirebaseAuth;
import com.ryfsystems.a3dprinter.R;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    CardView cvUsers, cvPrinters, cvServiceOrder, cvLogout;
    ImageView ivUsers, ivPrinters;
    TextView tvUsers, tvPrinters, tvLoggedAs;

    Boolean isAdmin;
    String userName;

    Intent i;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SharedPreferences sh = getSharedPreferences("userPreferences", MODE_PRIVATE);

        isAdmin = sh.getInt("admin", 0) == 1;
        userName = sh.getString("userName", "");

        cvUsers = findViewById(R.id.cvUsers);
        cvPrinters = findViewById(R.id.cvPrinters);
        cvServiceOrder = findViewById(R.id.cvServiceOrders);
        cvLogout = findViewById(R.id.cvLogout);

        ivUsers = findViewById(R.id.ivUsers);
        ivPrinters = findViewById(R.id.ivPrinters);

        tvUsers = findViewById(R.id.tvUsers);
        tvPrinters = findViewById(R.id.tvPrinters);
        tvLoggedAs = findViewById(R.id.tvLoggedAs);

        cvUsers.setOnClickListener(this);
        cvPrinters.setOnClickListener(this);
        cvServiceOrder.setOnClickListener(this);
        cvLogout.setOnClickListener(this);

        tvLoggedAs.setText("Usuario: " + userName);

        enableCardsRoles(isAdmin);
    }

    private void enableCardsRoles(Boolean isAdmin) {
        if (!isAdmin) {
            cvUsers.setEnabled(false);
            cvUsers.setElevation(0);
            ivUsers.setImageAlpha(20);
            tvUsers.setAlpha(0.2f);
            cvPrinters.setEnabled(false);
            cvPrinters.setElevation(0);
            ivPrinters.setImageAlpha(20);
            tvPrinters.setAlpha(0.2f);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.cvUsers:
                i = new Intent(getApplicationContext(), UsersActivity.class);
                startActivity(i);
                break;
            case R.id.cvPrinters:
                i = new Intent(getApplicationContext(), PrintersActivity.class);
                startActivity(i);
                break;
            case R.id.cvServiceOrders:
                i = new Intent(getApplicationContext(), ServiceOrdersActivity.class);
                startActivity(i);
                break;
            case R.id.cvLogout:
                FirebaseAuth.getInstance().signOut();
                i = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(i);
                finish();
                break;
        }
    }

    @Override
    public void onBackPressed() {
        FirebaseAuth.getInstance().signOut();
        i = new Intent(getApplicationContext(), LoginActivity.class);
        startActivity(i);
        finish();
    }
}