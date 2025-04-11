<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.List, com.model.Book" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>


<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Serenity Books</title>
    <link rel="stylesheet" href="style/css/style.css">
    <link rel="stylesheet" href="style/css/books-table-fix.css">
</head>
<body>

 <jsp:include page="header.html"></jsp:include>

    <!-- Hero Section -->
    <section class="hero">
        <div class="container hero-content">
            <div class="hero-text">
                <h1>Discover Your Next Favorite Book</h1>
                <p>Explore our carefully curated collection of books from classic literature to contemporary bestsellers.</p>
                <button class="btn">Browse Collection</button>
            </div>
            <div class="hero-image">
                <img src="style/images/reading22.jpg" alt="Cozy book collection">
            </div>
        </div>
    </section>

    <!-- Books Section -->
    <section class="books-section">
        <div class="container">
            <div class="section-header">
                <h2>Our Collection</h2>
                <p>Browse through our extensive collection of books</p>
            </div>
            
            
           
            

           <!-- Table View -->
<table class="books-table" id="books-table-view">
    <thead>
        <tr>
            <th>Book ID</th>
            <th>Name</th>
            <th>Price</th>
            <th>Amount</th>
            <th>Description</th>
            <th>Issue Date</th>
            <th>Expire Date</th>
            <th>Action</th>
        </tr>
    </thead>
    <tbody>
        <%
        List<Book> books = (List<Book>) request.getAttribute("Books");
        if(books != null && !books.isEmpty()) {
            for(Book book : books) {
        %>
            <tr>
                <td><%= book.getId() %></td>
                <td><%= book.getName() %></td>
                <td>$<%= book.getPrice() %></td>
                <td><%= book.getTotalquantity()%></td>
                <td><%= book.getDesc() != null ? book.getDesc() : "NO DESCRIPTION" %></td>
    		    <td><%= book.getIssue_date() != null ? book.getIssue_date() : "_" %></td>
    		    <td><%= book.getExpire_date() != null ? book.getExpire_date() : "_" %></td>
                
        <!-- In your JSP file -->
<td>
    <% if (book.getDesc() == null) { %>
        <a href="BooksController?action=editInfo&id=<%= book.getId() %>" class="btn-update">Update</a>
        <a href="BooksController?action=delete&id=<%= book.getId() %>" class="btn-delete">Delete</a>
        <a href="BooksController?action=addDetails&id=<%= book.getId() %>" class="btn-update">Add Book Details</a>
          
    <% } else { %>
        <a href="BooksController?action=edit&id=<%= book.getId() %>" class="btn-update">Update</a>
        <a href="BooksController?action=delete&id=<%= book.getId() %>" class="btn-delete">Delete</a>
        <a href="BooksController?action=deleteDetails&id=<%= book.getId() %>" class="btn-delete">Delete Book Details</a>
    <% } %>
</td>
            </tr>
        <% 
            }
        } else {
        %>
            <tr>
                <td colspan="5">No books found or books list is null</td>
            </tr>
        <% } %>
    </tbody>
</table>
            
                     <!-- Add Book Button -->
        <div class="add-book-container">
            <a href="add-book.html" class="btn add-book-btn">
                <i class="fas fa-plus"></i> Add New Book
            </a>
        </div>
            
        </div>

    </section>

   <jsp:include page="footer.html"></jsp:include>
   <jsp:include page="delete-alert.html"></jsp:include>
   <jsp:include page="success-failure.jsp"></jsp:include>
 

</body>
</html>