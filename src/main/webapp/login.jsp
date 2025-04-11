<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Serenity Books - Login</title>
        <link rel="stylesheet" href="style/css/style.css">
    <link rel="stylesheet" href="style/css/login.css">
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
        <div class="login-container">
            <div class="login-section">
                <h2>Welcome back,</h2>
                <p class="subtitle">Book lover</p>
                
                <form action="UsersController" method="get" class="login-form" >
                    <div class="form-group">
                        <input type="text" id="username" placeholder="email" name="email" required>
                    </div>
                    <div class="form-group">
                        <input type="password" id="password" placeholder="Password" name="Password" required>
                    </div>
                    
                    <div class="form-group checkbox">
                        <input type="checkbox" id="terms" name='rememberMe'>
                        <label for="terms">Remember me </label>
                    </div>
                    
                    <input type="hidden" name="action" value="login" >
                    
                    <button type="submit" class="login-btn">Sign In</button>
                   
                    
                    
                </form>
            </div>
            
            <div class="signup-section">
                <div class="signup-content">
                    <h2>New here?</h2>
                    <p>Join us and discover your next favorite book from our collection of curated titles.</p>
                    
                    <a href="signup.jsp">
					    <button class="signup-btn">Sign Up</button>
					</a>
            
                </div>
            </div>
        </div>
    </main>

    <footer>
        <p>&copy; 2025 Serenity Books. All rights reserved.</p>
    </footer>
    
       <jsp:include page="success-failure.jsp"></jsp:include>
</body>
</html>