package com.ryfsystems.a3dprinter.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.FirebaseApp;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.ryfsystems.a3dprinter.R;
import com.ryfsystems.a3dprinter.adapters.ServiceOrderAdapter;
import com.ryfsystems.a3dprinter.models.ServiceOrder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ServiceOrdersActivity extends AppCompatActivity implements View.OnClickListener {

    Button btnAddOrder;
    RecyclerView rvOrders;

    List<ServiceOrder> serviceOrderFbList = new ArrayList<>();
    Map<String, String> orderMap = new HashMap<>();
    FirebaseFirestore firebaseFirestore;

    ServiceOrderAdapter serviceOrderAdapter;

    String userId;
    Boolean isAdmin;

    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service_orders);

        SharedPreferences sh = getSharedPreferences("userPreferences", MODE_PRIVATE);

        userId = sh.getString("userId", "");
        isAdmin = sh.getInt("admin", 0) == 1;

        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Espere...");
        progressDialog.setMessage("Cargando Ordenes...");
        progressDialog.setCanceledOnTouchOutside(false);

        btnAddOrder = findViewById(R.id.btnAddOrder);
        btnAddOrder.setOnClickListener(this);

        rvOrders = findViewById(R.id.rvOrders);
        rvOrders.setHasFixedSize(true);
        rvOrders.setLayoutManager(new LinearLayoutManager(this));

        initializeFirebase();

        listFbServiceOrders(isAdmin);

    }

    private void listFbServiceOrders(Boolean isAdmin) {
        progressDialog.show();

        if (isAdmin) {
            firebaseFirestore.collection("ServiceOrder")
                    .orderBy("odate")
                    .get()
                    .addOnCompleteListener(task -> {
                        progressDialog.dismiss();
                        if (task.isSuccessful()) {
                            for (DocumentSnapshot documentSnapshot : task.getResult()) {
                                ServiceOrder serviceOrder = new ServiceOrder(
                                        documentSnapshot.getString("oid"),
                                        documentSnapshot.getString("uid"),
                                        documentSnapshot.getString("pid"),
                                        documentSnapshot.getString("pserial"),
                                        documentSnapshot.getDate("odate")
                                );
                                serviceOrderFbList.add(serviceOrder);
                            }
                        }
                        serviceOrderAdapter = new ServiceOrderAdapter(ServiceOrdersActivity.this, serviceOrderFbList);
                        rvOrders.setAdapter(serviceOrderAdapter);
                    })
                    .addOnFailureListener(e -> {
                        progressDialog.dismiss();
                        Toast.makeText(ServiceOrdersActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    });
        } else {
            firebaseFirestore.collection("ServiceOrder")
                    .orderBy("odate")
                    .whereEqualTo("uid", userId)
                    .get()
                    .addOnCompleteListener(task -> {
                        progressDialog.dismiss();
                        if (task.isSuccessful()) {
                            for (DocumentSnapshot documentSnapshot : task.getResult()) {
                                ServiceOrder serviceOrder = new ServiceOrder(
                                        documentSnapshot.getString("oid"),
                                        documentSnapshot.getString("uid"),
                                        documentSnapshot.getString("pid"),
                                        documentSnapshot.getString("pserial"),
                                        documentSnapshot.getDate("odate")
                                );
                                serviceOrderFbList.add(serviceOrder);
                            }
                        }
                        serviceOrderAdapter = new ServiceOrderAdapter(ServiceOrdersActivity.this, serviceOrderFbList);
                        rvOrders.setAdapter(serviceOrderAdapter);
                    })
                    .addOnFailureListener(e -> {
                        progressDialog.dismiss();
                        Toast.makeText(ServiceOrdersActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    });
        }
    }

    private void initializeFirebase() {
        FirebaseApp.initializeApp(this);
        firebaseFirestore = FirebaseFirestore.getInstance();
    }

    @Override
    public void onClick(View v) {
        Intent i;
        if (v.getId() == R.id.btnAddOrder) {
            i = new Intent(getApplicationContext(), ServiceOrderManagementActivity.class);
            startActivity(i);
            finish();
        }
    }
}