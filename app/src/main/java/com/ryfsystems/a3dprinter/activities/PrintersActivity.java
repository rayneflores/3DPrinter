package com.ryfsystems.a3dprinter.activities;

import android.app.ProgressDialog;
import android.content.Intent;
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
import com.ryfsystems.a3dprinter.adapters.PrinterAdapter;
import com.ryfsystems.a3dprinter.models.Printer;

import java.util.ArrayList;
import java.util.List;

public class PrintersActivity extends AppCompatActivity implements View.OnClickListener {

    Button btnAddPrinter;
    RecyclerView rvPrinters;

    List<Printer> printerFbList = new ArrayList<>();
    RecyclerView.LayoutManager layoutManager;
    FirebaseFirestore firebaseFirestore;
    PrinterAdapter printerAdapter;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_printers);

        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Espere...");
        progressDialog.setMessage("Cargando Impresoras...");
        progressDialog.setCanceledOnTouchOutside(false);

        btnAddPrinter = findViewById(R.id.btnAddPrinter);
        btnAddPrinter.setOnClickListener(this);

        rvPrinters = findViewById(R.id.rvPrinters);

        rvPrinters.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        rvPrinters.setLayoutManager(layoutManager);

        initializeFirebase();

        listFbPrinters();

    }

    private void initializeFirebase() {
        FirebaseApp.initializeApp(this);
        firebaseFirestore = FirebaseFirestore.getInstance();
    }

    private void listFbPrinters() {
        progressDialog.show();

        firebaseFirestore.collection("Printer")
                .get()
                .addOnCompleteListener(task -> {
                    progressDialog.dismiss();
                    for (DocumentSnapshot documentSnapshot : task.getResult()) {
                        Printer printer = new Printer(
                                documentSnapshot.getString("pid"),
                                documentSnapshot.getString("pname"),
                                documentSnapshot.getString("pbrand"),
                                documentSnapshot.getString("pmodel"),
                                documentSnapshot.getString("pimage")
                        );
                        printerFbList.add(printer);
                    }
                    printerAdapter = new PrinterAdapter(PrintersActivity.this, printerFbList);
                    rvPrinters.setAdapter(printerAdapter);
                })
                .addOnFailureListener(e -> {
                    progressDialog.dismiss();
                    Toast.makeText(PrintersActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                });
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