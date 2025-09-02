package com.telran.cars.dto;

import com.telran.cars.dto.enums.State;

import java.io.Serializable;
import java.util.Objects;

public class Car  implements Serializable {
    private String regNumber;
    private String color;
    private State state = State.EXELLENT;
    private String modelName;
    private boolean inUse;
    private boolean flRemove;

    public Car() {};

    public Car(String regNumber, String color, String modelName) {
        this.regNumber = regNumber;
        this.color = color;
        this.modelName = modelName;
    }

    public String getRegNumber() {
        return regNumber;
    }

    public String getColor() {
        return color;
    }

    public State getState() {
        return state;
    }

    public String getModelName() {
        return modelName;
    }

    public boolean isInUse() {
        return inUse;
    }

    public boolean isFlRemove() {
        return flRemove;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public void setState(State state) {
        this.state = state;
    }

    public void setInUse(boolean inUse) {
        this.inUse = inUse;
    }

    public void setFlRemove(boolean flRemove) {
        this.flRemove = flRemove;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Car car = (Car) o;
        return Objects.equals(regNumber, car.regNumber);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(regNumber);
    }

    @Override
    public String toString() {
        final String RESET  = "\u001B[0m";
        final String YELLOW = "\u001B[33m";
        return "Car: " +
                YELLOW + "regNumber " + RESET + getRegNumber() +
                ", " + YELLOW + "color " + RESET + getColor() +
                ", " + YELLOW + "state " + RESET + getState() +
                ", " + YELLOW + "modelName " + RESET + getModelName() +
                ", " + YELLOW + "inUse " + RESET + isInUse() +
                ", " + YELLOW + "flRemove " + RESET + isFlRemove() +
                "\n";
    }
}
