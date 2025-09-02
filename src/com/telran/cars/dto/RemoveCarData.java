package com.telran.cars.dto;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;

public class RemoveCarData implements Serializable {
    Car car;
    List<RentRecord> removedRecords;


    public RemoveCarData() {
    }

    public RemoveCarData(Car car, List<RentRecord> removedRecords) {
        this.car = car;
        this.removedRecords = removedRecords;
    }

    public Car getCar() {
        return car;
    }

    public List<RentRecord> getRemovedRecords() {
        return removedRecords;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        RemoveCarData that = (RemoveCarData) o;
        return Objects.equals(car, that.car) && Objects.equals(removedRecords, that.removedRecords);
    }

    @Override
    public int hashCode() {
        return Objects.hash(car, removedRecords);
    }

    @Override
    public String toString() {
        final String RESET   = "\u001B[0m";
        final String MAGENTA = "\u001B[95m";

        return "RemoveCarData: " +
                MAGENTA + "car "            + RESET + getCar() +
                ", " + MAGENTA + "removedRecords " + RESET + getRemovedRecords() +
                "\n";
    }
}
