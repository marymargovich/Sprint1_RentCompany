package com.telran.cars.items.driver;

import com.telran.cars.dto.Car;
import com.telran.cars.items.RentCompanyItem;
import com.telran.cars.models.IRentCompany;
import view.InputOutput;

import java.util.List;

public class GetCarsByDriver extends RentCompanyItem {
    public GetCarsByDriver(InputOutput inOut, IRentCompany company, String fileName) {
        super(inOut, company, fileName);
    }

    @Override
    public String displayName() {
        return "Display cars data driving a given car";
    }

    @Override
    public void perform() {
            Long licenseId = getLicenseIdIfExists();
            if (licenseId == null)
                return;
        List<Car> cars= company.getCarsByDriver(licenseId);

        if(cars.isEmpty()){
            inOut.outputLine("No cars for "+ licenseId);
            return;
        }
            cars.forEach(inOut:: outputLine);

    }

}
