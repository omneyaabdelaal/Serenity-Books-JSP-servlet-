package com.controller;

import java.io.IOException;

import javax.annotation.Resource;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

import com.serviceImpl.UserServicesImpl;

/**
 * Servlet implementation class UsersController
 */
@WebServlet("/UsersController")
public class UsersController extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	   @Resource(name = "jdbc/item")
	    private DataSource dataSource;
	    private UserServicesImpl userServicesImpl ;

	/**
	 * @see Servlet#init(ServletConfig)
	 */
	public void init() throws ServletException {
		 super.init();
		 userServicesImpl=new UserServicesImpl(dataSource);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

	}

}
