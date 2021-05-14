package com.ryfsystems.a3dprinter.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.ryfsystems.a3dprinter.R;
import com.ryfsystems.a3dprinter.entities.Printer;

import java.util.List;

import lombok.NonNull;

public class PrinterAdapter extends RecyclerView.Adapter<PrinterAdapter.PrinterViewHolder> implements AdapterView.OnItemClickListener {

    List<Printer> printerList;
    Context context;

    public PrinterAdapter(List<Printer> printerList, Context context) {
        this.printerList = printerList;
        this.context = context;
    }

    @NonNull
    @Override
    public PrinterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.printer_item, parent, false);
        PrinterViewHolder holder = new PrinterViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull PrinterViewHolder holder, int position) {
        holder.txtPrinterName.setText(printerList.get(position).getPName());
        holder.txtPrinterBrand.setText(printerList.get(position).getPBrand());
        holder.txtPrinterModel.setText(printerList.get(position).getPModel());
        Glide.with(this.context)
                .load(printerList.get(position).getPImage())
                .placeholder(R.drawable.ic_launcher_foreground)
                .centerInside()
                .error(R.drawable.ic_launcher_foreground)
                .into(holder.ivPrinterImage);
    }

    @Override
    public int getItemCount() {
        return printerList.size();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Object element = parent.getItemAtPosition(position);


        Toast.makeText(this.context, element.toString(), Toast.LENGTH_SHORT).show();
        /*Printer selected = new Printer();
        selected.setPId();*/
    }


    public static class PrinterViewHolder extends RecyclerView.ViewHolder {
        ImageView ivPrinterImage;
        TextView txtPrinterName, txtPrinterBrand, txtPrinterModel;

        public PrinterViewHolder(@NonNull View itemView) {
            super(itemView);
            ivPrinterImage = itemView.findViewById(R.id.ivPrinterImage);
            txtPrinterName = itemView.findViewById(R.id.txtPrinterName);
            txtPrinterBrand = itemView.findViewById(R.id.txtPrinterBrand);
            txtPrinterModel = itemView.findViewById(R.id.txtPrinterModel);
        }
    }
}
