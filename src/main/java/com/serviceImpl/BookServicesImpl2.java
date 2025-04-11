package com.serviceImpl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.sql.DataSource;

import com.DatabaseConnection.DatabaseConnection;
import com.model.Book;
import com.service.BookServices;

public class BookServicesImpl2 implements BookServices {
    
    private final DatabaseConnection dbConnection;

    public BookServicesImpl2(DataSource dataSource) {
        this.dbConnection = new DatabaseConnection(dataSource);
    }

    @Override
    public List<Book> getAllBooks() {
        List<Book> books = new ArrayList<>();
        String sql = "SELECT B.BOOKID, B.BOOKNAME, B.BOOKPRICE, B.TotalQuantatity, "
                   + "BD.DESCRIPTION, BD.ISSUE_DATE, BD.EXPIRY_DATE "
                   + "FROM BOOKS B "
                   + "LEFT OUTER JOIN BOOK_DETAILS BD ON B.BOOKID = BD.BOOKID";
        
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                books.add(new Book(
                    rs.getInt("BOOKID"),
                    rs.getString("BOOKNAME"),
                    rs.getDouble("BOOKPRICE"),
                    rs.getInt("TotalQuantatity"),
                    rs.getString("DESCRIPTION"),
                    rs.getDate("ISSUE_DATE"),
                    rs.getDate("EXPIRY_DATE")
                ));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to retrieve all books", e);
        }
        return books;
    }

    @Override
    public Book getBookByID(int id) {
        String sql = "SELECT B.BOOKID, B.BOOKNAME, B.BOOKPRICE, B.TotalQuantatity, "
                   + "BD.DESCRIPTION, BD.EXPIRY_DATE, BD.ISSUE_DATE "
                   + "FROM BOOKS B "
                   + "INNER JOIN BOOK_DETAILS BD "
                   + "ON B.BOOKID = BD.BOOKID "
                   + "WHERE B.BOOKID = ?";
        
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, id);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new Book(
                        rs.getInt("BOOKID"),
                        rs.getString("BOOKNAME"),
                        rs.getDouble("BOOKPRICE"),
                        rs.getInt("TotalQuantatity"),
                        rs.getString("DESCRIPTION"),
                        rs.getDate("ISSUE_DATE"),
                        rs.getDate("EXPIRY_DATE")
                    );
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to retrieve book with ID: " + id, e);
        }
        return null;
    }

    public Book getBookInfo(int id) {
        String sql = "SELECT * FROM BOOKS WHERE BookID = ?";
        
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, id);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new Book(
                        rs.getInt("BookID"),
                        rs.getString("BookName"),
                        rs.getDouble("BookPrice"),
                        rs.getInt("TotalQuantatity")
                    );
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to retrieve basic book info for ID: " + id, e);
        }
        return null;
    }

    @Override
    public boolean addBook(Book book) {
        if (book == null) {
            throw new IllegalArgumentException("Book cannot be null");
        }
        
        try (Connection conn = dbConnection.getConnection()) {
            conn.setAutoCommit(false);
            
            try {
                // Insert into BOOKS table
                try (PreparedStatement stmt = conn.prepareStatement(
                        "INSERT INTO BOOKS VALUES(?, ?, ?, ?)")) {
                    stmt.setInt(1, book.getId());
                    stmt.setString(2, book.getName());
                    stmt.setDouble(3, book.getPrice());
                    stmt.setInt(4, book.getTotalquantity());
                    stmt.executeUpdate();
                }
                
                // Insert into BOOK_DETAILS table
                try (PreparedStatement detailStmt = conn.prepareStatement(
                        "INSERT INTO BOOK_DETAILS (BookID, DESCRIPTION, ISSUE_DATE, EXPIRY_DATE) VALUES (?, ?, ?, ?)")) {
                    detailStmt.setInt(1, book.getId());
                    detailStmt.setString(2, book.getDesc());
                    detailStmt.setDate(3, book.getIssue_date());
                    detailStmt.setDate(4, book.getExpire_date());
                    detailStmt.executeUpdate();
                }
                
                conn.commit();
                return true;
            } catch (SQLException e) {
                conn.rollback();
                throw new RuntimeException("Failed to add book with ID: " + book.getId(), e);
            } finally {
                conn.setAutoCommit(true);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Database connection error while adding book", e);
        }
    }

    @Override
    public boolean addBookDetails(Book book) {
        if (book == null) {
            throw new IllegalArgumentException("Book cannot be null");
        }
        
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(
                 "INSERT INTO BOOK_DETAILS (BookID, DESCRIPTION, ISSUE_DATE, EXPIRY_DATE) VALUES (?, ?, ?, ?)")) {
            
            conn.setAutoCommit(false);
            
            try {
                stmt.setInt(1, book.getId());
                stmt.setString(2, book.getDesc());
                stmt.setDate(3, book.getIssue_date());
                stmt.setDate(4, book.getExpire_date());
                stmt.executeUpdate();
                
                conn.commit();
                return true;
            } catch (SQLException e) {
                conn.rollback();
                throw new RuntimeException("Failed to add book details for book ID: " + book.getId(), e);
            } finally {
                conn.setAutoCommit(true);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Database connection error while adding book details", e);
        }
    }

    @Override
    public boolean updateBook(Book book) {
        if (book == null) {
            throw new IllegalArgumentException("Book cannot be null");
        }
        
        try (Connection conn = dbConnection.getConnection()) {
            conn.setAutoCommit(false);
            
            try {
                // Update BOOKS table
                try (PreparedStatement booksStmt = conn.prepareStatement(
                        "UPDATE BOOKS SET BookName = ?, BookPrice = ?, TotalQuantatity = ? WHERE BookID = ?")) {
                    booksStmt.setString(1, book.getName());
                    booksStmt.setDouble(2, book.getPrice());
                    booksStmt.setInt(3, book.getTotalquantity());
                    booksStmt.setInt(4, book.getId());
                    booksStmt.executeUpdate();
                }
                
                // Update BOOK_DETAILS table
                try (PreparedStatement detailsStmt = conn.prepareStatement(
                        "UPDATE BOOK_DETAILS SET DESCRIPTION = ?, ISSUE_DATE = ?, EXPIRY_DATE = ? WHERE BookID = ?")) {
                    detailsStmt.setString(1, book.getDesc());
                    detailsStmt.setDate(2, book.getIssue_date());
                    detailsStmt.setDate(3, book.getExpire_date());
                    detailsStmt.setInt(4, book.getId());
                    detailsStmt.executeUpdate();
                }
                
                conn.commit();
                return true;
            } catch (SQLException e) {
                conn.rollback();
                throw new RuntimeException("Failed to update book with ID: " + book.getId(), e);
            } finally {
                conn.setAutoCommit(true);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Database connection error while updating book", e);
        }
    }

    @Override
    public boolean updateBookInfo(Book book) {
        if (book == null) {
            throw new IllegalArgumentException("Book cannot be null");
        }
        
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(
                 "UPDATE BOOKS SET BookName = ?, BookPrice = ?, TotalQuantatity = ? WHERE BookID = ?")) {
            
            conn.setAutoCommit(false);
            
            try {
                stmt.setString(1, book.getName());
                stmt.setDouble(2, book.getPrice());
                stmt.setInt(3, book.getTotalquantity());
                stmt.setInt(4, book.getId());
                stmt.executeUpdate();
                
                conn.commit();
                return true;
            } catch (SQLException e) {
                conn.rollback();
                throw new RuntimeException("Failed to update book info for ID: " + book.getId(), e);
            } finally {
                conn.setAutoCommit(true);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Database connection error while updating book info", e);
        }
    }

    @Override
    public boolean deleteBookByID(int id) {
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement("DELETE FROM BOOKS WHERE BookID = ?")) {
            
            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;
            
        } catch (SQLException e) {
            throw new RuntimeException("Failed to delete book with ID: " + id, e);
        }
    }

    @Override
    public boolean deleteBookDetailsByID(int id) {
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement("DELETE FROM BOOK_DETAILS WHERE BookID = ?")) {
            
            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;
            
        } catch (SQLException e) {
            throw new RuntimeException("Failed to delete book details for ID: " + id, e);
        }
    }
}