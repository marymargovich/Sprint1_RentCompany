package com.telran.cars.dto;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

public class RentRecord implements Serializable {
    private String regNumber;
    private long licenseId;
    private LocalDate rentDate;
    private LocalDate returnDate;
    private int rentDays;//
    private int damages;
    private int tankPercent;
    private double cost;


    public RentRecord(String regNumber,
                      long licenseId,
                      LocalDate rentDate,
                      int rentDays) {
        this.regNumber = regNumber;
        this.licenseId = licenseId;
        this.rentDate = rentDate;
        this.rentDays = rentDays;
    }

    public String getRegNumber() {
        return regNumber;
    }

    public long getLicenseId() {
        return licenseId;
    }

    public LocalDate getRentDate() {
        return rentDate;
    }

    public LocalDate getReturnDate() {
        return returnDate;
    }

    public int getRentDays() {
        return rentDays;
    }

    public int getDamages() {
        return damages;
    }

    public int getTankPercent() {
        return tankPercent;
    }

    public double getCost() {
        return cost;
    }

    public void setDamages(int damages) {
        this.damages = damages;
    }

    public void setReturnDate(LocalDate returnDate) {
        this.returnDate = returnDate;
    }

    public void setTankPercent(int tankPercent) {
        this.tankPercent = tankPercent;
    }

    public void setCost(double cost) {
        this.cost = cost;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        RentRecord that = (RentRecord) o;
        return licenseId == that.licenseId && rentDays == that.rentDays && damages == that.damages && tankPercent == that.tankPercent && Double.compare(cost, that.cost) == 0 && Objects.equals(regNumber, that.regNumber) && Objects.equals(rentDate, that.rentDate) && Objects.equals(returnDate, that.returnDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(regNumber, licenseId, rentDate, returnDate, rentDays, damages, tankPercent, cost);
    }

    @Override
    public String toString() {
        final String RESET = "\u001B[0m";
        final String PURPLE = "\u001B[35m";

        return "RentRecord: " +
                PURPLE + "regNumber " + RESET + getRegNumber() +
                ", " + PURPLE + "licenseId " + RESET + getLicenseId() +
                ", " + PURPLE + "rentDate " + RESET + getRentDate() +
                ", " + PURPLE + "returnDate " + RESET + getReturnDate() +
                ", " + PURPLE + "rentDays " + RESET + getRentDays() +
                ", " + PURPLE + "damages " + RESET + getDamages() +
                ", " + PURPLE + "tankPercent " + RESET + getTankPercent() +
                ", " + PURPLE + "cost " + RESET + getCost() +
                "\n";
    }
}
