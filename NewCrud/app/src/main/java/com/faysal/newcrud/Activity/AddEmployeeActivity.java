// ✅ Full CRUD - AddEmployeeActivity.java
package com.faysal.newcrud.Activity;


import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;


import com.faysal.newcrud.R;
import com.faysal.newcrud.service.ApiService;
import com.faysal.newcrud.util.ApiClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddEditEmployeeActivity extends AppCompatActivity {

    private EditText editName, editEmail, editDesignation;
    private Button btnSave;
    private ApiService apiService;

    private Employee employee;
    private boolean isEditMode = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit_employee);

        editName = findViewById(R.id.editName);
        editEmail = findViewById(R.id.editEmail);
        editDesignation = findViewById(R.id.editDesignation);
        btnSave = findViewById(R.id.btnSave);

        apiService = ApiClient.getClient().create(ApiService.class);

        if (getIntent().hasExtra("employee")) {
            employee = (Employee) getIntent().getSerializableExtra("employee");
            if (employee != null) {
                isEditMode = true;
                editName.setText(employee.getName());
                editEmail.setText(employee.getEmail());
                editDesignation.setText(employee.getDesignation());
            }
        } else {
            employee = new Employee();
        }

        btnSave.setOnClickListener(v -> saveEmployee());
    }

    private void saveEmployee() {
        String name = editName.getText().toString().trim();
        String email = editEmail.getText().toString().trim();
        String designation = editDesignation.getText().toString().trim();

        if (TextUtils.isEmpty(name) || TextUtils.isEmpty(email) || TextUtils.isEmpty(designation)) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        employee.setName(name);
        employee.setEmail(email);
        employee.setDesignation(designation);

        Call<Employee> call;
        if (isEditMode) {
            call = apiService.updateEmployee(employee.getId(), employee);
        } else {
            call = apiService.addEmployee(employee);
        }

        call.enqueue(new Callback<Employee>() {
            @Override
            public void onResponse(@NonNull Call<Employee> call, @NonNull Response<Employee> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(AddEditEmployeeActivity.this, isEditMode ? "Updated!" : "Added!", Toast.LENGTH_SHORT).show();
                    setResult(RESULT_OK);
                    finish();
                } else {
                    Toast.makeText(AddEditEmployeeActivity.this, "Failed: " + response.message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<Employee> call, @NonNull Throwable t) {
                Toast.makeText(AddEditEmployeeActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
