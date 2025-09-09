package com.telran;

import com.telran.cars.dto.Car;
import com.telran.cars.dto.Driver;
import com.telran.cars.dto.Model;
import com.telran.cars.dto.RentRecord;
import com.telran.cars.items.SaveAndExitItem;
import com.telran.cars.items.clerk.AddDriverItem;
import com.telran.cars.items.driver.GetCarItem;
import com.telran.cars.items.driver.GetDriverItem;
import com.telran.cars.items.manager.AddCarItem;
import com.telran.cars.items.manager.AddModelItem;
import com.telran.cars.models.IRentCompany;
import com.telran.cars.models.RentCompanyEmbedded;
import com.telran.utils.Persistable;
import view.*;

import java.time.LocalDate;
import java.util.List;

public class RentCompanyApplItems {


    static IRentCompany company;
    static InputOutput inOut;
    private static final String FILE_NAME = "company-items.data";




    public static void main(String[] args) {
        inOut = new ConsoleInputOutput();
        company = RentCompanyEmbedded.restoreFromFile(FILE_NAME);
        Menu menu = new Menu(getMainMenu(), inOut);
        menu.runMenu();
    }

    private static Item[] getMainMenu() {
        Item[] items = {
                new SubMenu("Clerk", inOut, getClerkItems()),
                new SubMenu("Driver", inOut, getDriverItems()),
                new SubMenu("Manager", inOut, getManagerItems()),
                new SubMenu("Statist", inOut, getStatistItems()),
                new SubMenu("Technician", inOut, getTecnitionItems()),
                new SaveAndExitItem(inOut, company, FILE_NAME)

        };
        return items;
    }

    private static Item[] getTecnitionItems() {
        Item[] items = {
                new ExitItem()
        };
        return  items;
    }

    private static Item[] getStatistItems() {
        Item[] items = {
                new ExitItem()
        };
        return  items;
    }

    private static Item[] getManagerItems() {
        Item[] items = {
                new AddModelItem(inOut, company, FILE_NAME),
                new AddCarItem(inOut, company, FILE_NAME),
                new ExitItem()
        };
        return  items;
    }

    private static Item[] getDriverItems() {
        Item[] items = {
                new GetCarItem(inOut, company, FILE_NAME),
                new GetDriverItem(inOut, company, FILE_NAME),
                new ExitItem()
        };
        return  items;
    }

    private static Item[] getClerkItems() {
        Item[] items = {
                new AddDriverItem(inOut, company,FILE_NAME),
                new ExitItem()
        };
        return  items;
    }

}