package com.itesm.domain.models.demo;

public class LazyInitDemoResult {
    private String strategy;
    private String status;
    private String message;
    private String leccion;

    public LazyInitDemoResult() {}

    public LazyInitDemoResult(String strategy, String status, String message, String leccion) {
        this.strategy = strategy;
        this.status = status;
        this.message = message;
        this.leccion = leccion;
    }

    public String getStrategy() { return strategy; }
    public void setStrategy(String strategy) { this.strategy = strategy; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
    public String getLeccion() { return leccion; }
    public void setLeccion(String leccion) { this.leccion = leccion; }
}