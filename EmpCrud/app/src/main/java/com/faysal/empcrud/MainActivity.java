package com.faysal.empcrud;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.faysal.empcrud.model.Employee;
import com.faysal.empcrud.service.ApiService;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    private TextInputEditText editTextDob;
    private TextInputLayout dateLayout;
    private Button save;
    private ApiService apiService;
    private EditText textName, textEmail, textdsignation, numberage, multilineaddress, desimalesalary;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Initialize views
        editTextDob = findViewById(R.id.editTextDob);
        dateLayout = findViewById(R.id.dateLayout);
        textName = findViewById(R.id.textName);
        textEmail = findViewById(R.id.textEmail);
        textdsignation = findViewById(R.id.textDesignation);
        numberage = findViewById(R.id.numberAge);
        multilineaddress = findViewById(R.id.multilineAddress);
        desimalesalary = findViewById(R.id.decimalSalary);
        save = findViewById(R.id.btnSave);

        // Retrofit initialization
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://10.0.2.2:8081/")
//                .baseUrl("http://192.168.0.78:8081/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        apiService = retrofit.create(ApiService.class);

        dateLayout.setEndIconOnClickListener(v -> showDatePicker());
        save.setOnClickListener(v -> saveEmployee());
    }

    private void showDatePicker() {
        final Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        android.app.DatePickerDialog datePickerDialog = new android.app.DatePickerDialog(
                this,
                (view, selectedYear, selectedMonth, selectedDay) -> {
                    String selectedDate = selectedYear + "/" + (selectedMonth + 1) + "/" + selectedDay;
                    editTextDob.setText(selectedDate);
                },
                year, month, day
        );
        datePickerDialog.show();
    }

    private void saveEmployee() {
        String name = textName.getText().toString().trim();
        String email = textEmail.getText().toString().trim();
        String designation = textdsignation.getText().toString().trim();
        int age = Integer.parseInt(numberage.getText().toString().trim());
        String address = multilineaddress.getText().toString().trim();
        double salary = Double.parseDouble(desimalesalary.getText().toString().trim());
        String dobString = editTextDob.getText().toString().trim();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd");
        LocalDate dob = LocalDate.parse(dobString, formatter);


        Employee employee = new Employee();
        employee.setName(name);
        employee.setEmail(email);
        employee.setDesignation(designation);
        employee.setAge(age);
        employee.setAddress(address);
        employee.setDob(dob.toString());
        employee.setSalary(salary);

        Call<Employee> call = apiService.saveEmployee(employee);
        String string = call.toString();
        System.out.println(string);

        call.enqueue(new Callback<Employee>() {
            @Override
            public void onResponse(Call<Employee> call, Response<Employee> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(MainActivity.this, "Employee saved successfully!",
                            Toast.LENGTH_SHORT).show();
                    clearForm();

                    Intent intent = new Intent(MainActivity.this, Employee.class);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(MainActivity.this, "Failed to save employee: " +
                            response.message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Employee> call, Throwable t) {
                Toast.makeText(MainActivity.this, "Error: " + t.getMessage(),
                        Toast.LENGTH_LONG).show();
            }
        });
    }

    private void clearForm() {
        textName.setText("");
        textEmail.setText("");
        textdsignation.setText("");
        numberage.setText("");
        multilineaddress.setText("");
        desimalesalary.setText("");
        editTextDob.setText("");
    }
}
