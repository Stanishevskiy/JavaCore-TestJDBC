package test.connection;

import org.sqlite.SQLiteDataSource;

import java.sql.Connection;
import java.sql.SQLException;

public class DataSourceConnection {

    public static void main(String[] args) {

        try {
            SQLiteDataSource sqLiteDataSource = new SQLiteDataSource();
            Connection conn = sqLiteDataSource.getConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
