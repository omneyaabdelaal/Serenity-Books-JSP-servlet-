package com.DatabaseService;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

public class DatabaseService {
    private static DatabaseService instance;
    private DataSource dataSource;
    
    private DatabaseService() {
        try {
            Context initContext = new InitialContext();
            Context envContext = (Context) initContext.lookup("java:comp/env");
            dataSource = (DataSource) envContext.lookup("jdbc/item");
        } catch (NamingException e) {
            e.printStackTrace();
        }
    }
    
    public static synchronized DatabaseService getInstance() {
        if (instance == null) {
            instance = new DatabaseService();
        }
        return instance;
    }
    
    public DataSource getDataSource() {
        return dataSource;
    }
}