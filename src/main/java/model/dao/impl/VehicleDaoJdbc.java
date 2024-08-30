package model.dao.impl;

import model.dao.VehicleDAO;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class VehicleDaoJdbc implements VehicleDAO {

    private Connection conn;

    public VehicleDaoJdbc(Connection conn) {
        if (conn == null) {
            throw new IllegalArgumentException("Connection cannot be null");
        }
        this.conn = conn;
    }

    @Override
    public Long getVehicleByPlate(String plate) {
        String sql = "SELECT id_veiculo FROM Veiculo WHERE placa = ?";
        Long idVehicle = null;

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, plate);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    idVehicle = rs.getLong("id_veiculo");
                } else {
                    return null;
                }
            }
        } catch (SQLException e) {
            System.err.println("SQL Exception: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Error fetching vehicle by plate", e);
        }

        return idVehicle;
    }
}
