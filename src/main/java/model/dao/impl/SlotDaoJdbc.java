package model.dao.impl;

import db.DbException;
import model.dao.SlotDAO;
import model.enums.VehicleCategory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class SlotDaoJdbc implements SlotDAO {

    private Connection conn;

    public SlotDaoJdbc(Connection conn) {
        this.conn = conn;
    }

    @Override
    public Integer [] findAvailableMotoSpot(VehicleCategory category) {
        String sql = "SELECT id_vaga FROM Vaga WHERE categoria = ? AND ocupada = FALSE ORDER BY id_vaga LIMIT 1";

        try (PreparedStatement st = conn.prepareStatement(sql)) {
            String typeSlot;

            if (category == VehicleCategory.MENSALISTA) {
                typeSlot = "Mensalista";
            } else {
                typeSlot = "Avulso";
            }
            st.setString(1, typeSlot);

            try (ResultSet rs = st.executeQuery()) {
                if (rs.next()) {
                    return new Integer[]{rs.getInt("id_vaga")};
                }
            }
        } catch (Exception e) {
            throw new DbException(e.getMessage());
        }

        return null;
    }

    @Override
    public Integer [] findAvailableCarSpot(VehicleCategory category) {
        String sql = "SELECT v1.id_vaga AS vaga1, v2.id_vaga AS vaga2 " +
                "FROM Vaga v1 JOIN Vaga v2 ON v1.id_vaga + 1 = v2.id_vaga " +
                "WHERE v1.categoria = ? AND v2.categoria = ? " +
                "AND v1.ocupada = FALSE AND v2.ocupada = FALSE " +
                "ORDER BY v1.id_vaga LIMIT 1";

        try (PreparedStatement st = conn.prepareStatement(sql)) {

            String typeSlot;

            if (category == VehicleCategory.MENSALISTA) {
                typeSlot = "Mensalista";
            } else {
                typeSlot = "Avulso";
            }

            st.setString(1, typeSlot);
            st.setString(2, typeSlot);


            try (ResultSet rs = st.executeQuery()) {
                if (rs.next()) {
                    return new Integer[] { rs.getInt("vaga1"), rs.getInt("vaga2") };
                }
            }
        } catch (Exception e) {
            throw new DbException(e.getMessage());
        }

        return null;
    }

    @Override
    public Integer[] findAvailableTruckSpot(VehicleCategory category) {
        String sql = "SELECT v1.id_vaga AS vaga1, v2.id_vaga AS vaga2, v3.id_vaga AS vaga3, v4.id_vaga AS vaga4 " +
                "FROM Vaga v1 " +
                "JOIN Vaga v2 ON v1.id_vaga + 1 = v2.id_vaga " +
                "JOIN Vaga v3 ON v2.id_vaga + 1 = v3.id_vaga " +
                "JOIN Vaga v4 ON v3.id_vaga + 1 = v4.id_vaga " +
                "WHERE v1.categoria = ? AND v2.categoria = ? AND v3.categoria = ? AND v4.categoria = ? " +
                "AND v1.ocupada = FALSE AND v2.ocupada = FALSE AND v3.ocupada = FALSE AND v4.ocupada = FALSE " +
                "ORDER BY v1.id_vaga LIMIT 1";

        try (PreparedStatement st = conn.prepareStatement(sql)) {

            String typeSlot;

            if (category == VehicleCategory.MENSALISTA) {
                typeSlot = "Mensalista";
            } else {
                typeSlot = "Avulso";
            }

            st.setString(1, typeSlot);
            st.setString(2, typeSlot);
            st.setString(3, typeSlot);
            st.setString(4, typeSlot);

            try (ResultSet rs = st.executeQuery()) {
                if (rs.next()) {
                    return new Integer[]{rs.getInt("vaga1"), rs.getInt("vaga2"), rs.getInt("vaga3"), rs.getInt("vaga4")};
                }
            }
        } catch (Exception e) {
            throw new DbException(e.getMessage());
        }

        return null;
    }

    @Override
    public void occupySlots(Integer[] slotIds) {
        String updateSlotSql = "UPDATE Vaga SET ocupada = TRUE WHERE id_vaga = ?";

        try (PreparedStatement updateSlotStmt = conn.prepareStatement(updateSlotSql)) {
            for (int slotId : slotIds) {
                updateSlotStmt.setInt(1, slotId);
                int affectedRows = updateSlotStmt.executeUpdate();

                if (affectedRows == 0) {
                    throw new SQLException("Failed to update slot with id: " + slotId);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Error updating slots to occupied", e);
        }
    }

    @Override
    public void associateSlotsWithParking(Long parkingId, Integer[] slotIds) {
        String insertSlotOccupySql = "INSERT INTO Vaga_Ocupacao (id_vaga, id_estacionamento) VALUES (?, ?)";

        try (PreparedStatement insertSlotOccupyStmt = conn.prepareStatement(insertSlotOccupySql)) {
            for (int slotId : slotIds) {
                insertSlotOccupyStmt.setInt(1, slotId);
                insertSlotOccupyStmt.setLong(2, parkingId);
                int affectedRows = insertSlotOccupyStmt.executeUpdate();

                if (affectedRows == 0) {
                    throw new SQLException("Failed to associate slot with id: " + slotId + " to parking with id: " + parkingId);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Error associating slots with parking", e);
        }
    }

}
