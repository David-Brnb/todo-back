package com.itesm.domain.models.demo;

import java.util.List;

public class TodoDemoResult {
    private String strategy;
    private long durationMs;
    private int count;
    private String leccion;
    private List<TodoView> data;

    public TodoDemoResult() {}

    public TodoDemoResult(String strategy, long durationMs, int count, String leccion, List<TodoView> data) {
        this.strategy = strategy;
        this.durationMs = durationMs;
        this.count = count;
        this.leccion = leccion;
        this.data = data;
    }

    public String getStrategy() { return strategy; }
    public void setStrategy(String strategy) { this.strategy = strategy; }
    public long getDurationMs() { return durationMs; }
    public void setDurationMs(long durationMs) { this.durationMs = durationMs; }
    public int getCount() { return count; }
    public void setCount(int count) { this.count = count; }
    public String getLeccion() { return leccion; }
    public void setLeccion(String leccion) { this.leccion = leccion; }
    public List<TodoView> getData() { return data; }
    public void setData(List<TodoView> data) { this.data = data; }
}