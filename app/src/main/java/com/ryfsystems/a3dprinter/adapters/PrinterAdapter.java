package com.ryfsystems.a3dprinter.adapters;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.ryfsystems.a3dprinter.R;
import com.ryfsystems.a3dprinter.activities.PrinterManagementActivity;
import com.ryfsystems.a3dprinter.activities.PrintersActivity;
import com.ryfsystems.a3dprinter.models.Printer;
import com.ryfsystems.a3dprinter.viewHolders.PrinterViewHolder;

import java.util.List;

public class PrinterAdapter extends RecyclerView.Adapter<PrinterViewHolder> {

    PrintersActivity printersActivity;
    List<Printer> printerList;

    Printer printerSelected;

    public PrinterAdapter(PrintersActivity printersActivity, List<Printer> printerList) {
        this.printersActivity = printersActivity;
        this.printerList = printerList;
    }

    @NonNull
    @Override
    public PrinterViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View itemView = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.printer_item, viewGroup, false);

        PrinterViewHolder viewHolder = new PrinterViewHolder(itemView);

        viewHolder.setOnClickListener(new PrinterViewHolder.ClickListener() {
            @Override
            public void onItemClick(View view, int position) {

                printerSelected = new com.ryfsystems.a3dprinter.models.Printer(
                        printerList.get(position).getPId(),
                        printerList.get(position).getPName(),
                        printerList.get(position).getPBrand(),
                        printerList.get(position).getPModel(),
                        printerList.get(position).getPImage()
                );

                Bundle bundle = new Bundle();
                bundle.putSerializable("printer", printerSelected);
                Intent intent = new Intent(printersActivity, PrinterManagementActivity.class);
                intent.putExtras(bundle);
                printersActivity.startActivity(intent);
                printersActivity.finish();
            }

            @Override
            public void onItemLongClick(View view, int position) {

            }
        });

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull PrinterViewHolder viewHolder, int i) {
        viewHolder.txtPrinterName.setText(printerList.get(i).getPName());
        viewHolder.txtPrinterBrand.setText(printerList.get(i).getPBrand());
        viewHolder.txtPrinterModel.setText(printerList.get(i).getPModel());
        Glide.with(printersActivity)
                .load(printerList.get(i).getPImage())
                .placeholder(R.drawable.ic_launcher_foreground)
                .centerInside()
                .error(R.drawable.ic_launcher_foreground)
                .into(viewHolder.ivPrinterImage);
    }

    @Override
    public int getItemCount() {
        return printerList.size();
    }
}
