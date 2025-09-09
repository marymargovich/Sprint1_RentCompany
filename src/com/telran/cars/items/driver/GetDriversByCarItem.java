package com.telran.cars.items.driver;

import com.telran.cars.dto.Driver;
import com.telran.cars.items.RentCompanyItem;
import com.telran.cars.models.IRentCompany;
import view.InputOutput;

import java.util.List;

public class GetDriversByCarItem extends RentCompanyItem {
    public GetDriversByCarItem(InputOutput inOut, IRentCompany company, String fileName) {
        super(inOut, company, fileName);
    }

    @Override
    public String displayName() {
        return "Display data of drivers who have driven a given car.";
    }

    @Override
    public void perform() {
        String regNumber = getRegNumberExisted();
        if (regNumber == null)
            return;
        List<Driver> drivers = company.getDriversByCar(regNumber);
        if(drivers.isEmpty()) {
            inOut.outputLine("No driver of car with number" + regNumber);
            return;
        }
        drivers.forEach(inOut:: outputLine);


    }
}
