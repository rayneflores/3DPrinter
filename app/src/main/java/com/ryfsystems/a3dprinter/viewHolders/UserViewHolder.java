package com.ryfsystems.a3dprinter.viewHolders;

import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ryfsystems.a3dprinter.R;

public class UserViewHolder extends RecyclerView.ViewHolder {

    public TextView txtListUserId, txtListUserName, txtListUserPassword, txtListUserEmail, txtListUserPhone, txtListUserRole;
    public CheckBox chkListUserIsAdmin;

    View mView;
    private UserViewHolder.ClickListener mClickListener;


    public UserViewHolder(@NonNull View itemView) {
        super(itemView);

        mView = itemView;

        itemView.setOnClickListener(v -> mClickListener.onItemClick(v, getAdapterPosition()));
        itemView.setOnLongClickListener(v -> {
            mClickListener.onItemLongClick(v, getAdapterPosition());
            return true;
        });

        txtListUserId = itemView.findViewById(R.id.txtListUserId);
        txtListUserName = itemView.findViewById(R.id.txtListUserName);
        txtListUserPassword = itemView.findViewById(R.id.txtListUserPassword);
        txtListUserEmail = itemView.findViewById(R.id.txtListUserEmail);
        txtListUserPhone = itemView.findViewById(R.id.txtListUserPhone);
        chkListUserIsAdmin = itemView.findViewById(R.id.chkListUserIsAdmin);
    }

    public void setOnClickListener(UserViewHolder.ClickListener clickListener) {
        mClickListener = clickListener;
    }

    public interface ClickListener {
        void onItemClick(View view, int position);
        void onItemLongClick(View view, int position);
    }
}
