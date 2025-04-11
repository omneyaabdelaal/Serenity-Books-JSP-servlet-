package com.serviceImpl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.sql.DataSource;

import com.DatabaseConnection.DatabaseConnection;
import com.model.Users;
import com.service.UserServices;

import com.validation.UserDataValidationImpl;

public class UserServicesImpl implements UserServices {

    private final DatabaseConnection dbConnection;

    public UserServicesImpl(DataSource dataSource) {
        this.dbConnection = new DatabaseConnection(dataSource);
    }

    private boolean validateUserData(Users user, String desc) {
        UserDataValidationImpl validator = new UserDataValidationImpl(user);
        if (desc.equals("login")) {
            return validator.validateE_mail() && validator.validatePass();
        } else if (desc.equals("sign-up")) {
            return validator.validateName() && validator.validateE_mail() && validator.validatePass();
        }
        return false;
    }

    @Override
    public boolean login(Users user) {
        if (!validateUserData(user, "login")) {
            System.out.println(user.getE_mail()+">>>"+user.getPass());
            System.out.println("Login validation failed");
            return false;
        }

        String sql = "SELECT E_MAIL, PASS FROM USERS WHERE E_MAIL=? AND PASS=?";
        System.out.println("Attempting login for email: " + user.getE_mail());

        try (Connection conn = dbConnection.getConnection()) {
            if (conn == null) {
                System.out.println("Connection is null");
                return false;
            }
            
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setString(1, user.getE_mail());
                stmt.setString(2, user.getPass());
                
                System.out.println("Executing query: " + sql);
                System.out.println("With params: " + user.getE_mail() + ", " + user.getPass());
                
                try (ResultSet rs = stmt.executeQuery()) {
                    boolean hasResult = rs.next();
                    System.out.println("Login result: " + (hasResult ? "Success" : "Failed"));
                    
                    if (hasResult) {
                        // Populate user with the full data
                        user.setE_mail(rs.getString("E_MAIL"));
                        // Can add more fields here from the database if needed
                    }
                    
                    return hasResult;
                }
            }
        } catch (SQLException e) {
            System.err.println("Error during login: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean createAccount(Users user) {
        if (!validateUserData(user, "sign-up")) {
            System.out.println("Sign-up validation failed");
            return false;
        }

        // Check if email already exists - should return false if email exists
        if (checkEmailExists(user.getE_mail())) {
            System.out.println("Email already exists: " + user.getE_mail());
            return false;
        }

        String sql = "INSERT INTO USERS(F_NAME, L_NAME, E_MAIL, PASS) VALUES(?, ?, ?, ?)";
        System.out.println("Attempting to create account for: " + user.getF_name() + " " + user.getL_name());
        
        try (Connection conn = dbConnection.getConnection()) {
            if (conn == null) {
                System.out.println("Connection is null");
                return false;
            }
            
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setString(1, user.getF_name());
                stmt.setString(2, user.getL_name());
                stmt.setString(3, user.getE_mail());
                stmt.setString(4, user.getPass());
                
                System.out.println("Executing insert query");
                System.out.println("With params: " + user.getF_name() + ", " + user.getL_name() + 
                                 ", " + user.getE_mail() + ", " + user.getPass());
                
                int rowsAffected = stmt.executeUpdate();
                System.out.println("Rows affected: " + rowsAffected);
                return rowsAffected > 0;
            }
        } catch (SQLException e) {
            System.err.println("Error creating user account: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }

    private boolean checkEmailExists(String email) {
        String sql = "SELECT E_MAIL FROM USERS WHERE E_MAIL=?";
        System.out.println("Checking if email exists: " + email);

        try (Connection conn = dbConnection.getConnection()) {
            if (conn == null) {
                System.out.println("Connection is null");
                return false;
            }
            
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setString(1, email);
                
                System.out.println("Executing email check query");
                
                try (ResultSet rs = stmt.executeQuery()) {
                    boolean exists = rs.next();
                    System.out.println("Email exists: " + exists);
                    return exists;
                }
            }
        } catch (SQLException e) {
            System.err.println("Error checking email existence: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public Users getUserByEmail(String email) {
    	  String sql = "SELECT E_MAIL, PASS FROM USERS WHERE E_MAIL=?";
          System.out.println("Checking if email exists: " + email);

          try (Connection conn = dbConnection.getConnection()) {
              if (conn == null) {
                  System.out.println("Connection is null");
                  return null;
              }
              
              try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                  stmt.setString(1, email);
                  
                  System.out.println("Executing email check query");
                  
                  try (ResultSet rs = stmt.executeQuery()) {
                      if(rs.next()) {
                    	  Users user=new Users(
                    			  rs.getString("E_MAIL"),
                    			  rs.getString("PASS")
                    			  );
                    	  return user;
                      }
                      
                     
                  }
              }
          } catch (SQLException e) {
              System.err.println("Error checking email existence: " + e.getMessage());
              e.printStackTrace();
          }
          return null;

    }
}