package com.example.barberia;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.example.barberia.API.ApiClient;
import com.example.barberia.API.ApiInterface;
import com.example.barberia.API.ApiResponse;
import com.example.barberia.model.Customer;
import com.example.barberia.model.LoginRequest;
import com.example.barberia.model.LoginResponse;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import java.util.List;

public class LoginActivity extends AppCompatActivity {
    private TextInputEditText emailEditText, passwordEditText;
    private MaterialButton loginButton;
    private TextView registerText;
    private ApiInterface apiInterface;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        emailEditText = findViewById(R.id.email_edit_text);
        passwordEditText = findViewById(R.id.password_edit_text);
        loginButton = findViewById(R.id.login_button);
        registerText = findViewById(R.id.register_text);

        apiInterface = ApiClient.getClient().create(ApiInterface.class);
        sharedPreferences = getSharedPreferences("BarberiaPrefs", MODE_PRIVATE);

        if (isLoggedIn()) {
            startActivity(new Intent(LoginActivity.this, ReservaActivity.class));
            finish();
        }

        loginButton.setOnClickListener(v -> login());
        registerText.setOnClickListener(v -> startActivity(new Intent(LoginActivity.this, RegisterActivity.class)));
    }

    private void login() {
        String email = emailEditText.getText().toString();
        String password = passwordEditText.getText().toString();

        LoginRequest loginRequest = new LoginRequest(email, password);

        Call<ApiResponse<LoginResponse>> call = apiInterface.loginCustomer(loginRequest);
        call.enqueue(new Callback<ApiResponse<LoginResponse>>() {
            @Override
            public void onResponse(Call<ApiResponse<LoginResponse>> call, Response<ApiResponse<LoginResponse>> response) {
                if (response.isSuccessful() && response.body() != null && !response.body().isError()) {
                    LoginResponse loginResponse = response.body().getBody();
                    saveUserSession(loginResponse.getCustomer(), loginResponse.getToken());
                    startActivity(new Intent(LoginActivity.this, ReservaActivity.class));
                    finish();
                } else {
                    Toast.makeText(LoginActivity.this, "Credenciales inválidas", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<LoginResponse>> call, Throwable t) {
                Toast.makeText(LoginActivity.this, "Error de red", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void saveUserSession(Customer customer, String token) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt("customer_id", customer.getCustomer_id());
        editor.putString("email", customer.getEmail());
        editor.putString("first_name", customer.getFirst_name());
        editor.putString("last_name", customer.getLast_name());
        editor.putString("token", token);
        editor.apply();
    }

    private boolean isLoggedIn() {
        return sharedPreferences.contains("token");
    }
}