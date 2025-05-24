package com.faysal.empcrud.adapter;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.faysal.empcrud.R;
import com.faysal.empcrud.model.Employee;

import java.util.List;

public class EmployeeAdapter extends RecyclerView.Adapter<EmployeeAdapter.EmployeeViewHolder> {

    private List<Employee> employeeList;

    public EmployeeAdapter(List<Employee> employeeList) {
        this.employeeList = employeeList;
    }

    @NonNull
    @Override
    public EmployeeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater

                .from(parent.getContext())
                .inflate(R.layout.item_employee,parent,false);
        return  new EmployeeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull EmployeeViewHolder holder, int position) {

        Employee employee = employeeList.get(position);
        holder.nameText.setText(employee.getName());
        holder.emailText.setText(employee.getEmail());
        holder.designationText.setText(employee.getDesignation());


        holder.updateButton.setOnClickListener( v-> {
            Log.d( "Update", "Update clicked for" + employee.getName());
        });


        holder.deleteButton.setOnClickListener( v-> {
            Log.d( "Delete", "Delete clicked for" + employee.getName());
        });

    }

    @Override
    public int getItemCount() {
        return employeeList.size();
    }

    public static class EmployeeViewHolder extends RecyclerView.ViewHolder {

        TextView nameText,emailText,designationText;

        Button updateButton,deleteButton;
        public EmployeeViewHolder(@NonNull View itemView) {
            super(itemView);

            nameText = itemView.requireViewById(R.id.nameText);
            emailText = itemView.requireViewById(R.id.emailText);
            designationText = itemView.requireViewById(R.id.designationText);
            updateButton = itemView.requireViewById(R.id.updateButton);
            deleteButton = itemView.requireViewById(R.id.deleteButton);
        }
    }
}
