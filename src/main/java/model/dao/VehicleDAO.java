package model.dao;

import model.entities.Vehicle;

public interface VehicleDAO {
    Long getVehicleByPlate(String plate);
    Vehicle getVehicleById(Long id);
    Long insertVehicle(Vehicle vehicle);
    Long getVehicleIdByPlate(String plate);
}
