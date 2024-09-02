package model.dao;

import model.entities.Parking;

public interface ParkingDAO {
    Long insertParkingEntry(Parking parking);
    Parking getParkingEntryById(Long parkingId);
    Long getParkingId(Long vehicleId);
    boolean hasActiveParking(Long vehicleId);
    void updateParkingExit(Long vehicleId, int exitGate);
    Parking getParkingExitById(Long parkingId);
    void updateParkingValue(Long parkingId, Double valuePaid);

}
