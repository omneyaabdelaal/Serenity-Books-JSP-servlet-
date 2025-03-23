package com.model;

import java.sql.Date;

public class Book {
	
	private int id;
	private String name;
	private double price;
	private int totalquantity;
	private String desc;
	private Date issue_date;
	private Date expire_date;
	
	public Book( int id,String name,double price,int totalquantity, String desc ,Date issue_date,Date expire_date) {
		this.id=id;
		this.name=name;
		this.price=price;
		this.totalquantity=totalquantity;
		this.desc=desc;
		this.issue_date=issue_date;
		this.expire_date=expire_date;
	}
	/*public Book(int id, String name, double price, int totalquantity) {
	    this(id, name, price, totalquantity, null, null, null);
	}*/
	

	public Book( int id,String desc,Date issue_date,Date expire_date) {
		this.id=id;
		this.desc=desc;
		this.issue_date=issue_date;
		this.expire_date=expire_date;
	}
	
	public Book( int id,String name,double price,int totalquantity) {
		this.id=id;
		this.name=name;
		this.price=price;
		this.totalquantity=totalquantity;

	}
	
	public Book(String name,double price,int totalquantity) {
		this.name=name;
		this.price=price;
		this.totalquantity=totalquantity;

	}


	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public double getPrice() {
		return price;
	}
	public void setPrice(double price) {
		this.price = price;
	}
	public int getTotalquantity() {
		return totalquantity;
	}
	public void setTotalquantity(int totalquantity) {
		this.totalquantity = totalquantity;
	}
	
	public String getDesc() {
		return desc;
	}


	public void setDesc(String desc) {
		this.desc = desc;
	}


	public Date getIssue_date() {
		return issue_date;
	}


	public void setIssue_date(Date issue_date) {
		this.issue_date = issue_date;
	}


	public Date getExpire_date() {
		return expire_date;
	}


	public void setExpire_date(Date expire_date) {
		this.expire_date = expire_date;
	}


}
