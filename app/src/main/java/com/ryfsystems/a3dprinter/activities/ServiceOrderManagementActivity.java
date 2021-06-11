package com.ryfsystems.a3dprinter.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.FirebaseApp;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.ryfsystems.a3dprinter.R;
import com.ryfsystems.a3dprinter.models.ServiceOrder;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ServiceOrderManagementActivity extends AppCompatActivity {

    Bundle received;
    Button btnRegisterOrder;
    public ArrayAdapter<String> printersAdapter;

    TextView txtOrderId, txtOrderPrinterSerial, txtOrderDate, lblServiceOrderManagementTitle;
    public RadioButton rbStatNew, rbStatInProcess, rbStatDone, rbStatRejected;

    FirebaseFirestore firebaseFirestore;

    ServiceOrder serviceOrderReceived;

    String userId;
    String userName;
    public List<String> printersList;
    Spinner spPrinterList;
    String printerName = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service_order_management);

        SharedPreferences sh = getSharedPreferences("userPreferences", MODE_PRIVATE);

        userId = sh.getString("userId", "");
        userName = sh.getString("userName", "");

        spPrinterList = findViewById(R.id.spPrinterList);

        txtOrderId = findViewById(R.id.txtOrderId);
        txtOrderPrinterSerial = findViewById(R.id.txtOrderPrinterSerial);
        txtOrderDate = findViewById(R.id.txtOrderDate);
        txtOrderDate.setText(new Date().toString());

        rbStatNew = findViewById(R.id.rbStatNew);
        rbStatInProcess = findViewById(R.id.rbStatInProcess);
        rbStatDone = findViewById(R.id.rbStatDone);
        rbStatRejected = findViewById(R.id.rbStatRejected);

        rbStatNew.setChecked(true);

        setRbState(false);

        lblServiceOrderManagementTitle = findViewById(R.id.lblServiceOrderManagementTitle);

        btnRegisterOrder = findViewById(R.id.btnRegisterOrder);

        initializeFirebase();

        received = getIntent().getExtras();

        CollectionReference printers = firebaseFirestore.collection("Printer");
        printersList = new ArrayList<>();
        printersAdapter = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_spinner_item, printersList);
        printersAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spPrinterList.setAdapter(printersAdapter);
        printers.get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot snapshot : task.getResult()) {
                            String printer = snapshot.getString("pname");
                            printersList.add(printer);
                        }
                        printersAdapter.notifyDataSetChanged();

                        if (received != null) {

                            lblServiceOrderManagementTitle.setText(R.string.actualizar_orden);
                            btnRegisterOrder.setText(R.string.actualizar);

                            serviceOrderReceived = (ServiceOrder) received.getSerializable("serviceOrder");

                            userName = serviceOrderReceived.getUName();
                            txtOrderId.setText(serviceOrderReceived.getOId());
                            txtOrderPrinterSerial.setText(serviceOrderReceived.getPSerial());
                            txtOrderDate.setText(serviceOrderReceived.getODate().toString());

                            setRbState(true);

                            switch (serviceOrderReceived.getOStatus()) {
                                case 1:
                                    rbStatNew.setChecked(true);
                                    break;
                                case 2:
                                    rbStatInProcess.setChecked(true);
                                    break;
                                case 3:
                                    rbStatDone.setChecked(true);
                                    break;
                                case 4:
                                    rbStatRejected.setChecked(true);
                                    break;
                            }

                            int pos = printersAdapter.getPosition(serviceOrderReceived.getPName());

                            spPrinterList.setSelection(pos);
                        }
                    }
                })
                .addOnFailureListener(e -> Toast.makeText(ServiceOrderManagementActivity.this, "Error al Cargar las Impresoras. Additional Data: " + e.getMessage(), Toast.LENGTH_SHORT).show());

        spPrinterList.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                printerName = String.valueOf(parent.getItemAtPosition(position));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void setRbState(boolean state) {
        rbStatNew.setEnabled(state);
        rbStatInProcess.setEnabled(state);
        rbStatDone.setEnabled(state);
        rbStatRejected.setEnabled(state);
    }

    private void initializeFirebase() {
        FirebaseApp.initializeApp(this);
        firebaseFirestore = FirebaseFirestore.getInstance();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void onClick(View v) {
        if (allFilled()) {
            if (received != null) {
                updateOrder(
                        txtOrderId.getText().toString(),
                        txtOrderPrinterSerial.getText().toString());

            } else {
                createOrder(txtOrderPrinterSerial.getText().toString());
            }
        } else {
            Toast.makeText(getApplicationContext(), "Debe llenar todos los Campos", Toast.LENGTH_SHORT).show();
        }
    }

    private void updateOrder(String id, String serial) {

        ServiceOrder serviceOrder = new ServiceOrder();
        serviceOrder.setOId(id);
        serviceOrder.setUName(userName);
        serviceOrder.setPName(printerName);
        serviceOrder.setPSerial(serial);
        serviceOrder.setODate(new Date());
        if (rbStatNew.isChecked()) {
            serviceOrder.setOStatus(1);
        } else if (rbStatInProcess.isChecked()) {
            serviceOrder.setOStatus(2);
        } else if (rbStatDone.isChecked()) {
            serviceOrder.setOStatus(3);
        } else {
            serviceOrder.setOStatus(4);
        }

        DocumentReference documentReference = firebaseFirestore.collection("ServiceOrder").document(serviceOrder.getOId());
        documentReference.set(serviceOrder);

        Toast.makeText(getApplicationContext(), "Orden Actualizada Satisfactoriamente", Toast.LENGTH_SHORT).show();
        startActivity(new Intent(getApplicationContext(), ServiceOrdersActivity.class));
        finish();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void createOrder(String serial) {

        DocumentReference creationReference = firebaseFirestore.collection("ServiceOrder").document();

        ServiceOrder serviceOrder = new ServiceOrder();
        serviceOrder.setOId(creationReference.getId());
        serviceOrder.setPSerial(serial);
        serviceOrder.setPName(printerName);
        serviceOrder.setUName(userName);
        serviceOrder.setODate(new Date());
        serviceOrder.setOStatus(1);

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
        return !txtOrderPrinterSerial.getText().toString().trim().equalsIgnoreCase("");
    }
}