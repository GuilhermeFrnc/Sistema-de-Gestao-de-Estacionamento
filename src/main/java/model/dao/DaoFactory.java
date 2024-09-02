package model.dao;

import db.DB;
import model.dao.impl.MonthlyDaoJdbc;
import model.dao.impl.ParkingDaoJdbc;
import model.dao.impl.SlotDaoJdbc;
import model.dao.impl.VehicleDaoJdbc;
import model.entities.Parking;
import model.entities.Vehicle;
import model.enums.VehicleCategory;


public class DaoFactory {


    public static VehicleDAO checkVehicle(){
        return new VehicleDaoJdbc((DB.getConnection()));
    }

    public static Long checkMonthy(String plate){
        MonthlyDAO monthlyDao = new MonthlyDaoJdbc(DB.getConnection());
        return monthlyDao.getMonthlyByPlate(plate);
    }

    public static MonthlyDAO createMonthyDao(){
        return new MonthlyDaoJdbc(DB.getConnection());
    }

    public static Long getIdVehicleMonthly(String plate){
        MonthlyDAO monthlyDao = new MonthlyDaoJdbc(DB.getConnection());
        return monthlyDao.getIdVehicleMonthy(plate);
    }

    public static Vehicle getVehicleDb(Long id){
        VehicleDAO vehicleDAO = new VehicleDaoJdbc(DB.getConnection());
        return  vehicleDAO.getVehicleById(id);
    }

    public static Long createVehicle(Vehicle vehicle){
        VehicleDAO vehicleDAO = new VehicleDaoJdbc(DB.getConnection());
        return vehicleDAO.insertVehicle(vehicle);
    }

    public static Integer [] checkSlotMoto(VehicleCategory category){
        SlotDAO slotDAO =new SlotDaoJdbc(DB.getConnection());
        return slotDAO.findAvailableMotoSpot(category);
    }

    public static Integer [] checkSlotCar(VehicleCategory category){
        SlotDAO slotDAO =new SlotDaoJdbc(DB.getConnection());
        return slotDAO.findAvailableCarSpot(category);
    }


    public static Integer [] checkSlotTruck(VehicleCategory category){
        SlotDAO slotDAO =new SlotDaoJdbc(DB.getConnection());
        return slotDAO.findAvailableTruckSpot(category);
    }

    public static void occupySlots(Integer [] slotIds){
        SlotDAO slotDAO = new SlotDaoJdbc(DB.getConnection());
        slotDAO.occupySlots(slotIds);
    }

    public static void insertSlotOccupy(Long paking, Integer[] slotIds){
        SlotDAO slotDAO = new SlotDaoJdbc(DB.getConnection());
        slotDAO.associateSlotsWithParking(paking, slotIds);
    }

    public static Long entryParking(Parking parking){
        ParkingDAO parkingDAO = new ParkingDaoJdbc(DB.getConnection());
        return parkingDAO.insertParkingEntry(parking);
    }

    public static Parking getParkingEntryById(Long id){
        ParkingDAO parkingDAO = new ParkingDaoJdbc(DB.getConnection());
        return parkingDAO.getParkingEntryById(id);
    }

    public  static Long getIdParking(Long idVehicle){
        ParkingDAO parkingDAO = new ParkingDaoJdbc(DB.getConnection());
        return parkingDAO.getParkingId(idVehicle);
    }

    public static boolean searchVehicleRegistration(Long idVehicle){
        ParkingDAO parkingDAO = new ParkingDaoJdbc(DB.getConnection());
        return parkingDAO.hasActiveParking(idVehicle);
    }

    public static Long getVehicleIdByPlate(String plate){
        VehicleDAO vehicleDAO = new VehicleDaoJdbc(DB.getConnection());
        return vehicleDAO.getVehicleByPlate(plate);
    }

    public static void updateParkingExit(Long vehicleId, int exitGate){
        ParkingDAO parkingDAO = new ParkingDaoJdbc(DB.getConnection());
        parkingDAO.updateParkingExit(vehicleId, exitGate);
    }

    public static Parking getParkingExitById(Long parkingId){
        ParkingDAO parkingDAO = new ParkingDaoJdbc(DB.getConnection());
        return parkingDAO.getParkingExitById(parkingId);
    }

    public static Integer[] checkAndDisassociateSlotsFromParking(Long parkingId){
        SlotDAO slotDAO = new SlotDaoJdbc(DB.getConnection());
        return slotDAO.checkAndDisassociateSlotsFromParking(parkingId);
    }

    public static void unoccupySlots(Integer[] slotIds){
        SlotDAO slotDAO = new SlotDaoJdbc(DB.getConnection());
        slotDAO.unoccupySlots(slotIds);
    }

    public static void updateParkingValue(Long parkingId, Double valuePaid){
        ParkingDAO parkingDAO = new ParkingDaoJdbc(DB.getConnection());
        parkingDAO.updateParkingValue(parkingId, valuePaid);
    }


}
