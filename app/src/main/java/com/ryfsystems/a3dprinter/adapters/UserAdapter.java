package com.ryfsystems.a3dprinter.adapters;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

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
                        userList.get(position).getUIsAdmin()
                );

                Bundle bundle = new Bundle();
                bundle.putSerializable("user", userSelected);
                Intent intent = new Intent(usersActivity, UserManagementActivity.class);
                intent.putExtras(bundle);
                usersActivity.startActivity(intent);
                usersActivity.finish();

                /*Toast.makeText(usersActivity,
                        "Data: " + userList.get(position).getUIsAdmin(),
                        Toast.LENGTH_SHORT).show();*/
            }

            @Override
            public void onItemLongClick(View view, int position) {

            }
        });

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull UserViewHolder viewHolder, int i) {
        boolean checked = false;
        try {
            checked = userList.get(i).getUIsAdmin() == 1L;
        } catch (Exception e) {
            Toast.makeText(usersActivity, e.getMessage(), Toast.LENGTH_SHORT).show();
        }


        viewHolder.txtListUserId.setText(userList.get(i).getUId());
        viewHolder.txtListUserName.setText(userList.get(i).getUName());
        viewHolder.txtListUserPassword.setText(userList.get(i).getUPassword());
        viewHolder.txtListUserEmail.setText("Email: " + userList.get(i).getUEmail());
        viewHolder.txtListUserPhone.setText("Telefono: " + userList.get(i).getUPhone());
        viewHolder.chkListUserIsAdmin.setChecked(checked);
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }
}
