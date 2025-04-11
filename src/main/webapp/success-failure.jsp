<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
	<html>
	<head>
	<meta charset="UTF-8">
	<title>Insert title here</title>
    <link rel="stylesheet" href="style/css/model.css">
</head>
<body>
  <!-- Modal Popup for messages -->
<div id="messageModal" class="modal">
  <div class="modal-content">
    <span class="close-modal">&times;</span>
    <div id="modalMessage"></div>
  </div>
</div>

<script>
  // Modal functionality
  var modal = document.getElementById("messageModal");
  var modalMessage = document.getElementById("modalMessage");
  var closeBtn = document.getElementsByClassName("close-modal")[0];
  
  // Close modal when clicking the X
  closeBtn.onclick = function() {
    modal.style.display = "none";
  }
  
  // Close modal when clicking outside of it
  window.onclick = function(event) {
    if (event.target == modal) {
      modal.style.display = "none";
    }
  }
  
  // Check for messages and show modal if needed
  window.onload = function() {
  <% if (request.getAttribute("successMessage") != null) { %>
    modalMessage.innerHTML = '<div class="success-message"><%= request.getAttribute("successMessage") %></div>';
    modal.style.display = "block";
    <% request.removeAttribute("successMessage"); %>
  <% } else if (request.getAttribute("errorMessage") != null) { %>
    modalMessage.innerHTML = '<div class="error-message"><%= request.getAttribute("errorMessage") %></div>';
    modal.style.display = "block";
    <% request.removeAttribute("errorMessage"); %>
  <% } %>
}
  // Automatically close modal after 3 seconds
  setTimeout(function() {
    if (modal.style.display === "block") {
      modal.style.display = "none";
    }
  }, 3000);
</script>
</body>
</html>