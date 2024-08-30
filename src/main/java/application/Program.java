package application;

import model.dao.DaoFactory;
import model.dao.MonthlyDAO;
import model.entities.Monthly;
import model.entities.VehicleCategory;
import model.entities.VehicleType;

public class Program {
    public static void main(String[] args) {

        Long existingMonthly = DaoFactory.checkMonthy("1234567");

        System.out.println(existingMonthly);

        MonthlyDAO dao = DaoFactory.createMonthyDao();
        Monthly monthly = new Monthly("4334hgf", VehicleType.CARRO, VehicleCategory.MENSALISTA);
        dao.insert(monthly);
    }
}
