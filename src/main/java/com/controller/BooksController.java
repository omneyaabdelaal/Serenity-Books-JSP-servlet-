package com.controller;

import java.io.IOException;
import java.sql.Date;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import com.DatabaseService.DatabaseService;
import com.model.Book;
import com.serviceImpl.BookServicesImpl;

@WebServlet("/BooksController")
public class BooksController extends HttpServlet {

    private static final long serialVersionUID = 1L;

    private BookServicesImpl bookServicesImpl;
    
    @Override
    public void init() throws ServletException {
        super.init();
        bookServicesImpl = new BookServicesImpl(DatabaseService.getInstance().getDataSource());
    }
    
    //////////////////////////////////////
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    	
        String action = request.getParameter("action");
        
        if(action == null) {
            loadBooks(request, response);
        } else {
            switch(action) {
                case "list":
                    loadBooks(request, response);
                    break;
                case "edit":
                    loadBook(request, response);
                    break;
                case "delete":
                    deleteBook(request, response);
                    break;
                case "addDetails":
                	loadBookInfo(request, response);
                    break;
                case "editInfo":
                	loadBookInfo(request, response);
                    break;
                case "deleteDetails":
                	deleteBookDetails(request, response);
                	break;
                	
                default:
                    loadBooks(request, response);
            }
        }
    }

    
  
protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        String action = request.getParameter("action");
        
        if(action == null) {
            loadBooks(request, response);
        } else {
            switch(action) {
                case "edit":
                    updateBook(request, response);
                    break;
                case "add":
                    addBook(request, response);
                    break;
                    
                case "addDetails":
                	addBookDetails(request, response);
                    break;
                case "editInfo":
                	updateBookInfo(request, response);
                    break;
                	
                // Add other POST actions as needed
                default:
                    loadBooks(request, response);
            }
        }
    }
    
    
/////////////////////////////////
    private void loadBooks(HttpServletRequest request, HttpServletResponse response) {
        List<Book> books = bookServicesImpl.getAllBooks();
        request.setAttribute("Books", books);
        
     
        
        try {
            request.getRequestDispatcher("/home.jsp").forward(request, response);
        } catch (ServletException | IOException e) {
            e.printStackTrace();
        }
    }
        
        
/////////////////////////////////
    private void loadBook(HttpServletRequest request, HttpServletResponse response) {
        try {
            String idParam = request.getParameter("id");
            System.out.println("ID parameter received: " + idParam);
            
            int id = Integer.parseInt(idParam);
            System.out.println("Attempting to load book with ID: " + id);
            
            Book book = bookServicesImpl.getBookByID(id);
            request.setAttribute("Book", book);
            
            if (book == null) {
                System.out.println("Book is null");
                
            } else {
                System.out.println("Book found, forwarding to update.jsp");    
                request.getRequestDispatcher("/update.jsp").forward(request, response);
            }
        } catch (Exception e) {
            System.out.println("Exception in loadBook: " + e.getMessage());
            e.printStackTrace();
            HttpSession session = request.getSession();
            session.setAttribute("errorMessage", "Error loading book: " + e.getMessage());
            try {
                response.sendRedirect(request.getContextPath() + "/BooksController?action=list");
            } catch (IOException ioe) {
                ioe.printStackTrace();
            }
        }
    }
    
    private void loadBookInfo(HttpServletRequest request, HttpServletResponse response) {
  
    	 try {
             String idParam = request.getParameter("id");
             System.out.println("ID parameter received: " + idParam);
             
             int id = Integer.parseInt(idParam);
             System.out.println("Attempting to load book with ID: " + id);
             
             Book book = bookServicesImpl.getBookInfo(id);
             request.setAttribute("Book", book);
             
             if (book == null) {
                 System.out.println("Book is null");
                 
             } else {
                 System.out.println("Book found, forwarding to update.jsp"); 
                 if(request.getParameter("action").equals("addDetails")) {
                     request.getRequestDispatcher("/add-details.jsp").forward(request, response);
                 } else {
                     request.getRequestDispatcher("/update-info.jsp").forward(request, response);
                 }
             }
             
         } catch (Exception e) {
             System.out.println("Exception in loadBook: " + e.getMessage());
             e.printStackTrace();
             HttpSession session = request.getSession();
             session.setAttribute("errorMessage", "Error loading book: " + e.getMessage());
             try {
                 response.sendRedirect(request.getContextPath() + "/BooksController?action=list");
             } catch (IOException ioe) {
                 ioe.printStackTrace();
             }
         }
  	}
    
    	 
    ///////////////////////////////
    private void updateBook(HttpServletRequest request, HttpServletResponse response) {
        try {
            Book book = new Book(
                Integer.parseInt(request.getParameter("book-id")),
                request.getParameter("book-title"),
                Double.parseDouble(request.getParameter("book-price")),  
                Integer.parseInt(request.getParameter("book-amount")),
                request.getParameter("desc"),
                Date.valueOf(request.getParameter("issue-date")),
                Date.valueOf(request.getParameter("ex-date"))
            );
            
            boolean result = bookServicesImpl.updateBook(book);
            
            HttpSession session = request.getSession();
            if (result) {
            	session.setAttribute("successMessage", "Book updated successfully!");
            } else {
            	session.setAttribute("errorMessage", "Failed to update book. Please try again.");
            }
            
            response.sendRedirect(request.getContextPath() + "/BooksController?action=list");
            
        } catch (Exception e) {
            System.out.println("Exception in updateBook: " + e.getMessage());
            e.printStackTrace();
            HttpSession session = request.getSession();
            session.setAttribute("errorMessage", "Error updating book: " + e.getMessage());
            try {
                response.sendRedirect(request.getContextPath() + "/BooksController?action=list");
            } catch (IOException ioe) {
                ioe.printStackTrace();
            }
        }
    }
    //////////////////////////////////////////////////////////
    
    
    private void updateBookInfo(HttpServletRequest request, HttpServletResponse response) {
        try {
            Book book = new Book(
                Integer.parseInt(request.getParameter("book-id")),
                request.getParameter("book-title"),
                Double.parseDouble(request.getParameter("book-price")),  
                Integer.parseInt(request.getParameter("book-amount"))   
            );
            
            boolean result = bookServicesImpl.updateBookInfo(book);
            
            HttpSession session = request.getSession();
            if (result) {
            	session.setAttribute("successMessage", "Book updated successfully!");
            } else {
            	session.setAttribute("errorMessage", "Failed to update book. Please try again.");
            }
            
            response.sendRedirect(request.getContextPath() + "/BooksController?action=list");
            
        } catch (Exception e) {
            System.out.println("Exception in updateBook: " + e.getMessage());
            e.printStackTrace();
            HttpSession session = request.getSession();
            session.setAttribute("errorMessage", "Error updating book: " + e.getMessage());
            try {
                response.sendRedirect(request.getContextPath() + "/BooksController?action=list");
            } catch (IOException ioe) {
                ioe.printStackTrace();
            }
        }
    }
    
    /////////////////////// 
    private void deleteBook(HttpServletRequest request, HttpServletResponse response) throws IOException {
        
        try {
            int id = Integer.parseInt(request.getParameter("id"));
            boolean result = bookServicesImpl.deleteBookByID(id);
            
            HttpSession session = request.getSession();
            if (result) {
                session.setAttribute("successMessage", "Book deleted successfully!");
            } else {
                session.setAttribute("errorMessage", "Failed to delete book. Please try again.");
            }
            
            // Redirect to the book list
            response.sendRedirect(request.getContextPath() + "/BooksController?action=list");
        } catch (Exception e) {
            System.out.println("Exception in deleteBook: " + e.getMessage());
            e.printStackTrace();
            HttpSession session = request.getSession();
            session.setAttribute("errorMessage", "Error deleting book: " + e.getMessage());
            response.sendRedirect(request.getContextPath() + "/BooksController?action=list");
        }
    }
    
    
    /////////////////////////
    
    /////////////////////// 
    private void deleteBookDetails(HttpServletRequest request, HttpServletResponse response) throws IOException {
        
        try {
            int id = Integer.parseInt(request.getParameter("id"));
            boolean result = bookServicesImpl.deleteBookDetailsByID(id);
            
            HttpSession session = request.getSession();
            if (result) {
                session.setAttribute("successMessage", "Book details deleted successfully!");
            } else {
                session.setAttribute("errorMessage", "Failed to delete book details. Please try again.");
            }
            
            // Redirect to the book list
            response.sendRedirect(request.getContextPath() + "/BooksController?action=list");
        } catch (Exception e) {
            System.out.println("Exception in deleteBookDetails: " + e.getMessage());
            e.printStackTrace();
            HttpSession session = request.getSession();
            session.setAttribute("errorMessage", "Error deleting book details: " + e.getMessage());
            response.sendRedirect(request.getContextPath() + "/BooksController?action=list");
        }
    }
    
    
    /////////////////////////
    private void addBook(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            Book book = new Book(
                Integer.parseInt(request.getParameter("book-id")),
                request.getParameter("book-title"),
                Double.parseDouble(request.getParameter("book-price")), 
                Integer.parseInt(request.getParameter("book-amount")),
                request.getParameter("desc"),
                Date.valueOf(request.getParameter("issue-date")),
                Date.valueOf(request.getParameter("ex-date"))
            );
           
            boolean result = bookServicesImpl.addBook(book);
           
            HttpSession session = request.getSession();
            if (result) {
                session.setAttribute("successMessage", "Book added successfully!");
            } else {
                session.setAttribute("errorMessage", "Failed to add book. Please try again.");
            }
            
            response.sendRedirect(request.getContextPath() + "/BooksController?action=list");
        } catch (Exception e) {
            System.out.println("Exception in addBook: " + e.getMessage());
            e.printStackTrace();
            HttpSession session = request.getSession();
            session.setAttribute("errorMessage", "Error adding book: " + e.getMessage());
            response.sendRedirect(request.getContextPath() + "/BooksController?action=list");
        }
    }
    
    ////////////////////////////////////////////////
    private void addBookDetails(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            Book book = new Book(
                Integer.parseInt(request.getParameter("book-id")),
                request.getParameter("desc"),
                Date.valueOf(request.getParameter("issue-date")),
                Date.valueOf(request.getParameter("ex-date"))
            );
           
            boolean result = bookServicesImpl.addBookDetails(book);
            System.out.println(result);
           
            HttpSession session = request.getSession();
            if (result) {
                session.setAttribute("successMessage", "Book details added successfully!");
            } else {
                session.setAttribute("errorMessage", "Failed to add book details. Please try again.");
            }
            
            response.sendRedirect(request.getContextPath() + "/BooksController?action=list");
        } catch (Exception e) {
            System.out.println("Exception in addBookDetails: " + e.getMessage());
            e.printStackTrace();
            HttpSession session = request.getSession();
            session.setAttribute("errorMessage", "Error adding book details: " + e.getMessage());
            response.sendRedirect(request.getContextPath() + "/BooksController?action=list");
        }
    }
}