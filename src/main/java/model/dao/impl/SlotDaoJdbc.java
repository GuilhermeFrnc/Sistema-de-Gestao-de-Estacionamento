package model.dao.impl;

import db.DbException;
import model.dao.SlotDAO;
import model.entities.VehicleCategory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class SlotDaoJdbc implements SlotDAO {

    private Connection conn;

    public SlotDaoJdbc(Connection conn) {
        this.conn = conn;
    }

    @Override
    public Integer findAvailableMotoSpot(VehicleCategory category) {
        String sql = "SELECT id_vaga FROM Vaga WHERE tipo = ? AND ocupada = FALSE ORDER BY id_vaga LIMIT 1";

        try (PreparedStatement st = conn.prepareStatement(sql)) {
            String typeSlot;

            if (category == VehicleCategory.MENSALISTA) {
                typeSlot = "Mensalista";
            } else {
                typeSlot = "Normal";
            }
            st.setString(1, typeSlot);

            try (ResultSet rs = st.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("id_vaga");
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
                "WHERE v1.tipo = ? AND v2.tipo = ? " +
                "AND v1.ocupada = FALSE AND v2.ocupada = FALSE " +
                "ORDER BY v1.id_vaga LIMIT 1";

        try (PreparedStatement st = conn.prepareStatement(sql)) {

            String typeSlot;

            if (category == VehicleCategory.MENSALISTA) {
                typeSlot = "Mensalista";
            } else {
                typeSlot = "Normal";
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

}
