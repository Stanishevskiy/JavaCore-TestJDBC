package test.connection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DriverConnection {

    public static void main(String[] args) {

        try {
            Connection conn = DriverManager.getConnection("jdbc:sqlite:testDB.db");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
