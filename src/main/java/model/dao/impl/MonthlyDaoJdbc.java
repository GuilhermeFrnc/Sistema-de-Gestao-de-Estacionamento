package model.dao.impl;

import model.dao.DaoFactory;
import model.dao.MonthlyDAO;
import model.dao.VehicleDAO;
import model.entities.Monthly;

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

    @Override
    public Monthly insert(Monthly monthly) {
        String insertVehicleSql = "INSERT INTO Veiculo (placa, tipo, categoria) VALUES (?, ?, ?)";
        String insertMonthlySql = "INSERT INTO Mensalidade (id_veiculo, valor, data_pagamento) VALUES (?, 80.00, NOW())";

        try {
            conn.setAutoCommit(false);

            try (PreparedStatement insertVehicleStmt = conn.prepareStatement(insertVehicleSql, Statement.RETURN_GENERATED_KEYS)) {
                insertVehicleStmt.setString(1, monthly.getPlate());
                insertVehicleStmt.setString(2, monthly.getType().toString());
                insertVehicleStmt.setString(3, monthly.getCategory().toString());
                int affectedRows = insertVehicleStmt.executeUpdate();

                if (affectedRows == 0) {
                    throw new SQLException("Failed to insert vehicle.");
                }

                long vehicleId;
                try (ResultSet generatedKeys = insertVehicleStmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        vehicleId = generatedKeys.getLong(1);
                    } else {
                        throw new SQLException("Failed to obtain vehicle ID.");
                    }
                }

                try (PreparedStatement insertMensalidadeStmt = conn.prepareStatement(insertMonthlySql)) {
                    insertMensalidadeStmt.setLong(1, vehicleId);
                    insertMensalidadeStmt.executeUpdate();
                }
            }

            conn.commit();

        } catch (SQLException e) {
            e.printStackTrace();
            try {
                conn.rollback();
            } catch (SQLException rollbackEx) {
                rollbackEx.printStackTrace();
            }
            throw new RuntimeException("Error inserting monthly data", e);
        }

        return monthly;
    }

}
