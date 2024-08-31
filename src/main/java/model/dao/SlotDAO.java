package model.dao;

import model.entities.VehicleCategory;

import java.util.Optional;

public interface SlotDAO {
    Integer findAvailableMotoSpot(VehicleCategory category);
}
