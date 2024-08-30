package model.dao.impl;

import model.dao.DaoFactory;
import model.dao.MonthlyDAO;
import model.dao.VehicleDAO;

import java.sql.*;

public class MonthlyDaoJdbc implements MonthlyDAO {
    private Connection conn;

    public MonthlyDaoJdbc(Connection conn) {
        this.conn = conn;
    }


    @Override
    public Long getMonthlyByPlate(String plate) {
        VehicleDAO vehicleDao = DaoFactory.checkVehicle();
        Long idVehicle = vehicleDao.getVehicleByPlate(plate);

        if (idVehicle == null){
            return null;
        }

        String sql = "SELECT id_mensalidade FROM Mensalidade WHERE id_veiculo = ?";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, idVehicle);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getLong("id_mensalidade");
                } else {
                    return null;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Erro ao verificar status do mensalista", e);
        }


    }
}
