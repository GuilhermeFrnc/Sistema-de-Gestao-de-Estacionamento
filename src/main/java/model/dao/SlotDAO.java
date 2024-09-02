package model.dao;

import model.enums.VehicleCategory;

public interface SlotDAO {
    Integer [] findAvailableMotoSpot(VehicleCategory category);
    Integer[] findAvailableCarSpot(VehicleCategory category);
    Integer[] findAvailableTruckSpot(VehicleCategory category);
    void occupySlots(Integer [] slotIds);
    void associateSlotsWithParking(Long parkingId, Integer[] slotIds);
    Integer[] checkAndDisassociateSlotsFromParking(Long parkingId);
    void unoccupySlots(Integer[] slotIds);
}
