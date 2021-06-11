package com.ryfsystems.a3dprinter.viewHolders;

import android.view.View;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ryfsystems.a3dprinter.R;

public class ServiceOrderViewHolder extends RecyclerView.ViewHolder {

    public TextView txtListOrderId, txtListOrderUserName, txtListOrderPrinterName, txtListOrderPrinterSerial, txtListOrderDate;
    public RadioButton rbStatNew, rbStatInProcess, rbStatDone, rbStatRejected;

    View mView;
    private ServiceOrderViewHolder.ClickListener mClickListener;

    public ServiceOrderViewHolder(@NonNull View itemView) {
        super(itemView);

        mView = itemView;

        itemView.setOnClickListener(v -> mClickListener.onItemClick(v, getAdapterPosition()));
        itemView.setOnLongClickListener(v -> {
            mClickListener.onItemLongClick(v, getAdapterPosition());
            return true;
        });

        txtListOrderId = itemView.findViewById(R.id.txtListOrderId);
        txtListOrderUserName = itemView.findViewById(R.id.txtListOrderUserName);
        txtListOrderPrinterName = itemView.findViewById(R.id.txtListOrderPrinterName);
        txtListOrderPrinterSerial = itemView.findViewById(R.id.txtListOrderPrinterSerial);
        txtListOrderDate = itemView.findViewById(R.id.txtListOrderDate);
        rbStatNew = itemView.findViewById(R.id.rbStatNew);
        rbStatInProcess = itemView.findViewById(R.id.rbStatInProcess);
        rbStatDone = itemView.findViewById(R.id.rbStatDone);
        rbStatRejected = itemView.findViewById(R.id.rbStatRejected);
    }

    public void setOnClickListener(ServiceOrderViewHolder.ClickListener clickListener) {
        mClickListener = clickListener;
    }

    public interface ClickListener {
        void onItemClick(View view, int position);

        void onItemLongClick(View view, int position);
    }
}
