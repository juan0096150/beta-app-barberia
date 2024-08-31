package com.example.barberia.API;

import com.example.barberia.model.Appointment;
import com.example.barberia.model.Barber;
import com.example.barberia.model.Customer;
import com.example.barberia.model.LoginRequest;
import com.example.barberia.model.LoginResponse;
import com.example.barberia.model.Service;

import retrofit2.Call;
import retrofit2.http.*;
import java.util.List;

public interface ApiInterface {
    @GET("customers")
    Call<ApiResponse<List<Customer>>> getCustomers();

    @GET("barbers")
    Call<ApiResponse<List<Barber>>> getBarbers();

    @GET("services")
    Call<ApiResponse<List<Service>>> getServices();

    @POST("appointments")
    Call<ApiResponse<Appointment>> createAppointment(@Body Appointment appointment);

    @POST("customers")
    Call<ApiResponse<Customer>> createCustomer(@Body Customer customer);

    @POST("customers/login")
    Call<ApiResponse<LoginResponse>> loginCustomer(@Body LoginRequest loginRequest);
}