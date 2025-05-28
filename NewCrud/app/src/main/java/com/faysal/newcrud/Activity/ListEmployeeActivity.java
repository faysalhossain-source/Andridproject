package com.faysal.newcrud.Activity;

import android.os.Bundle;
import android.util.Log;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.faysal.newcrud.R;

import com.faysal.newcrud.adapter.EmployeeAdapter;
import com.faysal.newcrud.service.ApiService;
import com.faysal.newcrud.util.ApiClient;


import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ListEmployeeActivity extends AppCompatActivity {

    private static final String TAG = "EmployeeListActivity";
    private RecyclerView recyclerView;
    private EmployeeAdapter employeeAdapter;
    private List<Employee> employeeList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_employee_list);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        recyclerView = findViewById(R.id.employeeRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        employeeAdapter = new EmployeeAdapter(this, employeeList);
        recyclerView.setAdapter(employeeAdapter);

        fetchEmployees();
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

    private <Employee> void fetchEmployees() {
        ApiService apiService = ApiClient.getApiService();
        Call<List<Employee>> call = apiService.getAllEmployee();

        call.enqueue(new Callback<List<Employee>>() {
            @Override
            public void onResponse(Call<List<Employee>> call, Response<List<Employee>> response) {



            }

            @Override
            public void onFailure(Call<List<Employee>> call, Throwable t) {

            }

            @Override
            public <Employee> void onResponse(@NonNull Call<List<Employee>> call, @NonNull Response<List<Employee>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Employee> employees = response.body();

                    Log.d(TAG, "Fetched " + employees.size() + " employees");

                    DiffUtil.DiffResult result = DiffUtil.calculateDiff(
                            new EmployeeDiffCallback(employeeList, employees));
                    employeeList.clear();
                    employeeList.addAll(employees);
                    result.dispatchUpdatesTo(employeeAdapter);
                } else {
                    Log.e(TAG, "API Response Error: " + response.code());
                }
            }

            @Override
            public <Employee> void onFailure(@NonNull Call<List<Employee>> call, @NonNull Throwable t) {
                Log.e(TAG, "API Call Failed: " + t.getMessage());
            }
        });
    }
}
