package model.entities;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Parking {
    private Long idVehicle;
    private Integer idGate;
    private Date date = null;

    public Parking( Integer idGate) {
        this.idGate = idGate;
    }

    public Parking(Long idVehicle, Integer idGate) {
        this.idVehicle = idVehicle;
        this.idGate = idGate;
    }

    public Parking(Long idVehicle, Integer idGate, Date date) {
        this.idVehicle = idVehicle;
        this.idGate = idGate;
        this.date =date;
    }

    public Long getIdVehicle() {
        return idVehicle;
    }

    public void setIdVehicle(Long idVehicle) {
        this.idVehicle = idVehicle;
    }


    public Integer getIdGate() {
        return idGate;
    }

    public void setIdGate(Integer idGate) {
        this.idGate = idGate;
    }

    public Date getDate() {
        return date;
    }

    public String getFormattedDate() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        return dateFormat.format(date);
    }

    public String getFormattedTime() {
        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");
        return timeFormat.format(date);
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
