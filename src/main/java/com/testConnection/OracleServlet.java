package com.testConnection;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;
import javax.annotation.Resource;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

@WebServlet("/oracleServlet")
public class OracleServlet extends HttpServlet {
    
    private static final long serialVersionUID = 1L;

    // Inject DataSource using @Resource annotation
    @Resource(name = "jdbc/item")
    private DataSource dataSource;

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        response.setContentType("text/html");
        try (PrintWriter out = response.getWriter()) {
            
            out.println("<h2>Debug: Servlet reached</h2>");
            
            // Get connection from DataSource
            try (Connection con = dataSource.getConnection();
                 Statement st = con.createStatement();
                 ResultSet rs = st.executeQuery("SELECT * FROM BOOKS")) {

                out.println("<p>Connected to the database successfully.</p>");
                out.println("<h3>Employee Data:</h3><ul>");
                
                while (rs.next()) {
                    out.println("<li>ID: " + rs.getInt(1) + ", Name: " + rs.getString(2) + ", price: " + rs.getDouble(3) +", totalQuantity: " + rs.getInt(4)+"</li>");
                }
                out.println("</ul>");

            } catch (SQLException e) {
                out.println("<p style='color:red;'>SQL Error: " + e.getMessage() + "</p>");
                e.printStackTrace(out);
            }
        }
    	
    	
    }
}


