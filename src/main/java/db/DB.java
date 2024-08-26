package db;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class DB {
    private static Connection conn = null;

    public static Connection getConn() {
        if (conn == null) {
            try {
                Properties props = loadProperties();
                String url = props.getProperty("dburl");
                conn = DriverManager.getConnection(url, props);
            } catch (SQLException e) {
                throw new DbException(e.getMessage());
            }
        }
        return conn;
    }

    public static void closeConnection() {
        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException e) {
                throw new DbException(e.getMessage());
            }
        }
    }

    private static Properties loadProperties() {
        try (InputStream is = DB.class.getClassLoader().getResourceAsStream("db.properties")) {
            Properties props = new Properties();
            if (is == null) {
                throw new DbException("Arquivo db.properties não encontrado");
            }
            props.load(is);
            return props;
        } catch (IOException e) {
            throw new DbException(e.getMessage());
        }
    }
}
