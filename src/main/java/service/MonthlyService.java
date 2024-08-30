package service;

import model.dao.DaoFactory;
import model.dao.MonthlyDAO;
import model.entities.Monthly;
import model.entities.VehicleCategory;
import model.entities.VehicleType;

public class MonthlyService {
    public void registerMonthly(String plate, int model) {
        if (plate.length() < 7 || plate.length() > 8) {
            throw new IllegalArgumentException("Placa inválida");
        }

        VehicleType vehicleType;
        if (model == 1) {
            vehicleType = VehicleType.CARRO;
        } else if (model == 2) {
            vehicleType = VehicleType.MOTO;
        } else {
            throw new IllegalArgumentException("Tipo de veículo inválido");
        }

        Long existingMonthly = DaoFactory.checkMonthy(plate);
        MonthlyDAO dao = DaoFactory.createMonthyDao();

        if (existingMonthly == null) {
            Monthly monthly = new Monthly(plate, vehicleType, VehicleCategory.MENSALISTA);
            dao.insert(monthly);
        } else {
            throw new IllegalArgumentException("Esse veiculo ja e mensalista");
        }
    }
}
