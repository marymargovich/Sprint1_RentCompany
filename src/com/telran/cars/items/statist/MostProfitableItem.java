package com.telran.cars.items.statist;

import com.telran.cars.items.RentCompanyItem;
import com.telran.cars.models.IRentCompany;
import view.InputOutput;

import java.util.List;

public class MostProfitableItem extends RentCompanyItem {
    public MostProfitableItem(InputOutput inOut, IRentCompany company, String fileName) {
        super(inOut, company, fileName);
    }

    @Override
    public String displayName() {
        return "Display most profitable model names";
    }

    @Override
    public void perform() {
        fillFromToDates();
        if(from == null || to == null)
            return;
        List< String > models = company.getMostProfitableCarModels(from, to );

        if( models.isEmpty()){
            inOut.outputLine("No models");
        }
        models.forEach(inOut:: outputLine);


    }
}
