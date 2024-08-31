package com.example.barberia;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.barberia.API.ApiClient;
import com.example.barberia.API.ApiInterface;
import com.example.barberia.API.ApiResponse;
import com.example.barberia.model.Customer;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterActivity extends AppCompatActivity {
    private EditText firstNameEditText, lastNameEditText, emailEditText, phoneEditText, passwordEditText;
    private Button registerButton;
    private ApiInterface apiInterface;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        firstNameEditText = findViewById(R.id.first_name_edit_text);
        lastNameEditText = findViewById(R.id.last_name_edit_text);
        emailEditText = findViewById(R.id.email_edit_text);
        phoneEditText = findViewById(R.id.phone_edit_text);
        passwordEditText = findViewById(R.id.password_edit_text);
        registerButton = findViewById(R.id.register_button);

        apiInterface = ApiClient.getClient().create(ApiInterface.class);

        registerButton.setOnClickListener(v -> register());
    }

    private void register() {
        String firstName = firstNameEditText.getText().toString();
        String lastName = lastNameEditText.getText().toString();
        String email = emailEditText.getText().toString();
        String phone = phoneEditText.getText().toString();
        String password = passwordEditText.getText().toString();

        Customer newCustomer = new Customer();
        newCustomer.setFirst_name(firstName);
        newCustomer.setLast_name(lastName);
        newCustomer.setEmail(email);
        newCustomer.setPhone_number(phone);
        newCustomer.setPassword(password);

        Call<ApiResponse<Customer>> call = apiInterface.createCustomer(newCustomer);
        call.enqueue(new Callback<ApiResponse<Customer>>() {
            @Override
            public void onResponse(Call<ApiResponse<Customer>> call, Response<ApiResponse<Customer>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Toast.makeText(RegisterActivity.this, "Registro exitoso", Toast.LENGTH_SHORT).show();
                    finish(); // Volver a la pantalla de login
                } else {
                    Toast.makeText(RegisterActivity.this, "Error al registrar", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<Customer>> call, Throwable t) {
                Toast.makeText(RegisterActivity.this, "Error de red", Toast.LENGTH_SHORT).show();
            }
        });
    }
}