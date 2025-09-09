package com.telran.cars.items.clerk;

import com.telran.cars.items.RentCompanyItem;
import com.telran.cars.models.IRentCompany;
import view.InputOutput;

import java.time.LocalDate;

public class ReturnCarItem extends RentCompanyItem {


    public ReturnCarItem(InputOutput inOut, IRentCompany company, String fileName) {
        super(inOut, company, fileName);
    }

    @Override
    public String displayName() {
        return "Return car";
    }

    @Override
    public void perform() {
        String regNumber = getRegNumberExisted();
        if (regNumber == null)
            return;
        Long licenseId = getLicenseIdIfExists();
        if (licenseId == null)
            return;
        LocalDate returnDate = inOut.inputDate("Enter initial "+ format, format);
        if (returnDate == null)
            return;
        Integer damage = inOut.inputInteger("Enter percent of damage", 0, 100);
        if( damage == null) return;

        Integer tankPercent = inOut.inputInteger("Enter tank percent", 0, 100);
        if( tankPercent == null) return;

        inOut.outputLine(company.returnCar(regNumber, licenseId, returnDate, damage, tankPercent));

        saveResult();



    }
}
