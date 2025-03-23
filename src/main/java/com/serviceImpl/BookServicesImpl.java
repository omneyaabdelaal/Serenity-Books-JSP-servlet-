package com.serviceImpl;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.sql.DataSource;
import com.model.Book;
import com.service.BookServices;

public class BookServicesImpl implements BookServices {
    
    private DataSource dataSource;
    
    public BookServicesImpl(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public List<Book> getAllBooks() {
        List<Book> books = new ArrayList<>();
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        
        try {
            conn = dataSource.getConnection();
            String sql = "SELECT B.BOOKID, B.BOOKNAME, B.BOOKPRICE, B.TotalQuantatity, BD.DESCRIPTION, BD.ISSUE_DATE, BD.EXPIRY_DATE \r\n"
            		+ "FROM BOOKS B \r\n"
            		+ "LEFT OUTER JOIN BOOK_DETAILS BD ON B.BOOKID = BD.BOOKID";
            stmt = conn.prepareStatement(sql);
            rs = stmt.executeQuery();
            
            
            
            while (rs.next()) {
                     // This could be null
                
                books.add(new Book(
                    
                    rs.getInt(1),
                    rs.getString(2),
                    rs.getDouble(3),
                    rs.getInt(4),
                    rs.getString(5),  // If there's no matching row in BOOK_DETAILS, this will be null
                    rs.getDate(6),    // This will be null too
                    rs.getDate(7)   // Might be null
                ));
            }
        } catch (SQLException e) {
            System.err.println("Error retrieving all books: " + e.getMessage());
            e.printStackTrace();
        } finally {
            closeResources(rs, stmt, conn);
        }
        return books;
    }

    @Override
    public Book getBookByID(int id) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        
        try {
            conn = dataSource.getConnection();
            String sql = "SELECT B.BOOKID, B.BOOKNAME, B.BOOKPRICE, B.TotalQuantatity, " 
                    + "BD.DESCRIPTION, BD.EXPIRY_DATE, BD.ISSUE_DATE " 
                    + "FROM BOOKS B " 
                    + "INNER JOIN BOOK_DETAILS BD " 
                    + "ON B.BOOKID = BD.BOOKID " 
                    + "WHERE B.BOOKID = ?";

            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, id);
            rs = stmt.executeQuery();
            
            if (rs.next()) {
                return new Book(
                		rs.getInt(1),
                        rs.getString(2),
                        rs.getDouble(3),
                        rs.getInt(4),
                        rs.getString(5),  // If there's no matching row in BOOK_DETAILS, this will be null
                        rs.getDate(6),    // This will be null too
                        rs.getDate(7)
                );
            }
        } catch (SQLException e) {
            System.err.println("Error retrieving book by ID: " + e.getMessage());
            e.printStackTrace();
        } finally {
            closeResources(rs, stmt, conn);
        }
        return null;
    }
    
    ////////////////////////////////////////
    public Book getBookInfo(int id) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        
        try {
            conn = dataSource.getConnection();
            String sql = "SELECT * FROM BOOKS WHERE BookID = ?";
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, id);
            rs = stmt.executeQuery();
            
            if (rs.next()) {
                return new Book(
                    rs.getInt(1),
                    rs.getString(2),
                    rs.getDouble(3),
                    rs.getInt(4)
                );
            }
        } catch (SQLException e) {
            System.err.println("Error retrieving book by ID: " + e.getMessage());
            e.printStackTrace();
        } finally {
            closeResources(rs, stmt, conn);
        }
        return null;
    }

    ///////////////////////////////////////

    @Override
    public boolean addBook(Book book) {
        Connection conn = null;
        PreparedStatement stmt = null;
        boolean success = false;
        
        try {
            conn = dataSource.getConnection();
            conn.setAutoCommit(false); 
            
           
            String sql = "INSERT INTO BOOKS VALUES(?, ?, ?, ?)";
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, book.getId());
            stmt.setString(2, book.getName());
            stmt.setDouble(3, book.getPrice());
            stmt.setInt(4, book.getTotalquantity());
            int rowsAffected1 = stmt.executeUpdate();
            
            // Close first statement
            stmt.close();
            
            // Insert into BOOK_DETAILS table
            sql = "INSERT INTO BOOK_DETAILS (BookID, DESCRIPTION, ISSUE_DATE, EXPIRY_DATE) VALUES (?, ?, ?, ?)";
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, book.getId()); // Fixed: Use book ID for foreign key
            stmt.setString(2, book.getDesc());
            stmt.setDate(3, book.getIssue_date());
            stmt.setDate(4, book.getExpire_date());
            int rowsAffected2 = stmt.executeUpdate();
            
            // Commit transaction if both operations were successful
            if (rowsAffected1 > 0 && rowsAffected2 > 0) {
                conn.commit();
                success = true;
            } else {
                conn.rollback();
            }
            
        } catch (SQLException e) {
            try {
                if (conn != null) conn.rollback();
            } catch (SQLException ex) {
                System.err.println("Error rolling back transaction: " + ex.getMessage());
            }
            System.err.println("Error adding book: " + e.getMessage());
            e.printStackTrace();
        } finally {
            try {
                if (conn != null) conn.setAutoCommit(true);
            } catch (SQLException e) {
                System.err.println("Error resetting auto-commit: " + e.getMessage());
            }
            closeResources(null, stmt, conn);
        }
        
        return success;
    }
    
   ////////////////////////////////////// 
    @Override
	public boolean addBookDetails(Book book) {
		Connection conn = null;
        PreparedStatement stmt = null;
        boolean success = false;
        
        try {
            conn = dataSource.getConnection();
            conn.setAutoCommit(false); 
            
            // Insert into BOOK_DETAILS table
            String sql = "INSERT INTO BOOK_DETAILS (BookID, DESCRIPTION, ISSUE_DATE, EXPIRY_DATE) VALUES (?, ?, ?, ?)";
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, book.getId()); // Fixed: Use book ID for foreign key
            stmt.setString(2, book.getDesc());
            stmt.setDate(3, book.getIssue_date());
            stmt.setDate(4, book.getExpire_date());
            int rowsAffected = stmt.executeUpdate();
            
            if (rowsAffected > 0) {
                conn.commit();
                success = true;
            } else {
                conn.rollback();
            }
            
        } catch (SQLException e) {
            try {
                if (conn != null) conn.rollback();
            } catch (SQLException ex) {
                System.err.println("Error rolling back transaction: " + ex.getMessage());
            }
            System.err.println("Error adding book details: " + e.getMessage());
            e.printStackTrace();
        } finally {
            try {
                if (conn != null) conn.setAutoCommit(true);
            } catch (SQLException e) {
                System.err.println("Error resetting auto-commit: " + e.getMessage());
            }
            closeResources(null, stmt, conn);
        }
        
        return success;
	}
    
    //////////////////////////////////////////////
    
    
    @Override
    public boolean updateBook(Book book) {
        Connection conn = null;
        PreparedStatement stmt = null;
        boolean success = false;
        
        try {
            conn = dataSource.getConnection();
            conn.setAutoCommit(false); // Start transaction
            
            // Update BOOKS table
            String sql = "UPDATE BOOKS SET BookName = ?, BookPrice = ?, TotalQuantatity = ? WHERE BookID = ?";
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, book.getName());
            stmt.setDouble(2, book.getPrice());
            stmt.setInt(3, book.getTotalquantity());
            stmt.setInt(4, book.getId());
            int rowsAffected1 = stmt.executeUpdate();
            
            // Close first statement
            stmt.close();
            
            // Update BOOK_DETAILS table
            sql = "UPDATE BOOK_DETAILS SET DESCRIPTION = ?, ISSUE_DATE = ?, EXPIRY_DATE = ? WHERE BookID = ?";
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, book.getDesc());
            stmt.setDate(2, book.getIssue_date());
            stmt.setDate(3, book.getExpire_date());
            stmt.setInt(4, book.getId());
            int rowsAffected2 = stmt.executeUpdate();
            
            // Commit transaction if both operations were successful
            if (rowsAffected1 > 0 && rowsAffected2 > 0) {
                conn.commit();
                success = true;
            } else {
                conn.rollback();
            }
            
        } catch (SQLException e) {
            try {
                if (conn != null) conn.rollback();
            } catch (SQLException ex) {
                System.err.println("Error rolling back transaction: " + ex.getMessage());
            }
            System.err.println("Error updating book: " + e.getMessage());
            e.printStackTrace();
        } finally {
            try {
                if (conn != null) conn.setAutoCommit(true);
            } catch (SQLException e) {
                System.err.println("Error resetting auto-commit: " + e.getMessage());
            }
            closeResources(null, stmt, conn);
        }
        
        return success;
    }
    ////////////////////////////////////////////////
   
    @Override
	public boolean updateBookInfo(Book book) {
    	Connection conn = null;
        PreparedStatement stmt = null;
        boolean success = false;
        
        try {
            conn = dataSource.getConnection();
            conn.setAutoCommit(false); // Start transaction
            
            // Update BOOKS table
            String sql = "UPDATE BOOKS SET BookName = ?, BookPrice = ?, TotalQuantatity = ? WHERE BookID = ?";
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, book.getName());
            stmt.setDouble(2, book.getPrice());
            stmt.setInt(3, book.getTotalquantity());
            stmt.setInt(4, book.getId());
            int rowsAffected = stmt.executeUpdate();

            if (rowsAffected > 0) {
                conn.commit();
                success = true;
            } else {
                conn.rollback();
            }
            
        } catch (SQLException e) {
            try {
                if (conn != null) conn.rollback();
            } catch (SQLException ex) {
                System.err.println("Error rolling back transaction: " + ex.getMessage());
            }
            System.err.println("Error updating book: " + e.getMessage());
            e.printStackTrace();
        } finally {
            try {
                if (conn != null) conn.setAutoCommit(true);
            } catch (SQLException e) {
                System.err.println("Error resetting auto-commit: " + e.getMessage());
            }
            closeResources(null, stmt, conn);
        }
        
        return success;
	}
   
    ///////////////////////////////////////////////

    @Override
    public boolean deleteBookByID(int id) {
        Connection conn = null;
        PreparedStatement stmt = null;
        boolean success = false;
        
        try {
            conn = dataSource.getConnection();
            
            // Delete from BOOKS table (cascading delete should handle BOOK_DETAILS)
            String sql = "DELETE FROM BOOKS WHERE BookID = ?";
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, id);
            int rowsAffected = stmt.executeUpdate();
            
            success = (rowsAffected > 0);
            
        } catch (SQLException e) {
            System.err.println("Error deleting book: " + e.getMessage());
            e.printStackTrace();
        } finally {
            closeResources(null, stmt, conn);
        }
        
        return success;
    }
    
    
    @Override
	public boolean deleteBookDetailsByID(int id) {
    	  Connection conn = null;
          PreparedStatement stmt = null;
          boolean success = false;
          
          try {
              conn = dataSource.getConnection();
              
              // Delete from BOOKS table (cascading delete should handle BOOK_DETAILS)
              String sql = "DELETE FROM BOOK_DETAILS WHERE BookID = ?";
              stmt = conn.prepareStatement(sql);
              stmt.setInt(1, id);
              int rowsAffected = stmt.executeUpdate();
              
              success = (rowsAffected > 0);
              
          } catch (SQLException e) {
              System.err.println("Error deleting book: " + e.getMessage());
              e.printStackTrace();
          } finally {
              closeResources(null, stmt, conn);
          }
          
          return success;
	}
    
    // Helper method to close resources
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