package com.ryfsystems.a3dprinter.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.FirebaseApp;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.ryfsystems.a3dprinter.R;
import com.ryfsystems.a3dprinter.models.Printer;

public class PrinterManagementActivity extends AppCompatActivity implements View.OnClickListener {

    Bundle received;
    Button btnPrinterRegister;
    EditText txtPrinterName, txtPrinterBrand, txtPrinterModel, txtPrinterUrlPhoto;
    TextView lblTitle;

    String printerId = null;

    FirebaseFirestore firebaseFirestore;

    Printer printerReceived;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_printer_management);

        btnPrinterRegister = findViewById(R.id.btnPrinterRegister);
        txtPrinterName = findViewById(R.id.txtPrinterName);
        txtPrinterBrand = findViewById(R.id.txtPrinterBrand);
        txtPrinterModel = findViewById(R.id.txtPrinterModel);
        txtPrinterUrlPhoto = findViewById(R.id.txtPrinterUrlPhoto);
        lblTitle = findViewById(R.id.lblPrinterManagementTitle);

        initializeFirebase();

        received = getIntent().getExtras();

        if (received != null) {
            lblTitle.setText(R.string.actualizar_impresora);
            btnPrinterRegister.setText(R.string.actualizar);

            printerReceived = (Printer) received.getSerializable("printer");

            printerId = printerReceived.getPId();
            txtPrinterName.setText(printerReceived.getPName());
            txtPrinterBrand.setText(printerReceived.getPBrand());
            txtPrinterModel.setText(printerReceived.getPModel());
            txtPrinterUrlPhoto.setText(printerReceived.getPImage());
        }
    }

    private void initializeFirebase() {
        FirebaseApp.initializeApp(this);
        firebaseFirestore = FirebaseFirestore.getInstance();
    }

    @Override
    public void onClick(View v) {
        if (allFilled()) {
            if (received != null) {
                updatePrinter(
                        printerId,
                        txtPrinterName.getText().toString(),
                        txtPrinterBrand.getText().toString(),
                        txtPrinterModel.getText().toString(),
                        txtPrinterUrlPhoto.getText().toString()
                );
            } else {
                createPrinter(
                        txtPrinterName.getText().toString(),
                        txtPrinterBrand.getText().toString(),
                        txtPrinterModel.getText().toString(),
                        txtPrinterUrlPhoto.getText().toString()
                );
            }
        } else {
            Toast.makeText(getApplicationContext(), "Debe llenar todos los Campos", Toast.LENGTH_SHORT).show();
        }

    }

    private void createPrinter(String name, String brand, String model, String image) {

        DocumentReference creationReference = firebaseFirestore.collection("Printer").document();

        Printer printer = new Printer();
        printer.setPId(creationReference.getId());
        printer.setPName(name);
        printer.setPBrand(brand);
        printer.setPModel(model);
        printer.setPImage(image);

        firebaseFirestore.collection("Printer").document(creationReference.getId())
                .set(printer)
                .addOnSuccessListener(documentReference -> {
                    Toast.makeText(getApplicationContext(), "Impresora Registrada Satisfactoriamente", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(getApplicationContext(), PrintersActivity.class));
                    finish();
                })
                .addOnFailureListener(e -> Toast.makeText(getApplicationContext(), "No se pudieron registrar los datos de la Impresora. Additional data: " + e.getMessage(), Toast.LENGTH_SHORT).show());
    }

    private boolean allFilled() {
        return
                !txtPrinterName.getText().toString().trim().equalsIgnoreCase("") &&
                        !txtPrinterBrand.getText().toString().trim().equalsIgnoreCase("") &&
                        !txtPrinterModel.getText().toString().trim().equalsIgnoreCase("") &&
                        !txtPrinterUrlPhoto.getText().toString().trim().equalsIgnoreCase("");
    }

    private void updatePrinter(String id, String name, String brand, String model, String image) {

        Printer printer = new Printer(id, name, brand, model, image);

        DocumentReference documentReference = firebaseFirestore.collection("Printer").document(printer.getPId());
        documentReference.set(printer);

        Toast.makeText(getApplicationContext(), "Impresora Actualizada Satisfactoriamente", Toast.LENGTH_SHORT).show();
        startActivity(new Intent(getApplicationContext(), PrintersActivity.class));
        finish();
    }
}