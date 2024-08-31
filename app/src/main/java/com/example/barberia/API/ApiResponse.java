package com.example.barberia.API;

public class ApiResponse<T> {
    private boolean error;
    private int status;
    private String barberia;
    private T body;

    // Getters y setters
    public boolean isError() {
        return error;
    }

    public void setError(boolean error) {
        this.error = error;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getBarberia() {
        return barberia;
    }

    public void setBarberia(String barberia) {
        this.barberia = barberia;
    }

    public T getBody() {
        return body;
    }

    public void setBody(T body) {
        this.body = body;
    }
}