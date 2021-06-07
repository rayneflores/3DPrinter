package com.ryfsystems.a3dprinter.adapters;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ryfsystems.a3dprinter.R;
import com.ryfsystems.a3dprinter.activities.UserManagementActivity;
import com.ryfsystems.a3dprinter.activities.UsersActivity;
import com.ryfsystems.a3dprinter.models.User;
import com.ryfsystems.a3dprinter.viewHolders.UserViewHolder;

import java.util.List;

public class UserAdapter extends RecyclerView.Adapter<UserViewHolder> {

    UsersActivity usersActivity;
    List<User> userList;

    User userSelected;

    public UserAdapter(UsersActivity usersActivity, List<User> userList) {
        this.usersActivity = usersActivity;
        this.userList = userList;
    }

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View itemView = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.users_layout, viewGroup, false);

        UserViewHolder viewHolder = new UserViewHolder(itemView);

        viewHolder.setOnClickListener(new UserViewHolder.ClickListener() {
            @Override
            public void onItemClick(View view, int position) {

                userSelected = new User(
                        userList.get(position).getUId(),
                        userList.get(position).getUName(),
                        userList.get(position).getUPassword(),
                        userList.get(position).getUEmail(),
                        userList.get(position).getUPhone(),
                        userList.get(position).getUAdmin()
                );

                Bundle bundle = new Bundle();
                bundle.putSerializable("user", userSelected);
                Intent intent = new Intent(usersActivity, UserManagementActivity.class);
                intent.putExtras(bundle);
                usersActivity.startActivity(intent);
                usersActivity.finish();
            }

            @Override
            public void onItemLongClick(View view, int position) {

            }
        });

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull UserViewHolder viewHolder, int i) {

        viewHolder.txtListUserId.setText(userList.get(i).getUId());
        viewHolder.txtListUserName.setText(userList.get(i).getUName());
        viewHolder.txtListUserPassword.setText(userList.get(i).getUPassword());
        viewHolder.txtListUserEmail.setText("Email: " + userList.get(i).getUEmail());
        viewHolder.txtListUserPhone.setText("Telefono: " + userList.get(i).getUPhone());
        viewHolder.chkListUserIsAdmin.setChecked(userList.get(i).getUAdmin() == 1L);
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }
}
