package com.faysal.newcrud.Activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;


import com.faysal.newcrud.R;

import com.faysal.newcrud.service.ApiService;
import com.faysal.newcrud.util.ApiClient;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.Gson;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddEmployeeActivity extends AppCompatActivity {

    private TextInputEditText editTextDob;
    private TextInputLayout dateLayout;
    private EditText textName, textEmail, textDesignation, numberAge, multilineAddress, decimalSalary;
    private Button btnSave;

    private int employeeId = -1;
    private boolean isEditMode = false;
    private ApiService apiService = ApiClient.getApiService();


    @SuppressLint({"MissingInflatedId", "RestrictedApi"})
    @Override
    protected <Employee> void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_add_employee);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(true);
        }

        // View binding
        editTextDob = findViewById(R.id.editTextDob);
        dateLayout = findViewById(R.id.datelayout);
        textName = findViewById(R.id.textName);
        textEmail = findViewById(R.id.textEmail);
        textDesignation = findViewById(R.id.textDesignation);
        numberAge = findViewById(R.id.NumberAge);
        multilineAddress = findViewById(R.id.multilineAddress);
        decimalSalary = findViewById(R.id.decimalSalary);
        btnSave = findViewById(R.id.btnSave);

        Intent intent = getIntent();
        if (intent.hasExtra("employee")) {
            Employee employee = new Gson().fromJson(intent.getStringExtra("employee"), Employee.class);
            employeeId = employee.getClass().getModifiers();

            textName.setText(employee.getName());
            textEmail.setText(employee.getEmail());
            textDesignation.setText(employee.getDesignation());
            numberAge.setText(String.valueOf(employee.getAge()));
            multilineAddress.setText(employee.getAddress());
            editTextDob.setText(employee.getDob());
            decimalSalary.setText(String.valueOf(employee.getSalary()));

            btnSave.setText(R.string.update);
            isEditMode = true;
        }

        btnSave.setOnClickListener(v -> saveOrUpdateEmployee());
        dateLayout.setEndIconOnClickListener(v -> showMaterialDatePicker());
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

    private void showMaterialDatePicker() {
        MaterialDatePicker<Long> datePicker = MaterialDatePicker.Builder.datePicker()
                .setTitleText("Select Date of Birth")
                .setSelection(MaterialDatePicker.todayInUtcMilliseconds())
                .build();

        datePicker.show(getSupportFragmentManager(), "DOB_PICKER");

        datePicker.addOnPositiveButtonClickListener(selection -> {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
            String dob = sdf.format(new Date(selection));
            editTextDob.setText(dob);
        });
    }

    private void saveOrUpdateEmployee() {
        String name = textName.getText().toString().trim();
        String email = textEmail.getText().toString().trim();
        String designation = textDesignation.getText().toString().trim();
        int age = Integer.parseInt(numberAge.getText().toString().trim());
        String address = multilineAddress.getText().toString().trim();
        String dobString = editTextDob.getText().toString().trim();
        double salary = Double.parseDouble(decimalSalary.getText().toString().trim());

        Employee employee = new Employee();
        if (isEditMode) {
            employee.setId(employeeId);
        }

        employee.setName(name);
        employee.setEmail(email);
        employee.setDesignation(designation);
        employee.setAge(age);
        employee.setAddress(address);
        employee.setDob(dobString);
        employee.setSalary(salary);

        Call<Employee> call = isEditMode
                ? apiService.updateEmployee(employeeId, employee)
                : apiService.saveEmployee(employee);

        call.enqueue(new Callback<Employee>() {
            @Override
            public void onResponse(@NonNull Call<Employee> call, @NonNull Response<Employee> response) {
                if (response.isSuccessful()) {
                    String message = isEditMode ? "Employee Updated Successfully!" : "Employee Saved Successfully!";
                    Toast.makeText(AddEmployeeActivity.this, message, Toast.LENGTH_SHORT).show();
                    clearForm();
                    startActivity(new Intent(AddEmployeeActivity.this, EmployeeListActivity.class));
                    finish();
                } else {
                    Toast.makeText(AddEmployeeActivity.this, "Failed to save employee: " + response.message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<Employee> call, @NonNull Throwable t) {
                Toast.makeText(AddEmployeeActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void clearForm() {
        textName.setText("");
        textEmail.setText("");
        textDesignation.setText("");
        numberAge.setText("");
        multilineAddress.setText("");
        editTextDob.setText("");
        decimalSalary.setText("");
    }
}
