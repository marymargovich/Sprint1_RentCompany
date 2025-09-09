package com.telran.cars.items;

import com.telran.cars.models.IRentCompany;
import com.telran.utils.Persistable;
import view.InputOutput;
import view.Item;

import java.time.LocalDate;

public abstract class RentCompanyItem implements Item {
    protected InputOutput inOut;
    protected IRentCompany company;
    protected String fileName;


    protected LocalDate from;
    protected LocalDate to;
    protected String format = "yyyy-MM-dd";


    public RentCompanyItem(InputOutput inOut, IRentCompany company, String fileName) {
        this.inOut = inOut;
        this.company = company;
        this.fileName = fileName;
    }

    protected String getRegNumberNotExisted() {
        String regNumber = inOut.inputString("Enter regNumber");
        if (regNumber == null) return null;

        if (company.getCar(regNumber) != null) {
            inOut.outputLine("Car already exists");
            return null;
        }
        return regNumber;

    }

    protected Long getLicenseIdIfNotExists() {
        Long id = inOut.inputLong("Enter licenses ID");
        if (id == null) return null;
        if (company.getDriver(id) != null) {
            inOut.outputLine("Driver already exists");
            return null;
        }
        return id;

    }

    protected Long getLicenseIdIfExists() {
        Long id = inOut.inputLong("Enter licenses ID");
        if (id == null) return null;
        if (company.getDriver(id) == null) {
            inOut.outputLine("Driver not found");
            return null;
        }
        return id;

    }

    protected String getRegNumberExisted(){
        String regNumber = inOut.inputString("Enter regNumber");
        if (regNumber == null) return null;

        if (company.getCar(regNumber) == null) {
            inOut.outputLine("Car not found");
            return null;
        }
        return regNumber;

    }

    protected void fillFromToDates(){
        from = inOut. inputDate("Enter date from "+ format, format);
        if( from == null) return;
        to = inOut.inputDate("Enter date to "+ format, format);
        if( to == null) return;

        if ( from.isAfter(to)){
            inOut.outputLine("date from can't be after date to");
            from = to = null;
            return;

        }
    }
    protected void saveResult(){
        String res = inOut.inputString("Enter yes if you want to save changes");
        if( res.equalsIgnoreCase("yes"))
            ((Persistable)company).save(fileName);

    }

}
