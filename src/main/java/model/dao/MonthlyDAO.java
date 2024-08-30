package model.dao;

import model.entities.Monthly;

public interface MonthlyDAO {
    Long getMonthlyByPlate(String plate);
    Monthly insert(Monthly monthly);
}
