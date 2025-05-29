package com.faysal.jobkhujibd2.activity;

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


import com.faysal.jobkhujibd2.R;
import com.faysal.jobkhujibd2.model.User;
import com.faysal.jobkhujibd2.service.ApiService;

import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class AddUserActivity extends AppCompatActivity {

    private EditText textFirstName, textLastName, textPhoneNumber, textGender, textAge;
    private Button btnSave;
    private ApiService apiService;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_add_user);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        textFirstName = findViewById(R.id.textFirstName);
        textLastName = findViewById(R.id.textLastName);
        textPhoneNumber = findViewById(R.id.textPhoneNumber);
        textGender = findViewById(R.id.textGender);
        textAge = findViewById(R.id.textAge);
        btnSave = findViewById(R.id.btnSave);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://10.0.2.2:8081/") // Emulator
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        apiService = retrofit.create(ApiService.class);

        btnSave.setOnClickListener(v -> saveEmployee());
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

    private void saveEmployee() {
        String firstName = textFirstName.getText().toString().trim();
        String lastName = textLastName.getText().toString().trim();
        String phoneNumber = textPhoneNumber.getText().toString().trim();
        String gender = textGender.getText().toString().trim();
        int age = Integer.parseInt(textAge.getText().toString().trim());

        User user = new User();
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setPhoneNumber(phoneNumber);
        user.setGender(gender);
        user.setAge(age);

        Call<User> call = apiService.saveUser(user);

        call.enqueue(new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<User> call, @NonNull Response<User> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(AddUserActivity.this, "Employee saved successfully!", Toast.LENGTH_SHORT).show();
                    clearForm();
                    Intent intent = new Intent(AddUserActivity.this, UserListActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(AddUserActivity.this, "Failed: " + response.message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<User> call, @NonNull Throwable t) {
                Toast.makeText(AddUserActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void clearForm() {
        textFirstName.setText("");
        textLastName.setText("");
        textPhoneNumber.setText("");
        textGender.setText("");
        textAge.setText("");
    }
}
