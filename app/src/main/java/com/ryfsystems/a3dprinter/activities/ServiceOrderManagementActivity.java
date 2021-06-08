package com.ryfsystems.a3dprinter.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.FirebaseApp;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.ryfsystems.a3dprinter.R;
import com.ryfsystems.a3dprinter.models.ServiceOrder;

import java.util.Date;

public class ServiceOrderManagementActivity extends AppCompatActivity {

    Bundle received;
    Button btnRegisterOrder;

    TextView txtOrderPrinterSerial, txtOrderDate, lblServiceOrderManagementTitle;

    FirebaseFirestore firebaseFirestore;

    ServiceOrder serviceOrderReceived;

    String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service_order_management);

        SharedPreferences sh = getSharedPreferences("userPreferences", MODE_PRIVATE);

        userId = sh.getString("userId", "");

        txtOrderPrinterSerial = findViewById(R.id.txtOrderPrinterSerial);
        txtOrderDate = findViewById(R.id.txtOrderDate);
        txtOrderDate.setText(new Date().toString());

        lblServiceOrderManagementTitle = findViewById(R.id.lblServiceOrderManagementTitle);

        btnRegisterOrder = findViewById(R.id.btnRegisterOrder);

        initializeFirebase();

        received = getIntent().getExtras();

        if (received != null) {
            lblServiceOrderManagementTitle.setText(R.string.actualizar_orden);
            btnRegisterOrder.setText(R.string.actualizar);

            serviceOrderReceived = (ServiceOrder) received.getSerializable("serviceOrder");
        }
    }

    private void initializeFirebase() {
        FirebaseApp.initializeApp(this);
        firebaseFirestore = FirebaseFirestore.getInstance();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void onClick(View v) {
        if (allFilled()) {
            if (received != null) {

            } else {
                createOrder(
                        txtOrderPrinterSerial.getText().toString()
                );
            }
        } else {
            Toast.makeText(getApplicationContext(), "Debe llenar todos los Campos", Toast.LENGTH_SHORT).show();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void createOrder(String serial) {

        DocumentReference creationReference = firebaseFirestore.collection("ServiceOrder").document();

        ServiceOrder serviceOrder = new ServiceOrder();
        serviceOrder.setOId(creationReference.getId());
        serviceOrder.setPSerial(serial);
        serviceOrder.setUId(userId);
        serviceOrder.setODate(new Date());

        firebaseFirestore.collection("ServiceOrder").document(creationReference.getId())
                .set(serviceOrder)
                .addOnSuccessListener(documentReference -> {
                    Toast.makeText(getApplicationContext(), "Orden Registrada Satisfactoriamente", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(getApplicationContext(), ServiceOrdersActivity.class));
                    finish();
                })
                .addOnFailureListener(e -> Toast.makeText(getApplicationContext(), "No se pudieron registrar los datos de la Orden. Additional data: " + e.getMessage(), Toast.LENGTH_SHORT).show());
    }

    private boolean allFilled() {
        return
                !txtOrderDate.getText().toString().trim().equalsIgnoreCase("") &&
                        !txtOrderPrinterSerial.getText().toString().trim().equalsIgnoreCase("");
    }
}