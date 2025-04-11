package com.DatabaseConnection;


import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

public class DatabaseConnection {
    private final DataSource dataSource;
    
    public DatabaseConnection(DataSource dataSource) {
        this.dataSource = dataSource;
    }
    
    public Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }
}