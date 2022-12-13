package me.picro.myauth;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Database {

    // settings
    private final String HOST;
    private final int PORT;
    private final String DATABASE;
    private final String USERNAME;
    private final String PASSWORD;

    private Connection connection;

    public Database(String HOST, int PORT, String DATABASE, String USERNAME, String PASSWORD) {
        this.HOST = HOST;
        this.PORT = PORT;
        this.DATABASE = DATABASE;
        this.USERNAME = USERNAME;
        this.PASSWORD = PASSWORD;
    }

    public void connect() throws SQLException {
        connection = DriverManager.getConnection(
                "jdbc:mysql://" + HOST + ":" + PORT + "/" + DATABASE + "?useSSL=false&autoReconnect=true",
                USERNAME,
                PASSWORD
        );
    }

    public boolean isConnected() {
        return connection != null;
    }

    public Connection getConnection() {
        return connection;
    }

    public void disconnect() {
        if (isConnected()) {
            try {
                connection.close();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }

}
