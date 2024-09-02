package model.dao.impl;

import model.dao.ParkingDAO;
import model.entities.Parking;

import java.sql.*;


public class ParkingDaoJdbc implements ParkingDAO {

    private Connection conn;

    public ParkingDaoJdbc(Connection conn) {
        this.conn = conn;
    }

    @Override
    public Long insertParkingEntry(Parking parking) {
        String insertParkingSql = "INSERT INTO Estacionamento (id_veiculo, data_entrada, id_cancela_entrada) VALUES (?, NOW(), ?)";
        long parkingId = -1;

        try (PreparedStatement insertParkingStmt = conn.prepareStatement(insertParkingSql, Statement.RETURN_GENERATED_KEYS)) {
            insertParkingStmt.setLong(1, parking.getIdVehicle());
            insertParkingStmt.setString(2, parking.getIdGate().toString());

            int affectedRows = insertParkingStmt.executeUpdate();

            if (affectedRows == 0) {
                throw new SQLException("Failed to insert parking entry.");
            }

            try (ResultSet generatedKeys = insertParkingStmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    parkingId = generatedKeys.getLong(1);
                } else {
                    throw new SQLException("Failed to obtain parking ID.");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Error inserting parking entry", e);
        }

        return parkingId;
    }

    @Override
    public Parking getParkingEntryById(Long parkingId) {
        String selectParkingSql = "SELECT id_veiculo, id_cancela_entrada, data_entrada FROM Estacionamento WHERE id_estacionamento = ?";
        Parking parking = null;

        try (PreparedStatement selectParkingStmt = conn.prepareStatement(selectParkingSql)) {
            selectParkingStmt.setLong(1, parkingId);
            try (ResultSet rs = selectParkingStmt.executeQuery()) {
                if (rs.next()) {
                    Long idVehicle = rs.getLong("id_veiculo");
                    Timestamp entryDate = rs.getTimestamp("data_entrada");
                    Integer idGate = rs.getInt("id_cancela_entrada");

                    parking = new Parking(idVehicle, idGate, entryDate);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Error retrieving parking entry", e);
        }

        return parking;
    }

    @Override
    public Long getParkingId(Long vehicleId) {
        String selectParkingSql = "SELECT id_estacionamento FROM Estacionamento WHERE id_veiculo = ? AND data_saida IS NULL";
        Long parkingId = null;

        try (PreparedStatement selectParkingStmt = conn.prepareStatement(selectParkingSql)) {
            selectParkingStmt.setLong(1, vehicleId);

            try (ResultSet rs = selectParkingStmt.executeQuery()) {
                if (rs.next()) {
                    parkingId = rs.getLong("id_estacionamento");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Error retrieving parking ID", e);
        }

        return parkingId;
    }

    @Override
    public boolean hasActiveParking(Long vehicleId) {
        String selectParkingSql = "SELECT id_estacionamento FROM Estacionamento WHERE id_veiculo = ? AND data_saida IS NULL";

        try (PreparedStatement selectParkingStmt = conn.prepareStatement(selectParkingSql)) {
            selectParkingStmt.setLong(1, vehicleId);

            try (ResultSet rs = selectParkingStmt.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Error checking active parking", e);
        }
    }

    @Override
    public void updateParkingExit(Long vehicleId, int exitGate) {
        String updateParkingSql = "UPDATE Estacionamento SET data_saida = NOW(), id_cancela_saida = ? WHERE id_veiculo = ? AND data_saida IS NULL";

        try (PreparedStatement updateParkingStmt = conn.prepareStatement(updateParkingSql)) {
            updateParkingStmt.setString(1, String.valueOf(exitGate));
            updateParkingStmt.setLong(2, vehicleId);

            int affectedRows = updateParkingStmt.executeUpdate();

            if (affectedRows == 0) {
                throw new SQLException("Failed to update parking exit. No matching entry found.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Error updating parking exit", e);
        }
    }

    @Override
    public Parking getParkingExitById(Long parkingId) {
        String selectParkingSql = "SELECT id_veiculo, id_cancela_saida, data_saida FROM Estacionamento WHERE id_estacionamento = ?";
        Parking parking = null;

        try (PreparedStatement selectParkingStmt = conn.prepareStatement(selectParkingSql)) {
            selectParkingStmt.setLong(1, parkingId);
            try (ResultSet rs = selectParkingStmt.executeQuery()) {
                if (rs.next()) {
                    Long idVehicle = rs.getLong("id_veiculo");
                    Timestamp exitDate = rs.getTimestamp("data_saida");
                    int exitGate = rs.getInt("id_cancela_saida");

                    parking = new Parking(idVehicle, exitGate, exitDate);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Error retrieving parking exit", e);
        }

        return parking;
    }

    @Override
    public void updateParkingValue(Long parkingId, Double valuePaid) {
        String updateParkingSql = "UPDATE Estacionamento SET valor_pago = ? WHERE id_estacionamento = ?";

        try (PreparedStatement updateParkingStmt = conn.prepareStatement(updateParkingSql)) {
            updateParkingStmt.setDouble(1, valuePaid);
            updateParkingStmt.setLong(2, parkingId);

            int affectedRows = updateParkingStmt.executeUpdate();

            if (affectedRows == 0) {
                throw new SQLException("Failed to update parking value. No matching entry found.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Error updating parking value", e);
        }
    }

}
