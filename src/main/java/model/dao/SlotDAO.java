package model.dao;

import model.enums.VehicleCategory;

public interface SlotDAO {
    Integer findAvailableMotoSpot(VehicleCategory category);
    Integer[] findAvailableCarSpot(VehicleCategory category);
    Integer[] findAvailableTruckSpot(VehicleCategory category);
}
