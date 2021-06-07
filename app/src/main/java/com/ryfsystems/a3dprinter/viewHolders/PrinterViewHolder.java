package com.ryfsystems.a3dprinter.viewHolders;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ryfsystems.a3dprinter.R;

public class PrinterViewHolder extends RecyclerView.ViewHolder {

    public ImageView ivPrinterImage;
    public TextView txtPrinterName, txtPrinterBrand, txtPrinterModel;

    View mView;
    private PrinterViewHolder.ClickListener mClickListener;


    public PrinterViewHolder(@NonNull View itemView) {
        super(itemView);

        mView = itemView;

        itemView.setOnClickListener(v -> mClickListener.onItemClick(v, getAdapterPosition()));
        itemView.setOnLongClickListener(v -> {
            mClickListener.onItemLongClick(v, getAdapterPosition());
            return true;
        });

        ivPrinterImage = itemView.findViewById(R.id.ivPrinterImage);
        txtPrinterName = itemView.findViewById(R.id.txtPrinterName);
        txtPrinterBrand = itemView.findViewById(R.id.txtPrinterBrand);
        txtPrinterModel = itemView.findViewById(R.id.txtPrinterModel);
    }

    public void setOnClickListener(PrinterViewHolder.ClickListener clickListener) {
        mClickListener = clickListener;
    }

    public interface ClickListener {
        void onItemClick(View view, int position);

        void onItemLongClick(View view, int position);
    }
}
