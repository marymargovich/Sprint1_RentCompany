package com.telran.test;

import com.telran.cars.dto.Car;
import com.telran.cars.dto.Driver;
import com.telran.cars.dto.Model;
import com.telran.cars.models.IRentCompany;
import com.telran.cars.models.RentCompanyEmbedded;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class RentCompanyEnbeddedStatisticTests {
    private static final String MODEL = "model";
    private static final String CAR = "car";
    IRentCompany company = new RentCompanyEmbedded();
    int[] prices = {100, 100, 100, 100, 10000};
    int[] years = {2000, 1995, 1950, 1948};
    LocalDate fromDate = LocalDate.of(1900, 1, 1);
    LocalDate toDate = LocalDate.of(2300, 1, 1);
    LocalDate rentDate = LocalDate.of (2020, 1, 1);


    @BeforeEach
    void setUp() throws Exception{
        creatModels ();
        creatCars();
        creatDrivers();
        rentReturns();
        company.setFinePercent(8);
        company.setGasPrice(13);
    }

    private void rentReturns() {
        int[] licenseID = {0,0, 1,1,2, 2,3,3,0};
        String[] regNumbers = {CAR+0, CAR+1, CAR+0, CAR+1, CAR+2, CAR+3, CAR+2, CAR+3, CAR+4};
        int rentDays = 5;
        LocalDate cursor = rentDate;
        for (int i = 0; i < regNumbers.length; i++) {
            company.rentCar(regNumbers[i], licenseID[i],cursor, rentDays );
            company.returnCar(regNumbers[i], licenseID[i], cursor.plusDays(rentDays), 0, 100);
            cursor= cursor.plusDays(rentDays+1);


        }
    }

    private void creatDrivers() {
        for(int licenseId=0; licenseId<years.length; licenseId++)
            company. addDriver (new Driver(licenseId, "name"+licenseId,
                    years[licenseId], "phone"+licenseId));

    }

    private void creatCars() {
        for (int i = 0; i < prices.length; i++) {
            company.addCar(new Car(CAR+ i, "Colour"+i, MODEL+i));

        }
    }

    private void creatModels() {
        for (int i = 0; i < prices.length; i++) {
            company.addModel(new Model(MODEL+i, 50, "company"+ i, "company"+1, prices[i]));


        }
    }


    @Test
    void
    testGetMostPopularCarModels() {
        int ageYoungFrom = rentDate.getYear()-years[0];
        int ageYoungTo = rentDate.getYear()-years[1]+1;
        int ageOldFrom = rentDate.getYear()-years [2];
        int ageOlaTo = rentDate.getYear()-years[3]+1;
        List<String> youngModel = company
                .getMostPopularCarModels(fromDate, toDate, ageYoungFrom, ageYoungTo);
        assertEquals(2, youngModel. size ());
        assertTrue(youngModel.contains(MODEL+0));
        assertTrue(youngModel.contains(MODEL+1));

        List<String> oldModel = company
                .getMostPopularCarModels(fromDate, toDate, ageOldFrom, ageOlaTo);
        assertEquals(2, oldModel. size ());
        assertTrue(oldModel.contains(MODEL+2));
        assertTrue(oldModel.contains(MODEL+3));



    }

    @Test
    void testGetMostProfitableCarModels() {
        List<String> profitableModel = company.getMostProfitableCarModels(fromDate, toDate);
        assertEquals(1, profitableModel.size());
        assertEquals(MODEL+4, profitableModel.get(0));
    }

    @Test
    void testGetMostActiveDrivers() {
        List<Driver> activeDrivers = company.getMostActiveDrivers();
        assertEquals(1, activeDrivers.size());
        assertEquals(0L, activeDrivers.get(0).getLicenseId());

    }
    @Test
    void testGetMostPopularCarModels_negative (){
    int ageYoungFrom = rentDate.getYear() - years [0];
    int ageYoungTo = rentDate.getYear() - years [1] + 1;
    int ageOldFrom = rentDate.getYear () - years [2];
    int ageOldTo = rentDate.getYear() - years [3] + 1;
    List<String> negative1 = company
            .getMostPopularCarModels(fromDate, fromDate.plusYears(50),
                    ageYoungFrom, ageYoungTo);
        assertTrue (negative1.isEmpty());
        List<String> negative2 = company.getMostPopularCarModels(fromDate, toDate,40, 50);
        assertTrue(negative2.isEmpty());
}
    @Test
    void testGetMostActiveDrivers1() {
        company.removeCar(CAR+4);

        List<Driver> activeDrivers = company.getMostActiveDrivers();
        assertEquals(4, activeDrivers.size());
        assertTrue(activeDrivers.contains(company.getDriver(0)));
        assertTrue(activeDrivers.contains(company.getDriver(1)));
        assertTrue(activeDrivers.contains(company.getDriver(2)));
        assertTrue(activeDrivers.contains(company.getDriver(3)));

        //assertEquals(0L, activeDrivers.get(0).getLicenseId());

        List<String> youngModels = company
                .getMostPopularCarModels(fromDate, toDate
                        , 0, 120);
        assertEquals(4, youngModels.size());
        assertTrue(youngModels.contains(MODEL+1));
        assertTrue(youngModels.contains(MODEL+0));
        assertTrue(youngModels.contains(MODEL+2));
        assertTrue(youngModels.contains(MODEL+3));

    }
}
