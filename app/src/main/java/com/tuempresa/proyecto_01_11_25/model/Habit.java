package com.tuempresa.proyecto_01_11_25.model;

import java.util.ArrayList;
import java.util.List;

public class Habit {

    public enum HabitType {
        EXERCISE,   // acelerómetro
        WALK,       // distancia
        DEMO,       // botón
        READ        // cámara + ML Kit
    }

    private long id = -1;
    private String title;
    private String goal;
    private String category;
    private HabitType type;
    private boolean completed;
    private double targetValue = 0.0;
    private String targetUnit = null;

    public Habit(String title, String goal, String category, HabitType type) {
        this.title = title;
        this.goal = goal;
        this.category = category;
        this.type = type;
        this.completed = false;
    }

    public long getId() { return id; }
    public void setId(long id) { this.id = id; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getGoal() { return goal; }
    public void setGoal(String goal) { this.goal = goal; }
    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }
    public HabitType getType() { return type; }
    public void setType(HabitType type) { this.type = type; }
    public boolean isCompleted() { return completed; }
    public void setCompleted(boolean completed) { this.completed = completed; }
    public double getTargetValue() { return targetValue; }
    public void setTargetValue(double targetValue) { this.targetValue = targetValue; }
    public String getTargetUnit() { return targetUnit; }
    public void setTargetUnit(String targetUnit) { this.targetUnit = targetUnit; }

    /** 4 hábitos por defecto al abrir la app */
    public static List<Habit> defaultHabits() {
        List<Habit> list = new ArrayList<>();
        list.add(new Habit("Ejercicio", "Goal: movimiento detectado", "salud", HabitType.EXERCISE));
        list.add(new Habit("Caminar", "Goal: 150 metros", "salud", HabitType.WALK));
        list.add(new Habit("Leer", "Goal: detectar página de libro", "educación", HabitType.READ));
        list.add(new Habit("Demo", "Goal: tocar para completar", "general", HabitType.DEMO));
        return list;
    }
}
