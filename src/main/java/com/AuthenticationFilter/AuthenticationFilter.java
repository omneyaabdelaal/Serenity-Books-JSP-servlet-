package com.AuthenticationFilter;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.DataSource;

import com.DatabaseService.DatabaseService;
import com.model.Users;
import com.serviceImpl.UserServicesImpl;

/**
 * Servlet Filter implementation class AuthenticationFilter
 */
@WebFilter("/*")
public class AuthenticationFilter extends HttpFilter implements Filter {
	
	 private static final String[] PUBLIC_PATHS = {
		        "/login.jsp", "/signup.jsp", 
		         "/style/","/UsersController"
		    };

	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		
		    HttpServletRequest httpRequest = (HttpServletRequest) request;
	        HttpServletResponse httpResponse = (HttpServletResponse) response;
	        HttpSession session = httpRequest.getSession(false);
	        
	        String requestPath = httpRequest.getRequestURI().substring(httpRequest.getContextPath().length());
	        
	        boolean isPublicResource = false;
	        for (String path : PUBLIC_PATHS) {
	            if (requestPath.startsWith(path)) {
	                isPublicResource = true;
	                break;
	            }
	        }
	        System.out.println("Request path: " + requestPath);
	        System.out.println("Is public resource: " + isPublicResource);
	        
	        boolean isLoggedIn = (session != null && session.getAttribute("User") != null);
	        System.out.println("IS LOGGED IN:"+isLoggedIn);
	        
	        
	        if (!isLoggedIn && !isPublicResource) {
	        	System.out.println("MUST CALL <authenticateWithCookies>:"+isLoggedIn);
	            isLoggedIn = authenticateWithCookies(httpRequest);
	            System.out.println("Cookie authentication attempt: " + isLoggedIn);
	        }
	        
	        
	        if (isLoggedIn || isPublicResource) {
	            chain.doFilter(request, response);
	        } else {
	            httpResponse.sendRedirect(httpRequest.getContextPath() + "/login.jsp");
	        }
	}
	
	private boolean authenticateWithCookies(HttpServletRequest request) {
		Cookie[] cookies=request.getCookies();
		 System.out.println("THIS IS <cookies[]>: "+cookies);
		if(cookies==null) {
			return false;
		}
		
		String email=null;
		String authToken=null;
		
		for(Cookie cookie:cookies) {
			if("userEmail".equals(cookie.getName())) {
				email=cookie.getValue();
				
			}else if("authToken".equals(cookie.getName())) {
				authToken=cookie.getValue();
			}
		}
		
		
	  if(email !=null && authToken !=null) {
		  System.out.println("THIS IS DATASOURCE OBJECT>>>>>"+DatabaseService.getInstance().getDataSource());
		  
		  UserServicesImpl userServices = new UserServicesImpl(DatabaseService.getInstance().getDataSource());
	      Users user = userServices.getUserByEmail(email);
	      
	      if(user !=null) {
	    	  String expectedToken = generateAuthToken(email, user.getPass());
	    	  if (authToken.equals(expectedToken)) {
	                // Auto login
	                HttpSession session = request.getSession();
	                session.setAttribute("User", user);
	                return true;
	    	  }
	      }
				  
	  }
	  
	  return false;
		
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

}
