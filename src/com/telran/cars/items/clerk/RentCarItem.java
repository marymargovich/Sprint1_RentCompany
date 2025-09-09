package com.telran.cars.items.clerk;

import com.telran.cars.items.RentCompanyItem;
import com.telran.cars.models.IRentCompany;
import view.InputOutput;

import java.time.LocalDate;

public class RentCarItem extends RentCompanyItem {
    private static final int MIN_RENT_DAYS = 1;
    private static final int MAX_RENT_DAYS = 30;



    public RentCarItem(InputOutput inOut, IRentCompany company, String fileName) {
        super(inOut, company, fileName);
    }

    @Override
    public String displayName() {
        return "Rent car";
    }

    @Override
    public void perform() {

        String regNumber = getRegNumberExisted();
        if( regNumber == null) return;
        Long licenseId = getLicenseIdIfExists();
        if( licenseId == null) return;
        LocalDate rentDate = inOut.inputDate("Enter initial date"+ format, format);
        if (rentDate == null) return;
        Integer rentDays = inOut.inputInteger("Enter rent days %d-%s", MIN_RENT_DAYS, MAX_RENT_DAYS);
        //RentRecord rr = new RentRecord(regNumber, licenseId, rentDate, rentDays);

        inOut.outputLine(company.rentCar(regNumber, licenseId, rentDate, rentDays));

        saveResult();


    }

}
