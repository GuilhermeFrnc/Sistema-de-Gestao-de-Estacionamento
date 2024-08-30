package model.dao;

import db.DB;
import model.dao.impl.MonthlyDaoJdbc;
import model.dao.impl.VehicleDaoJdbc;


public class DaoFactory {


    public static VehicleDAO checkVehicle(){
        return new VehicleDaoJdbc((DB.getConnection()));
    }

    public static Long checkMonthy(String plate){
        MonthlyDAO monthlyDao = new MonthlyDaoJdbc(DB.getConnection());
        return monthlyDao.getMonthlyByPlate(plate);
    }
}
