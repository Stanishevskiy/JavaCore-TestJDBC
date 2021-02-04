package test.crud;

import java.sql.*;

public class ContactsTable {

    public static final String DB_NAME = "testDB.db";
    public static final String CONNECTION_STRING = "jdbc:sqlite:" + DB_NAME;

    public static final String TABLE_CONTACTS = "contacts";

    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_PHONE = "phone";
    public static final String COLUMN_EMAIL = "email";

    public static void main(String[] args) {

        try (Connection conn = DriverManager.getConnection(CONNECTION_STRING);
             Statement stmt = conn.createStatement()) {
            conn.setAutoCommit(false);
            stmt.execute("CREATE TABLE IF NOT EXISTS " + TABLE_CONTACTS + " ("
                    + COLUMN_NAME + " TEXT, "
                    + COLUMN_PHONE  + " INTEGER, "
                    + COLUMN_EMAIL + " TEXT)");
            stmt.execute("INSERT INTO " + TABLE_CONTACTS + " ("
                    + COLUMN_NAME + ", " + COLUMN_PHONE + ", " + COLUMN_EMAIL + ")" +
                    "VALUES ('mike', 12345, 'mike@mail.com')");
            stmt.execute("INSERT INTO " + TABLE_CONTACTS + " ("
                    + COLUMN_NAME + ", " + COLUMN_PHONE + ", " + COLUMN_EMAIL + ")" +
                    "VALUES ('nike', 23456, 'nike@mail.com')");
            stmt.execute("INSERT INTO " + TABLE_CONTACTS + " ("
                    + COLUMN_NAME + ", " + COLUMN_PHONE + ", " + COLUMN_EMAIL + ")" +
                    "VALUES ('tolstjak', 34567, 'tolstjak@mail.com')");
            stmt.execute("UPDATE " + TABLE_CONTACTS + " " +
                    "SET " + COLUMN_EMAIL + " = 'tolstjak100@mail.com'" +
                    "WHERE " + COLUMN_NAME + " = 'tolstjak'");
            ResultSet resultSet = stmt.executeQuery("SELECT * FROM " + TABLE_CONTACTS);
            while (resultSet.next()) {
                System.out.println(resultSet.getString(COLUMN_NAME) + " "
                        + resultSet.getInt(COLUMN_PHONE) + " "
                        + resultSet.getString(COLUMN_EMAIL));
            }
            resultSet.close();
            stmt.execute("DELETE FROM " + TABLE_CONTACTS
                    + " WHERE " + COLUMN_NAME + " = 'tolstjak'");
            stmt.execute("DROP TABLE IF EXISTS " + TABLE_CONTACTS);
            conn.commit();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
