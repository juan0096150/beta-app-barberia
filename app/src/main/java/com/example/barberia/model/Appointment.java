package com.example.barberia.model;

public class Appointment {
    private String appointment_date;
    private int customer_id;
    private int barber_id;

    public int getService_id() {
        return service_id;
    }

    public void setService_id(int service_id) {
        this.service_id = service_id;
    }

    public String getAppointment_date() {
        return appointment_date;
    }

    public void setAppointment_date(String appointment_date) {
        this.appointment_date = appointment_date;
    }

    public int getBarber_id() {
        return barber_id;
    }

    public void setBarber_id(int barber_id) {
        this.barber_id = barber_id;
    }

    public int getCustomer_id() {
        return customer_id;
    }

    public void setCustomer_id(int customer_id) {
        this.customer_id = customer_id;
    }

    private int service_id;

    // Constructor sin argumentos
    public Appointment() {}

    // Constructor con argumentos
    public Appointment(String appointment_date, int customer_id, int barber_id, int service_id) {
        this.appointment_date = appointment_date;
        this.customer_id = customer_id;
        this.barber_id = barber_id;
        this.service_id = service_id;
    }

    // Getters y setters
}