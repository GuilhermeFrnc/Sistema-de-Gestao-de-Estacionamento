package model.entities;

import model.enums.VehicleCategory;
import model.enums.VehicleType;

public class Vehicle {
    private String plate;
    private VehicleType type;
    private VehicleCategory category;


    public Vehicle() {
    }

    public Vehicle(String plate, VehicleType type, VehicleCategory category) {
        this.plate = plate;
        this.type = type;
        this.category = category;
    }

    public String getPlate() {
        return plate;
    }

    public void setPlate(String plate) {
        this.plate = plate;
    }

    public VehicleType getType() {
        return type;
    }

    public void setType(VehicleType type) {
        this.type = type;
    }

    public VehicleCategory getCategory() {
        return category;
    }

    public void setCategory(VehicleCategory category) {
        this.category = category;
    }
}
