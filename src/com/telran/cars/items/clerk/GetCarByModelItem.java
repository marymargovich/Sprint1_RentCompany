package com.telran.cars.items.clerk;

import com.telran.cars.dto.Car;
import com.telran.cars.items.RentCompanyItem;
import com.telran.cars.models.IRentCompany;
import view.InputOutput;

import java.util.List;

public class GetCarByModelItem extends RentCompanyItem {
    public GetCarByModelItem(InputOutput inOut, IRentCompany company, String fileName) {
        super(inOut, company, fileName);
    }

    @Override
    public String displayName() {
        return "Display free cars by model";
    }

    @Override
    public void perform() {
        List<String> modelNames = company.getModelNames();
        String modelName = inOut.inputString("Enter model name ", modelNames);
        if( modelNames == null) return;

        List<Car> modelCars = company.getCarsByModel(modelName);
        if(modelCars.isEmpty()){
            inOut.outputLine("No cars of "+ modelNames);
            return;
        }
        modelCars.forEach(inOut::outputLine);



    }
}
