package com.serviceImpl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.sql.DataSource;
import com.model.Users;
import com.service.UserServices;
import com.validation.UserDataValidationImpl;

public class UserServicesImpl implements UserServices {
    
    private DataSource dataSource;
    
    public UserServicesImpl(DataSource dataSource) {
        this.dataSource = dataSource;
    }
    
    @Override
    public boolean login(Users user) {
  
        UserDataValidationImpl validator = new UserDataValidationImpl(user);
        
 
        if (!validator.validateE_mail() || !validator.validatePass()) {
            return false;
        }
        
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        
        try {
            conn = dataSource.getConnection();
            String sql = "SELECT E_MAIL, PASS FROM USERS WHERE E_MAIL=? AND PASS=?";
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, user.getE_mail());
            stmt.setString(2, user.getPass());
            rs = stmt.executeQuery();
            
            return rs.next(); // Returns true if user exists with matching credentials
        } catch (SQLException e) {
            System.err.println("Error during login: " + e.getMessage());
            e.printStackTrace();
        } finally {
            closeResources(rs, stmt, conn);
        }
        
        return false;
    }
    
    @Override
    public boolean createAccount(Users user) {
 
        UserDataValidationImpl validator = new UserDataValidationImpl(user);

        if (!validator.validateName() || !validator.validateE_mail() || !validator.validatePass()) {
            return false;
        }
        
        Connection conn = null;
        PreparedStatement stmt = null;
        
        try {
            conn = dataSource.getConnection();
            String sql = "INSERT INTO USERS(F_NAME, L_NAME, E_MAIL, PASS) VALUES(?, ?, ?, ?)";
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, user.getF_name());
            stmt.setString(2, user.getL_name());
            stmt.setString(3, user.getE_mail());
            stmt.setString(4, user.getPass());
            
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
            
        } catch (SQLException e) {
            System.err.println("Error creating user account: " + e.getMessage());
            e.printStackTrace();
        } finally {
            closeResources(null, stmt, conn);
        }
        
        return false;
    }
    
    private void closeResources(ResultSet rs, PreparedStatement stmt, Connection conn) {
        try {
            if (rs != null) rs.close();
            if (stmt != null) stmt.close();
            if (conn != null) conn.close();
        } catch (SQLException e) {
            System.err.println("Error closing resources: " + e.getMessage());
            e.printStackTrace();
        }
    }
}