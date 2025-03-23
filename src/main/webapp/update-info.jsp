<%@ page import="com.model.Book" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Update Book - Serenity Books</title>
    <link rel="stylesheet" href="style/css/style.css">
    <link rel="stylesheet" href="style/css/update-style.css">
</head>
<body>
 <jsp:include page="header.html"></jsp:include>

    <!-- Main Content -->
    <section class="page-content">
        <div class="container">
            <div class="section-header">
                <h2>Update Book Details</h2>
                <p>Make changes to the book information</p>
            </div>

            <div class="update-form">
            
            <%
            Book book=(Book)request.getAttribute("Book");
            if(book !=null)
            {
            %>
                <form action="BooksController" method="post">
                    <div class="form-group">
                        <label for="book-id">Book ID</label>
                        <input type="text" id="book-id" name="book-id" value="<%= book.getId() %>" readonly>
                    </div>
                    
                    <div class="form-group">
                        <label for="book-title">Book Title</label>
                        <input type="text" id="book-title" name="book-title" value="<%= book.getName() %>" required>
                    </div>
                    
                    <div class="form-group">
                        <label for="book-price">Price ($)</label>
                        <input type="number" id="book-price" name="book-price" step="0.01" value="<%= book.getPrice() %>" required>
                    </div>
                    
                    <div class="form-group">
                        <label for="book-amount">Amount in Stock</label>
                        <input type="number" id="book-amount" name="book-amount" value="<%= book.getTotalquantity() %>" required>
                    </div>
                    
                    
                    <input type="hidden" name="action" value="editInfo">
                 
                   
                    
                    <div class="form-actions">
                        <button type="submit" class="btn">Save Changes</button>
                        <a href="BooksController" class="btn-secondary">Cancel</a>
                    </div>
                </form>
            </div>
        </div>
        
            <% } else { %>
            <div class="error-message">
                <p>Book not found or could not be loaded.</p>
                <a href="BooksController" class="btn">Return to Book List</a>
            </div>
            <% } %>
    </section>
<jsp:include page="footer.html"></jsp:include>
</body>
</html>