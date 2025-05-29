package com.faysal.jobkhujibd2.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.faysal.jobkhujibd2.R;
import com.faysal.jobkhujibd2.activity.AddUserActivity;
import com.faysal.jobkhujibd2.model.User;
import com.faysal.jobkhujibd2.service.ApiService;
import com.faysal.jobkhujibd2.util.ApiClient;
import com.google.gson.Gson;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.UserViewHolder> {

    private Context context;
    private List<User> userList;
    private ApiService apiService;

    public UserAdapter(Context context, List<User> userList) {
        this.context = context;
        this.userList = userList;
        this.apiService = ApiClient.getApiService();
    }

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.item_user, parent, false);

        return new UserViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {
        User user = userList.get(position);

        // Display all fields
        holder.firstNameText.setText("First Name: " + user.getFirstName());
        holder.lastNameText.setText("Last Name: " + user.getLastName());
        holder.phoneNumberText.setText("Phone: " + user.getPhoneNumber());
        holder.genderText.setText("Gender: " + user.getGender());
        holder.ageText.setText("Age: " + (user.getAge() != null ? user.getAge().toString() : "N/A"));

        // Update button
        holder.updateButton.setOnClickListener(v -> {
            Log.d("Update", "Update clicked for " + user.getFirstName());
            Intent intent = new Intent(context, AddUserActivity.class);
            intent.putExtra("user", new Gson().toJson(user));
            context.startActivity(intent);
        });

        // Delete button
        holder.deleteButton.setOnClickListener(v -> {
            Log.d("Delete", "Delete clicked for " + user.getFirstName());
            new AlertDialog.Builder(context)
                    .setTitle("Delete")
                    .setMessage("Are you sure you want to delete " + user.getFirstName() + "?")
                    .setPositiveButton("Yes", (dialog, which) -> apiService.deleteUser(user.getId())
                            .enqueue(new Callback<>() {
                                @Override
                                public void onResponse(@NonNull Call<Void> call, @NonNull Response<Void> response) {
                                    if (response.isSuccessful()) {
                                        int adapterPosition = holder.getAdapterPosition();
                                        if (adapterPosition != RecyclerView.NO_POSITION) {
                                            userList.remove(adapterPosition);
                                            notifyItemRemoved(adapterPosition);
                                            notifyItemRangeChanged(adapterPosition, userList.size());
                                            Toast.makeText(context, "Deleted successfully", Toast.LENGTH_SHORT).show();
                                        }
                                    } else {
                                        Toast.makeText(context, "Failed to delete", Toast.LENGTH_SHORT).show();
                                    }
                                }

                                @Override
                                public void onFailure(@NonNull Call<Void> call, @NonNull Throwable t) {
                                    Toast.makeText(context, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            }))
                    .setNegativeButton("Cancel", null)
                    .show();
        });
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    public static class UserViewHolder extends RecyclerView.ViewHolder {
        TextView firstNameText, lastNameText, phoneNumberText, genderText, ageText;
        Button updateButton, deleteButton;

        public UserViewHolder(@NonNull View itemView) {
            super(itemView);
            firstNameText = itemView.findViewById(R.id.firstNameText);
            lastNameText = itemView.findViewById(R.id.textLastName);
            phoneNumberText = itemView.findViewById(R.id.textPhoneNumber);
            genderText = itemView.findViewById(R.id.textGender);
            ageText = itemView.findViewById(R.id.numberAge);
            updateButton = itemView.findViewById(R.id.updateButton);
            deleteButton = itemView.findViewById(R.id.deleteButton);
        }
    }
}
