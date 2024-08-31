package model.dao;

import db.DB;
import model.dao.impl.MonthlyDaoJdbc;
import model.dao.impl.SlotDaoJdbc;
import model.dao.impl.VehicleDaoJdbc;
import model.entities.VehicleCategory;


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

    public static Integer checkSlotMoto(VehicleCategory category){
        SlotDAO slotDAO =new SlotDaoJdbc(DB.getConnection());
        return slotDAO.findAvailableMotoSpot(category);
    }
}
