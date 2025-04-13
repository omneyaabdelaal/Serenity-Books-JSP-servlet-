package com.controller;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.DataSource;

import com.DatabaseService.DatabaseService;
import com.model.Users;
import com.serviceImpl.UserServicesImpl;

@WebServlet("/UsersController")
public class UsersController extends HttpServlet {
    private static final long serialVersionUID = 1L;
    
   
   
    private UserServicesImpl userServicesImpl;
    
    @Override
    public void init() throws ServletException {
        super.init();
        userServicesImpl = new UserServicesImpl(DatabaseService.getInstance().getDataSource());
    }
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");
        
        if (action == null) {
            login(request, response);
        }
        

            switch (action) {
                case "sign-up":
                    signUp(request, response);
                    break;
                case "login":
                    login(request, response);
                    break;
                case "logout":
                    logout(request, response);
                    break;
                default:
                	login(request, response);
            }
       
    }
    
    protected void signUp(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Users user = new Users(
                request.getParameter("firstName"),
                request.getParameter("lastName"),
                request.getParameter("email"),
                request.getParameter("password")
        );
        
        boolean result = userServicesImpl.createAccount(user);
        
        HttpSession session = request.getSession();
        if (!result) {
            // Account creation failed
        	session.setAttribute("errorMessage", "Account creation failed. This email may already be registered.");
            request.getRequestDispatcher("signup.jsp").forward(request, response);
        } else {
            // Account created successfully
            session.setAttribute("User", user);
            response.sendRedirect(request.getContextPath() + "/BooksController");
        }
    }
    
    protected void login(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Users user = new Users(request.getParameter("email"),request.getParameter("Password"));
        System.out.println(request.getParameter("email"));
        boolean result = userServicesImpl.login(user);
        
        HttpSession session = request.getSession();
        if (result) {
            // Login successful
            session.setAttribute("User", user);
            
            if ("on".equals(request.getParameter("rememberMe"))) {
            Cookie emailCookie=new Cookie("userEmail",user.getE_mail());
            Cookie authCookie=new Cookie("authToken",generateAuthToken(user.getE_mail(),user.getPass()));
            int maxAge= 30*24*60*60; //30 days
            emailCookie.setMaxAge(maxAge);
            authCookie.setMaxAge(maxAge);
            
            emailCookie.setPath("/");
            authCookie.setPath("/");
            
            response.addCookie(emailCookie);
            response.addCookie(authCookie);
        }
            response.sendRedirect(request.getContextPath() + "/BooksController");
        } else {
            // Login failed
        	session.setAttribute("errorMessage", "Invalid email or password. Please try again.");
            request.getRequestDispatcher("login.jsp").forward(request, response);
        }
    }
    
   
    
    protected String generateAuthToken(String email, String password) {
    	
    	String combined= email + ":" + password + ":" + "YOUR_SECRET_KEY";
    	
    	try {
			MessageDigest digest = MessageDigest.getInstance("SHA-256");
			byte[] hash = digest.digest(combined.getBytes(StandardCharsets.UTF_8));
			return Base64.getEncoder().encodeToString(hash);
			
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	return null;
    }
    
    protected void logout(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Invalidate session
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }
        
        // Delete cookies
        Cookie[] cookies = request.getCookies();
        System.out.println("IN LOG OUT >>> cookies= "+cookies);
        if (cookies != null) {
            for (Cookie cookie : cookies){
                if ("userEmail".equals(cookie.getName()) || "authToken".equals(cookie.getName())) {
                    cookie.setValue("");
                    cookie.setMaxAge(0);
                    cookie.setPath("/");
                    response.addCookie(cookie);
                }
            }
        }
        
        response.sendRedirect(request.getContextPath() + "/login.jsp");
    }
    
}