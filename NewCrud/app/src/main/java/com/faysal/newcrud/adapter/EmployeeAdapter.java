// EmployeeAdapter.java
package com.faysal.newcrud.adapter;




import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.employeecrud.R;
import com.example.employeecrud.model.Employee;
import com.example.employeecrud.ui.AddEditEmployeeActivity;

import java.util.List;

public class EmployeeAdapter extends RecyclerView.Adapter<EmployeeAdapter.EmployeeViewHolder> {

    private List<Employee> employeeList;
    private Context context;

    public EmployeeAdapter(Context context, List<Employee> employees) {
        this.context = context;
        this.employeeList = employees;
    }

    @NonNull
    @Override
    public EmployeeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_employee, parent, false);
        return new EmployeeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull EmployeeViewHolder holder, int position) {
        Employee emp = employeeList.get(position);
        holder.name.setText(emp.getName());
        holder.email.setText(emp.getEmail());
        holder.designation.setText(emp.getDesignation());

        holder.btnEdit.setOnClickListener(v -> {
            Intent intent = new Intent(context, AddEditEmployeeActivity.class);
            intent.putExtra("employee", emp);
            context.startActivity(intent);
        });

        holder.btnDelete.setOnClickListener(v -> {
            if (context instanceof EmployeeListActivity) {
                ((EmployeeListActivity) context).deleteEmployee(emp.getId(), position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return employeeList.size();
    }

    public static class EmployeeViewHolder extends RecyclerView.ViewHolder {
        TextView name, email, designation;
        Button btnEdit, btnDelete;

        public EmployeeViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.textName);
            email = itemView.findViewById(R.id.textEmail);
            designation = itemView.findViewById(R.id.textDesignation);
            btnEdit = itemView.findViewById(R.id.btnEdit);
            btnDelete = itemView.findViewById(R.id.btnDelete);
        }
    }
}
