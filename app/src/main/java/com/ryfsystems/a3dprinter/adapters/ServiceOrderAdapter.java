package com.ryfsystems.a3dprinter.adapters;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.StyleSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ryfsystems.a3dprinter.R;
import com.ryfsystems.a3dprinter.activities.ServiceOrderManagementActivity;
import com.ryfsystems.a3dprinter.activities.ServiceOrdersActivity;
import com.ryfsystems.a3dprinter.models.ServiceOrder;
import com.ryfsystems.a3dprinter.viewHolders.ServiceOrderViewHolder;

import java.util.List;

public class ServiceOrderAdapter extends RecyclerView.Adapter<ServiceOrderViewHolder> {

    ServiceOrdersActivity serviceOrdersActivity;
    List<ServiceOrder> serviceOrderList;

    ServiceOrder serviceOrderSelected;

    public ServiceOrderAdapter(ServiceOrdersActivity serviceOrdersActivity, List<ServiceOrder> serviceOrderList) {
        this.serviceOrdersActivity = serviceOrdersActivity;
        this.serviceOrderList = serviceOrderList;
    }

    @NonNull
    @Override
    public ServiceOrderViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View itemView = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.orders_layout, viewGroup, false);

        ServiceOrderViewHolder viewHolder = new ServiceOrderViewHolder(itemView);

        viewHolder.setOnClickListener(new ServiceOrderViewHolder.ClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                serviceOrderSelected = new ServiceOrder(
                        serviceOrderList.get(position).getOId(),
                        serviceOrderList.get(position).getUId(),
                        serviceOrderList.get(position).getPId(),
                        serviceOrderList.get(position).getPSerial(),
                        serviceOrderList.get(position).getODate()
                );

                Bundle bundle = new Bundle();
                bundle.putSerializable("serviceOrder", serviceOrderSelected);
                Intent intent = new Intent(serviceOrdersActivity, ServiceOrderManagementActivity.class);
                intent.putExtras(bundle);
                serviceOrdersActivity.startActivity(intent);
                serviceOrdersActivity.finish();
            }

            @Override
            public void onItemLongClick(View view, int position) {

            }
        });

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ServiceOrderViewHolder viewHolder, int i) {

        viewHolder.txtListOrderId.setText("Orden: " + serviceOrderList.get(i).getOId());
        SpannableString userId = new SpannableString("Id de Usuario: " + serviceOrderList.get(i).getUId());
        userId.setSpan(new StyleSpan(Typeface.BOLD), 0, 13, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        viewHolder.txtListOrderUser.setText(userId);
        SpannableString printerId = new SpannableString("Id de Impresora: " + serviceOrderList.get(i).getPId());
        printerId.setSpan(new StyleSpan(Typeface.BOLD), 0, 15, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        viewHolder.txtListOrderPrinterName.setText(printerId);
        SpannableString printerSerial = new SpannableString("Serial de Impresora: " + serviceOrderList.get(i).getPSerial());
        printerSerial.setSpan(new StyleSpan(Typeface.BOLD), 0, 19, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        viewHolder.txtListOrderPrinterSerial.setText(printerSerial);
        SpannableString orderDate = new SpannableString("Fecha de la Orden: " + serviceOrderList.get(i).getODate().toString());
        orderDate.setSpan(new StyleSpan(Typeface.BOLD), 0, 17, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        viewHolder.txtListOrderDate.setText(orderDate);
    }

    @Override
    public int getItemCount() {
        return serviceOrderList.size();
    }
}
