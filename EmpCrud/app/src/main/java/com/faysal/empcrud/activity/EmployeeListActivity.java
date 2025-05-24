package com.faysal.empcrud.activity;

import android.os.Bundle;
import android.util.Log;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.faysal.empcrud.R;
import com.faysal.empcrud.adapter.EmployeeAdapter;
import com.faysal.empcrud.model.Employee;
import com.faysal.empcrud.service.ApiService;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class EmployeeListActivity extends AppCompatActivity {

    private static final String TAG = "EmployeeListActivity";

    private RecyclerView recyclerView;

    private EmployeeAdapter adapter;

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

        adapter = new EmployeeAdapter(employeeList);
        recyclerView = findViewById(R.id.employeeRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);


        fetchEmployee();
    }

    private void fetchEmployee() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://10.0.2.2.:8080/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ApiService apiService = retrofit.create(ApiService.class);
        Call<List<Employee>> call = apiService.getAllEmployee();

        call.enqueue(new Callback<List<Employee>>() {
            @Override
            public void onResponse(Call<List<Employee>> call, Response<List<Employee>> response) {
                if (response.isSuccessful()) {
                    List<Employee> employees = response.body();
                    for (Employee emp : employees) {
                        Log.d(TAG, "ID : " + emp.getId() + "Name : " + emp.getName()
                                + "Designation : " + emp.getDesignation());
                    }
                    employeeList.clear();
                    employeeList.addAll(employees);
                    adapter.notifyDataSetChanged();

                } else {
                    Log.e(TAG, "API Response Error" + response.code());
                }
            }

            @Override
            public void onFailure(Call<List<Employee>> call, Throwable t) {
                Log.e(TAG, "API Call Failed" + t.getMessage());
            }
        });
    }

    public List<Employee> getEmployeeList() {
        return employeeList;
    }

    public void setEmployeeList(List<Employee> employeeList) {
        this.employeeList = employeeList;
    }
}