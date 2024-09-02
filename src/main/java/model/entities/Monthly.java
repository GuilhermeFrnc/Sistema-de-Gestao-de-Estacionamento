package model.entities;

import model.enums.VehicleCategory;
import model.enums.VehicleType;

import java.util.Date;

public class Monthly extends Vehicle{
    private Date paymentDate;

    public Monthly(){}

    public Monthly(String s, VehicleType vehicleType, VehicleCategory vehicleCategory) {
        super(s, vehicleType, vehicleCategory);
    }


    public Date getPaymentDate() {
        return paymentDate;
    }

    public void setPaymentDate(Date paymentDate) {
        this.paymentDate = paymentDate;
    }
}
