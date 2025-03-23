package com.model;

public class Users {
	
	private int id;
	private String f_name;
	private String l_name;
	private String e_mail;
	private String pass;
	
	
	
	public Users ( String f_name, String l_name, String e_mail, String pass) {
		this.f_name = f_name;
		this.l_name = l_name;
		this.e_mail = e_mail;
		this.pass = pass;
	}
	public Users ( String e_mail, String pass) {

		this.e_mail = e_mail;
		this.pass = pass;
	}
	
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getF_name() {
		return f_name;
	}
	public void setF_name(String f_name) {
		this.f_name = f_name;
	}
	public String getL_name() {
		return l_name;
	}
	public void setL_name(String l_name) {
		this.l_name = l_name;
	}
	public String getE_mail() {
		return e_mail;
	}
	public void setE_mail(String e_mail) {
		this.e_mail = e_mail;
	}
	public String getPass() {
		return pass;
	}
	public void setPass(String pass) {
		this.pass = pass;
	}

}
