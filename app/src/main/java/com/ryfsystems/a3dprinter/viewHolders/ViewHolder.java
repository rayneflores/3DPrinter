package com.ryfsystems.a3dprinter.viewHolders;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ryfsystems.a3dprinter.R;

public class ViewHolder extends RecyclerView.ViewHolder {

    public TextView txtListUserName, txtListUserEmail, txtListUserPhone;

    View mView;
    private ViewHolder.ClickListener mClickListener;


    public ViewHolder(@NonNull View itemView) {
        super(itemView);

        mView = itemView;

        itemView.setOnClickListener(v -> mClickListener.onItemClick(v, getAdapterPosition()));
        itemView.setOnLongClickListener(v -> {
            mClickListener.onItemLongClick(v, getAdapterPosition());
            return true;
        });

        txtListUserName = itemView.findViewById(R.id.txtListUserName);
        txtListUserEmail = itemView.findViewById(R.id.txtListUserEmail);
        txtListUserPhone = itemView.findViewById(R.id.txtListUserPhone);
    }

    public void setOnClickListener(ViewHolder.ClickListener clickListener) {
        mClickListener = clickListener;
    }

    public interface ClickListener {
        void onItemClick(View view, int position);

        void onItemLongClick(View view, int position);
    }
}
