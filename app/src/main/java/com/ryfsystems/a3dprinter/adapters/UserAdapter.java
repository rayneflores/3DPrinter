package com.ryfsystems.a3dprinter.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ryfsystems.a3dprinter.R;
import com.ryfsystems.a3dprinter.activities.UsersActivity;
import com.ryfsystems.a3dprinter.models.User;
import com.ryfsystems.a3dprinter.viewHolders.UserViewHolder;

import java.util.List;

public class UserAdapter extends RecyclerView.Adapter<UserViewHolder> {

    UsersActivity usersActivity;
    List<User> userList;

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
                String userName = userList.get(position).getUUserName();
                String userEmail = userList.get(position).getUEmail();
                String userPhone = userList.get(position).getUPhone();

                Toast.makeText(usersActivity, "Data: " +
                                userName + " | " +
                                userEmail + " | " +
                                userPhone
                        , Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onItemLongClick(View view, int position) {

            }
        });

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull UserViewHolder viewHolder, int i) {
        viewHolder.txtListUserName.setText(userList.get(i).getUName());
        viewHolder.txtListUserEmail.setText(userList.get(i).getUEmail());
        viewHolder.txtListUserPhone.setText(userList.get(i).getUPhone());
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }
}
