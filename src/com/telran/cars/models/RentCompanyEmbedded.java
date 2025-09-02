package com.telran.cars.models;

import com.telran.cars.dto.*;
import static com.telran.cars.dto.enums.CarsReturnCode.*;

import com.telran.cars.dto.enums.CarsReturnCode;
import com.telran.cars.dto.enums.State;
import com.telran.utils.Persistable;

import java.io.*;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.*;

public class RentCompanyEmbedded extends AbstractRenCompany implements Persistable {

    private static final int REMOVE_TRESHOLD = 60;
    private static final int BAD_TRASHOLD = 30;
    private static final int GOOD_TRASHOLD = 10;
    Map<String, Car> cars = new HashMap<>();// key - regNumber

    Map <Long, Driver> drivers = new HashMap<>();// key license Driver

    Map <String, Model> models = new HashMap<>(); //key model name

    Map<String , List<Car>> modelCars = new HashMap<>(); // key model name-> list cars

    Map <Long, List<RentRecord>> driverRecords = new HashMap<>(); // key  license id -> list records

    Map< String, List< RentRecord>> carRecords = new HashMap<>();// key regNumber

    Map<LocalDate, List<RentRecord>> records = new TreeMap<>();





    @Override
    public CarsReturnCode addModel(Model model) {
        return models.putIfAbsent(model.getModelName(), model)
                == null ? OK : MODEL_EXISTS;

    }

    @Override
    public Model getModel(String modelName) {
        return models.get(modelName);
    }

    @Override
    public CarsReturnCode addCar(Car car) {
        if (!models.containsKey(car.getModelName()))
            return NO_MODEL;
        boolean res = cars.putIfAbsent(car.getRegNumber(), car)==null;
        if(!res)
            return CAR_EXISTS;
        addModelCars(car);
        return OK;

    }

    private void addModelCars(Car car) {
        String modelName  = car.getModelName();
        List <Car> listCars = modelCars.getOrDefault(modelName, new ArrayList<>());
        listCars.add(car);
        modelCars.putIfAbsent(modelName, listCars);


    }

    @Override
    public Car getCar(String regNumber) {

        return cars.get(regNumber);
    }

    @Override
    public CarsReturnCode addDriver(Driver driver) {

        return drivers.putIfAbsent(driver.getLicenseId(), driver)
                == null ? OK: DRIVER_EXISTS;
    }

    @Override
    public Driver getDriver(long licenseId) {
        return drivers.get(licenseId);
    }

    @Override
    public CarsReturnCode rentCar(String regNumber,
                                  long licenseId,
                                  LocalDate rentDate,
                                  int rentDays) {
        Car car = getCar(regNumber);
        if( car == null) return NO_CAR;
        if( car.isFlRemove())return CAR_REMOVED;
        if(car.isInUse())return CAR_IN_USE;
        if(!drivers.containsKey(licenseId)) return NO_DRIVER;
        RentRecord record = new RentRecord(regNumber, licenseId, rentDate, rentDays);
        addToCarRecords(record);
        addToDriverRecords(record);
        addToRecords(record);
        car.setInUse(true);
        return OK;
    }

    private void addToDriverRecords(RentRecord record) {
        driverRecords.computeIfAbsent(record.getLicenseId(), k-> new ArrayList<>()).add(record);
    }

    private void addToCarRecords(RentRecord record) {
        carRecords.computeIfAbsent(record.getRegNumber(),k-> new ArrayList<>()).add(record);


    }

    private void addToRecords(RentRecord record) {
//        LocalDate rentDate = record.getRentDate();
//        List<RentRecord> listRecords = records.getOrDefault(rentDate, new ArrayList<>());
//        listRecords.add(record);
//        records.putIfAbsent(rentDate, listRecords);

        records.computeIfAbsent(record.getRentDate(), k-> new ArrayList<>()).add(record);



    }

    @Override
    public List<Car> getCarsByDriver(long licenseId) {
        List<RentRecord> listRecords = driverRecords.getOrDefault(licenseId, new ArrayList<>());
        return listRecords.stream()
                .map(r-> getCar(r.getRegNumber()))
                .distinct()
                .toList();
    }

    @Override
    public List<Driver> getDriversByCar(String regNumber) {
        List<RentRecord> listRecords = carRecords.getOrDefault(regNumber, new ArrayList<>());
        return listRecords.stream()
                .map(r-> getDriver(r.getLicenseId()))
                .distinct()
                .toList();
    }

    @Override
    public List<Car> getCarsByModel(String modelName) {
        List<Car> res = modelCars.getOrDefault(modelName, new ArrayList<>());

        return res.stream()
                .filter(c-> !c.isFlRemove()&& !c.isInUse())
                .toList();
    }

    @Override
    public List<RentRecord> getRentRecordsAtDate(LocalDate from, LocalDate to) {
        Collection<List<RentRecord>> res = ((TreeMap<LocalDate, List<RentRecord>>)records).subMap(from, to).values();
        return res.stream()
                .flatMap(i -> i.stream())
                .toList();
    }

    @Override
    public RemoveCarData removeCar(String regNumber) {
        Car car = getCar(regNumber);
        if(car == null || car.isFlRemove())
            return null;
        car.setFlRemove(true);
        return car.isInUse() ? new RemoveCarData(car, null)
                : actualCarRemove(car);

    }


    private RemoveCarData actualCarRemove(Car car) {
        String regNumber = car.getRegNumber();
        List<RentRecord> removedRecords = carRecords.get(regNumber);
        if(removedRecords != null && !removedRecords.isEmpty()){
            removeFromDriverRecords(removedRecords);
            removeFromRecords(removedRecords);

        }
        cars.remove(regNumber);
        carRecords.remove(regNumber);
        String modelName = car.getModelName();
        List<Car> list = modelCars.get(modelName);
        if(list !=null)
            list.removeIf(c-> regNumber.equals(c.getRegNumber()));


        return new RemoveCarData(car, removedRecords == null ? new ArrayList<>(): removedRecords);

    }

    private void removeFromRecords(List<RentRecord> removedRecords) {
        if(removedRecords == null) return;
        removedRecords.forEach(r-> {
            List<RentRecord> daylist = records.get(r.getRentDate());
            if (daylist != null) daylist.remove(r);
        });
    }

    private void removeFromDriverRecords(List<RentRecord> removedRecords) {
        if(removedRecords == null) return;
        removedRecords.forEach(r->{
            List<RentRecord> drList = driverRecords.get(r.getLicenseId());
            if(drList != null) drList.remove(r);
        });

    }

    @Override
    public List<RemoveCarData> removeModel(String modelName) {
        List<Car> carsModel = modelCars.getOrDefault(modelName, new ArrayList<>());

        return carsModel.stream()
                .filter(c-> c.isFlRemove())
                .map(c-> removeCar(c.getRegNumber()))
                .filter(Objects :: nonNull)
                .toList();
    }

    @Override
    public RemoveCarData returnCar(String regNumber, long licenseId, LocalDate returnDate, int damage, int tankPercent) {
        RentRecord record = getRentRecord(regNumber, licenseId);
        if (record == null)
            return new RemoveCarData(null, null);
        Car car = getCar(regNumber);
        updateRecord(record, returnDate, damage, tankPercent);
        car.setInUse(false);
        updateCar(car, damage);
        if (damage > REMOVE_TRESHOLD || car.isFlRemove()) {
            car.setFlRemove(true);
            RemoveCarData remCarData = actualCarRemove(car);
            return remCarData != null ? remCarData : new RemoveCarData(car, new ArrayList<>());
        }
        return  new RemoveCarData(car, null);
    }

    private void updateRecord(RentRecord record, LocalDate returnDate, int damage, int tankPercent) {
        record.setDamages(damage);
        record.setTankPercent(tankPercent);
        record.setReturnDate(returnDate);

        double cost = computeCost(getRentPricePerDay(record.getRegNumber()),
                record.getRentDays(), getDaysDelay(record), tankPercent, getTankVolume(record.getRegNumber()));

    }

    private int getTankVolume(String regNumber) {
        String modelName = cars.get(regNumber).getModelName();
        return models.get(modelName).getGasTank();

    }

    private int getDaysDelay(RentRecord record) {
        long actualDays = ChronoUnit.DAYS.between(record.getRentDate(), record.getReturnDate());
        int delta = (int)(actualDays-record.getRentDays());
        return delta <= 0 ? 0: delta;
    }

    private int getRentPricePerDay(String regNumber) {
        String modelName = cars.get(regNumber).getModelName();
        return  models.get (modelName).getPriceDay();
    }

    private void updateCar(Car car, int damage) {
        car.setInUse(false);
        if(damage > BAD_TRASHOLD) car.setState(State.BAD);
        else if(damage >= GOOD_TRASHOLD) car.setState(State.GOOD);
        else car.setState(State.EXELLENT);

    }


    private RentRecord getRentRecord(String regNumber, long licenseId) {
        List<RentRecord> list= driverRecords.get(licenseId);
        if( list == null) return null;
        return list. stream()
                .filter(r-> r.getRegNumber().equals(regNumber)
                        && r.getReturnDate() == null).findFirst().orElse(null);
    }

    @Override
    public void save(String fileName) {
        try(ObjectOutputStream outputStream
                    = new ObjectOutputStream
                (new FileOutputStream(fileName))){
            outputStream.writeObject(this);
        } catch (IOException e) {
            System.out.println("Error in method save " + e.getMessage());
        }


    }
    public static IRentCompany restoreFromFile(String fileName){
        try(ObjectInputStream inputStream =
                new ObjectInputStream
                        (new FileInputStream(fileName))){
            return (IRentCompany) inputStream.readObject();
        } catch (Exception e) {
            System.out.println(fileName+ "new object has been created"+ e.getMessage());
            return new RentCompanyEmbedded();

        }
    }


}
