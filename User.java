package com.BankingSys;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class User {
	
	private Connection connection;
	private Scanner scanner;
	

	public User(Connection connection, Scanner scanner)
	{
		this.connection=connection;
		this.scanner=scanner;
		
	}
	public void registration() throws SQLException {
		scanner.nextLine();
		System.out.println("Enter full name :");
		String full_name=scanner.nextLine();
		System.out.println("Enter the email :");
		String email=scanner.nextLine();
		System.out.println("Enter the password :");
		String password=scanner.nextLine();
		 
		if(user_exists(email)) {
			System.out.println("user already exist for this email address :");
			return;
			}
		String query="insert into user (full_name,email,password) values (?,?,?)";
		
		PreparedStatement ps =connection.prepareStatement(query);
		ps.setString(1, full_name);
		ps.setString(2, email);
		ps.setString(3, password);
		
		int rowsAffected=ps.executeUpdate();
		if(rowsAffected>0) {
			System.out.println("Registration Successfull!");
		}else {
			System.out.println("Registration Failed!");

		}
	}
	
	public String login() throws SQLException {
		scanner.nextLine();
		System.out.println("enter Email :");
		String email=scanner.nextLine();
		System.out.println("enter password :");
		String password=scanner.nextLine();
		
		String query="select * from user where email =? and password=?";
		PreparedStatement ps =connection.prepareStatement(query);
		ps.setString(1, email);
		ps.setString(2, password);
		ResultSet rs =ps.executeQuery();
	
		if(rs.next()) {
			return email;
		}else {
			return null;
		}
		
		
	}
	
	

	private boolean user_exists(String email) throws SQLException {
		String query="select * from user where email = ?";
		PreparedStatement ps =connection.prepareStatement(query);
		ps.setString(1, email);
		ResultSet rs =ps.executeQuery();
		if(rs.next()) {
			return true;
		}else {
			return false;
		}
		
	}
	

}
