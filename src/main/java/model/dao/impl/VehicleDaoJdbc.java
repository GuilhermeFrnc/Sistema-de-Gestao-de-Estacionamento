package model.dao.impl;

import model.dao.VehicleDAO;
import model.entities.Vehicle;
import model.enums.VehicleCategory;
import model.enums.VehicleType;

import java.sql.*;

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

    @Override
    public Vehicle getVehicleById(Long id) {
        String sql = "SELECT placa, tipo, categoria FROM Veiculo WHERE id_veiculo = ?";
        Vehicle vehicle = null;

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, id);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    String placa = rs.getString("placa");
                    VehicleType tipo = VehicleType.valueOf(rs.getString("tipo"));
                    VehicleCategory categoria = VehicleCategory.valueOf(rs.getString("categoria"));

                    vehicle = new Vehicle(placa, tipo, categoria);
                } else {
                    return null;
                }
            }
        } catch (SQLException e) {
            System.err.println("SQL Exception: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Error fetching vehicle by id", e);
        }

        return vehicle;
    }

    @Override
    public Long insertVehicle(Vehicle vehicle) {
        String insertVehicleSql = "INSERT INTO Veiculo (placa, tipo, categoria) VALUES (?, ?, ?)";
        long vehicleId = -1;

        try (PreparedStatement insertVehicleStmt = conn.prepareStatement(insertVehicleSql, Statement.RETURN_GENERATED_KEYS)) {
            insertVehicleStmt.setString(1, vehicle.getPlate());
            insertVehicleStmt.setString(2, vehicle.getType().toString());
            insertVehicleStmt.setString(3, vehicle.getCategory().toString());
            int affectedRows = insertVehicleStmt.executeUpdate();

            if (affectedRows == 0) {
                throw new SQLException("Failed to insert vehicle.");
            }

            try (ResultSet generatedKeys = insertVehicleStmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    vehicleId = generatedKeys.getLong(1);
                } else {
                    throw new SQLException("Failed to obtain vehicle ID.");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Error inserting vehicle", e);
        }

        return vehicleId;


    }

    @Override
    public Long getVehicleIdByPlate(String plate) {
        String getVehicleIdSql = "SELECT id FROM Veiculo WHERE placa = ?";
        long vehicleId = -1;

        try (PreparedStatement getVehicleIdStmt = conn.prepareStatement(getVehicleIdSql)) {
            getVehicleIdStmt.setString(1, plate);
            try (ResultSet resultSet = getVehicleIdStmt.executeQuery()) {
                if (resultSet.next()) {
                    vehicleId = resultSet.getLong("id");
                } else {
                    throw new SQLException("Vehicle with plate " + plate + " not found.");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Error retrieving vehicle ID", e);
        }

        return vehicleId;
    }
}
