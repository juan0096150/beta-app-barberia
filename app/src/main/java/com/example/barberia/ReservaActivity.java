package com.example.barberia;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;

import com.example.barberia.API.ApiClient;
import com.example.barberia.API.ApiInterface;
import com.example.barberia.API.ApiResponse;
import com.example.barberia.model.Appointment;
import com.example.barberia.model.Barber;
import com.example.barberia.model.Service;
import com.google.android.material.button.MaterialButton;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class ReservaActivity extends AppCompatActivity {
    private TextView welcomeText, selectedDateTimeText;
    private Spinner barberSpinner, serviceSpinner;
    private MaterialButton dateButton, timeButton, reserveButton;
    private ApiInterface apiInterface;
    private int selectedBarberId = -1;
    private int loggedInCustomerId = -1;
    private SharedPreferences sharedPreferences;
    private int selectedServiceId = -1;
    private Calendar selectedDateTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reserva);

        initializeViews();
        setupSharedPreferences();
        setupApiInterface();
        loadUserInfo();
        loadServices();
        loadBarbers();
        setupDateTimeListeners();

        reserveButton.setOnClickListener(v -> createAppointment());
    }

    private void initializeViews() {
        welcomeText = findViewById(R.id.welcome_text);
        selectedDateTimeText = findViewById(R.id.selected_datetime);
        barberSpinner = findViewById(R.id.barber_spinner);
        serviceSpinner = findViewById(R.id.service_spinner);
        dateButton = findViewById(R.id.date_button);
        timeButton = findViewById(R.id.time_button);
        reserveButton = findViewById(R.id.reserve_button);
    }

    private void setupSharedPreferences() {
        sharedPreferences = getSharedPreferences("BarberiaPrefs", MODE_PRIVATE);
    }

    private void setupApiInterface() {
        apiInterface = ApiClient.getClient().create(ApiInterface.class);
    }

    private void loadUserInfo() {
        loggedInCustomerId = sharedPreferences.getInt("customer_id", -1);
        String firstName = sharedPreferences.getString("first_name", "");
        welcomeText.setText("Bienvenido, " + firstName);
    }

    private void loadServices() {
        if (apiInterface == null) {
            Log.e("ReservaActivity", "Error: API interface no inicializada");
            Toast.makeText(this, "Error: API interface no inicializada", Toast.LENGTH_SHORT).show();
            return;
        }

        Log.d("ReservaActivity", "Iniciando carga de servicios");
        Call<ApiResponse<List<Service>>> call = apiInterface.getServices();
        call.enqueue(new Callback<ApiResponse<List<Service>>>() {
            @Override
            public void onResponse(Call<ApiResponse<List<Service>>> call, Response<ApiResponse<List<Service>>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Service> services = response.body().getBody();
                    Log.d("ReservaActivity", "Servicios cargados: " + services.size());
                    setupServiceSpinner(services);
                } else {
                    Log.e("ReservaActivity", "Error al cargar servicios. Código: " + response.code());
                    if (response.errorBody() != null) {
                        try {
                            Log.e("ReservaActivity", "Error body: " + response.errorBody().string());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    handleApiError("Error al cargar servicios");
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<List<Service>>> call, Throwable t) {
                Log.e("ReservaActivity", "Error de red al cargar servicios", t);
                handleApiError("Error de red: " + t.getMessage());
            }
        });
    }


    private void setupServiceSpinner(List<Service> services) {
        ArrayAdapter<Service> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, services);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        serviceSpinner.setAdapter(adapter);

        serviceSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedServiceId = services.get(position).getService_id();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                selectedServiceId = -1;
            }
        });
    }

    private void loadBarbers() {
        if (apiInterface == null) {
            Log.e("ReservaActivity", "Error: API interface no inicializada");
            Toast.makeText(this, "Error: API interface no inicializada", Toast.LENGTH_SHORT).show();
            return;
        }

        Log.d("ReservaActivity", "Iniciando carga de barberos");
        Call<ApiResponse<List<Barber>>> call = apiInterface.getBarbers();
        call.enqueue(new Callback<ApiResponse<List<Barber>>>() {
            @Override
            public void onResponse(Call<ApiResponse<List<Barber>>> call, Response<ApiResponse<List<Barber>>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Barber> barbers = response.body().getBody();
                    Log.d("ReservaActivity", "Barberos cargados: " + barbers.size());
                    setupBarberSpinner(barbers);
                } else {
                    Log.e("ReservaActivity", "Error al cargar barberos. Código: " + response.code());
                    if (response.errorBody() != null) {
                        try {
                            Log.e("ReservaActivity", "Error body: " + response.errorBody().string());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    handleApiError("Error al cargar barberos");
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<List<Barber>>> call, Throwable t) {
                Log.e("ReservaActivity", "Error de red al cargar barberos", t);
                handleApiError("Error de red: " + t.getMessage());
            }
        });
    }

    private void setupBarberSpinner(List<Barber> barbers) {
        ArrayAdapter<Barber> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, barbers);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        barberSpinner.setAdapter(adapter);

        barberSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedBarberId = barbers.get(position).getBarber_id();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                selectedBarberId = -1;
            }
        });
    }

    private void setupDateTimeListeners() {
        selectedDateTime = Calendar.getInstance();

        dateButton.setOnClickListener(v -> showDatePicker());
        timeButton.setOnClickListener(v -> showTimePicker());
    }

    private void showDatePicker() {
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                new ContextThemeWrapper(this, R.style.CustomDatePickerStyle),
                (view, year, month, dayOfMonth) -> {
                    selectedDateTime.set(Calendar.YEAR, year);
                    selectedDateTime.set(Calendar.MONTH, month);
                    selectedDateTime.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                    updateSelectedDateTime();
                },
                selectedDateTime.get(Calendar.YEAR),
                selectedDateTime.get(Calendar.MONTH),
                selectedDateTime.get(Calendar.DAY_OF_MONTH)
        );
        datePickerDialog.show();
    }

    private void showTimePicker() {
        TimePickerDialog timePickerDialog = new TimePickerDialog(
                new ContextThemeWrapper(this, R.style.CustomTimePickerStyle),
                (view, hourOfDay, minute) -> {
                    selectedDateTime.set(Calendar.HOUR_OF_DAY, hourOfDay);
                    selectedDateTime.set(Calendar.MINUTE, minute);
                    updateSelectedDateTime();
                },
                selectedDateTime.get(Calendar.HOUR_OF_DAY),
                selectedDateTime.get(Calendar.MINUTE),
                true
        );
        timePickerDialog.show();
    }

    private void updateSelectedDateTime() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault());
        String formattedDateTime = sdf.format(selectedDateTime.getTime());
        selectedDateTimeText.setText("Cita programada para: " + formattedDateTime);
    }

    private void createAppointment() {
        if (!validateAppointmentData()) {
            return;
        }

        Appointment appointment = createAppointmentObject();
        Call<ApiResponse<Appointment>> call = apiInterface.createAppointment(appointment);
        call.enqueue(new Callback<ApiResponse<Appointment>>() {
            @Override
            public void onResponse(Call<ApiResponse<Appointment>> call, Response<ApiResponse<Appointment>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Toast.makeText(ReservaActivity.this, "Cita creada con éxito", Toast.LENGTH_SHORT).show();
                    // Aquí puedes navegar a otra actividad o actualizar la UI
                } else {
                    handleApiError("Error al crear la cita");
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<Appointment>> call, Throwable t) {
                handleApiError("Error de red: " + t.getMessage());
            }
        });
    }

    private boolean validateAppointmentData() {
        if (selectedBarberId == -1 || loggedInCustomerId == -1 || selectedServiceId == -1) {
            Toast.makeText(this, "Por favor, seleccione un barbero y un servicio", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (selectedDateTime == null) {
            Toast.makeText(this, "Por favor, seleccione fecha y hora", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

    private Appointment createAppointmentObject() {
        Appointment appointment = new Appointment();
        appointment.setCustomer_id(loggedInCustomerId);
        appointment.setBarber_id(selectedBarberId);
        appointment.setService_id(selectedServiceId);

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        String formattedDateTime = sdf.format(selectedDateTime.getTime());
        appointment.setAppointment_date(formattedDateTime);

        return appointment;
    }

    private void handleApiError(String errorMessage) {
        Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_reserva, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_logout) {
            logout();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void logout() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();

        Intent intent = new Intent(ReservaActivity.this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }
}