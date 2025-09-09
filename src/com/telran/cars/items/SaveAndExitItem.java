package com.telran.cars.items;

import com.telran.cars.models.IRentCompany;
import com.telran.utils.Persistable;
import view.InputOutput;

public class SaveAndExitItem extends RentCompanyItem{

    public SaveAndExitItem(InputOutput inOut,
                           IRentCompany company,
                           String fileName) {
        super(inOut, company, fileName);
    }

    @Override
    public String displayName() {
        return "Save and Exit";
    }

    @Override
    public void perform() {
        ((Persistable) company).save(fileName);

    }

    public boolean isExit(){
        return true;
    }
}
