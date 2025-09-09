package com.telran.test;

import com.telran.cars.dto.*;
import com.telran.cars.models.IRentCompany;
import com.telran.cars.models.RentCompanyEmbedded;
import com.telran.utils.Persistable;
import org.junit.jupiter.api.Test;

import java.time.*;
import java.util.*;


import static com.telran.cars.dto.enums.CarsReturnCode.*;
import static org.junit.jupiter.api.Assertions.*;

class RentCompanyEmbeddedTest {

    final String MODEL_NAME = "Model1";
    final int GAS_TANK = 50;
    final int PRICE_PER_DAY = 200;
    final long LICENSE = 1000L;
    final String REG_NUMBER = "100";
    final String COMPANY = "Company1";
    final String COUNTRY = "Country1";
    final String COLOUR = "colour1";
    final String NAME = "name1";
    final int YEAR_OF_BIRTH = 1990;
    final String PHONE = "123456789";

    final LocalDate RENT_DATE = LocalDate.of(2025, 8, 1);
    final int RENT_DAYS = 3;

    private Model model;
    private Car car;
    private Driver driver;

    private IRentCompany company;


    @org.junit.jupiter.api.BeforeEach
    void setUp() {
        model = new Model(MODEL_NAME, GAS_TANK, COMPANY, COUNTRY, PRICE_PER_DAY);
        car = new Car(REG_NUMBER, COLOUR, MODEL_NAME);
        driver = new Driver(LICENSE, NAME, YEAR_OF_BIRTH, PHONE);
        company = new RentCompanyEmbedded();
        ((Persistable) company).save("company2.data");

    }

    @org.junit.jupiter.api.Test
    void testAdd_get_Entities_OK() {
        assertEquals(OK, company.addModel(model));
        assertEquals(OK, company.addCar(car));
        assertEquals(OK, company.addDriver(driver));

        assertEquals(model, company.getModel(MODEL_NAME));
        assertEquals(car, company.getCar(REG_NUMBER));
        assertEquals(driver, company.getDriver(LICENSE));


    }

    @org.junit.jupiter.api.Test
    void addCar_NO_MODEL() {
        assertEquals(NO_MODEL, company.addCar(car));
        assertNull(company.getCar(REG_NUMBER));

    }

    @org.junit.jupiter.api.Test
    void add_DuplicateEntitiesL() {
        assertEquals(OK, company.addModel(model));
        assertEquals(OK, company.addCar(car));
        assertEquals(OK, company.addDriver(driver));

        assertEquals(MODEL_EXISTS, company.addModel(model));
        assertEquals(CAR_EXISTS, company.addCar(car));
        assertEquals(DRIVER_EXISTS, company.addDriver(driver));


    }

    @org.junit.jupiter.api.Test
    void get_when_NOT_ADDED() {
        assertNull(company.getCar(REG_NUMBER));
        assertNull(company.getDriver(LICENSE));
        assertNull(company.getModel(MODEL_NAME));


    }


    @org.junit.jupiter.api.Test
    void save_Restored_OK() {
        assertEquals(OK, company.addModel(model));
        assertEquals(OK, company.addCar(car));
        assertEquals(OK, company.addDriver(driver));

        String file = "companyTest.data";
        ((Persistable) company).save(file);
        IRentCompany restored = RentCompanyEmbedded.restoreFromFile(file);
        assertNotNull(restored);

        assertEquals(model, restored.getModel(MODEL_NAME));
        assertEquals(car, restored.getCar(REG_NUMBER));
        assertEquals(driver, restored.getDriver(LICENSE));

    }

    @org.junit.jupiter.api.Test
    void save_Restored_Fail() {
        String file = "companyTest_fail.data";
        IRentCompany restored = RentCompanyEmbedded.restoreFromFile(file);
        assertNotNull(restored);
        assertTrue(restored instanceof RentCompanyEmbedded);
        assertNull(restored.getModel(MODEL_NAME));
        assertNull(restored.getCar(REG_NUMBER));
        assertNull(restored.getDriver(LICENSE));


    }

    @Test
    void testRentCar() {
        assertEquals(OK, company.addModel(model));
        assertEquals(OK, company.addCar(car));
        assertEquals(OK, company.addDriver(driver));

        assertEquals(OK, company.rentCar(REG_NUMBER, LICENSE, RENT_DATE, RENT_DAYS));
    }

    @Test
    void testRentCarNO_CAR() {
        assertEquals(OK, company.addModel(model));
        //assertEquals(OK, company.addCar(car));
        assertEquals(OK, company.addDriver(driver));

        assertEquals(NO_CAR, company.rentCar(REG_NUMBER, LICENSE, RENT_DATE, RENT_DAYS));
    }

    @Test
    void testRentCarNO_DRVER() {
        assertEquals(OK, company.addModel(model));
        assertEquals(OK, company.addCar(car));
        //assertEquals(OK, company.addDriver(driver));

        assertEquals(NO_DRIVER, company.rentCar(REG_NUMBER, LICENSE, RENT_DATE, RENT_DAYS));
    }

    @Test
    void testRentCarRemove_Use() {
        assertEquals(OK, company.addModel(model));
        assertEquals(OK, company.addCar(car));
        assertEquals(OK, company.addDriver(driver));

        car.setFlRemove(true);
        assertEquals(CAR_REMOVED, company.rentCar(REG_NUMBER, LICENSE, RENT_DATE, RENT_DAYS));
        car.setFlRemove(false);
        car.setInUse(true);
        assertEquals(CAR_IN_USE, company.rentCar(REG_NUMBER, LICENSE, RENT_DATE, RENT_DAYS));


    }

    @Test
    void testRentRecords() {
        assertEquals(OK, company.addModel(model));
        assertEquals(OK, company.addCar(car));
        assertEquals(OK, company.addDriver(driver));

        RentRecord expected = new RentRecord(REG_NUMBER, LICENSE, RENT_DATE, RENT_DAYS);
        company.rentCar(REG_NUMBER, LICENSE, RENT_DATE, RENT_DAYS);
        List<RentRecord> res = company.getRentRecordsAtDate(RENT_DATE, RENT_DATE.plusDays(RENT_DAYS));
        assertEquals(1, res.size());
        assertEquals(expected, res.get(0));
        List<RentRecord> res1 = company.getRentRecordsAtDate(RENT_DATE.plusDays(RENT_DAYS), RENT_DATE.plusDays(RENT_DAYS));
        assertTrue(res1.isEmpty());


    }

    @Test
    void testCarsByDriver() {
        assertEquals(OK, company.addModel(model));
        assertEquals(OK, company.addCar(car));
        assertEquals(OK, company.addDriver(driver));
        company.rentCar(REG_NUMBER, LICENSE, RENT_DATE, RENT_DAYS);
        List<Car> res = company.getCarsByDriver(LICENSE);
        assertEquals(1, res.size());
        assertEquals(car, res.get(0));

        res = company.getCarsByDriver(LICENSE + 1);
        assertTrue(res.isEmpty());

    }

    @Test
    void testCarsByCar() {
        assertEquals(OK, company.addModel(model));
        assertEquals(OK, company.addCar(car));
        assertEquals(OK, company.addDriver(driver));

        company.rentCar(REG_NUMBER, LICENSE, RENT_DATE, RENT_DAYS);

        List<Driver> res = company.getDriversByCar(REG_NUMBER);
        assertEquals(1, res.size());
        assertEquals(driver, res.get(0));

        res = company.getDriversByCar(REG_NUMBER + 1);
        assertTrue(res.isEmpty());
    }

    @Test
    void testCarsByModel() {
        assertEquals(OK, company.addModel(model));
        assertEquals(OK, company.addCar(car));
        assertEquals(OK, company.addDriver(driver));

        List<Car> res = company.getCarsByModel(MODEL_NAME);
        assertEquals(1, res.size());
        assertEquals(car, res.get(0));

        Car car1 = new Car(REG_NUMBER + 1, COLOUR + "R", MODEL_NAME);
        assertEquals(OK, company.addCar(car1));
        car1.setInUse(true);
        res = company.getCarsByModel(MODEL_NAME);
        assertEquals(1, res.size());
        assertEquals(car, res.get(0));

        car1.setInUse(false);
        car1.setFlRemove(true);
        res = company.getCarsByModel(MODEL_NAME);
        assertEquals(1, res.size());
        assertEquals(car, res.get(0));

        res = company.getCarsByModel(MODEL_NAME + 11);
        assertTrue(res.isEmpty());

    }

    @Test
    void testRemoveCarsInUse() {
        assertEquals(OK, company.addModel(model));
        assertEquals(OK, company.addCar(car));
        assertEquals(OK, company.addDriver(driver));

        company.rentCar(REG_NUMBER, LICENSE, RENT_DATE, RENT_DAYS);
        RemoveCarData nC = new RemoveCarData(car, null);
        assertEquals(nC, company.removeCar(REG_NUMBER));

        assertNull(company.removeCar(REG_NUMBER + 100));
        car.setFlRemove(true);
        assertNull(company.removeCar(REG_NUMBER));

    }

    @Test
    void testRemoveCarsNotInUse() {
        assertEquals(OK, company.addModel(model));
        assertEquals(OK, company.addCar(car));
        assertEquals(OK, company.addDriver(driver));
        RemoveCarData nC = new RemoveCarData(car, new ArrayList<>());
        assertEquals(nC, company.removeCar(REG_NUMBER));


    }

    @Test
    void testRemoveCarWithHistory() {
        assertEquals(OK, company.addModel(model));
        assertEquals(OK, company.addCar(car));
        assertEquals(OK, company.addDriver(driver));

        company.rentCar(REG_NUMBER, LICENSE, RENT_DATE, RENT_DAYS);

        assertFalse(company.getCarsByDriver(LICENSE).isEmpty());
        assertFalse(company.getDriversByCar(REG_NUMBER).isEmpty());
        assertFalse(company.getRentRecordsAtDate(RENT_DATE, RENT_DATE.plusDays(RENT_DAYS)).isEmpty());

        company.returnCar(REG_NUMBER, LICENSE, RENT_DATE.plusDays(RENT_DAYS), 100, 10);

        assertTrue(company.getCarsByDriver(LICENSE).isEmpty());
        assertTrue(company.getDriversByCar(REG_NUMBER).isEmpty());
        assertTrue(company.getRentRecordsAtDate(RENT_DATE, RENT_DATE.plusDays(RENT_DAYS)).isEmpty());

        RemoveCarData removeCar = company.removeCar(REG_NUMBER);
        assertNull(company.getCar(REG_NUMBER));

        assertTrue(company.getCarsByDriver(LICENSE).isEmpty());
        assertTrue(company.getDriversByCar(REG_NUMBER).isEmpty());
        assertTrue(company.getRentRecordsAtDate(RENT_DATE, RENT_DATE.plusDays(RENT_DAYS)).isEmpty());

        List<Car> modelCars = company.getCarsByModel(MODEL_NAME);
        //assertFalse(modelCars.contains(car));
        assertEquals(0, modelCars.size());


    }

    @Test
    void testRemoveCarTwice() {
        assertEquals(OK, company.addModel(model));
        assertEquals(OK, company.addCar(car));
        assertEquals(OK, company.addDriver(driver));

        RemoveCarData firstRemoveCar = company.removeCar(REG_NUMBER);
        assertNotNull(firstRemoveCar);
        assertEquals(car, firstRemoveCar.getCar());

        RemoveCarData secondRemoveCar = company.removeCar(REG_NUMBER);
        assertNull(secondRemoveCar);

    }

    @Test
    void testRemoveCarNotExists() {
        RemoveCarData removed = company.removeCar(REG_NUMBER + "X");
        assertNull(removed);

    }


    @Test
    void testReturnCarWithHighDamage() {
        assertEquals(OK, company.addModel(model));
        assertEquals(OK, company.addCar(car));
        assertEquals(OK, company.addDriver(driver));

        assertEquals(OK, company.rentCar(REG_NUMBER, LICENSE, RENT_DATE, RENT_DAYS));

        int damage = 70;
        RemoveCarData remData = company.returnCar(REG_NUMBER, LICENSE,
                RENT_DATE.plusDays(RENT_DAYS), damage, 50);

        assertTrue(remData.getCar().isFlRemove());
        assertEquals(car, remData.getCar());
        assertNotNull(remData.getRemovedRecords());
        assertFalse(remData.getRemovedRecords().isEmpty());

        assertNull(company.getCar(REG_NUMBER));
        assertTrue(company.getCarsByDriver(LICENSE).isEmpty());

    }

    @Test
    void testReturnCarWithLowDamage() {
        assertEquals(OK, company.addModel(model));
        assertEquals(OK, company.addCar(car));
        assertEquals(OK, company.addDriver(driver));

        assertEquals(OK, company.rentCar(REG_NUMBER, LICENSE, RENT_DATE, RENT_DAYS));

        int damage = 5;
        RemoveCarData remData = company.returnCar(REG_NUMBER, LICENSE,
                RENT_DATE.plusDays(RENT_DAYS), damage, 80);

        assertFalse(remData.getCar().isFlRemove());
        assertEquals(car, remData.getCar());
        assertNull(remData.getRemovedRecords());

    }

    @Test
    void testReturnCarWithoutRent() {
        assertEquals(OK, company.addModel(model));
        assertEquals(OK, company.addCar(car));
        assertEquals(OK, company.addDriver(driver));

        RemoveCarData remData = company.returnCar(REG_NUMBER, LICENSE,
                RENT_DATE.plusDays(RENT_DAYS), 10, 50);

        assertNull(remData.getCar());
        assertNull(remData.getRemovedRecords());


    }
    @Test
    void testReturnCar_AlreadyMarkedForRemoval() {
        assertEquals(OK, company.addModel(model));
        assertEquals(OK, company.addCar(car));
        assertEquals(OK, company.addDriver(driver));

        company.rentCar(REG_NUMBER, LICENSE, RENT_DATE, RENT_DAYS);

        car.setFlRemove(true);

        RemoveCarData remData = company.returnCar(REG_NUMBER, LICENSE,
                RENT_DATE.plusDays(RENT_DAYS), 10, 50);

        assertTrue(remData.getCar().isFlRemove());
        assertEquals(car, remData.getCar());

        assertNotNull(remData.getRemovedRecords());
        assertFalse(remData.getRemovedRecords().isEmpty());

        assertNull(company.getCar(REG_NUMBER));



    }
}
