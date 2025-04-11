<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Serenity Books - Sign Up</title>
      <link rel="stylesheet" href="style/css/style.css">
    <link rel="stylesheet" href="style/css/sign-up.css">

</head>
<body>
  <header>
    <div class="container header-content">
        <div class="logo">Serenity <span>Books</span></div>
        <nav>
            <ul>
                
            </ul>
        </nav>
    </div>
</header>

    <main>
        <div class="signup-container">
            <div class="signup-section">
                <h2>Create an Account</h2>
                <p class="subtitle">Join our community of book lovers</p>
                
                <form class="signup-form" action="UsersController" method="post">
                    <div class="form-row">
                        <div class="form-group">
                            <label for="firstName">First Name</label>
                            <input type="text" id="firstName" name="firstName" required>
                            <p class="password-hint">only letters allowed</p>
                        </div>
                        <div class="form-group">
                            <label for="lastName">Last Name</label>
                            <input type="text" id="lastName" name="lastName" required>
                            <p class="password-hint">only letters allowed</p>
                        </div>
                    </div>
                    
                    <div class="form-group">
                        <label for="email">Email Address</label>
                        <input type="email" id="email" name="email" required>
                        <p class="password-hint">must end with @gmail.com</p>
                    </div>
                    
                    <div class="form-group">
                        <label for="password">Password</label>
                        <input type="password" id="password" name="password" required>
                        <p class="password-hint">Must be at least 6 characters with a number, capital letter, small letter, digit and a special character</p>
                    </div>
                    
                    <div class="form-group">
                        <label for="confirmPassword">Confirm Password</label>
                        <input type="password" id="confirmPassword" required>
                        <p class="password-error" id="password-error" style="color: red; display: none;">Passwords don't match</p>
                    </div>
                    
                    <div class="form-group checkbox">
                        <input type="checkbox" id="terms" required>
                        <label for="terms">I agree to the <a href="#">Terms & Conditions</a> and <a href="#">Privacy Policy</a></label>
                    </div>
                    
                     <input type="hidden" name="action" value="sign-up">
                    
                    <button type="submit" class="signup-btn">Create Account</button>
                    
                    
                </form>
            </div>
            
            <div class="welcome-section">
                <div class="welcome-content">
                    <h2>Welcome to Serenity Books</h2>
                    <p>Discover your next favorite book from our carefully curated collection of classics and contemporary bestsellers.</p>
                    <p class="already-member">Already a member? <a href="login.jsp">Sign In</a></p>
                </div>
            </div>
        </div>
    </main>

    <footer>
        <p>&copy; 2025 Serenity Books. All rights reserved.</p>
    </footer>
    
   
<script>
    document.addEventListener('DOMContentLoaded', function() {
        // Get the form element
        const form = document.querySelector('.signup-form');
        
        // Get password fields
        const password = document.getElementById('password');
        const confirmPassword = document.getElementById('confirmPassword');
        
        // Function to check password match
       function checkPasswordsMatch() {
   			 const errorElement = document.getElementById('password-error');
    
			  if (password.value !== confirmPassword.value) {
			        confirmPassword.setCustomValidity("Passwords don't match");
			        errorElement.style.display = 'block';
			    } else {
			        confirmPassword.setCustomValidity('');
			        errorElement.style.display = 'none';
			    }
}
        
        // Add event listeners
        password.addEventListener('change', checkPasswordsMatch);
        confirmPassword.addEventListener('keyup', checkPasswordsMatch);
        
        // Form submission validation
        form.addEventListener('submit', function(event) {
            if (password.value !== confirmPassword.value) {
                event.preventDefault();
                alert("Passwords don't match. Please try again.");
            }
        });
    });
</script>

   <jsp:include page="success-failure.jsp"></jsp:include>
</body>
</html>