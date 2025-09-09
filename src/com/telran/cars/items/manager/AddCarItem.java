package com.telran.cars.items.manager;

import com.telran.cars.dto.Car;
import com.telran.cars.items.RentCompanyItem;
import com.telran.cars.models.IRentCompany;
import view.InputOutput;

import java.util.List;

public class AddCarItem extends RentCompanyItem {

    public AddCarItem(InputOutput inOut,
                      IRentCompany company,
                      String fileName) {
        super(inOut, company, fileName);
    }

    @Override
    public String displayName() {
        return "Add new Car";
    }

    @Override
    public void perform() {
        String regNumber = getRegNumberNotExisted ();
        if( regNumber == null) return;

        String colour = inOut.inputString("Enter Colour");
        if( colour == null) return;
        List<String> models = company.getModelNames();
        String modelName = inOut.inputString("Enter model name for list", models);
        Car car = new Car(regNumber, colour, modelName);

        inOut.outputLine(company.addCar(car));

        saveResult();







    }
}
